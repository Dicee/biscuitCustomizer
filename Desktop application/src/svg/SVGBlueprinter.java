package svg;

import static entities.Customization.URL_MODE;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import entities.Batch;
import entities.Customization;

public class SVGBlueprinter {
	private static final float		POUCE_TO_MILLI	= 25.4f;
	private static final float		PIX_TO_MILLI;
	
	static {
		int resolution = Toolkit.getDefaultToolkit().getScreenResolution();
		PIX_TO_MILLI   = 1/(resolution / POUCE_TO_MILLI);
		System.out.println(PIX_TO_MILLI);
	}

	public static File getBluePrint(Batch batch, String output) throws IOException, JDOMException {
		return getBluePrint(batch,Toolkit.getDefaultToolkit().getScreenSize(),output);
	}
	
	public static File getBluePrint(Batch batch, Dimension resolution, String output) throws IOException, JDOMException {
		Document doc;
		String ref = batch.getBiscuit().getRef();
		Properties properties = new Properties();
		properties.load(SVGBlueprinter.class.getResourceAsStream(String.format("data/%s/%s.properties",ref,ref)));
		doc = templatize(batch,getPattern(batch,properties),properties,resolution);
		
		File         result = new File(output);
		XMLOutputter out    = new XMLOutputter(Format.getPrettyFormat());
		out.output(doc,new BufferedWriter(new FileWriter(result)));
		return result;
	}

	private static List<Element> getPattern(Batch batch, Properties biscuit) throws IOException, JDOMException {
		List<Element> result = new ArrayList<>();
		for (Customization custom : batch.getCustomizations()) {
			String input = custom.getData();
			Element pattern;
			if (custom.getMode() == URL_MODE) {
				//This piece of code deserves an explanation. It's quite twisted because we are dependent of two QR code APIs which
				//have both their pros and cons. The first API let us use a transparent background but generates an ugly SVG. At the contrary,
				//the second API forces us to remove a white background but generates a nice SVG. Then, we have decided to use the first API
				//client-side to limit the complexity of the JS code, and finally use the second API to actually generate the template. As these APIs
				//don't use the same standard to designate the size of the QR code, we need to convert it.
				URL url = new URL("http://www.esponce.com/api/v3/generate?content=" + custom.getData() + 
						"&format=png&padding=0&background=%2300ffffff&size=" + custom.getSize());
			
				float      scale   = parseFloat(biscuit.getProperty("custom.edge")) / batch.getBiscuit().getEdgeLength() / PIX_TO_MILLI;
				int        size    = (int) Math.ceil(ImageIO.read(url).getHeight() * scale);
				url                = new URL(String.format("http://api.qrserver.com/v1/create-qr-code/?data=%s&size=%dx%d&format=svg",input,size,size));
				SAXBuilder builder = new SAXBuilder();
				Document   doc     = builder.build(url);
				Element    root    = doc.getRootElement(); 
				pattern            = root.getChild("g",root.getNamespace());
			} else {
				pattern            = new Element("text","http://www.w3.org/2000/svg");
				pattern.setAttribute("font-size",custom.getSize() + "");
				pattern.addContent(custom.getData());
			}
			pattern.setAttribute("xtop",custom.getX() + "");
			pattern.setAttribute("ytop",custom.getY() + "");
			result.add(pattern);
		}
		return result;
	}
	
	private static Document templatize(Batch batch, List<Element> pattern, Properties biscuit, Dimension resolution) throws JDOMException, IOException {
		SAXBuilder    builder = new SAXBuilder();
		String        ref     = batch.getBiscuit().getRef();
		Document      doc     = builder.build(SVGBlueprinter.class.getResource(String.format("data/%s/%s_template.svg",ref,ref)));
		Element       root    = doc.getRootElement(); 
		float         margin  = parseFloat(biscuit.getProperty("repeat.spacing")) / PIX_TO_MILLI;
		int           rows    = parseInt(biscuit.getProperty("repeat.x"));
		int           cols    = parseInt(biscuit.getProperty("repeat.y"));
		float         edge    = parseFloat(biscuit.getProperty("pattern.edge")) / PIX_TO_MILLI;
		
		for (int i=0 ; i<cols ; i++)
			for (int j=0 ; j<rows ; j++) 
				for (Element elt : pattern) {
					Element clone = elt.clone();
					float  xtop   = parseFloat(elt.getAttributeValue("xtop"));
					float  ytop   = parseFloat(elt.getAttributeValue("ytop" ));
					float  dx     = margin + i*(margin + edge) + xtop;
					float  dy     = margin + j*(margin + edge) + ytop;
					String sdx    = (dx + "").replace(',','.');
					String sdy    = (dy + "").replace(',','.');
					clone.setAttribute(new Attribute("transform",String.format("translate(%s,%s)",sdx,sdy)));
					root.addContent(clone);
				}
		return doc;
	}
}
