package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class GenericEJB {
	@PersistenceContext
	protected EntityManager em;
	
	public <T> void persist(T t) {
		em.persist(t);
	}
	
	public <T> T find(Class<T> c, Object key) {
		return em.find(c,key);
	}
	
	public <T> void remove(T t) {
		em.remove(t);
	}
	
	public <T> void remove(Class<T> c, Object key) {
		T t = (T) find(c,key);
		if (t != null)
			em.remove(t); 
		else System.err.println("Not found : " + c + ", " + key);
	}
	
	public <T> T merge(T t) {
		return em.merge(t);
	}
}
