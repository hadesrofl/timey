package timey.controller.view;

import timey.controller.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategoryEditDialogController {
	@FXML
	private TextField categoryField;
	@FXML
	private ComboBox<String> categoryCombo;
	private Stage dialogStage;
	private boolean okClicked = false;
	private boolean changed = false;
	private MainApp mainApp;
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	private void initialize() {

	}

	public void setNewDialog(boolean newCategory) {
		if (newCategory) {
			categoryCombo.setVisible(false);
		}

	}
	
	public void setMainApp(MainApp main){
		this.mainApp = main;
	}
	
	public void setData(){
		categoryData = mainApp.getDBConn().getCategories(mainApp.getUserID());
		categoryCombo.setItems(categoryData);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}
	public boolean isSomethingChanged(){
		return changed;
	}

	@FXML
	private void handleOK() {

		if (categoryCombo.isVisible()) {
			String selected = categoryCombo.getSelectionModel()
					.getSelectedItem();
			for (int i = 0; i < categoryData.size(); i++) {
				if (selected.compareTo(categoryData.get(i)) == 0) {
					
					categoryData.set(i, categoryField.getText());
					changed = mainApp.getDBConn().handleEditCategory(categoryField.getText(), mainApp.getUserID(), selected);
				}
			}
		} else if (categoryField.getText().compareTo("") != 0
				& categoryField.getText() != null) {
			categoryData.add(categoryField.getText());
			mainApp.getDBConn().handleNewCategory(categoryField.getText(), mainApp.getUserID());

		}
		
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
