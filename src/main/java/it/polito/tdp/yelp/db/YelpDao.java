package it.polito.tdp.yelp.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Review;
import it.polito.tdp.yelp.model.User;

public class YelpDao {

	
	public List<Business> getAllBusiness(){
		String sql = "SELECT * FROM Business";
		List<Business> result = new ArrayList<Business>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Business business = new Business(res.getString("business_id"), 
						res.getString("full_address"),
						res.getString("active"),
						res.getString("categories"),
						res.getString("city"),
						res.getInt("review_count"),
						res.getString("business_name"),
						res.getString("neighborhoods"),
						res.getDouble("latitude"),
						res.getDouble("longitude"),
						res.getString("state"),
						res.getDouble("stars"));
				result.add(business);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Review> getAllReviews(){
		String sql = "SELECT * FROM Reviews";
		List<Review> result = new ArrayList<Review>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Review review = new Review(res.getString("review_id"), 
						res.getString("business_id"),
						res.getString("user_id"),
						res.getDouble("stars"),
						res.getDate("review_date").toLocalDate(),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("review_text"));
				result.add(review);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Map<String,User> getAllUsers(){
		String sql = "SELECT * FROM Users";
		Map<String,User> result = new HashMap<String,User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.put(user.getUserId(),user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public Map<String,User> getAllUtentiCheHannoFattoXReviews(int nReviews){
		String sql = "SELECT DISTINCT * "
				+ "FROM users " 
				+ "WHERE user_id IN (SELECT DISTINCT user_id "
				+ "						FROM reviews "
				+ "						GROUP BY user_id "
				+ "						HAVING COUNT(review_id)>=?)";
		Map<String,User> result = new HashMap<String,User>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nReviews);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				User user = new User(res.getString("user_id"),
						res.getInt("votes_funny"),
						res.getInt("votes_useful"),
						res.getInt("votes_cool"),
						res.getString("name"),
						res.getDouble("average_stars"),
						res.getInt("review_count"));
				
				result.put(user.getUserId(),user);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Similarity> getAllSimilarity(int nReviews, int anno){
		String sql = "SELECT DISTINCT r1.user_id,r2.user_id, COUNT(r1.business_id=r2.business_id) "
				+ "FROM reviews r1,reviews r2			"
				+ "WHERE r1.user_id IN (SELECT DISTINCT user_id "
				+ "							FROM users "
				+ "							WHERE user_id IN (SELECT DISTINCT user_id "
				+ "													FROM reviews "
				+ "													GROUP BY user_id "
				+ "													HAVING COUNT(review_id)>=?)) "
				+ "AND r2.user_id IN (SELECT DISTINCT user_id "
				+ "							FROM users "
				+ "							WHERE user_id IN (SELECT DISTINCT user_id "
				+ "													FROM reviews\n"
				+ "													GROUP BY user_id\n"
				+ "													HAVING COUNT(review_id)>=?)) "
				+ "AND YEAR(r1.review_date)=? "
				+ "AND YEAR(r2.review_date)=? "
				+ "AND r1.user_id>r2.user_id "
				+ "AND r1.business_id=r2.business_id "
				+ "GROUP BY r1.user_id,r2.user_id";
		List<Similarity> result = new ArrayList<Similarity>();
		Connection conn = DBConnect.getConnection();
		Map<String,User> m= this.getAllUsers();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, nReviews);
			st.setInt(2, nReviews);
			st.setInt(3, anno);
			st.setInt(4, anno);
			ResultSet res = st.executeQuery();
			
			while (res.next()) {
				
				Similarity similarity=new Similarity(m.get(res.getString("r1.user_id")),
						m.get(res.getString("r2.user_id")),
						res.getDouble("COUNT(r1.business_id=r2.business_id)"));
				
				result.add(similarity);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
