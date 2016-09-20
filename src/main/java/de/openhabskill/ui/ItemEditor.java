package de.openhabskill.ui;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import de.openhabskill.entity.Item;
import de.openhabskill.entity.ItemRepository;
import de.openhabskill.entity.ItemType;

@SpringComponent
@UIScope
public class ItemEditor extends VerticalLayout {
	private static final long serialVersionUID = 1L;

	private static float FIELD_WIDTH = 300;

	private final ItemRepository repository;

	/**
	 * The currently edited customer
	 */
	private Item item;

	/* Fields to edit properties in Customer entity */
	TextField itemName = new TextField("Item name");
	ComboBox itemType = new ComboBox("Item type");
	TextField location = new TextField("Location");
	TextField openHabItem = new TextField("OpenHab Item");
	Label infoLabel = new Label();

	/* Action buttons */
	Button save = new Button("Save", FontAwesome.SAVE);
	Button cancel = new Button("Cancel");
	Button delete = new Button("Delete", FontAwesome.TRASH_O);
	CssLayout actions = new CssLayout(save, cancel, delete);

	@Autowired
	public ItemEditor(ItemRepository repository) {
		this.repository = repository;

		VerticalLayout items = new VerticalLayout();
		VerticalLayout info = new VerticalLayout();

		itemName.setWidth(FIELD_WIDTH, Unit.PIXELS);

		itemType.setWidth(FIELD_WIDTH, Unit.PIXELS);
		itemType.addValueChangeListener(e -> changeInfoLabel(item));
		itemType.addItems(ItemType.values());

		location.setWidth(FIELD_WIDTH, Unit.PIXELS);

		openHabItem.setWidth(FIELD_WIDTH, Unit.PIXELS);

		infoLabel.setWidth(FIELD_WIDTH * 2, Unit.PIXELS);

		items.addComponents(itemType, itemName, location, openHabItem, actions);
		info.addComponents(infoLabel);

		HorizontalLayout main = new HorizontalLayout(items, info);
		main.setSpacing(true);
		addComponents(main);

		// Configure and style components
		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setStyleName(ValoTheme.BUTTON_PRIMARY);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// wire action buttons to save, delete and reset
		save.addClickListener(e -> repository.save(item));
		delete.addClickListener(e -> repository.delete(item));
		cancel.addClickListener(e -> editItem(item));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editItem(Item it) {
		final boolean persisted = it.getId() != null;
		if (persisted) {
			// Find fresh entity for editing
			item = repository.findOne(it.getId());
		} else {
			item = it;
		}
		cancel.setVisible(persisted);

		// Bind item properties to similarly named fields
		// Could also use annotation or "manual binding" or programmatically
		// moving values from fields to entities before saving
		BeanFieldGroup.bindFieldsUnbuffered(item, this);

		setVisible(true);

		// A hack to ensure the whole form is visible
		save.focus();
		// Select all text in firstName field automatically
		itemName.selectAll();
	}

	public void setChangeHandler(ChangeHandler h) {
		// ChangeHandler is notified when either save or delete
		// is clicked
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

	public void changeInfoLabel(Item item) {
		switch (item.getItemType()) {
		case COLOR:
			infoLabel.setCaption("Color intent");
			infoLabel.setValue(
					"Can be used to control RGB lights. This is for the OpenHab Color Item. For item name the mandatory "
							+ "value is 'COLOR'. I figured it is enough to support one Color item per location. So I skiped the item name "
							+ "value.");
			itemName.setValue("COLOR");
			itemName.setEnabled(false);
			break;
		case ROLLERSHUTTER:
			infoLabel.setCaption("Rollershutter intent");
			infoLabel.setValue("You can use something like 'north', 'small' or 'all' as item name.");
			itemName.setEnabled(true);
			break;
		case NUMBER:
			infoLabel.setCaption("Number intent");
			infoLabel.setValue(
					"Right now, this is only for asking for temperature values. So the only possible item name is 'temperature'");
			itemName.setValue("TEMPERATURE");
			itemName.setEnabled(false);
			break;
		case TV:
			infoLabel.setCaption("TV intent");
			infoLabel.setValue(
					"This intent is WIP! - For the switch channel command put 'CHANNEL' in the item name field. Otherwise put "
							+ " the name of the activity or action in the item name field. For the location, the only supported is"
							+ "'living room' right now. So it has to be living room in the location.");
			itemName.setEnabled(true);
			break;
		default:
			infoLabel.setCaption("default intent");
			infoLabel.setValue("For item name use something like light or socket. You must use singular expressions!");
			itemName.setEnabled(true);
			break;
		}
	}
}