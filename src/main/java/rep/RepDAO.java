package rep;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RepDAO {

		private Connection conn;
		private ResultSet rs;
		
		public RepDAO() {
			try {
					String dbURL = "jdbc:mysql://localhost:3306/BBS?serverTimezone=UTC";
					String dbID = "root";
					String dbPassword = "asdf.3887.";
					Class.forName("com.mysql.cj.jdbc.Driver");
					conn = DriverManager.getConnection(dbURL, dbID, dbPassword);
				
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			public ArrayList<Rep> getList(int bbsID, int pageNumber) {
				String sql = "select * from rep where replyID<? and replyAvailable=1 and bbsID=? order by replyID desc limit 10";
				ArrayList<Rep> list=new ArrayList<Rep>();
				try {
					PreparedStatement pstmt=conn.prepareStatement(sql);
					pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
					pstmt.setInt(2, bbsID);
					rs = pstmt.executeQuery();
					while(rs.next()) {
						Rep rep = new Rep();
						rep.setUserID(rs.getString(1));
						rep.setReplyID(rs.getInt(2));
						rep.setReplyContent(rs.getString(3));
						rep.setBbsID(bbsID);
						rep.setReplyAvailable(1); // rs.getInt(5) => out of index 오류
						list.add(rep);
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				return list;
			}
			
			public int getNext() {
				String sql = "select replyID from rep order by replyID desc";
				try {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					rs = pstmt.executeQuery();
					if(rs.next()) {
						System.out.println(rs.getInt(1)); //select 문에서 첫번째 값
						return rs.getInt(1) + 1; //현재 인덱스(개시글개수) +1 반환
					}
					return 1;
				}catch(Exception e) {
					e.printStackTrace();
				}
				return -1;
			}
			
			public int write(int bbsID, String replyContent, String userID) {
				String sql="insert into rep values(?, ?, ?, ?, ?)";
				try {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setString(1, userID);
					pstmt.setInt(2, getNext());
					pstmt.setString(3, replyContent);
					pstmt.setInt(4, bbsID);
					pstmt.setInt(5, 1);
					return pstmt.executeUpdate();
				}catch(Exception e) {
					e.printStackTrace();
				}
				return -1;
			}
}
