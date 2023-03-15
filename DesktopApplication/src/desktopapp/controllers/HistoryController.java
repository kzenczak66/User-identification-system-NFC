package desktopapp.controllers;

import desktopapp.app.DbConnect;
import desktopapp.data.History;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class HistoryController implements Initializable {

    @FXML
    private TableView<History> tabHistory;

    @FXML
    private TableColumn<History, Integer> colId_h;

    @FXML
    private TableColumn<History, String> colOsoba;

    @FXML
    private TableColumn<History, String> colData;

    @FXML
    private TableColumn<History, String> colGodzina;

    @FXML
    private TableColumn<History, String> colRodzaj;

    @FXML
    private Button btnExit;

    @FXML
    private Button btnOdswiez;

    @FXML
    private TextField txtSearch;

    @FXML
    private ToggleGroup szukaj;

    @FXML
    private RadioButton chkOsoba;

    @FXML
    private RadioButton chkData;

    @FXML
    private RadioButton chkRodzaj;

    private ObservableList<History> list;
    private Connection conn;
    private DbConnect dbConnect;
    Parent root = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dbConnect = new DbConnect();
        populateTableView();
    }

    private void populateTableView() {
        try {
            list = FXCollections.observableArrayList();
            conn = dbConnect.getConnection();
            String sql = "SELECT historia.id_historia,CONCAT(pracownicy.imie , ' ', pracownicy.nazwisko) as osoba,historia.data,TIME_FORMAT(historia.godzina,'%H:%i:%s') as godzina,historia.rodzaj FROM pracownicy inner join historia on pracownicy.id_pracownika = historia.osoba";

            ResultSet set = conn.createStatement().executeQuery(sql);

            while (set.next()) {
                History historia = new History();
                historia.setId_h(set.getInt(1));
                historia.setOsoba(set.getString(2));
                historia.setData(set.getString(3));
                historia.setGodzina(set.getString(4));
                historia.setRodzaj(set.getString(5));


                list.add(historia);
            }

            colId_h.setCellValueFactory(new PropertyValueFactory<>("id_h"));
            colOsoba.setCellValueFactory(new PropertyValueFactory<>("osoba"));
            colData.setCellValueFactory(new PropertyValueFactory<>("data"));
            colGodzina.setCellValueFactory(new PropertyValueFactory<>("godzina"));
            colRodzaj.setCellValueFactory(new PropertyValueFactory<>("rodzaj"));

            tabHistory.setItems(list);

            tabHistory.getSortOrder().add(colId_h);

            FilteredList<History> filteredData = new FilteredList<>(list, b -> true);

            szukaj.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
                public void changed(ObservableValue<? extends Toggle> ov, Toggle toggle, Toggle new_toggle) {
                    if (chkOsoba.isSelected()) {
                        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                            filteredData.setPredicate(history -> {
                                if (newValue == null || newValue.isEmpty()) {
                                    return true;
                                }

                                String lowerCaseFilter = newValue.toLowerCase();

                                if (history.getOsoba().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                    return true;
                                }
                                return false;
                            });
                        });
                    } else if (chkData.isSelected()) {
                        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                            filteredData.setPredicate(history -> {
                                if (newValue == null || newValue.isEmpty()) {
                                    return true;
                                }

                                String lowerCaseFilter = newValue.toLowerCase();

                                if (history.getData().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                    return true;
                                }
                                return false;
                            });
                        });
                    } else if (chkRodzaj.isSelected()) {
                        txtSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                            filteredData.setPredicate(history -> {
                                if (newValue == null || newValue.isEmpty()) {
                                    return true;
                                }

                                String lowerCaseFilter = newValue.toLowerCase();

                                if (history.getRodzaj().toLowerCase().indexOf(lowerCaseFilter) != -1) {
                                    return true;
                                }
                                return false;
                            });
                        });
                    }
                }
            });


            SortedList<History> sortedData = new SortedList<>(filteredData);

            sortedData.comparatorProperty().bind(tabHistory.comparatorProperty());
            tabHistory.setItems(sortedData);
            tabHistory.getSortOrder().add(colId_h);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
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

    public void btnExitOnAction(javafx.event.ActionEvent actionEvent) {
        try {
            root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
            Stage mainStage = new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            exitAlert(mainStage);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void btnOdswiezOnAction(ActionEvent actionEvent)
    {
        populateTableView();
    }
}
