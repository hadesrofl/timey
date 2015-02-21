package timey.controller.view;

import timey.controller.MainApp;
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
	private MainApp mainApp;
	private ObservableList<String> categoryData = FXCollections
			.observableArrayList();

	@FXML
	private void initialize() {
	}
	
	public void setMainApp(MainApp main){
		this.mainApp = main;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(){
		categoryData = mainApp.getDBConn().getCategories(mainApp.getUserID());
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
				mainApp.getDBConn().handleRemoveCategory(selected, mainApp.getUserID());
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
