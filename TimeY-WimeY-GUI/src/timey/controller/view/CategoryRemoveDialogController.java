package timey.controller.view;

import org.controlsfx.control.action.Action;
import org.controlsfx.dialog.Dialog;
import org.controlsfx.dialog.Dialogs;

import timey.controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> CategoryRemoveDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 12:51:22
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the Category Remove Dialog.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
@SuppressWarnings("deprecation")
public class CategoryRemoveDialogController {
	@FXML
	/**
	 * ComboBox for selecting a category
	 */
	private ComboBox<String> categoryCombo;
	/**
	 * Stage of this dialog
	 */
	private Stage dialogStage;
	/**
	 * Boolean for the remove button
	 */
	private boolean removeClicked = false;
	/**
	 * MainApp of TimeY-WimeY
	 */
	private MainApp mainApp;
	/**
	 * List of categories
	 */
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	/**
	 * Initializes the Dialog
	 */
	private void initialize() {
	}

	/**
	 * Sets MainApp to this dialog
	 * 
	 * @param main
	 *            is the mainApp
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	/**
	 * Sets the Stage for this dialog
	 * 
	 * @param dialogStage
	 *            is the stage of this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Gets the categories of the user from the database and sets it into the
	 * ComboBox
	 */
	public void setData() {
		categoryData = mainApp.getDBConn().getCategories(mainApp.getUserID());
		categoryCombo.setItems(categoryData);
	}

	/**
	 * Getter of isRemoveClicked
	 * 
	 * @return isRemoveClicked
	 */
	public boolean isRemoveClicked() {
		return removeClicked;
	}

	@FXML
	/**
	 * Handles the action if remove is clicked. Removes category from database
	 */
	private void handleRemove() {
		String selected = categoryCombo.getSelectionModel().getSelectedItem();
		Action response = Dialogs
				.create()
				.title("Remove Category: " + selected)
				.masthead(
						"You want to delete "
								+ selected
								+ "! All times with that category will be deleted too!")
				.message("Are you sure you want to delete " + selected + "?")
				.showConfirm();

		if (response == Dialog.ACTION_YES) {
			for (int i = 0; i < categoryData.size(); i++) {
				if (selected.compareTo(categoryData.get(i)) == 0) {
					categoryData.remove(i);
					mainApp.getDBConn().handleRemoveCategory(selected,
							mainApp.getUserID());
				}
			}
		}
		removeClicked = true;
		dialogStage.close();
	}

	@FXML
	/**
	 * Closes the stage if cancel is pressed
	 */
	private void handleCancel() {
		dialogStage.close();
	}
}
