package bean;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import ejb.RawTypeEJB;
import entities.Batch;

@RequestScoped
@ManagedBean
public class ReviewBean {
	private Batch 				batch;
	@EJB
	private RawTypeEJB<Batch> 	ejb;
	
	@PostConstruct
	private void init() {
		Map<String,String> 	params 	= FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		try {
			long id = Long.parseLong(params.get("id"));
			batch 	= ejb.find(Batch.class, id);	
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	public Batch getBatch() {
		return batch;
	}
	
}
