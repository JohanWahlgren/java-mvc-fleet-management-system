package com.javengers;

import javafx.application.Application;
import javafx.stage.Stage;
import com.javengers.models.VehicleRegister;

public class Test extends Application {
    // The main method is the entry point of any Java application.
    // For JavaFX applications, it should call the launch() method, which in turn calls the start() method
    public static void main(String[] args) {
        launch(args);
    }

    // The start method is the main entry point for all JavaFX applications.
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create an instance of the VehicleRegister class
        VehicleRegister vehicleRegister = new VehicleRegister();
    }
}
