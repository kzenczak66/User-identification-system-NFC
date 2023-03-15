package desktopapp.controllers;

import desktopapp.app.DbConnect;
import desktopapp.data.Workers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import java.util.ResourceBundle;

public class WorkersController implements Initializable {
    @FXML
    private TableView<Workers> tabWorkers;

    @FXML
    private TableColumn<Workers, Integer> colId_p;

    @FXML
    private TableColumn<Workers, String> colImie;

    @FXML
    private TableColumn<Workers, String> colNazwisko;

    @FXML
    private TableColumn<Workers, String> colLogin;

    @FXML
    private TableColumn<Workers, String> colHaslo;

    @FXML
    private TableColumn<Workers, String> colId_u;

    @FXML
    private TableColumn<Workers, String> colUpraw;

    @FXML
    private TableColumn colEdytuj;

    @FXML
    private TableColumn colUsun;

    @FXML
    private Button btnAddWorker;

    @FXML
    private Button btnExit;

    private Connection conn;
    private DbConnect dbConnect;
    private ObservableList<Workers> list;
    Parent root=null;

    private static String uprawnienia;
    public String getUprawnienia()
    {
        return uprawnienia;
    }

    public static void setUprawnienia(String upraw)
    {
        uprawnienia=upraw;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        dbConnect=new DbConnect();
        populateTableView();
    }

    private void populateTableView()
    {
        try
        {
            list= FXCollections.observableArrayList();
            conn= dbConnect.getConnection();
            String sql="SELECT * FROM pracownicy";
            ResultSet set=conn.createStatement().executeQuery(sql);

            while(set.next())
            {
                Workers osoba=new Workers();
                osoba.setId_p(set.getInt(1));
                osoba.setImie(set.getString(2));
                osoba.setNazwisko(set.getString(3));
                osoba.setLogin(set.getString(4));
                osoba.setHaslo(set.getString(5));
                osoba.setId_u(set.getString(6));
                osoba.setUprawnienia(set.getString(7));

                list.add(osoba);
            }

            colId_p.setCellValueFactory(new PropertyValueFactory<>("id_p"));
            colImie.setCellValueFactory(new PropertyValueFactory<>("imie"));
            colNazwisko.setCellValueFactory(new PropertyValueFactory<>("nazwisko"));
            colLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
            colHaslo.setCellValueFactory(new PropertyValueFactory<>("haslo"));
            colId_u.setCellValueFactory(new PropertyValueFactory<>("id_u"));
            colUpraw.setCellValueFactory(new PropertyValueFactory<>("uprawnienia"));

            //EDYCJA UZYTKOWNIKA
            Callback<TableColumn<Workers,String>, TableCell<Workers,String>> cellFactory=(param)->
            {
                final TableCell<Workers,String> cell = new TableCell<Workers,String>()
                {
                    @Override
                    public void updateItem(String item, boolean empty)
                    {
                        super.updateItem(item,empty);

                        if(empty)
                        {
                            setGraphic(null);
                            setText(null);
                        }
                        else
                        {
                            final Button editButton=new Button("Edytuj");
                            editButton.setOnAction(event ->
                            {
                                Workers o=getTableView().getItems().get(getIndex());

                                EditWorkerController.setId_p(o.getId_p());
                                EditWorkerController.setImie(o.getImie());
                                EditWorkerController.setNazwisko(o.getNazwisko());
                                EditWorkerController.setLogin(o.getLogin());
                                EditWorkerController.setHaslo(o.getHaslo());
                                EditWorkerController.setId_u(o.getId_u());
                                EditWorkerController.setUpra(o.getUprawnienia());
                                try
                                {
                                    root = FXMLLoader.load(getClass().getResource("../fxml/editworker.fxml"));
                                    Stage mainStage=new Stage();
                                    mainStage.initStyle(StageStyle.UTILITY);
                                    mainStage.setTitle("Aplikacja - edycja pracownika");
                                    mainStage.setResizable(false);
                                    mainStage.setScene(new Scene(root));
                                    mainStage.show();
                                    exitAlert(mainStage);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            });
                            setGraphic(editButton);
                            setText(null);
                        }
                    };
                };
                return cell;
            };
            colEdytuj.setCellFactory(cellFactory);

            //USUWANIE UZYTKOWNIKA
            Callback<TableColumn<Workers,String>, TableCell<Workers,String>> celllFactory=(param)->
            {
                    final TableCell<Workers,String> cell = new TableCell<Workers,String>()
                    {
                        @Override
                        public void updateItem(String item, boolean empty)
                        {
                            super.updateItem(item,empty);

                            if(empty)
                            {
                                setGraphic(null);
                                setText(null);
                            }
                            else
                            {
                                final Button usunButton=new Button("Usuń");
                                usunButton.setOnAction(event ->
                                {
                                    Workers o=getTableView().getItems().get(getIndex());

                                    int idDel=o.getId_p();
                                    DbConnect connect=new DbConnect();
                                    Connection connDB=connect.getConnection();


                                    String sqlDel="DELETE FROM pracownicy WHERE id_pracownika='"+idDel+"'";

                                    try
                                    {
                                        Statement statementt = connDB.createStatement();
                                        statementt.executeUpdate(sqlDel);


                                        try
                                        {

                                            root = FXMLLoader.load(getClass().getResource("../fxml/workers.fxml"));
                                            Stage mainStage=new Stage();
                                            mainStage.initStyle(StageStyle.UTILITY);
                                            mainStage.setTitle("PolKomp - zarządzanie użytkownikami");
                                            mainStage.setResizable(false);
                                            mainStage.setScene(new Scene(root));

                                            mainStage.show();
                                            exitAlert(mainStage);
                                        }
                                        catch (IOException e)
                                        {

                                            e.printStackTrace();
                                        }

                                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                                        alert.setHeaderText("Usunięto wpis!");
                                        alert.setContentText(null);
                                        alert.show();


                                    }
                                    catch (SQLException throwables)
                                    {
                                        Alert alert=new Alert(Alert.AlertType.ERROR);
                                        alert.setHeaderText("Nie można usunąć użytkownika!");
                                        alert.setContentText(null);
                                        alert.show();
                                        throwables.printStackTrace();
                                    }

                                });
                                setGraphic(usunButton);
                                setText(null);
                            }
                        };
                    };
                    return cell;
                };
            colUsun.setCellFactory(celllFactory);

            tabWorkers.setItems(list);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    public void exitAlert(Stage mainStage)
    {
        mainStage.setOnCloseRequest(e->
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
        Stage stage=(Stage) btnExit.getScene().getWindow();
        stage.close();
    }

    public void btnExitOnAction(ActionEvent actionEvent)
    {
        try
        {
            root = FXMLLoader.load(getClass().getResource("../fxml/main.fxml"));
            Stage mainStage=new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            exitAlert(mainStage);


        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void btnAddWorkerOnAction(ActionEvent actionEvent)
    {
        try
        {
            Stage stage=(Stage) btnAddWorker.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("../fxml/addworker.fxml"));
            Stage mainStage=new Stage();
            mainStage.initStyle(StageStyle.UTILITY);
            mainStage.setTitle("Aplikacja - dodaj pracownika");
            mainStage.setResizable(false);
            mainStage.setScene(new Scene(root));

            mainStage.show();
            mainStage.setOnCloseRequest(e->
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
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
