package demo.getting_started.mvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.zkoss.lang.Integers;
import org.zkoss.lang.Strings;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.select.SelectorComposer;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zk.ui.util.Clients;
import org.zkoss.zul.Button;
import org.zkoss.zul.Div;
import org.zkoss.zul.Intbox;
import org.zkoss.zul.Label;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;
import org.zkoss.zul.Listcell;
import org.zkoss.zul.Listitem;
import org.zkoss.zul.Textbox;
import org.zkoss.zul.ext.Selectable;

import demo.getting_started.tutorial.User;
import demo.getting_started.tutorial.UserService;
import demo.getting_started.tutorial.UserServiceImpl;

public class SearchController extends SelectorComposer<Component> {

	private static final long serialVersionUID = 1L;

	@Wire
	private Textbox keywordBox, txt_name, txt_email, txt_phone;
	@Wire
	Intbox txt_id;
	@Wire
	private Listbox userListbox, detailListbox, insertdetail;
	@Wire
	private Label ID;
	@Wire
	private Label NAME;
	@Wire
	private Component detailBox, detail_box;
	@Wire
	Button masterclick, detailclick, insertdb, addmaster;
	@Wire
	Div main, form;
	ListModelList<User> detailListModel;
	User detail;
	List<User> dummyresult;
	private UserService userService = new UserServiceImpl();

	@Override
	public void doAfterCompose(Component comp) throws Exception {
		super.doAfterCompose(comp);
		userService.fetch_master();
		List<User> result = userService.findAll();
		userListbox.setModel(new ListModelList<User>(result));
	}

	@Listen("onClick = #searchButton")
	public void search() {
		String keyword = keywordBox.getValue();
		List<User> result = userService.search(keyword);
		userListbox.setModel(new ListModelList<User>(result));

	}
	@Listen("onDoubleClick=#userListbox")
	public void insert_d(){
		this.masterclick();
		Set<User> selection = ((Selectable<User>) userListbox.getModel())
				.getSelection();
		if (selection != null && !selection.isEmpty()) {
			User selected = selection.iterator().next();
			int id = selected.getId();
			String name=selected.getName();
			txt_id.setValue(id);
			txt_name.setValue(name);
			detail_box.setVisible(true);
			txt_id.setDisabled(true);
			txt_name.setDisabled(true);
			addmaster.setLabel("New");
		}
	}

	@Listen("onSelect = #userListbox")
	public void showDetail() {
		detailBox.setVisible(true);
		Set<User> selection = ((Selectable<User>) userListbox.getModel())
				.getSelection();
		if (selection != null && !selection.isEmpty()) {
			User selected = selection.iterator().next();
			int id = selected.getId();
			String name = selected.getName();
			List<User> result = userService.fetch_detail(id);
			detailListbox.setModel(new ListModelList<User>(result));
		}
	}

	@Listen("onClick=#masterclick")
	public void masterclick() {

		if (masterclick.getLabel().equals("Insert")) {
			main.setVisible(false);
			form.setVisible(true);
			txt_id.setValue(null);
			txt_name.setValue(null);
			txt_id.setDisabled(false);
			addmaster.setLabel("Add");
			detail_box.setVisible(false);
			txt_name.setDisabled(false);
			masterclick.setLabel("Toggle");
		} else {
			form.setVisible(false);
			insertdetail.getItems().clear();
			main.setVisible(true);
			masterclick.setLabel("Insert");
		}
	}

	@Listen("onClick=#addmaster")
	public void insertmaster() {
		try {
			int id = txt_id.getValue();
			String name = txt_name.getValue();
			if (addmaster.getLabel().equals("Add")) {
				if (userService.insert_master(id, name)) {
					detail_box.setVisible(true);
					//userListbox.setModel(detailListModel);
					txt_id.setDisabled(true);
					txt_name.setDisabled(true);
					addmaster.setLabel("New");
				}
			} else {
				txt_id.setValue(null);
				txt_name.setValue(null);
				txt_id.setDisabled(false);
				txt_name.setDisabled(false);
				addmaster.setLabel("Add");
				insertdetail.getItems().clear();
				detail_box.setVisible(false);
			}
		} catch (NullPointerException e) {
			detail_box.setVisible(false);
			Clients.showNotification("Id must be not null");
		}
	}
    int a = 0;
	@Listen("onClick=#addrow")
	public void addrow() {
		Textbox txt1 = new Textbox();
		Textbox txt2 = new Textbox();
		// txt1.setId("1");
		// txt1.setId("2");
		Listcell l1 = new Listcell();
		Listcell l2 = new Listcell();
		Listcell l3 = new Listcell();
		l1.appendChild(txt1);
		l2.appendChild(txt2);
		Listitem it = new Listitem();
		it.setId("a"+a++);
		it.appendChild(l1);
		it.appendChild(l2);
		it.appendChild(l3);
		insertdetail.appendChild(it);
	}

	@Listen("onClick=#insertdb")
	public void insertdetail() {
		boolean tof = false;
		int id = txt_id.getValue();
		List<Listitem> items = insertdetail.getItems();
		int count = insertdetail.getItemCount();
		for (int i = 0; i < count; i++) {
			Textbox[] txt = new Textbox[2];
			for (int j = 0; j < 2; j++) {
				txt[j] = (Textbox) items.get(i).getChildren().get(j)
						.getFirstChild();
			}
			if(!Strings.isBlank(txt[0].getValue()) && !Strings.isBlank(txt[1].getValue())){
			tof = userService.insert_detail(txt[0].getValue(),
					txt[1].getValue(), id);
			}
		}
		if (tof) {
			insertdetail.getItems().clear();
			Clients.showNotification("Data Inserted");
		}
	}
}

// @Listen("onClick=#detailclick")
// public void detailclick() {
// Include include = (Include)Selectors.iterable(tb.getPage(), "#detailpg")
// .iterator().next();
// if(include.getSrc()=="/detail1.zul") {
// include.setSrc("/detail2.zul");
// detailclick.setLabel("Toggle");
// }
// else {
// include.setSrc("/detail1.zul");
// detailclick.setLabel("Insert");
// }
// }
