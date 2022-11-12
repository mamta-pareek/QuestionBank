
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

/**
 * Servlet implementation class AdminUpdate
 */
@WebServlet("/AdminUpdate")
public class AdminUpdate extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Connection con = DatabaseConnection.initializeDatabase();
			String query1 = "select * from question_bank where question_id = (?);";
			String query2 = "update question_bank set ques_category=(?), question = (?), option1=(?), option2=(?), option3=(?), option4=(?), ans=(?), marks=(?) where question_id = (?);";

			PreparedStatement st1 = con.prepareStatement(query1);
			PrintWriter out = response.getWriter();
			JSONObject outputObj = new JSONObject();

			String ques_id = request.getParameter("questionID");

			if (ques_id != null && !(ques_id.isEmpty())) {
				//System.out.println("hello");

				st1.setString(1, ques_id);

				ResultSet rs = st1.executeQuery();

				if (rs.next()) {

					String categ = request.getParameter("category");
					String ques = request.getParameter("question");
					String op1 = request.getParameter("option1");
					String op2 = request.getParameter("option2");
					String op3 = request.getParameter("option3");
					String op4 = request.getParameter("option4");
					String ans = request.getParameter("answer");
					String marks = request.getParameter("marks");

					if (categ != null && ques != null && op1 != null && op2 != null && op3 != null && op4 != null
							&& ans != null && marks != null) {
						if (!(categ.isEmpty()) && !(ques.isEmpty()) && !(op1.isEmpty()) && !(op2.isEmpty())
								&& !(op3.isEmpty()) && !(op4.isEmpty()) && !(ans.isEmpty()) && !(marks.isEmpty())) {
							if (categ.matches("[a-zA-Z]+")) {
								try {
									int mark = Integer.parseInt(marks);

								PreparedStatement st2 = con.prepareStatement(query2);

								st2.setString(1, categ);
								st2.setString(2, ques);
								st2.setString(3, op1);
								st2.setString(4, op2);
								st2.setString(5, op3);
								st2.setString(6, op4);
								st2.setString(7, ans);
								st2.setInt(8, mark);
								st2.setString(9, ques_id);

								st2.executeUpdate();
								outputObj.put("result", "Successfully updated");
								outputObj.put("status", response.getStatus());
								outputObj.put("error", false);
								out.println(outputObj);
							

								//out.println("<html><body><b>Successfully updated" + "</b></body></html>");

								st2.close();
								}
								catch (NumberFormatException e) {
									outputObj.put("result", "only integer values are allowed in marks field");
									outputObj.put("status", response.getStatus());
									outputObj.put("error", true);
									out.println(outputObj);
								}
							} else {
								
								outputObj.put("result", "only alphabatic letters allowed for category field");
								outputObj.put("status", response.getStatus());
								outputObj.put("error", true);
								out.println(outputObj);
								//out.println("<html><body><b>only alphabatic letters allowed for category field"
										//+ "</b></body></html>");
							}
						} else {
							outputObj.put("result", "Empty fields not allowed");
							outputObj.put("status", response.getStatus());
							outputObj.put("error", true);
							out.println(outputObj);
							//out.println("<html><body><b>Empty fields not allowed" + "</b></body></html>");
						}
					} else {
						outputObj.put("result", "Some values are missing");
						outputObj.put("status", response.getStatus());
						outputObj.put("error", true);
						out.println(outputObj);
						//out.println("<html><body><b>Some values are missing" + "</b></body></html>");
					}
				}

				else {
					outputObj.put("result", "no related data found!!");
					outputObj.put("status", response.getStatus());
					outputObj.put("error", false);
					out.println(outputObj);
					//out.println("<html><body><b>no related data found!! " + "</b></body></html>");
				}
			} else {
				outputObj.put("result", "Input not given for Question ID");
				outputObj.put("status", response.getStatus());
				outputObj.put("error", true);
				out.println(outputObj);
				//out.println("<html><body><b> Input not given for Question ID " + "</b></body></html>");
			}
			

			st1.close();
			con.close();
			

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
