package timey.controller.view;

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
	// TODO: Remove for prepared Statements
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	private void initialize() {
		// TODO: Remove for prepared Statements
		categoryData.add("Freizeit");
		categoryData.add("Studium");
	}

	public void setNewDialog(boolean newCategory) {
		if (newCategory) {
			categoryCombo.setVisible(false);
		}
		categoryCombo.setItems(categoryData);
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOK() {
		if (categoryCombo.isVisible()) {
			String selected = categoryCombo.getSelectionModel()
					.getSelectedItem();
			for (int i = 0; i < categoryData.size(); i++) {
				if (selected.compareTo(categoryData.get(i)) == 0) {
					categoryData.set(i, categoryField.getText());
				}
			}
			System.out.println("Category edited! (" + categoryField.getText()
					+ ")");
		} else if (categoryField.getText().compareTo("") != 0
				& categoryField.getText() != null) {
			categoryData.add(categoryField.getText());
			System.out.println("Created new Category! ("
					+ categoryField.getText() + ")");
		}
		okClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
