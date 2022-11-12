
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/AdminDelete")
public class AdminDelete extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Connection con = DatabaseConnection.initializeDatabase();
			String query1 = "select * from question_bank where question_id=(?);"; // for checking
																					// if record
																					// exists or
																					// not
			String query2 = "delete from question_bank where question_id = (?);"; // deleting
																					// record if
																					// exists

			PreparedStatement st1 = con.prepareStatement(query1);
			PrintWriter out = response.getWriter();
			JSONObject outputObj = new JSONObject();

		
			String quesId = request.getParameter("question_id");

			if (quesId != null && !(quesId.isEmpty())) {

				try {
					int ID = Integer.parseInt(quesId);

					st1.setInt(1, ID);

					ResultSet rs = st1.executeQuery();

					if (rs.next()) {
						PreparedStatement st2 = con.prepareStatement(query2);
						st2.setInt(1, ID);

						st2.executeUpdate();
						outputObj.put("result", "successfully deleted");
						outputObj.put("status", response.getStatus());
						outputObj.put("error", false);
						out.println(outputObj);

						//out.println("<html><body><b>Successfully deleted" + "</b></body></html>");

						st2.close();
					}

					else {
						outputObj.put("result", "no related data found!!");
						outputObj.put("status", response.getStatus());
						outputObj.put("error", false);
						out.println(outputObj);
						//out.println("<html><body><b>no related data found!! " + "</b></body></html>");
					}
				} catch (NumberFormatException e) {
					outputObj.put("result", "only integer values are allowed in  question_id");
					outputObj.put("status", response.getStatus());
					outputObj.put("error", true);
					out.println(outputObj);
					//out.println(
							//"<html><body><b>only integer values are allowed in  question_id" + "</b></body></html>");
				}
			} else {
				outputObj.put("result", "Empty field not allowed.");
				outputObj.put("status", response.getStatus());
				outputObj.put("error", true);
				out.println(outputObj);
				//out.println("<html><body><b> Empty field not allowed. " + "</b></body></html>");
			}

			st1.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
