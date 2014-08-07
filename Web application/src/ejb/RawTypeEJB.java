package ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class RawTypeEJB<T> {
	@PersistenceContext
	protected EntityManager em;
	
	public void persist(T t) {
		em.persist(t);
	}
	
	public T find(Class<T> c, Object key) {
		return em.find(c,key);
	}
	
	public void remove(T t) {
		em.remove(t);
	}
	
	public void remove(Class<T> c, Object key) {
		T t = (T) find(c,key);
		if (t != null)
			em.remove(t);
		else System.err.println("Not found : " + c + ", " + key);
	}
	
	public T merge(T t) {
		return em.merge(t);
	}
}
