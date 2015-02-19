package timey.controller.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class CategoryRemoveDialogController {
	@FXML
	private ComboBox<String> categoryCombo;
	private Stage dialogStage;
	private boolean removeClicked = false;

	// TODO: Remove for prepared Statements
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	private void initialize() {
		// TODO: Remove for prepared Statements
		categoryData.add("Freizeit");
		categoryData.add("Studium");
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		// TODO: Remove for prepared Statements
		categoryCombo.setItems(categoryData);
	}

	public boolean isRemoveClicked() {
		return removeClicked;
	}

	@FXML
	private void handleRemove() {
		String selected = categoryCombo.getSelectionModel().getSelectedItem();
		for(int i = 0; i < categoryData.size(); i++){
			if(selected.compareTo(categoryData.get(i)) == 0){
				categoryData.remove(i);
				System.out.println("Category removed! (" + selected + ")");
			}
		}
		
		removeClicked = true;
		dialogStage.close();
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
