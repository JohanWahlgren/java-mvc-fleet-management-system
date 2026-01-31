package com.javengers.controllers;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;

import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.beans.value.ChangeListener;

import com.javengers.models.MaintenanceSchedule;
import com.javengers.models.ServiceHistory;
import com.javengers.models.ServiceWorkshop;
import com.javengers.models.ServiceActivity;
import com.javengers.models.Vehicle;
import com.javengers.models.VehicleRegister;
import com.javengers.ui.VehicleTableContextMenu;
import com.javengers.ui.MaintenanceScheduleContextMenu;
import com.javengers.ui.ServiceHistoryContextMenu;
import com.javengers.ui.Utils;
import com.javengers.ui.WorkshopTableContextMenu;
import com.javengers.models.MaintenanceActivity;
import com.javengers.exceptions.DuplicateVinException;
import com.javengers.models.ServiceWorkshopRegister;
import com.javengers.models.Part;
import javafx.scene.control.TableRow;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MainViewController {
    private VehicleRegister vehicleRegister;
    private ServiceWorkshopRegister serviceWorkshopRegister;

    private final String[] imagePaths = new String[] {
        "/jpg/system_documentation_page-0001.jpg",
        "/jpg/system_documentation_page-0002.jpg",
        "/jpg/system_documentation_page-0003.jpg",
        "/jpg/system_documentation_page-0004.jpg",
        "/jpg/system_documentation_page-0005.jpg"
    };

    @FXML
    private GridPane detailsGridPane;

    @FXML
    private TableView<Vehicle> vehicleTableView;

    @FXML
    private TableView<ServiceActivity> serviceHistoryTableView;

    @FXML
    private TableView<ServiceWorkshop> serviceWorkshopTableView;

    @FXML
    private TableColumn<Vehicle, String> vehicleNameColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleVinColumn;

    @FXML
    private TableColumn<Vehicle, String> vehicleAvailabilityColumn;

    @FXML
    private TableColumn<Vehicle, String> workshopNameColumn;

    @FXML
    private TableColumn<Vehicle, String> workshopTypeColumn;

    @FXML
    private TableColumn<Vehicle, String> workshopAddressColumn;

    @FXML
    private TableColumn<Vehicle, ServiceHistory> serviceHistoryColumn;

    @FXML
    private TableColumn<ServiceActivity, LocalDate> serviceActivityDateColumn;

    @FXML
    private TableColumn<ServiceActivity, String> serviceActivityDescriptionColumn;

    @FXML
    private TableColumn<ServiceActivity, String> serviceActivityServiceWorkshopColumn;

    @FXML
    private TableColumn<ServiceActivity, Double> serviceActivityCostColumn;

    @FXML
    private TableColumn<ServiceActivity, String> serviceActivityPartsReplacedColumn;

    @FXML
    private TableView<MaintenanceActivity> maintenanceScheduleTableView;

    @FXML
    private TableColumn<MaintenanceActivity, String> maintenanceActivityDescriptionColumn;

    @FXML
    private TableColumn<MaintenanceActivity, ServiceWorkshop> maintenanceActivityServiceWorkshopColumn;

    @FXML
    private TableColumn<Vehicle, Date> maintenanceActivityDateColumn;
    
    @FXML
    private ImageView appIcon;

    public void initialize() {

        vehicleNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        vehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        vehicleVinColumn.setCellValueFactory(new PropertyValueFactory<>("vin"));

        workshopNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        workshopTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        workshopAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));

        serviceActivityDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        serviceActivityDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        serviceActivityServiceWorkshopColumn.setCellValueFactory(new PropertyValueFactory<>("serviceWorkshop"));
        serviceActivityCostColumn.setCellValueFactory(new PropertyValueFactory<>("costOfService"));
        serviceActivityPartsReplacedColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfPartsReplaced"));

        maintenanceActivityDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        maintenanceActivityDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        maintenanceActivityServiceWorkshopColumn.setCellValueFactory(new PropertyValueFactory<>("serviceWorkshop"));

        Image icon = new Image(getClass().getResourceAsStream("/icons/app_icon.png"));
        appIcon.setImage(icon);

        // Add a listener to the selection model of vehicleTableView
        vehicleTableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Vehicle>() {// If something changes in the table, the listener waits for the object
                                               // that is expecting something to be added to the TableView.
                    @Override
                    public void changed(ObservableValue<? extends Vehicle> observable, Vehicle oldValue,
                            Vehicle newValue) {// javaFx standard
                        // newValue is the newly selected vehicle
                        if (newValue != null) {
                            updateVehicleServiceHistory(newValue);
                            updateVehicleMaintenanceSchedule(newValue);
                            updateDetailsGridPane(newValue);
                        }
                    }
                });


        // Add a listener to the selection model for the workshops table
        serviceWorkshopTableView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<ServiceWorkshop>() {// // If something changes in the table, the listener waits for the object
                                                       // that is expecting something to be added to the TableView.
                    @Override
                    public void changed(ObservableValue<? extends ServiceWorkshop> observable, ServiceWorkshop oldValue,
                            ServiceWorkshop newValue) {// javafx standard
                        // newValue is the newly selected workshop
                        if (newValue != null) {
                            updateServiceWorkshopServiceHistory(newValue);
                            updateServiceWorkshopMaintenanceActivities(newValue);
                            updateDetailsGridPane(newValue);
                        }
                    }
                });


        // Add context menus
        serviceWorkshopTableView.setRowFactory(tv -> {
            TableRow<ServiceWorkshop> row = new TableRow<>();
            WorkshopTableContextMenu contextMenu = new WorkshopTableContextMenu(
                    MainViewController.this);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu));

            return row;
        });
        // Sets up a custom RowFactory for a TableView displaying objects of type ServiceWorkshop. 
        // RowFactory is used to create custom row objects for the table. 
        // A TableRow<ServiceWorkshop> and a custom WorkshopTableContextMenu 
        // are used to create a context menu when the user right-clicks on a row in the table.


        vehicleTableView.setRowFactory(tv -> {
            TableRow row = new TableRow<>();
            VehicleTableContextMenu contextMenu = new VehicleTableContextMenu(this);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu));

            return row;
        });

        maintenanceScheduleTableView.setRowFactory(tv -> {
            TableRow row = new TableRow<>();
            MaintenanceScheduleContextMenu contextMenu = new MaintenanceScheduleContextMenu(this);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu));

            return row;
        });

        // Update context menu for workshops

        serviceHistoryTableView.setRowFactory(tv -> {
            TableRow row = new TableRow<>();
            ServiceHistoryContextMenu contextMenu = new ServiceHistoryContextMenu(this);

            row.contextMenuProperty().bind(
                    Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu));

            return row;
        });

        vehicleAvailabilityColumn.setCellValueFactory(cellData -> {
            Vehicle vehicle = cellData.getValue();
            String availability = vehicle.getIsDecommissioned() ? "No" : "Yes";
            return new SimpleStringProperty(availability);
        });
    }

    public void refreshMainTables() {
        vehicleTableView.setItems(vehicleRegister.getVehicles());
        vehicleTableView.refresh();

        serviceWorkshopTableView.setItems(serviceWorkshopRegister.getWorkshops());
        serviceWorkshopTableView.refresh();

        updateVehicleMaintenanceSchedule(getSelectedVehicle());
        updateVehicleServiceHistory(getSelectedVehicle());
    }

    public void updateDetailsGridPane(Object object) {
        //takes an undefined object as parameter
        //checks if the object is a vehicle or serviceWorkshop
        //type cast the object to the selected workshop or vehicle
        detailsGridPane.getChildren().clear();
        
        if (object instanceof Vehicle) {
            Vehicle vehicle = (Vehicle) object;
            addLabelRow("VIN", vehicle.getVin());
            addLabelRow("Type", vehicle.getType());
            addLabelRow("Name", vehicle.getName());
            addLabelRow("Current Location", vehicle.getCurrentLocation());
            addLabelRow("Capacity", String.valueOf(vehicle.getCapacity()));
            addCheckBoxRow("Decommissioned", vehicle.getIsDecommissioned());
            addLabelRow("Total Service Cost", Double.toString(vehicle.getTotalServiceCost()));
            addLabelRow("Total Parts Replaced", Integer.toString(vehicle.getServiceHistory().getPartsReplaced().size()));
    

        } else if (object instanceof ServiceWorkshop) {
            ServiceWorkshop workshop = (ServiceWorkshop) object;
            addLabelRow("Type", workshop.getType());
            addLabelRow("Name", workshop.getName());
            addLabelRow("Address", workshop.getAddress());
        }
    }

    private void addLabelRow(String label, String value) {
        //method to display general detail values in a nice way 
        int rowIndex = detailsGridPane.getRowCount();
        detailsGridPane.add(new Label(label + ":"), 0, rowIndex);
        detailsGridPane.add(new Label(value), 1, rowIndex);
    }

    private void addCheckBoxRow(String label, boolean value) {
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(value);
        checkBox.setDisable(true);
        int rowIndex = detailsGridPane.getRowCount();
        detailsGridPane.add(new Label(label + ":"), 0, rowIndex);
        detailsGridPane.add(checkBox, 1, rowIndex);
    }

    public void updateVehicleMaintenanceSchedule(Vehicle vehicle) {
        ObservableList<MaintenanceActivity> maintenanceActivities;

        if (vehicle == null || vehicle.getMaintenanceSchedule() == null) {
            maintenanceActivities = FXCollections.observableArrayList();
        } else {
            maintenanceActivities = FXCollections.observableArrayList(vehicle.getMaintenanceSchedule().getMaintenanceActivities());
        }
        setServiceMaintenenceItems(maintenanceActivities);

    }

    public void updateServiceWorkshopMaintenanceActivities(ServiceWorkshop serviceWorkshop) {
        ObservableList<MaintenanceActivity> maintenanceActivities;

        if (serviceWorkshop == null || serviceWorkshop.getMaintenanceActivities() == null) {
            maintenanceActivities = FXCollections.observableArrayList();
        } else {
            maintenanceActivities = FXCollections.observableArrayList(serviceWorkshop.getMaintenanceActivities());
        }
        setServiceMaintenenceItems(maintenanceActivities);

    }

    private void setServiceMaintenenceItems(ObservableList<MaintenanceActivity> maintenanceActivities) {
        maintenanceScheduleTableView.setItems(maintenanceActivities);
        maintenanceScheduleTableView.refresh();
    }

    private void setServiceHistoryItems(ObservableList<ServiceActivity> serviceActivities) {
        serviceHistoryTableView.setItems(serviceActivities);
        serviceHistoryTableView.refresh();
    }

    public void updateVehicleServiceHistory(Vehicle vehicle) {
        ObservableList<ServiceActivity> serviceActivities;

        if (vehicle == null || vehicle.getServiceHistory() == null) {
            serviceActivities = FXCollections.observableArrayList();
        } else {
            serviceActivities = FXCollections.observableArrayList(vehicle.getServiceHistory().getServiceActivities());
        }

        setServiceHistoryItems(serviceActivities);
    }

    public void updateServiceWorkshopServiceHistory(ServiceWorkshop serviceWorkshop) {
        ObservableList<ServiceActivity> serviceActivities;

        if (serviceWorkshop == null || serviceWorkshop.getServiceActivities() == null) {
            serviceActivities = FXCollections.observableArrayList();
        } else {
            serviceActivities = FXCollections.observableArrayList(serviceWorkshop.getServiceActivities());
        }
        setServiceHistoryItems(serviceActivities);
    }

    public void setVehicleRegister(VehicleRegister vehicleRegister) {
        this.vehicleRegister = vehicleRegister;
        ObservableList<Vehicle> vehicles = FXCollections.observableArrayList(vehicleRegister.getVehicles());
        vehicleTableView.setItems(vehicles);
    }

    public void setServiceWorkshopRegister(ServiceWorkshopRegister serviceWorkshopRegister) {
        this.serviceWorkshopRegister = serviceWorkshopRegister;
        ObservableList<ServiceWorkshop> serviceWorkshops = FXCollections
                .observableArrayList(serviceWorkshopRegister.getWorkshops());
        serviceWorkshopTableView.setItems(serviceWorkshops);
    }

    // Get selected from table
    public Vehicle getSelectedVehicle() {
        return vehicleTableView.getSelectionModel().getSelectedItem();
    }

    private ServiceWorkshop getSelectedServiceWorkshop() {
        return serviceWorkshopTableView.getSelectionModel().getSelectedItem();
    }

    private ServiceActivity getSelectedServiceActivity() {
        return serviceHistoryTableView.getSelectionModel().getSelectedItem();
    }

    private MaintenanceActivity getSelectedMaintenanceActivity() {
        return maintenanceScheduleTableView.getSelectionModel().getSelectedItem();
    }

    
    // Function for code reuse, allowing us to display a popup.
    // Provide the method with an FXML file, enabling the code to interact with it,
    // thereby avoiding the need to rewrite the code repeatedly.
    private FXMLLoader getFXMLLoader(String fxmlFile) {
        URL location = getClass().getResource(fxmlFile);
        FXMLLoader fxmlLoader = new FXMLLoader(location);
        return fxmlLoader;
    }

    @FXML
    private void handleButtonCalculateTotalCostAction(ActionEvent event) {
        double totalCost = 0.0;

        if (vehicleRegister != null) {
            for (Vehicle vehicle : vehicleRegister.getVehicles()) {
                ServiceHistory serviceHistory = vehicle.getServiceHistory();

                if (serviceHistory != null) {
                    for (ServiceActivity serviceActivity : serviceHistory.getServiceActivities()) {
                        totalCost += serviceActivity.getCostOfService();
                    }
                }
            }
        }

        totalCost = Math.round(totalCost * 10.0) / 10.0;
        Utils.showAlert("Information", "Total Cost", "Total cost of all vehicles: " + totalCost);
    }
    

    @FXML
    private void handleAverageCostMenuItemAction(ActionEvent event) {
        int count = 0;
        double cost = 0.0;

        if (vehicleRegister != null) {
            for (Vehicle vehicle : vehicleRegister.getVehicles()) {
                ServiceHistory serviceHistory = vehicle.getServiceHistory();

                if (serviceHistory != null) {
                    for (ServiceActivity serviceActivity : serviceHistory.getServiceActivities()) {
                        cost += serviceActivity.getCostOfService();
                        count += serviceHistory.getServiceActivities().size();
                    }
                }
            }
        }

        if (count != 0) {
            double averageCost = cost / count;
    
            averageCost = Math.round(averageCost * 10.0) / 10.0;
    
            Utils.showAlert("Information", "Average Service Cost", "Average service cost is: " + averageCost);
        } else {
            Utils.showAlert("Information", "Average Service Cost", "No service activities available.");
        }
    }

    @FXML
    private void handleSeeMostExpensiveWorkshopMenuItemAction(ActionEvent event) {
        ServiceWorkshop workshop = serviceWorkshopRegister.getMostExpensiveWorkshop();
        if (workshop == null) {
            Utils.showAlert("Warning", "No Workshops", "No Workshops available.");
            return;
        }
        double cost = workshop.getWorkshopCost();

        cost = Math.round(cost * 10.0) / 10.0;
        
        Utils.showAlert("Information", "This is why we are going bankrupt", "The most expensive workshop is " + workshop.getName() + " and they have performed services for " + cost);
    }

    @FXML
    private void handleSeeMostExpensiveServiceItemAction(ActionEvent event) {
        ServiceActivity mostExpensiveServiceActivity = vehicleRegister.getMostExpensiveService();

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("This is why we are going bankrupt");
        alert.setHeaderText(null);

        VBox contentBox = new VBox();
        contentBox.getChildren().add(new Label("The most expensive service cost " + mostExpensiveServiceActivity.getCostOfService() + " and it was performed on " + mostExpensiveServiceActivity.getServiceHistory().getVehicle().getName() + "\nin workshop " + mostExpensiveServiceActivity.getServiceWorkshop().getName()));
        alert.getDialogPane().setContent(contentBox);
        alert.showAndWait();
    }

    private void handleVehicleFromFXML(Vehicle vehicle, boolean isEditMode) throws IOException {
        VehicleController vehicleController = new VehicleController();
        vehicleController.setVehicleRegister(vehicleRegister);
        
        if (isEditMode) {
            vehicleController.setVehicle(vehicle);
        }

        vehicleController.isEditMode = isEditMode;
        FXMLLoader fxmlLoader = getFXMLLoader("/fxml/VehicleView.fxml");
        fxmlLoader.setController(vehicleController);

        showFXMLFromLoader(fxmlLoader);

        Vehicle updatedVehicle = vehicleController.getVehicle();
        updateDetailsGridPane(updatedVehicle);
        refreshMainTables();
    }
    
    public void addVehicleFromFXML() throws IOException {
        handleVehicleFromFXML(null, false);
    }
    
    public void editVehicleFromFXML(Vehicle vehicle) throws IOException {
        handleVehicleFromFXML(vehicle, true);
    }

    public void editSelectedVehicleFromFXML() throws IOException{
        editVehicleFromFXML(getSelectedVehicle());
    }

    private void handleMaintenanceActivity(MaintenanceActivity maintenanceActivity, boolean isEditMode) throws IOException {
        Vehicle selectedVehicle = getSelectedVehicle();
        MaintenanceActivityController maintenanceActivityController = new MaintenanceActivityController();
        maintenanceActivityController.setServiceWorkshopRegister(serviceWorkshopRegister);
        
        if (isEditMode) {
            selectedVehicle = maintenanceActivity.getMaintenanceSchedule().getVehicle();
            maintenanceActivityController.setMaintenanceActivity(maintenanceActivity);
        }
        maintenanceActivityController.setVehicle(selectedVehicle);
        maintenanceActivityController.isEditMode = isEditMode;
    
        FXMLLoader fxmlLoader = getFXMLLoader("/fxml/MaintenanceActivityView.fxml");
        fxmlLoader.setController(maintenanceActivityController);
        
        showFXMLFromLoader(fxmlLoader);
    
        MaintenanceActivity updatedMaintenanceActivity = maintenanceActivityController.getMaintenanceActivity();
        if (updatedMaintenanceActivity != null) {
            updateVehicleMaintenanceSchedule(selectedVehicle);
            updateDetailsGridPane(selectedVehicle);
        }
    }

    public void addMaintenanceActivityFromFXML() throws IOException {
        handleMaintenanceActivity(null, false);
    }
    
    public void editMaintenanceActivityFromFXML(MaintenanceActivity maintenanceActivity) throws IOException {
        handleMaintenanceActivity(maintenanceActivity, true);
    }
    
    private void handleServiceActivity(ServiceActivity serviceActivity, boolean isEditMode) throws IOException {
        Vehicle selectedVehicle = getSelectedVehicle();
        ServiceActivityController serviceActivityController = new ServiceActivityController(this);
        serviceActivityController.setServiceWorkshopRegister(serviceWorkshopRegister);
        serviceActivityController.setVehicle(selectedVehicle);
    
        if (isEditMode) {
            selectedVehicle = serviceActivity.getServiceHistory().getVehicle();
            serviceActivityController.setActivity(serviceActivity);
        }
        serviceActivityController.isEditMode = isEditMode;
    
        FXMLLoader fxmlLoader = getFXMLLoader("/fxml/ServiceActivityView.fxml");
        fxmlLoader.setController(serviceActivityController);
    
        showFXMLFromLoader(fxmlLoader);
    
        ServiceActivity updatedServiceActivity = serviceActivityController.getServiceActivity();
        if (updatedServiceActivity != null) {
            updateVehicleServiceHistory(selectedVehicle);
            updateDetailsGridPane(selectedVehicle);
        }
    }

    
    public void addServiceActivityFromFXML() throws IOException {
        handleServiceActivity(null, false);
    }
    
    public void editServiceActivityFromFXML(ServiceActivity serviceActivity) throws IOException {
        handleServiceActivity(serviceActivity, true);
    }

    public void showTotalCostForVehicle(){
        Vehicle vehicle = getSelectedVehicle();
        if (vehicle == null)
            return;
        double totalCost = vehicle.getTotalServiceCost();

        // Create an alert of type INFORMATION
        Utils.showAlert("Information", "Total Cost of Service For Vehicle", "Total cost: " + Double.toString(totalCost));
    }

    @FXML
    private void handleButtonAddVehicleAction(ActionEvent event) throws IOException, DuplicateVinException {
        addVehicleFromFXML();
    }

    @FXML
    private void handleButtonAddServiceWorkshop(ActionEvent event) throws IOException {
        addWorkshopFromFXML();
    }

    @FXML
    private void handleButtonRemoveSelectedVehicle(ActionEvent event) {
        removeSelectedVehicle();
    }

    @FXML
    private void handleButtonRemoveSelectedServiceWorkshop(ActionEvent event) {
        removeSelectedWorkshop();
    }

    private void removeServiceHistoryAssociations(ServiceHistory serviceHistory) {
        // Remove all activities associated with the service history from their respective workshops.
        if (serviceHistory == null) {
            return;
        }
    
        for (ServiceActivity serviceActivity : serviceHistory.getServiceActivities()) {
            ServiceWorkshop associatedWorkshop = serviceActivity.getServiceWorkshop();
            associatedWorkshop.removeServiceActivity(serviceActivity);
        }
    }
    
    private void removeMaintenanceScheduleAssociations(MaintenanceSchedule maintenanceSchedule) {
        // Remove all activities associated with the maintenance schedule from their respective workshops.
        if (maintenanceSchedule == null) {
            return;
        }
    
        for (MaintenanceActivity maintenanceActivity : maintenanceSchedule.getMaintenanceActivities()) {
            ServiceWorkshop associatedWorkshop = maintenanceActivity.getServiceWorkshop();
            associatedWorkshop.removeMaintenanceActivity(maintenanceActivity);
            }
        }
    

    private void removeVehicleAssociations(Vehicle vehicle){

        vehicleRegister.removeVehicle(vehicle);
        vehicleTableView.getItems().remove(vehicle);
        ServiceHistory serviceHistory = vehicle.getServiceHistory();
        MaintenanceSchedule maintenanceSchedule = vehicle.getMaintenanceSchedule();
        removeServiceHistoryAssociations(serviceHistory);
        removeMaintenanceScheduleAssociations(maintenanceSchedule);

    }
 
    private void removeServiceWorkshopAssociations(ServiceWorkshop workshop){

        serviceWorkshopRegister.removeWorkshop(workshop);
        serviceWorkshopTableView.getItems().remove(workshop);
        for (MaintenanceActivity activity : workshop.getMaintenanceActivities()){
            activity.setServiceWorkshop(null);
        }
        for (ServiceActivity activity : workshop.getServiceActivities()){
            activity.setServiceWorkshop(null);
        }
    }

    // remove methods for items
    private void removeVehicle(Vehicle vehicle) {
        if (vehicle != null) {
            removeVehicleAssociations(vehicle);
        }
    }

    private void removeServiceWorkshop(ServiceWorkshop workshop) {
        if (workshop != null) {
            removeServiceWorkshopAssociations(workshop);
        }
    }

    private void removeServiceActivityAssociations(ServiceActivity serviceActivity){
        // Remove from it's history
        ServiceHistory history = serviceActivity.getServiceHistory();
        history.removeActivity(serviceActivity);
        // more from the workshop
        ServiceWorkshop workshop = serviceActivity.getServiceWorkshop();
        workshop.removeServiceActivity(serviceActivity);
    }

    private void removeMaintenanceActivityAssociations(MaintenanceActivity maintenanceActivity){
        // remove association to schedule
        MaintenanceSchedule maintenanceSchedule = maintenanceActivity.getMaintenanceSchedule();
        maintenanceSchedule.removeActivity(maintenanceActivity);
        // more from the workshop
        ServiceWorkshop workshop = maintenanceActivity.getServiceWorkshop();
        workshop.removeMaintenanceActivity(maintenanceActivity);
    }

    private void removeServiceActivity(ServiceActivity serviceActivity) {
        if (serviceActivity == null) return;

        removeServiceActivityAssociations(serviceActivity);
        updateVehicleServiceHistory(getSelectedVehicle());
    }

    private void removeMaintenanceActivity(MaintenanceActivity maintenanceActivity) {
        if (maintenanceActivity == null) return;
        
        removeMaintenanceActivityAssociations(maintenanceActivity);
        updateVehicleMaintenanceSchedule(getSelectedVehicle());
    }

    @FXML
    private void handleDisplayDocumentationAction(ActionEvent event) {
        VBox contentBox = new VBox(10);
        for (String imagePath : imagePaths) {
            Image image = new Image(getClass().getResourceAsStream(imagePath));
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(800);
            imageView.setPreserveRatio(true);
            contentBox.getChildren().add(imageView);
        }
    
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setPrefSize(800, 600);
        scrollPane.setFitToWidth(true);
    
        Stage stage = new Stage();
        stage.setScene(new Scene(scrollPane));
        stage.show();
    }
    
    public void removeSelectedVehicle(){
        removeVehicle(getSelectedVehicle());
    }

    public void removeSelectedServiceActivity(){
        removeServiceActivity(getSelectedServiceActivity());
    }

    public void removeSelectedMaintenanceActivity(){
        removeMaintenanceActivity(getSelectedMaintenanceActivity());
    }

    @FXML
    private void handleMenuItemDeleteVehicleAction(ActionEvent event) {
        removeSelectedVehicle();
    }

    private void showFXMLFromLoader(FXMLLoader loader) throws IOException {
        AnchorPane popupContent = loader.load();
    
        Stage popupStage = new Stage();
        popupStage.setScene(new Scene(popupContent));
        popupStage.showAndWait();
    }

    public void addWorkshopFromFXML() throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader("/fxml/ServiceWorkshopView.fxml");
        ServiceWorkshopController controller = new ServiceWorkshopController();
        fxmlLoader.setController(controller);
        controller.setServiceWorkshopRegister(serviceWorkshopRegister);

        showFXMLFromLoader(fxmlLoader);
        
        refreshMainTables();
    }

    public void editWorkshopFromFXML(ServiceWorkshop serviceWorkshop) throws IOException {
    
        FXMLLoader fxmlLoader = getFXMLLoader("/fxml/ServiceWorkshopView.fxml");
        ServiceWorkshopController controller = new ServiceWorkshopController();
        controller.isEditMode = true;
        controller.setServiceWorkshopRegister(serviceWorkshopRegister);
        fxmlLoader.setController(controller);
        controller.setServiceWorkshop(serviceWorkshop);
        showFXMLFromLoader(fxmlLoader);
        refreshMainTables();
    }

    public void editSelectedWorkshopFromFXML() throws IOException{
        editWorkshopFromFXML(getSelectedServiceWorkshop());
    }

    public void removeSelectedWorkshop() {
        removeServiceWorkshop(getSelectedServiceWorkshop()); 
    }

    public void editSelectedServiceActivityParts(){
        // Convienence method editing parts form the selected serviceactiviy.
        // This is useful in the context menu where we don't have access to the selected serviceActivity.
        ServiceActivity serviceActivity = getSelectedServiceActivity();

        editParts(serviceActivity.getPartsReplaced());
    }

    public void editSelectedServiceActivity(){
        ServiceActivity serviceActivity = getSelectedServiceActivity();

        try {
            editServiceActivityFromFXML(serviceActivity);
        } catch (IOException e) {
            Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
            e.printStackTrace();
        }
    }

    public void editSelectedMaintenanceActivity(){
        MaintenanceActivity activity = getSelectedMaintenanceActivity();

        try {
            editMaintenanceActivityFromFXML(activity);
        } catch (IOException e) {
            Utils.showAlert("Error", "Unexpected error", "Something unexpected happened. Contact IT-support.");
            e.printStackTrace();
        }
    }

    public void editParts(ObservableList<Part> partsReplaced) {

        //ObservableList<Part> partsReplaced = FXCollections.observableArrayList();

        // Load the DialogPane from FXML
        FXMLLoader loader = getFXMLLoader("/fxml/PartsReplacedDialogView.fxml");
    
        PartsReplacedDialogController partsReplacedDialogController = new PartsReplacedDialogController();
        // We have the initialte our controller ourself in order to set partsReplaced and 
        partsReplacedDialogController.setPartsReplaced(partsReplaced);
       
        loader.setController(partsReplacedDialogController);
        DialogPane dialogPane;
        try {
            dialogPane = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    
        // Create a Dialog and set its content to the loaded DialogPane
        Dialog<?> dialog = new Dialog<>();
        dialog.setDialogPane(dialogPane);
        dialog.initModality(Modality.APPLICATION_MODAL);
    
        // Show the dialog and wait for it to be closed
        dialog.showAndWait();

        // Check if vehicle now exeeds 100 parts replaced. In that case we should decomission with a warning. 
        Vehicle vehicle = getSelectedVehicle().getServiceHistory().getVehicle();
        int totalPartsreplaced = vehicle.getServiceHistory().getPartsReplaced().size();

        if(totalPartsreplaced > 100) {
            String message = (vehicle.getName() + " now has " + totalPartsreplaced + " parts replaced and is therefore decomissioned.");
            Utils.showAlert("Information", "Vehicle Info", message);
            vehicle.setDecommissioned(true);
        } else {
            vehicle.setDecommissioned(false);
        }
        // Update Views
        updateVehicleServiceHistory(vehicle);
        updateDetailsGridPane(vehicle);
        refreshMainTables();
    }
    
    public void listWorkshopsForSelectedVehicle() {
        Vehicle selectedVehicle = getSelectedVehicle();
        if (selectedVehicle != null) {
            ObservableList<ServiceWorkshop> workshops = selectedVehicle.getVisitedWorkshops();
            StringBuilder workshopsText = new StringBuilder();
            for (ServiceWorkshop workshop : workshops) {
            workshopsText.append(workshop.getName()).append("\n");
        }
        Utils.showAlert("Information", "Workshops That Has Serviced " + selectedVehicle.getName(), workshopsText.toString());
        }     
    } 
}