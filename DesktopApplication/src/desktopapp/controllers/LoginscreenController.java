package desktopapp.controllers;

import desktopapp.app.BCrypt;
import desktopapp.app.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;


public class LoginscreenController implements Initializable {
    @FXML
    private Button btn_zaloguj;
    @FXML
    private Button btn_wyjdz;
    @FXML
    private TextField text_login;
    @FXML
    private TextField text_IP;
    @FXML
    private PasswordField text_haslo;
    @FXML
    private Label lbl_login_info;

    public void btn_wyjdzOnAction(ActionEvent event) {
        Stage stage = (Stage) btn_wyjdz.getScene().getWindow();
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
                event.consume();
            } else {
                stage.close();
            }

        }
    }
    public void btn_zalogujOnAction(ActionEvent event) {
        if (!text_login.getText().isEmpty() && !text_haslo.getText().isEmpty()) {
            validateLogin();
        } else {
            lbl_login_info.setText("Nieprawidłowy login lub hasło!");
        }

    }
    public void validateLogin() {
        DbConnect connect = new DbConnect();
        Connection connDB = connect.getConnection();
        String login = text_login.getText();
        String haslo = text_haslo.getText();

        String passw = "SELECT id_pracownika,username,password,uprawnienia FROM pracownicy WHERE username='" + login + "'";

        try {
            Statement statement = connDB.createStatement();
            ResultSet queryResult = statement.executeQuery(passw);

            while (queryResult.next()) {
                int id_pracownika = queryResult.getInt(1);
                String loggin = queryResult.getString(2);
                String password = queryResult.getString(3);
                String uprawnienia = queryResult.getString(4);

                if (BCrypt.checkpw(haslo, password))
                {
                    if(uprawnienia.equals("A"))
                    {
                        Parent root = null;
                        try {
                            Stage stage = (Stage) btn_zaloguj.getScene().getWindow();
                            stage.close();
                            root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
                            Stage mainStage = new Stage();
                            mainStage.initStyle(StageStyle.UTILITY);
                            mainStage.setTitle("Aplikacja");
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
                    else
                    {
                        lbl_login_info.setText("Odmowa dostępu");
                    }

                } else {
                    lbl_login_info.setText("Nieprawidłowy login lub hasło!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
            lbl_login_info.setText("Błąd połączenia z bazą danych!");
        }


    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            text_IP.setText(String.valueOf(InetAddress.getLocalHost()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}

