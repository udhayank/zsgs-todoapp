package todo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class DataBaseController {

	Connection connection;
	Statement statement;
	
	public DataBaseController() {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/tododb", "root", "password");
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public JSONObject getAllToDos() throws SQLException {
		
		String query = "SELECT id, text, status FROM todo";
		
		ResultSet result = statement.executeQuery(query);
		
		JSONArray resArray = new JSONArray();
		
		while(result.next()) {
			
			JSONObject obj = new JSONObject();
			obj.put("id", result.getInt("id"));
			obj.put("text", result.getString("text"));
			obj.put("status", result.getBoolean("status"));
			resArray.add(obj);
			
		}
		
		JSONObject json = new JSONObject();
		json.put("data", resArray);
		
		return json;
		
	}

	public JSONObject addToDo(String text) throws SQLException {
		
		String query = "INSERT INTO todo (text) VALUES (\"" + text + "\");";
		
		PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		
		stmt.execute();
		
		ResultSet result = stmt.getGeneratedKeys();
		
		int key = -1;
		if (result.next()) {
			key = result.getInt(1);
		}
		
		if (key > 0) {
			JSONObject json = getById(key);
			return json;
		}
		
		return null;
	}

	@SuppressWarnings("unchecked")
	public JSONObject getById(int key) throws SQLException {
		
		String query = "SELECT id, text, status FROM todo WHERE id = " + key;
		
		ResultSet result = statement.executeQuery(query);
		
		JSONObject obj = new JSONObject();
		
		if(result.next()) {
			
			obj.put("id", result.getInt("id"));
			obj.put("text", result.getString("text"));
			obj.put("status", result.getBoolean("status"));
				
		}
		
		return obj;
		
	}

	public boolean deleteById(String id) {
		
		String query = "DELETE from todo WHERE id=" + id;
		try {
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}

	public boolean updateStatus(String id, String status) {
		
		String query = "UPDATE todo SET status="+status+" WHERE id="+id;
		try {
			statement.executeUpdate(query);
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
	}
	
}
