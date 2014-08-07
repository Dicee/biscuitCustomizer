package bean;

import static java.lang.Integer.parseInt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class QRCodeProxyServlet extends HttpServlet implements Servlet {
	static final long			serialVersionUID	= 1L;

	private FacesContextFactory	facesContextFactory;
	private Lifecycle			lifecycle;

	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
		facesContextFactory = (FacesContextFactory) FactoryFinder.getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
		lifecycle = lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		System.out.println(request.getParameter("amp;data"));
		int size             = parseInt(request.getParameter("size"));
		String data          = request.getParameter("amp;data").replace(" ","-");
		FacesContext context = facesContextFactory.getFacesContext(getServletContext(), request, response, lifecycle);
		try {
			BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
			try {
//				URL url = new URL(String.format("http://api.qrserver.com/v1/create-qr-code/?data=%s&size=%dx%d&format=png",
//						data,size,size));
				URL url = new URL("http://www.esponce.com/api/v3/generate?content=" + data + "&format=png&padding=0&background=%2300ffffff&size=" + size);
				byte[] buffer = new byte[1024];
				BufferedInputStream isr = new BufferedInputStream(url.openStream());
				while (isr.read(buffer) != -1)
					bos.write(buffer);
			} finally {
				if (bos != null)
					bos.close();
			}
		} finally {
			context.release();
		}
	}
}