package edu.guilford;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Pair;
import java.security.NoSuchAlgorithmException;

public class PasswordManagerFX extends Application {

    private HashtablePassword data = new HashtablePassword(15, 0.5F, 0);

    @Override
    public void start(Stage primaryStage) {
        // Show splash screen first
        showSplashScreen(primaryStage);
    }

    private void showSplashScreen(Stage primaryStage) {
        Stage splashStage = new Stage();
        StackPane splashRoot = new StackPane();
        ProgressBar progressBar = new ProgressBar(0);
        
        // Splash screen components
        Label title = new Label("PASSWORD MANAGER");
        title.setStyle("-fx-font-size: 24px; -fx-text-fill: white;");
        
        VBox splashBox = new VBox(20, title, progressBar);
        splashBox.setAlignment(Pos.CENTER);
        splashBox.setPadding(new Insets(20));
        
        splashRoot.setStyle("-fx-background-color: #2b2b2b;");
        splashRoot.getChildren().add(splashBox);
        
        Scene splashScene = new Scene(splashRoot, 400, 300);
        splashStage.setScene(splashScene);
        splashStage.show();

        // Simulate loading
        new Thread(() -> {
            for (int i = 0; i <= 100; i++) {
                try {
                    Thread.sleep(30);
                    final int progress = i;
                    Platform.runLater(() -> progressBar.setProgress(progress / 100.0));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Platform.runLater(() -> {
                splashStage.close();
                showMainUI(primaryStage);
            });
        }).start();
    }

    private void showMainUI(Stage primaryStage) {
        primaryStage.setTitle("Password Manager");

        // Create main layout
        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setStyle("-fx-background-color: #f5f5f5;");

        // Create buttons
        Button generateBtn = createStyledButton("GENERATE PASSWORD");
        Button encryptBtn = createStyledButton("ENCRYPT PASSWORD");
        Button storeBtn = createStyledButton("STORE PASSWORD");
        Button searchBtn = createStyledButton("SEARCH PASSWORD");
        Button deleteBtn = createStyledButton("DELETE PASSWORD");

        // Add button actions
        generateBtn.setOnAction(e -> showGeneratePasswordDialog());
        encryptBtn.setOnAction(e -> showEncryptPasswordDialog());
        storeBtn.setOnAction(e -> showStorePasswordDialog());
        searchBtn.setOnAction(e -> showSearchPasswordDialog());
        deleteBtn.setOnAction(e -> showDeletePasswordDialog());

        // Add buttons to layout
        mainLayout.getChildren().addAll(
            generateBtn, encryptBtn, storeBtn, searchBtn, deleteBtn
        );

        // Set scene and show stage
        Scene scene = new Scene(mainLayout, 400, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);
        button.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #3a3a3a; -fx-text-fill: white;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #4a4a4a; -fx-text-fill: white;"));
        button.setOnMouseExited(e -> button.setStyle("-fx-font-size: 14px; -fx-padding: 10px; -fx-background-color: #3a3a3a; -fx-text-fill: white;"));
        return button;
    }

    private void showGeneratePasswordDialog() {
        TextInputDialog dialog = new TextInputDialog("12");
        dialog.setTitle("Generate Password");
        dialog.setHeaderText("Enter password length");
        dialog.setContentText("Length (minimum 8 characters):");

        dialog.showAndWait().ifPresent(lengthStr -> {
            try {
                int length = Integer.parseInt(lengthStr);
                if (length >= 8) {
                    PasswordGenerator generator = new PasswordGenerator();
                    String password = generator.generatePassword(length);
                    
                    TextArea textArea = new TextArea(password);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Generated Password");
                    alert.setHeaderText("Your secure password:");
                    alert.getDialogPane().setContent(textArea);
                    alert.showAndWait();
                } else {
                    showAlert("Invalid Input", "Password length must be at least 8 characters!", Alert.AlertType.WARNING);
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid Input", "Please enter a valid number!", Alert.AlertType.ERROR);
            }
        });
    }

    private void showEncryptPasswordDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Encrypt Password");
        dialog.setHeaderText("Enter password to encrypt");
        dialog.setContentText("Password:");

        dialog.showAndWait().ifPresent(password -> {
            if (!password.isEmpty()) {
                try {
                    byte[] salt = passwordEncryption.getSalt();
                    String encrypted = passwordEncryption.get_SHA_1_SecurePassword(password, salt);
                    
                    TextArea textArea = new TextArea(encrypted);
                    textArea.setEditable(false);
                    textArea.setWrapText(true);
                    
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Encrypted Password");
                    alert.setHeaderText("Your encrypted password:");
                    alert.getDialogPane().setContent(textArea);
                    alert.showAndWait();
                } catch (NoSuchAlgorithmException e) {
                    showAlert("Error", "Encryption failed: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            } else {
                showAlert("Error", "Please enter a password!", Alert.AlertType.WARNING);
            }
        });
    }

    private void showStorePasswordDialog() {
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Store Password");
        dialog.setHeaderText("Enter account credentials");

        // Set the button types
        ButtonType storeButtonType = new ButtonType("Store", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(storeButtonType, ButtonType.CANCEL);

        // Create the username and password fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField username = new TextField();
        username.setPromptText("Username");
        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Username:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("Password:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default
        Platform.runLater(username::requestFocus);

        // Convert the result to a username-password-pair when the store button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == storeButtonType) {
                return new Pair<>(username.getText(), password.getText());
            }
            return null;
        });

        dialog.showAndWait().ifPresent(credentials -> {
            if (!credentials.getKey().isEmpty() && !credentials.getValue().isEmpty()) {
                data.add_Acc(credentials.getKey(), credentials.getValue());
                showAlert("Success", "Account stored successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Both fields are required!", Alert.AlertType.WARNING);
            }
        });
    }

    private void showSearchPasswordDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Search Password");
        dialog.setHeaderText("Enter account name to search");
        dialog.setContentText("Account:");

        dialog.showAndWait().ifPresent(account -> {
            Object password = data.get_Acc(account.toLowerCase());
            if (password != null) {
                TextArea textArea = new TextArea(password.toString());
                textArea.setEditable(false);
                textArea.setWrapText(true);
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Password Found");
                alert.setHeaderText("Password for " + account + ":");
                alert.getDialogPane().setContent(textArea);
                alert.showAndWait();
            } else {
                showAlert("Not Found", "Account not found!", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void showDeletePasswordDialog() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Delete Password");
        dialog.setHeaderText("Enter account name to delete");
        dialog.setContentText("Account:");

        dialog.showAndWait().ifPresent(account -> {
            Object result = data.remove_Acc(account.toLowerCase());
            if (result != null) {
                showAlert("Success", "Account deleted successfully!", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Not Found", "Account not found!", Alert.AlertType.INFORMATION);
            }
        });
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}