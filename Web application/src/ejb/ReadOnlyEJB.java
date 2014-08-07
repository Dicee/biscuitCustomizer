package ejb;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Stateless
public class ReadOnlyEJB extends GenericEJB{
	@PersistenceContext
	private EntityManager em;
	
	@SuppressWarnings("unchecked")
	public <T> List<T> retrieveAll(Class<T> c) {
		int    i = c.getName().lastIndexOf('.');
		String s = i == -1 ? c.getName() : c.getName().substring(i + 1);
		Query  q = em.createQuery(String.format("select o from %s o",s),c);
		return (List<T>) q.getResultList();
	}
}
