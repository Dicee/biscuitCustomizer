package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import utils.Pages;
import ejb.GenericEJB;
import ejb.UserManager;
import entities.Batch;
import entities.ClientOrder;

@ManagedBean
@SessionScoped
public class CartBean implements Serializable {
	private static final long	serialVersionUID	= 1L;
	
	@EJB
	private GenericEJB orderer;
	@EJB
	private UserManager userManager;
	
	@ManagedProperty(value="#{connectBean}")
	private SessionBean connectBean;
	
	private ClientOrder order;
	
	@PostConstruct
	private void init() {
		order = connectBean.isConnected() ? getCurrentOrder() : new ClientOrder();
	}
	
	public ClientOrder getCurrentOrder() {
		ClientOrder order = userManager.getCurrentOrder(connectBean.getUser());
		if (order == null)
			orderer.persist(order = new ClientOrder(connectBean.getUser()));
		return order;
	}
	
	public String order() {
		if (connectBean.isConnected()) {
			ClientOrder order = new ClientOrder(connectBean.getUser());
			orderer.persist(order);
			return Pages.orderSuccessful;
		} else
			return Pages.signin;
	}
	
	public void addBatch(Batch b) {
		order.getBatches().add(b);
		if (connectBean.isConnected())
			orderer.merge(order);
	}
	
	public List<Batch> getBatches() {
		return new ArrayList<>(order.getBatches());
	}

	public SessionBean getConnectBean() {
		return connectBean;
	}
	
	public ClientOrder getOrder() {
		return order;
	}
}
