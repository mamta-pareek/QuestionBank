
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/user_view")
public class user_view extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Connection con = DatabaseConnection.initializeDatabase();
			PrintWriter out = response.getWriter();
			JSONObject outputObj = new JSONObject();
			JSONArray output = new JSONArray();
			//List<JSONObject> arr = new ArrayList<JSONObject>();

			PreparedStatement st = con.prepareStatement("select * from question_bank where ques_category = (?);");

			String categ = request.getParameter("category");
			String num_of_questions = request.getParameter("no_of_questions");

			if (categ != null && !(categ.isEmpty()) && num_of_questions != null && !(num_of_questions.isEmpty())) {
				try {

					int num = Integer.parseInt(num_of_questions);

					st.setMaxRows(num);

					st.setString(1, categ);

					ResultSet rs = st.executeQuery();
					// rs.beforeFirst();
					if (!rs.next()) {
						outputObj.put("result", "no related data found");
						outputObj.put("status", response.getStatus());
						outputObj.put("error", false);
						out.println(outputObj);
					} else {

						do {
							
							outputObj.put("question_id", rs.getString("question_id"));
							outputObj.put("ques_category", rs.getString("ques_category"));
							outputObj.put("ques_category", rs.getString("question"));
							outputObj.put("option1", rs.getString("option1"));
							outputObj.put("option2", rs.getString("option2"));
							outputObj.put("option3", rs.getString("option3"));
							outputObj.put("option4", rs.getString("option4"));
							outputObj.put("ans", rs.getString("ans"));
							outputObj.put("marks", rs.getString("marks"));

							output.put(outputObj);
							outputObj = new JSONObject();
							
						} while (rs.next());
					
						out.println(output);

					}

					st.close();
					con.close();

				} catch (NumberFormatException e) {
					outputObj.put("result", e);
					outputObj.put("status", response.getStatus());
					outputObj.put("error", true);
					out.println(outputObj);

					// out.println("<html><body><b>only integer values are allowed for number of
					// questions"
					// + "</b></body></html>");
				}
			} else {
				outputObj.put("result", "empty fields not allowed");
				outputObj.put("status", response.getStatus());
				outputObj.put("error", true);
				out.println(outputObj);
				// out.println("<html><body><b>empty fields not allowed" +
				// "</b></body></html>");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

//
//
///* console.log("sending msg");
//obj = {name: "John", age: 30, city: "New York"};
//obj1 = {name: "mamta", age: 30, city: "New York"};
//obj.test="mamta";
//
//arr = [1,"2",obj1];
//obj.arr = arr;
//myJSON = JSON.stringify(obj);
//console.log(obj.arr[2]);
//console.log(myJSON); */
//
//msgobj = {
//	req_type : 1,
//	frndname : message.value
//};
////msgobj.frndname = message;
//console.log(msgobj);
///* test = JSON.stringify(msgobj);
//console.log(test); */
//
//webSocket.send(JSON.stringify(msgobj));
//console.log("sent");
