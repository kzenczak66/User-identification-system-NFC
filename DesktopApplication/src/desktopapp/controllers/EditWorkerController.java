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
import javafx.scene.input.ContextMenuEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditWorkerController implements Initializable {

    @FXML
    private TextField txtLogin;

    @FXML
    private Button btnEdit;

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
    private CheckBox chkPass;

    @FXML
    private CheckBox chkId;

    @FXML
    private Button btnExit;

    Parent root = null;
    public static int id_p;
    public static String imie;
    public static String nazwisko;
    public static String login;
    public static String haslo;
    public static String id_u;
    public static String upra;


    public static String dataArduino;

    public static String getDataArduino() {
        return dataArduino;
    }

    public static void setDataArduino(String dataArduino) {
        EditWorkerController.dataArduino = dataArduino;
    }

    public static int getId_p() {
        return id_p;
    }

    public static void setId_p(int id_pr) {
        id_p = id_pr;
    }

    public static String getImie() {
        return imie;
    }

    public static void setImie(String imiee) {
        imie = imiee;
    }

    public static String getNazwisko() {
        return nazwisko;
    }

    public static void setNazwisko(String nazwiskoo) {
        nazwisko = nazwiskoo;
    }

    public static String getLogin() {
        return login;
    }

    public static void setLogin(String loginn) {
        login = loginn;
    }

    public static String getHaslo() {
        return haslo;
    }

    public static void setHaslo(String hasloo) {
        haslo = hasloo;
    }

    public static String getId_u() {
        return id_u;
    }

    public static void setId_u(String id_urz) {
        id_u = id_urz;
    }

    public static String getUpra() {
        return upra;
    }

    public static void setUpra(String upraw) {
        upra = upraw;
    }

    @FXML
    void btnEditOnAction(ActionEvent event) {
        DbConnect connect = new DbConnect();
        Connection connDB = connect.getConnection();

        int id_p = getId_p();
        String imie = txtImie.getText();
        String nazwisko = txtNazwisko.getText();
        String login = txtLogin.getText();
        String id_u = txtId_urz.getText();


        if (radioA.isSelected()) {
            String upra = "A";

            danePol(connDB, id_p, imie, nazwisko, login, id_u, upra);

        } else if (radioP.isSelected()) {
            String upra = "U";
            danePol(connDB, id_p, imie, nazwisko, login, id_u, upra);
        }

    }


    public void danePol(Connection connDB, int id_p, String imie, String nazwisko, String login, String id_u, String upra) {
        if (chkPass.isSelected()) {
            String haslo = txtHaslo.getText();
            String hashHaslo = BCrypt.hashpw(haslo, BCrypt.gensalt(10));

            String sql = "UPDATE `pracownicy` SET `imie`='" + imie + "',`nazwisko`='" + nazwisko + "',`username` = '" + login + "', `password` = '" + hashHaslo + "',`idurzadzenia`='" + id_u + "', `uprawnienia` = '" + upra + "' WHERE `pracownicy`.`id_pracownika` = '" + id_p + "'";
            try {
                Statement statement = connDB.createStatement();
                statement.executeUpdate(sql);
                execSQL();
                statement.close();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Nie można edytować wpisu!");
                alert.setContentText(null);
                alert.show();
                throwables.printStackTrace();
            }
        } else {
            String haslo = txtHaslo.getText();
            String sql = "UPDATE `pracownicy` SET `imie`='" + imie + "',`nazwisko`='" + nazwisko + "',`username` = '" + login + "', `password` = '" + haslo + "',`idurzadzenia`='" + id_u + "', `uprawnienia` = '" + upra + "' WHERE `pracownicy`.`id_pracownika` = '" + id_p + "'";
            try {
                Statement statement = connDB.createStatement();
                statement.executeUpdate(sql);
                execSQL();
                statement.close();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Nie można edytować wpisu!");
                alert.setContentText(null);
                alert.show();
                throwables.printStackTrace();
            }
        }
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
            alert.setHeaderText("Zedytowano wpis!");
            alert.setContentText(null);
            alert.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void updateOnMouseClicked(ContextMenuEvent event) {
        txtId_urz.setText(Main.dataArduino);

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        txtImie.setText(imie);
        txtNazwisko.setText(nazwisko);
        txtLogin.setText(login);
        txtHaslo.setText(haslo);
        txtId_urz.setText(id_u);

        txtHaslo.disableProperty().bind(chkPass.selectedProperty().not());
        txtId_urz.disableProperty().bind(chkId.selectedProperty().not());


        if (upraw.equals("A")) {
            radioA.setSelected(true);
        } else if (upraw.equals("P")) {
            radioP.setSelected(true);
        }

    }

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

}
