package todo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


//@WebServlet("/ToDoServlet")
public class ToDoServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private DataBaseController db;
       
    
    public ToDoServlet() {
        super();
        // TODO Auto-generated constructor stub
        db = new DataBaseController();
    }
	
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	String method = request.getMethod();
    	if (method.equals("PATCH")) {
    		this.doPatch(request, response);
    	} else {
    		super.service(request, response);
    	}
    	
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		System.out.println("get");
		try {
			JSONObject json = db.getAllToDos();
			response.setContentType("application/json");
			response.getWriter().append(json.toJSONString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		StringBuffer sb =new StringBuffer();
		
		Scanner sc = new Scanner(request.getInputStream());
		
		while (sc.hasNextLine()) {
			sb.append(sc.nextLine());
		}
		
		try {
			JSONObject jo = (JSONObject) new JSONParser().parse(sb.toString());
			String text = (String) jo.get("text");
			JSONObject createdJson = db.addToDo(text);
			response.setContentType("application/json");
			response.getWriter().append(createdJson.toJSONString());
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (SQLException e) {			
			e.printStackTrace();
		} 
		
		
	}
	
	protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String status = request.getParameter("status");
		
		System.out.println("update " + id + " " + status);
		
		if (db.updateStatus(id, status)) {
			response.setStatus(200);
		}
		
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		String id = request.getParameter("id");
		System.out.println("delete " + id);
		
		if (db.deleteById(id)) {
			response.setStatus(200);
		}
		
	}

}

