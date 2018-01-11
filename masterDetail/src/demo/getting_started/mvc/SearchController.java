package demo.getting_started.mvc;

import java.util.List;
import java.util.Set;

import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Image;
import org.zkoss.zul.Include;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Popup;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ext.Selectable;

import demo.getting_started.tutorial.User;
import demo.getting_started.tutorial.UserService;
import demo.getting_started.tutorial.UserServiceImpl;

public class SearchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Textbox keywordBox;
	@Wire
	private Listbox userListbox,detailListbox;
	@Wire
	private Label ID;
	@Wire
	private Label NAME;
	@Wire
	private Component detailBox;
	@Wire
	Button masterclick,detailclick;
	@Wire
	Div main,form;
	

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);	
		userService.fetch_master();
		List<User> result = userService.findAll();
		userListbox.setModel(new ListModelList<User>(result));
	}

	private UserService userService = new UserServiceImpl();

	@Listen("onClick = #searchButton")
	public void search() {
		String keyword = keywordBox.getValue();
		List<User> result = userService.search(keyword);
		Clients.showBusy(userListbox, "Data fetching");
		if(result.size() != 0) {
			Clients.showNotification("Data Inserted",userListbox);

		} else {
			Clients.showNotification("No Data Found", userListbox);
			
		}
		Clients.clearBusy(userListbox);
		userListbox.setModel(new ListModelList<User>(result));

	}

	@Listen("onSelect = #userListbox")
	public void showDetail() {
		detailBox.setVisible(true);
		Set<User> selection = ((Selectable<User>) userListbox.getModel())
				.getSelection();
		if (selection != null && !selection.isEmpty()) {
			User selected = selection.iterator().next();
			int id=selected.getId();
			String name=selected.getName();
			List<User> result =userService.fetch_detail(id);
			detailListbox.setModel(new ListModelList<User>(result));
		}
	}
	@Listen("onClick=#masterclick") 
	public void masterclick() {

		if(masterclick.getLabel().equals("Insert")){
				main.setVisible(false);
				form.setVisible(true);
				masterclick.setLabel("Toggle");
				}
		else{
				form.setVisible(false);
				main.setVisible(true);
				masterclick.setLabel("Insert");
				}
		
//Include include = (Include)Selectors.iterable(tb.getPage(), "#masterpg")
//				.iterator().next();	
//if(include.getSrc()!="/master2.zul") {
//		include.setSrc("/master2.zul");
//		}
//		else {
//			include.setSrc("/master1.zul");
		
//		}
	}
	
//	@Listen("onClick=#detailclick") 
//	public void detailclick() {
//				Include include = (Include)Selectors.iterable(tb.getPage(), "#detailpg")
//						.iterator().next();
//				if(include.getSrc()=="/detail1.zul") {
//				include.setSrc("/detail2.zul");
//				detailclick.setLabel("Toggle");
//				}
//				else {
//					include.setSrc("/detail1.zul");
//					detailclick.setLabel("Insert");
//				}
//	}
}
