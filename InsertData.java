

import java.io.IOException;
//import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;



@WebServlet("/InsertData")
public class InsertData extends HttpServlet {
    private static final long serialVersionUID = 1L;
  
    protected void doPost(HttpServletRequest request, 
HttpServletResponse response)
        throws ServletException, IOException
    {
        try {
  
            // Initialize the database
            Connection con = DatabaseConnection.initializeDatabase();
  
            // Create a SQL query to insert data into demo table
            // demo table consists of two columns, so two '?' is used
            PreparedStatement st = con
                   .prepareStatement("INSERT into userinfo(username, password, email, role) VALUES(?, ?, ?, ?)");
            
            String username = request.getParameter("name");
            String password = request.getParameter("password");
            String mail = request.getParameter("mail");
            String role = request.getParameter("role");
            
            PreparedStatement st1 = con
                    .prepareStatement("select * from userinfo where username = (?)");
            st1.setString(1,username);
            ResultSet rs= st1.executeQuery();
            
            if(rs.next()) {
            	JOptionPane.showMessageDialog(null, "UserName Already Exists, Try another" );
            	//response.Write("<script language='JavaScript'>alert('UserName Already Exists, Try another')</script>");
            	response.sendRedirect("Signup.html");
            	st1.close();
            	
            }
            else {
            
  
            st.setString(1,username );
            st.setString(2, password);
            st.setString(3, mail);
            st.setString(4, role);
            
            st.executeUpdate();
            
            //request.login(username, password);
            response.sendRedirect("home.html");
            
        

            st.close();
            con.close();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

