package timey.controller.view;

import timey.controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * 
 * 
 * <b>Project:</b> TimeY-WimeY-GUI
 * <p>
 * <b>Packages:</b> timey.controller.view
 * </p>
 * <p>
 * <b>File:</b> CategoryEditDialogController.java
 * </p>
 * <p>
 * <b>last update:</b> 05.03.2015
 * </p>
 * <p>
 * <b>Time:</b> 12:44:10
 * </p>
 * <b>Description:</b>
 * <p>
 * This Class controls the category edit dialog.
 * </p>
 * <p>
 * Copyright (c) 2015 by Rene Kremer
 * </p>
 * 
 * @author Rene Kremer
 * @version 0.6
 */
public class CategoryEditDialogController {
	@FXML
	/**
	 * Textfield for entering a new name for a category
	 */
	private TextField categoryField;
	@FXML
	/**
	 * ComboBox for selecting a category to edit
	 */
	private ComboBox<String> categoryCombo;
	/**
	 * Stage of this dialog
	 */
	private Stage dialogStage;
	/**
	 * Boolean for ok button
	 */
	private boolean okClicked = false;
	/**
	 * Boolean for changes made to the database in this dialog
	 */
	private boolean changed = false;
	/**
	 * MainApp of TimeY-WimeY
	 */
	private MainApp mainApp;
	/**
	 * List of Categories
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
	 * Show ComboBox if Edit Category Button on DateView Scene is pressed
	 * 
	 * @param newCategory
	 *            true if a category shall be selected to edit, false if a new
	 *            category shall be entered
	 */
	public void setNewDialog(boolean newCategory) {
		if (newCategory) {
			categoryCombo.setVisible(false);
		}

	}

	/**
	 * Setter for the mainApp
	 * 
	 * @param main
	 *            is the mainApp class
	 */
	public void setMainApp(MainApp main) {
		this.mainApp = main;
	}

	/**
	 * Sets the data for this dialog (gets users categories)
	 */
	public void setData() {
		categoryData = mainApp.getDBConn().getCategories(mainApp.getUserID());
		categoryCombo.setItems(categoryData);
	}

	/**
	 * Sets the stage for this dialog
	 * 
	 * @param dialogStage
	 *            is the stage for this dialog
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Getter of the boolean if ok is clicked
	 * 
	 * @return true, if ok was clicked else false
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Getter of the boolean if there were updates in the database
	 * 
	 * @return changed
	 */
	public boolean isSomethingChanged() {
		return changed;
	}

	@FXML
	/**
	 * Handles the action if ok is clicked and either updates the selected category or inserts a new one
	 */
	private void handleOK() {

		if (categoryCombo.isVisible()) {
			String selected = categoryCombo.getSelectionModel()
					.getSelectedItem();
			for (int i = 0; i < categoryData.size(); i++) {
				if (selected.compareTo(categoryData.get(i)) == 0) {

					categoryData.set(i, categoryField.getText());
					changed = mainApp.getDBConn().handleEditCategory(
							categoryField.getText(), mainApp.getUserID(),
							selected);
				}
			}
		} else if (categoryField.getText().compareTo("") != 0
				& categoryField.getText() != null) {
			categoryData.add(categoryField.getText());
			mainApp.getDBConn().handleNewCategory(categoryField.getText(),
					mainApp.getUserID());

		}

		dialogStage.close();
	}

	@FXML
	/**
	 * Closes the Stage on cancel
	 */
	private void handleCancel() {
		dialogStage.close();
	}
}
