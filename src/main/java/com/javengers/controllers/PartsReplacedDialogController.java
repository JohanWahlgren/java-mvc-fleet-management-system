package com.javengers.controllers;

import com.javengers.models.Part;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;

public class PartsReplacedDialogController {

    @FXML
    private TableView<Part> partsTable;
    private ObservableList<Part> partsReplaced = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        TableColumn<Part, String> nameColumn = new TableColumn<>("Part Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setOnEditCommit(
            (TableColumn.CellEditEvent<Part, String> t) -> 
                (t.getTableView().getItems().get(
                t.getTablePosition().getRow())
                ).setName(t.getNewValue())
        );

        partsTable.setEditable(true);
        partsTable.getColumns().add(nameColumn);
        partsTable.setItems(partsReplaced);
    }

    private Part getSelectedPart(){
        return partsTable.getSelectionModel().getSelectedItem();
    }

    @FXML
    private void handleAddPart() {
        Part newPart = new Part("[Part Name]");
        partsReplaced.add(newPart);
    }

    @FXML
    private void handleRemovePart() {
        Part selectedPart = getSelectedPart();
        if (selectedPart != null) {
            partsReplaced.remove(selectedPart);
        }
    }

    @FXML
    private void handleAddPartsAction() {
        Stage stage = (Stage) partsTable.getScene().getWindow();
        stage.close();
    }

    public ObservableList<Part> getReplacedParts() {
        return partsReplaced;
    }

    public void setPartsReplaced(ObservableList<Part> partsReplaced){
        this.partsReplaced = partsReplaced;
    }
}