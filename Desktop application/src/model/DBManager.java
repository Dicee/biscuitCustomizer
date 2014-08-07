package model;

import static java.lang.String.format;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import entities.Batch;
import entities.Biscuit;
import entities.ClientOrder;
import entities.Customization;
import entities.User;

public class DBManager {
	public static final Properties connectionProp = new Properties();
	static {
		try {
			connectionProp.load(DBManager.class.getResourceAsStream("db.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private Connection conn;
	
	public void connect() throws SQLException {
		this.conn = DriverManager.getConnection(
			format("jdbc:mysql://%s:%s/",connectionProp.getProperty("server_name"),connectionProp.getProperty("port_number")),connectionProp);
		System.out.println("Connected to database");
	}
	
	public static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				format("jdbc:mysql://%s:%s/",connectionProp.getProperty("server_name"),connectionProp.getProperty("port_number")),connectionProp);
	}
	
	public List<ClientOrder> getAllOrders() throws SQLException {
		Statement         stmt         = null;
		String			  databaseName = connectionProp.getProperty("db_name");
		String            query        = format("select c.*,u.nom,u.prenom from %s.CLIENTORDER as c right join %s.USER as u on u.id=c.OWNER_ID",databaseName,databaseName);
		List<ClientOrder> result       = new ArrayList<>();

		try {
			stmt         = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				long   id      = rs.getLong("ID");
				String state   = rs.getString("STATE");
				long   date    = rs.getLong("CREATIONDATE");
				long   ownerId = rs.getLong("OWNER_ID");
				
				User user      = new User();
				user.setId(ownerId);
				user.setNom(rs.getString("NOM"));
				user.setPrenom(rs.getString("PRENOM"));
						
				ClientOrder co = new ExtendedClientOrder();
				co.setId(id);
				co.setState(state);
				co.setCreationDate(date);
				co.setOwner(user);
				co.setBatches(getBatches(co));
				
				result.add(co);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return result;
	}
	
	public List<Batch> getBatches(ClientOrder order) throws SQLException {
		Statement           stmt     = null;
		String              query    = format("select * from %s.BATCH where ORDER_ID=%d",connectionProp.getProperty("db_name"),order.getId());
		List<Batch>         result   = new ArrayList<>();
		Map<String,Biscuit> biscuits = getAllBiscuits();
		
		try {
			stmt         = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Batch b = new ExtendedBatch();
				b.setId(rs.getLong("ID"));
				b.setQt(rs.getInt("QT"));
				b.setBiscuit(biscuits.get(rs.getString("BISCUIT_REF")));
				b.setOrder(order);
				b.setCustomizations(getCustomizations(b.getId()));
				result.add(b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return result;
	}
	
	private Map<String,Biscuit> getAllBiscuits() throws SQLException {
		Statement           stmt   = null;
		String              query  = format("select REF,EDGELENGTH,NAME,XTOP,YTOP from %s.BISCUIT",connectionProp.getProperty("db_name"));
		Map<String,Biscuit> result = new HashMap<>();
		
		try {
			stmt         = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				Biscuit b = new Biscuit();
				b.setRef(rs.getString("REF"));
				b.setEdgeLength(rs.getInt("EDGELENGTH"));
				b.setName(rs.getString("NAME"));
				b.setxTop(rs.getInt("XTOP"));
				b.setyTop(rs.getInt("YTOP"));
				result.put(b.getRef(),b);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return result;
	}
	
	public List<Customization> getCustomizations(long batchId) throws SQLException {
		String 	  databaseName = connectionProp.getProperty("db_name");
		Statement stmt         = null;
		String    query        = format("select c.DATA,c.MODE,c.SIZE,c.X,c.Y from %s.CUSTOMIZATION as c right join" +  
				" (select * from %s.BATCH_CUSTOMIZATION where batch_id=%d) as tmp on c.id=tmp.customizations_ID",databaseName,databaseName,batchId);
		List<Customization> result = new ArrayList<>();
		
		try {
			stmt         = conn.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				String data  = rs.getString("DATA");
				int mode     = rs.getInt("MODE");
				int size     = rs.getInt("SIZE");
				float x      = rs.getFloat("X");
				float y      = rs.getFloat("Y");
				result.add(new ExtendedCustomization(x,y,mode,data,size));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (stmt != null)
				stmt.close();
		}
		return result;
	}
}
