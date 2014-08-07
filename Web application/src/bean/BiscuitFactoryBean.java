package bean;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import ejb.ReadOnlyEJB;
import entities.Batch;
import entities.Biscuit;
import entities.Customization;

@ManagedBean
@ViewScoped
public class BiscuitFactoryBean implements Serializable {
	private static final long	serialVersionUID	= 1L;

	private static final String ADDED_TO_CART_MSG = "<p class='info'>Votre lot de biscuits a été correctement ajouté au panier !</p>";
	
	@EJB
	private ReadOnlyEJB ejb;
	
	private List<Biscuit> biscuits;
	@NotNull
	@NotEmpty
	private String biscuitRef;
	private Biscuit selectedBiscuit;
	
	//////   Used for creating a new customization    //////
	private int mode;
	private String data, snippet = "";
	////////////////////////////////////////////////////////
	
	private List<Customization> customizations = new ArrayList<>();
	
	private Batch batch;
	
	@ManagedProperty(value="#{sessionBean}")
	private SessionBean sessionBean;
	
	public BiscuitFactoryBean() {
		this.batch   = new Batch();
		this.snippet = "";
	}
	
	@PostConstruct
	private void init() {
		selectedBiscuit = getBiscuits().isEmpty() ? null : getBiscuits().get(0);
	}
	
	public void submit() {
		batch.setBiscuit(ejb.find(Biscuit.class,biscuitRef));
		
		Map<String,String>  params    = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		List<Customization> customs   = batch.getCustomizations();
		int                 nObjects  = parseInt(params.get("numberOfObjects"));
		
		for (int i=0 ; i<nObjects; i++) {
			String data = params.get("custom_text" + i);
			int	   size = parseInt(params.get("size" + i));
			float  x    = parseFloat(params.get("x" + i));
			float  y    = parseFloat(params.get("y" + i));
			int    mode = parseInt(params.get("mode" + i));
			customs.add(new Customization(x,y,mode,data,size));	
		}
		sessionBean.addBatch(batch);
		
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO,ADDED_TO_CART_MSG,null);
		FacesContext.getCurrentInstance().addMessage(null, message);	
		batch = new Batch();
	}
	
	public void updateBiscuit() {
		selectedBiscuit = ejb.find(Biscuit.class,biscuitRef);
	}
	
	public List<Biscuit> getBiscuits() {
		if (biscuits == null)
			biscuits = ejb.retrieveAll(Biscuit.class);
		return biscuits;
	}
	
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public String getBiscuitRef() {
		return biscuitRef;
	}

	public void setBiscuitRef(String biscuitRef) {
		this.biscuitRef = biscuitRef;		
	}

	public String getSnippet() {
		return snippet;
	}

	public Biscuit getSelectedBiscuit() {
		return selectedBiscuit;
	}

	public void setSelectedBiscuit(Biscuit selectedBiscuit) {
		this.selectedBiscuit = selectedBiscuit;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public List<Customization> getCustomizations() {
		return customizations;
	}

	public void setCustomizations(List<Customization> customizations) {
		this.customizations = customizations;
	}
}
