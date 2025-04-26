import javafx.scene.control.*;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import polynomial.Polynomial;
import polynomialLists.PolynomialList;
import javafx.application.Application;
import javafx.scene.Scene;

import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;
 public class PolynomialGUI extends Application {
    public PolynomialGUI() {
        super();
    }
    private PolynomialList polynomialList = new PolynomialList();
    private javafx.scene.control.ListView<String> polynomialListView = new javafx.scene.control.ListView<>();
    private javafx.scene.control.TextField inputXField = new javafx.scene.control.TextField();
    private Label outputLabel = new Label("Results will appear here...");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Polynomial Calculator");

        // Main Layout
        BorderPane mainLayout = new BorderPane();
        Scene scene = new Scene(mainLayout, 600, 400);

        // Menu Bar
        MenuBar menuBar = createMenuBar();
        mainLayout.setTop(menuBar);

        // Center Panel for polynomial actions
        VBox centerPanel = createCenterPanel();
        mainLayout.setCenter(centerPanel);

        // Left Panel for displaying polynomials
        VBox leftPanel = createLeftPanel();
        mainLayout.setLeft(leftPanel);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Create the menu bar with options
    private MenuBar createMenuBar() {
        Menu fileMenu = new Menu("File");
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setOnAction(e -> System.exit(0));

        fileMenu.getItems().add(exitItem);

        Menu actionMenu = new Menu("Actions");
        MenuItem createPolynomialItem = new MenuItem("Create New Polynomial");
        createPolynomialItem.setOnAction(e -> createNewPolynomial());

        MenuItem evaluatePolynomialItem = new MenuItem("Evaluate Polynomial");
        evaluatePolynomialItem.setOnAction(e -> evaluatePolynomial());
        MenuItem addPolynomialsItem = new MenuItem("Add Two Polynomials");
        addPolynomialsItem.setOnAction(e -> addPolynomials());

        MenuItem derivePolynomialItem = new MenuItem("Calculate Derivative");
        derivePolynomialItem.setOnAction(e -> calculateDerivative());

        MenuItem deletePolynomialItem = new MenuItem("Delete Polynomial");
        deletePolynomialItem.setOnAction(e -> deletePolynomial());

        actionMenu.getItems().addAll(createPolynomialItem, evaluatePolynomialItem,
                addPolynomialsItem, derivePolynomialItem, deletePolynomialItem);

        return new MenuBar(fileMenu, actionMenu);
    }

    // Left panel with list of polynomials
    private VBox createLeftPanel() {
        VBox leftPanel = new VBox(10);
        leftPanel.setPadding(new javafx.geometry.Insets(10));

        Label polynomialsLabel = new Label("Polynomials:");
        leftPanel.getChildren().add(polynomialsLabel);

        polynomialListView.setPrefHeight(200);
        leftPanel.getChildren().add(polynomialListView);

        return leftPanel;
    }

    // Center panel for polynomial operations like creating and evaluating
    private VBox createCenterPanel() {
        VBox centerPanel = new VBox(10);
        centerPanel.setPadding(new javafx.geometry.Insets(10));

        Label inputLabel = new Label("Enter value for x:");
        centerPanel.getChildren().add(inputLabel);

        inputXField.setPromptText("Enter a value for x...");
        centerPanel.getChildren().add(inputXField);

        Button evaluateButton = new Button("Evaluate Polynomial");
        evaluateButton.setOnAction(e -> evaluatePolynomial());
        centerPanel.getChildren().add(evaluateButton);

        outputLabel.setStyle("-fx-font-weight: bold;");
        centerPanel.getChildren().add(outputLabel);

        return centerPanel;
    }

    // Create a new polynomial
    private void createNewPolynomial() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create Polynomial");
        dialog.setHeaderText("Enter polynomial degree and coefficients:");
        dialog.setContentText("Degree:");

        Optional<String> degreeResult = dialog.showAndWait();
        degreeResult.ifPresent(degree -> {
            try {
                int degreeInt = Integer.parseInt(degree);
                double[] coefficients = new double[degreeInt + 1];

                for (int i = degreeInt; i >= 0; i--) {
                    String coefficientStr = showInputDialog("Coefficient for x^" + i);
                    coefficients[i] = Double.parseDouble(coefficientStr);
                }

                Polynomial polynomial = new Polynomial(coefficients);
                polynomialList.add(polynomial);
                updatePolynomialList();
            } catch (NumberFormatException ex) {
                showAlert("Invalid input! Please enter valid numbers.");
            }
        });
    }

    // Evaluate polynomial at given x
    private void evaluatePolynomial() {
        try {
            double x = Double.parseDouble(inputXField.getText());
            if (polynomialList.size() > 0) {
                Polynomial polynomial = polynomialList.getByIndex(0);  // Just for example: using the first polynomial
                double result = polynomial.evaluate(x);
                outputLabel.setText("Result: y = " + result);
            } else {
                showAlert("No polynomials available to evaluate.");
            }
        } catch (NumberFormatException ex) {
            showAlert("Invalid input! Please enter a valid number for x.");
        }
    }
     private void addPolynomials() {
         if (polynomialList.size() < 2) {
             showAlert("You need at least two polynomials to perform addition.");
             return;
         }

         int firstId = promptForPolynomialID("Enter the ID of the first polynomial:");
         int secondId = promptForPolynomialID("Enter the ID of the second polynomial:");

         Polynomial p1 = polynomialList.getById(firstId);
         Polynomial p2 = polynomialList.getById(secondId);

         if (p1 == null || p2 == null) {
             showAlert("One or both IDs are invalid.");
             return;
         }

         Polynomial result = p1.add(p2);
         polynomialList.add(result);
         updatePolynomialList();
         outputLabel.setText("Added polynomials. Result: " + result);
     }
     private void calculateDerivative() {
         int id = promptForPolynomialID("Enter the ID of the polynomial to derive:");
         Polynomial poly = polynomialList.getById(id);
         if (poly == null) {
             showAlert("Invalid polynomial ID.");
             return;
         }

         Polynomial derivative = poly.derivative();
         polynomialList.add(derivative);
         updatePolynomialList();
         outputLabel.setText("Derived polynomial. Result: " + derivative);
     }
     private void deletePolynomial() {
         int id = promptForPolynomialID("Enter the ID of the polynomial to delete:");
         Polynomial poly = polynomialList.getById(id);
         if (poly == null) {
             showAlert("Invalid polynomial ID.");
             return;
         }

         polynomialList.deleteById(id);
         updatePolynomialList();
         outputLabel.setText("Deleted polynomial with ID: " + id);
     }

    // Update list view with polynomials
    private void updatePolynomialList() {
        polynomialListView.getItems().clear();
        for (int i = 0; i < polynomialList.size(); i++) {
            Polynomial polynomial = polynomialList.getByIndex(i);
            polynomialListView.getItems().add("[" + polynomial.getID() + "] " + polynomial);
        }
    }

    // Show simple input dialog
    private String showInputDialog(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Input");
        dialog.setHeaderText(prompt);
        Optional<String> result = dialog.showAndWait();
        return result.orElse("0");
    }
     private int promptForPolynomialID(String prompt) {
         TextInputDialog dialog = new TextInputDialog();
         dialog.setTitle("Polynomial ID");
         dialog.setHeaderText(prompt);
         Optional<String> result = dialog.showAndWait();
         try {
             return Integer.parseInt(result.orElse("-1"));
         } catch (NumberFormatException e) {
             return -1;
         }
     }
    // Show alert dialog
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
