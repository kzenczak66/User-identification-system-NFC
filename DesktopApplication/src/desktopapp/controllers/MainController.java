package desktopapp.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Optional;


public class MainController {

    @FXML
    private Button btnWyloguj;

    @FXML
    private Button btnWorkers;

    private static int kto_zalogowany;

    private static String uprawnienia;
    Parent root = null;
    private static int id_p = 0;


    public String getUprawnienia() {
        return uprawnienia;
    }

    public static void setUprawnienia(String upraw) {
        uprawnienia = upraw;
    }


    public void btnWylogujOnAction(ActionEvent event) {
        try {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Wyjście z aplikacji");
            alert.setHeaderText(null);
            alert.setContentText(String.format("Czy na pewno chcesz się wylogować?"));
            Optional<ButtonType> res = alert.showAndWait();

            if (res.isPresent()) {
                if (res.get().equals(ButtonType.CANCEL)) {
                    alert.close();
                } else {
                    Parent root = FXMLLoader.load(getClass().getResource("../fxml/loginscreen.fxml"));
                    Stage mainStage = new Stage();
                    mainStage.initStyle(StageStyle.UTILITY);
                    mainStage.setTitle("Aplikacja - logowanie");
                    mainStage.setResizable(false);
                    mainStage.setScene(new Scene(root));
                    mainStage.show();
                    exitAlert(mainStage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void exitAlert(Stage mainStage) {
        mainStage.setOnCloseRequest(e ->
        {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.getButtonTypes().remove(ButtonType.OK);
            alert.getButtonTypes().add(ButtonType.CANCEL);
            alert.getButtonTypes().add(ButtonType.YES);
            alert.setTitle("Wyjście z aplikacji");
            alert.setHeaderText(null);
            alert.setContentText(String.format("Czy na pewno chcesz wyjść?"));
            Optional<ButtonType> res = alert.showAndWait();

            if (res.isPresent()) {
                if (res.get().equals(ButtonType.CANCEL)) {
                    e.consume();
                } else {
                    mainStage.close();
                }
            }
        });
        Stage stage = (Stage) btnWyloguj.getScene().getWindow();
        stage.close();
    }

    public void btnWorkersOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnWorkers.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/workers.fxml"));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja - zarządzanie pracownikami");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            mainStage.setOnCloseRequest(e ->
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getButtonTypes().remove(ButtonType.OK);
                alert.getButtonTypes().add(ButtonType.CANCEL);
                alert.getButtonTypes().add(ButtonType.YES);
                alert.setTitle("Wyjście z aplikacji");
                alert.setHeaderText(null);
                alert.setContentText(String.format("Czy na pewno chcesz wyjść?"));
                Optional<ButtonType> res = alert.showAndWait();

                if (res.isPresent()) {
                    if (res.get().equals(ButtonType.CANCEL)) {
                        e.consume();
                    } else {
                        mainStage.close();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void btnHistoryOnAction(ActionEvent actionEvent) {
        try {
            Stage stage = (Stage) btnWorkers.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/history.fxml"));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja - zarządzanie historią");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            mainStage.setOnCloseRequest(e ->
            {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.getButtonTypes().remove(ButtonType.OK);
                alert.getButtonTypes().add(ButtonType.CANCEL);
                alert.getButtonTypes().add(ButtonType.YES);
                alert.setTitle("Wyjście z aplikacji");
                alert.setHeaderText(null);
                alert.setContentText(String.format("Czy na pewno chcesz wyjść?"));
                Optional<ButtonType> res = alert.showAndWait();

                if (res.isPresent()) {
                    if (res.get().equals(ButtonType.CANCEL)) {
                        e.consume();
                    } else {
                        mainStage.close();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
