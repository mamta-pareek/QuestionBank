
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@WebServlet("/AdminAdd")
public class AdminAdd extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Connection con = DatabaseConnection.initializeDatabase();
			PrintWriter out = response.getWriter();
			JSONObject outputObj = new JSONObject();

			PreparedStatement st = con.prepareStatement(
					"insert into question_bank( ques_category, question, option1, option2, option3, option4, ans, marks )"
							+ "values(?, ?, ?, ?, ?, ?, ?, ?)");

			String categ = request.getParameter("category");
			String ques = request.getParameter("question");
			String op1 = request.getParameter("option1");
			String op2 = request.getParameter("option2");
			String op3 = request.getParameter("option3");
			String op4 = request.getParameter("option4");
			String ans = request.getParameter("answer");
			String marks = request.getParameter("marks");

			if (categ != null && ques != null && op1 != null && op2 != null && op3 != null && op4 != null && ans != null
					&& marks != null) {

				if (!(categ.isEmpty()) && !(ques.isEmpty()) && !(op1.isEmpty()) && !(op2.isEmpty()) && !(op3.isEmpty())
						&& !(op4.isEmpty()) && !(ans.isEmpty()) && !(marks.isEmpty())) {

					if (categ.matches("[a-zA-Z]+")) {

						if (ans.equals(op1) || ans.equals(op2)|| ans.equals(op3) || ans.equals(op4)) {
							try {
								int mark = Integer.parseInt(marks);

								st.setString(1, categ);
								st.setString(2, ques);
								st.setString(3, op1);
								st.setString(4, op2);
								st.setString(5, op3);
								st.setString(6, op4);
								st.setString(7, ans);
								st.setInt(8, mark);

								st.executeUpdate();
								outputObj.put("result", "successfully inserted");
								outputObj.put("status", response.getStatus());
								outputObj.put("error", false);
								out.println(outputObj);

								// out.println("<html><body><b>Successfully Inserted" + "</b></body></html>");
							} catch (NumberFormatException e) {
								outputObj.put("result", "only integer values are allowed in marks field");
								outputObj.put("status", response.getStatus());
								outputObj.put("error", true);
								out.println(outputObj);
								// out.println("<html><body><b>only integer values are allowed in marks field"
								// + "</b></body></html>");
							}
						} else {
							outputObj.put("result", "answer should be one of the options.");
							outputObj.put("status", response.getStatus());
							outputObj.put("error", true);
							out.println(outputObj);

						}

					} else {
						outputObj.put("result", "Only alphabetic characters are permitted");
						outputObj.put("status", response.getStatus());
						outputObj.put("error", true);
						out.println(outputObj);
						// out.println("<html><body><b>Only alphabetic characters are permitted" +
						// "</b></body></html>");
					}

				} else {
					outputObj.put("result", "empty string not allowed");
					outputObj.put("status", response.getStatus());
					outputObj.put("error", true);

					out.println(outputObj);

					// out.println("<html><body><b>empty string not allowed" +
					// "</b></body></html>");
				}

			} else {
				outputObj.put("result", "some values are missing");
				outputObj.put("status", response.getStatus());
				outputObj.put("error", true);
				out.println(outputObj);
				// out.println("<html><body><b>some values not given" + "</b></body></html>");

			}

			st.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
