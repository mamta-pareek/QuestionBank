import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

//
@WebServlet("/UserView")
public class UserView extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {

			Connection con = DatabaseConnection.initializeDatabase();
			PreparedStatement st = con
					.prepareStatement("select question_id from question_bank where ques_category = (?);");
			PrintWriter out = response.getWriter();
			JSONObject outputObj = new JSONObject();
			JSONArray output = new JSONArray();

			String categ = request.getParameter("category");
			String num_of_questions = request.getParameter("no_of_questions");

			if (categ != null && num_of_questions != null) {
				if (!(categ.isEmpty()) && !(num_of_questions.isEmpty())) {
					try {
						int num = Integer.parseInt(num_of_questions);

						st.setString(1, categ);

						ResultSet rs = st.executeQuery();

						if (!rs.next()) {
							outputObj.put("result", "no related data found");
							outputObj.put("status", response.getStatus());
							outputObj.put("error", false);
							out.println(outputObj);
						} else {
							List<Integer> id_list = new ArrayList<>();

							do {
								ResultSetMetaData rsmd = rs.getMetaData();
								int columnCount = rsmd.getColumnCount();
								//System.out.println(columnCount);

								int i = 1;
								while (i <= columnCount) {
									id_list.add(rs.getInt(i++));
									//System.out.println(i);
								}

							} while (rs.next());

//							for (int j = 0; j < id_list.size(); j++) {
//								System.out.println(id_list.get(j));
//							}
							Collections.shuffle(id_list);
							PreparedStatement st1 = con
									.prepareStatement("select * from question_bank where question_id = (?);");

							if (num <= id_list.size()) {
								for (int i = 0; i < num; i++) {
									System.out.println(id_list.get(i));

									st1.setInt(1, id_list.get(i));
									ResultSet rs1 = st1.executeQuery();
									if (rs1.next()) {
//										System.out.println(rs1.getString("question_id"));
//										System.out.println(rs1.getString("ques_category"));
//										System.out.println(rs1.getString("question"));
//										System.out.println(rs1.getString("option1"));
//										System.out.println(rs1.getString("option2"));
//										System.out.println(rs1.getString("option3"));
//										System.out.println(rs1.getString("option4"));
//										System.out.println(rs1.getString("ans"));
//										System.out.println(rs1.getString("marks"));

										outputObj.put("question_id", rs1.getString("question_id"));
										outputObj.put("ques_category", rs1.getString("ques_category"));
										outputObj.put("question", rs1.getString("question"));
										outputObj.put("option1", rs1.getString("option1"));
										outputObj.put("option2", rs1.getString("option2"));
										outputObj.put("option3", rs1.getString("option3"));
										outputObj.put("option4", rs1.getString("option4"));
										outputObj.put("ans", rs1.getString("ans"));
										outputObj.put("marks", rs1.getString("marks"));

										output.put(outputObj);
										outputObj = new JSONObject();
									}

								}
								out.println(output);

							} else {
								for (int i = 0; i < id_list.size(); i++) {
									System.out.println(id_list.get(i));

									st1.setInt(1, id_list.get(i));
									ResultSet rs1 = st1.executeQuery();
									if (rs1.next()) {
										outputObj.put("question_id", rs1.getString("question_id"));
										outputObj.put("ques_category", rs1.getString("ques_category"));
										outputObj.put("question", rs1.getString("question"));
										outputObj.put("option1", rs1.getString("option1"));
										outputObj.put("option2", rs1.getString("option2"));
										outputObj.put("option3", rs1.getString("option3"));
										outputObj.put("option4", rs1.getString("option4"));
										outputObj.put("ans", rs1.getString("ans"));
										outputObj.put("marks", rs1.getString("marks"));

										output.put(outputObj);
										outputObj = new JSONObject();
									}

								}
								output.put("we have limited data. Sorry :(");
								out.println(output);
								// System.out.println("we have limited data. Sorry :(");
							}

							st.close();
							con.close();
						}
					} catch (NumberFormatException e) {
						outputObj.put("result", e);
						outputObj.put("status", response.getStatus());
						outputObj.put("error", true);
						out.println(outputObj);

					}
				} else {
					outputObj.put("result", "empty fields not allowed");
					outputObj.put("status", response.getStatus());
					outputObj.put("error", true);
					out.println(outputObj);
				}
			} else {
				outputObj.put("result", "some values are missing.");
				outputObj.put("status", response.getStatus());
				outputObj.put("error", true);
				out.println(outputObj);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
