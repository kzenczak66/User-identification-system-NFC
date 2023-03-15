package desktopapp.controllers;

import desktopapp.app.BCrypt;
import desktopapp.app.DbConnect;
import desktopapp.app.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddWorkerController implements Initializable {

    Parent root = null;
    public static String dataArduino;

    public static String getDataArduino() {
        return dataArduino;
    }

    public static void setDataArduino(String dataArduin) {
        dataArduino = dataArduin;
    }

    @FXML
    private TextField txtLogin;

    @FXML
    private Button btnAdd;

    @FXML
    private PasswordField txtHaslo;

    @FXML
    private TextField txtNazwisko;

    @FXML
    private ToggleGroup upraw;

    @FXML
    private RadioButton radioA;

    @FXML
    private RadioButton radioP;
    @FXML
    private TextField txtId_urz;

    @FXML
    private TextField txtImie;

    @FXML
    private Button btnExit;

    @FXML
    void btnExitOnAction(ActionEvent event) {
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/workers.fxml"));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja - zarządzanie pracownikami");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            exitAlert(mainStage);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnAddOnAction(ActionEvent event) {
        DbConnect connect = new DbConnect();
        Connection connDB = connect.getConnection();

        String imie = txtImie.getText();
        String nazwisko = txtNazwisko.getText();
        String login = txtLogin.getText();
        String haslo = txtHaslo.getText();
        String id_urz = txtId_urz.getText();

        String hashHaslo = BCrypt.hashpw(haslo, BCrypt.gensalt(10));


        if (imie.isEmpty() && nazwisko.isEmpty() && login.isEmpty() && haslo.isEmpty() && id_urz.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wymagane pola są puste!");
            alert.setContentText(null);
            alert.show();
        } else {
            if (radioA.isSelected()) {
                String uprawnienia = "A";
                String sql = "INSERT INTO `pracownicy` (`imie`,`nazwisko`,`username`, `password`,`idurzadzenia`,`uprawnienia`) " +
                        "                       VALUES ('" + imie + "','" + nazwisko + "','" + login + "', '" + hashHaslo + "','" + id_urz + "','" + uprawnienia + "')";
                try {
                    Statement statement = connDB.createStatement();
                    statement.executeUpdate(sql);

                    execSQL();
                    statement.close();

                } catch (SQLException throwables) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Nie można dodać wpisu!");
                    alert.setContentText(null);
                    alert.show();
                    throwables.printStackTrace();
                }
            } else if (radioP.isSelected()) {
                String uprawnienia = "U";
                String sql = "INSERT INTO `pracownicy` (`imie`,`nazwisko`,`username`, `password`,`idurzadzenia`,`uprawnienia`) VALUES ('" + imie + "','" + nazwisko + "','" + login + "', '" + hashHaslo + "','" + id_urz + "','" + uprawnienia + "')";
                try {
                    Statement statement = connDB.createStatement();
                    statement.executeUpdate(sql);

                    execSQL();
                    statement.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText("Nie można dodać wpisu!");
                    alert.setContentText(null);
                    alert.show();
                }
            }


        }
    }

    @FXML
    void updateOnMouseClicked(MouseEvent event) {
        txtId_urz.setText(Main.dataArduino);
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
        Stage stage = (Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void execSQL() {
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/workers.fxml"));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja - zarządzanie pracownikami");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            exitAlert(mainStage);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("Dodano wpis!");
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }
}
