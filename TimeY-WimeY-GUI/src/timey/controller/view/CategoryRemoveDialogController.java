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

@SuppressWarnings("deprecation")
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
		Action response = Dialogs.create()
		        .title("Remove Category: " + selected )
		        .masthead("You want to delete " + selected + "! All times with that category will be deleted too!")
		        .message("Are you sure you want to delete " + selected + "?")
		        .showConfirm();

		if (response == Dialog.ACTION_YES) {
			for(int i = 0; i < categoryData.size(); i++){
				if(selected.compareTo(categoryData.get(i)) == 0){
					categoryData.remove(i);
					mainApp.getDBConn().handleRemoveCategory(selected, mainApp.getUserID());
				}
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
