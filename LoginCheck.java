

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;


@WebServlet("/LoginCheck")
public class LoginCheck extends HttpServlet {
    private static final long serialVersionUID = 1L;
  
    protected void doPost(HttpServletRequest request, 
HttpServletResponse response)
        throws ServletException, IOException
    {
        try {
  
            // Initialize the database
            Connection con = DatabaseConnection.initializeDatabase();
  
            PreparedStatement st = con
                   .prepareStatement("select * from userinfo where username = (?) and password = (?) and role = (?);");
            
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String role = request.getParameter("role");

            
  
            st.setString(1,username );
            st.setString(2, password);
            st.setString(3, role);
            
            ResultSet rs= st.executeQuery();
            
            if(rs.next()) {
            
            response.sendRedirect("home.html");
            
            st.close();
            con.close();
            }
            else {
            	JOptionPane.showMessageDialog(null, "UserName or password doesn't exist, Try another" );
            	response.sendRedirect("Login.html");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
