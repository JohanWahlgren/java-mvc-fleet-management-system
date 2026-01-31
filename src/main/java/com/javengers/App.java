package com.javengers;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.javengers.controllers.MainViewController;
import com.javengers.models.ServiceWorkshop;
import com.javengers.models.ServiceWorkshopRegister;
import com.javengers.models.VehicleRegister;
import com.javengers.exceptions.DuplicateVinException;
import com.javengers.factories.RandomVehicleFactory;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private void populateWithTestData(VehicleRegister vehicleRegister, ServiceWorkshopRegister serviceWorkshopRegister) throws DuplicateVinException {
        ServiceWorkshop workshop1 = new ServiceWorkshop("Viking Workshop", "Internal", "123 Street");
        ServiceWorkshop workshop2 = new ServiceWorkshop("Johnsons Service Workshop", "External", "456 Avenue");
        serviceWorkshopRegister.addWorkshop(workshop1);
        serviceWorkshopRegister.addWorkshop(workshop2);
        // Create vehicles
        for (int i = 0; i < 10; i++){
            RandomVehicleFactory randomVehicleFactory = new RandomVehicleFactory(serviceWorkshopRegister);
            vehicleRegister.addVehicle(randomVehicleFactory.generateRandomVehicle());
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of the VehicleRegister class
        VehicleRegister vehicleRegister = new VehicleRegister();
        ServiceWorkshopRegister serviceWorkshopRegister = new ServiceWorkshopRegister();
        populateWithTestData(vehicleRegister, serviceWorkshopRegister);
    
        // Get the location of the MainView.fxml file using ClassLoader
        URL location = App.class.getResource("/fxml/MainView.fxml");
    
        // Create a new FXMLLoader with the location of the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(location);
    
        // Load the FXML file to create the UI layout as defined in MainView.fxml
        AnchorPane root = fxmlLoader.load();
    
        // Get the controller associated with the FXML file
        MainViewController controller = fxmlLoader.getController();
    
        // Set the vehicle register in the controller to link the UI with the vehicle registration logic
        controller.setVehicleRegister(vehicleRegister);
        controller.setServiceWorkshopRegister(serviceWorkshopRegister);
    
        // Create a scene with the root node and set it on the primary stage
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        scene.getStylesheets().add(getClass().getResource("/css/dark-theme.css").toExternalForm());
    
        // Set the title of the primary stage and display the stage
        primaryStage.setTitle("Vehicle Manager App");
        primaryStage.show();
    }    
}
