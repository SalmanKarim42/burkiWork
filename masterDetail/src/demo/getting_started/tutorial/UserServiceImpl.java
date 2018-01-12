package demo.getting_started.tutorial;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.zkoss.zk.ui.util.Clients;

public class UserServiceImpl implements UserService {

	Connection conn = null;
	PreparedStatement stmt = null;
	ResultSet rs = null;

	// data model
	private List<User> userList = new ArrayList<User>();

	public UserServiceImpl() {
		try {
			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(
					"jdbc:postgresql://localhost/postgres", "postgres",
					"postgres");
		} catch (Exception e) {
			System.out.println("Connection Failed because " + e);
		}
	}

	public void fetch_master() {
		try {
			// load driver and get a database connection
			stmt = conn.prepareStatement("select * from public.zkdb_master");
			rs = stmt.executeQuery();
			while (rs.next()) {
				userList.add(new User(rs.getInt(1), rs.getString(2)));
			}
		} catch (Exception e) {
			System.out.println("wahaj exception " + e);
		}
	}

	public List<User> fetch_detail(int selected_id) {
		List<User> detaillist = new ArrayList<User>();
		try {
			// load driver and get a database connection
			stmt = conn
					.prepareStatement("select email,phone from public.zkdb_detail where id="
							+ selected_id);
			rs = stmt.executeQuery();
			while (rs.next()) {
				detaillist.add(new User(rs.getString(1), rs.getString(2)));
			}
		} catch (Exception e) {
			System.out.println("wahaj exception " + e);
		}
		return detaillist;
	}

	public boolean insert_master(int id, String name) {
		try {
			// load driver and get a database connection
			stmt = conn
					.prepareStatement("insert into public.zkdb_master values(?,?)");
			stmt.setInt(1, id);
			stmt.setString(2, name);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			Clients.showNotification(id + " is already Exist");
			return false;
		}
	}

	public List<User> findAll() {
		return userList;
	}
	public boolean insert_detail(String email, String phone,int id) {
		try {
			// load driver and get a database connection
			stmt = conn.prepareStatement("insert into public.zkdb_detail values(nextval('seq_id'),?,?,?)");
			stmt.setString(1, email);
			stmt.setString(2, phone);
			stmt.setInt(3, id);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			Clients.showNotification(id+" not inserted in master ");
			return false;
		}
	}
	public List<User> search(String keyword) {
		List<User> result = new ArrayList<User>();
		if (keyword == null || "".equals(keyword)) {
			result = userList;
		} else {
			for (User c : userList) {
				if (c.getId().toString().toLowerCase()
						.contains(keyword.toLowerCase())
						|| c.getName().toLowerCase()
								.contains(keyword.toLowerCase())) {
					result.add(c);
				}
			}

		}
		return result;
	}

	public synchronized List<User> saveDetail(User user) {
		List<User> detaillist = new ArrayList<User>();
		detaillist.add(user);
		return detaillist;
	}
}