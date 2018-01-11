package demo.getting_started.tutorial;

import java.util.List;

public interface UserService {


	public List<User> findAll();
	public void fetch_master();
	public List<User> fetch_detail(int selected_id);
	public List<User> search(String keyword);
}
