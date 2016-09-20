package de.openhabskill.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemRepository;
import de.openhabskill.entity.ItemType;

/**
 * Simple CRUD UI for editing items. It's basically the example from here:
 * https://spring.io/guides/gs/crud-with-vaadin/
 * 
 * @author Reinhard
 *
 */
@SpringUI
@Theme("valo")
public class ItemUi extends UI {
	private static final long serialVersionUID = 1L;

	private final ItemRepository itemRepository;

	private final Grid grid;

	private final Button addNewBtn;

	private final ItemEditor editor;

	@Autowired
	ItemUi(ItemRepository itemRepository, ItemEditor editor) {
		this.itemRepository = itemRepository;
		this.grid = new Grid();
		this.editor = editor;
		this.addNewBtn = new Button("New item", FontAwesome.PLUS);
	}

	@Override
	protected void init(VaadinRequest request) {
		setContent(grid);
		listItems();
	}

	@SuppressWarnings("unchecked")
	private void listItems() {
		// build layout
		HorizontalLayout actions = new HorizontalLayout(addNewBtn);
		VerticalLayout mainLayout = new VerticalLayout(actions, grid, editor);
		setContent(mainLayout);

		// Configure layouts and components
		actions.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(300, Unit.PIXELS);
		grid.setWidth(900, Unit.PIXELS);
		grid.setColumns("itemName", "itemType", "location", "openHabItem");

		// Connect selected Customer to editor or hide if none is selected
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editor.setVisible(false);
			} else {
				editor.editItem((Item) grid.getSelectedRow());
			}
		});

		// Instantiate and edit new Item the new button is clicked
		addNewBtn.addClickListener(e -> editor.editItem(new Item(null, "", ItemType.SWITCH, "", "")));

		// Listen changes made by the editor, refresh data from backend
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listCustomers("");
		});

		grid.setContainerDataSource(new BeanItemContainer(Item.class, itemRepository.findAll()));
	}

	private void listCustomers(String text) {
		if (StringUtils.isEmpty(text)) {
			grid.setContainerDataSource(new BeanItemContainer(Item.class, itemRepository.findAll()));
		} else {
			// grid.setContainerDataSource(
			// new BeanItemContainer(Item.class,
			// itemRepository.findByLastNameStartsWithIgnoreCase(text)));
		}
	}
}
