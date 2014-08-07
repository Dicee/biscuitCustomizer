package ejb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import org.eclipse.persistence.exceptions.TransactionException;

import entities.Address;
import entities.ClientOrder;
import entities.User;
import entities.UserMetadata;

@Stateless
public class UserManager extends RawTypeEJB<User> {
	private static final String retrievePswdMetadata = "retrieve_password";
	private static final String retrieveCodeMetadata = "retrieve_code";
	
	@Override
	public void persist(User user) {
		user.setPassword(sha1(user.getPassword()));
		super.persist(user); 
	}
	
	public User getUserByEMail(String email) {
		try {
			Query q = em.createQuery("select u from User u where u.email=?1");
			q.setParameter("1",email);
			return (User) q.getSingleResult();	
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	public ClientOrder getCurrentOrder(User user) {
		try {
			Query q = em.createQuery(String.format("select c from ClientOrder c where c.state='%s' and c.owner.id=%d",ClientOrder.CREATED,user.getId()));
			return (ClientOrder) q.getSingleResult();
		} catch (NoResultException nre) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Address> getAddresses(User user) {
		Query q = em.createQuery("select a from Address where a.owner_id=?1");
		q.setParameter("1",user.getId());
		return (List<Address>) q.getResultList();
	}
	
	public void setRetrievePassword(User user, String confirmCode, String password) {
		UserMetadata pswdMetadata = new UserMetadata(user,retrievePswdMetadata,sha1(password));
		UserMetadata codeMetadata = new UserMetadata(user,retrieveCodeMetadata,confirmCode);
		Query        q            = em.createNativeQuery("select * from UserMetadata where user_id=?1 and field=?2");
		q.setParameter("1",user.getId());
		q.setParameter("2",retrievePswdMetadata);
		try {
			q.getSingleResult();
			em.merge(pswdMetadata);
			em.merge(codeMetadata);
		} catch (NoResultException nre) {
			em.persist(pswdMetadata);
			em.persist(codeMetadata);
		}
	}
	
	public boolean retrievePassword(User user, String confirmCode) throws TransactionException {
		Query q = em.createNativeQuery("select * from UserMetadata where user_id=?1 and field=?2 and value=?3");
		q.setParameter("1",user.getId());
		q.setParameter("2",retrieveCodeMetadata);
		q.setParameter("3",confirmCode);
		
		try {
			q.getSingleResult();
			
			q = em.createNativeQuery("select value from UserMetadata where user_id=?1 and field=?2");
			q.setParameter("1",user.getId());
			q.setParameter("2",retrievePswdMetadata);
			user.setPassword((String) q.getSingleResult());
			em.merge(user);
			
			q = em.createNativeQuery("delete from UserMetadata where user_id=?1 and (field=?2 or field=?3)");
			q.setParameter("1",user.getId());
			q.setParameter("2",retrievePswdMetadata);
			q.setParameter("3",retrieveCodeMetadata);
			q.executeUpdate();
			return true;
		} catch (NoResultException nre) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<ClientOrder> getOrders(User user, String... states) {
		String query = "select * from CLIENTORDER where owner_id=?1" + (states.length != 0 ? " and (" : "");
		for (int i=0 ; i<states.length ; i++)
			query += String.format("state='%s'",states[i]) + (i == states.length - 1 ? ")" : " or ");
		
		Query q = em.createNativeQuery(query,ClientOrder.class);
		q.setParameter("1",user.getId());
		return (List<ClientOrder>) q.getResultList();
	}
	
	public static String sha1(String input) {
		try {
			MessageDigest mDigest = MessageDigest.getInstance("SHA1");
			byte[] result = mDigest.digest(input.getBytes());
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < result.length; i++)
				sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
			return sb.toString();
		} catch (NoSuchAlgorithmException nsae) {
			return input;
		}
	}
}