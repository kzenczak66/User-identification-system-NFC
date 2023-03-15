package desktopapp.app;

import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;

public class CheckAndSend {

    public static String dataToArduino = "";
    public static int id_p=0;
    public static String rodzaj="";

    public void check() {

        try {
            DbConnect dbConnect = new DbConnect();

            Connection conn = dbConnect.getConnection();
            String sql = "SELECT id_pracownika, idurzadzenia FROM pracownicy where idurzadzenia='" + Main.dataArduino + "'";
            ResultSet set = conn.createStatement().executeQuery(sql);
            if(set.next())
            {
                id_p=set.getInt(1);
                String checkID = Main.dataArduino;
                String checkChar=Main.checkChar;
                System.out.println(id_p);
                LocalDate data= LocalDate.now();
                LocalTime godzina=LocalTime.now();
                dataToArduino = "wejscie";
                if(checkChar.equals("w"))
                {
                    rodzaj="wejscie";

                    String dodajSQL="INSERT INTO `historia` (`osoba`, `data`, `godzina`, `rodzaj`) VALUES ('" + id_p + "','" + data + "', '" + godzina + "','" + rodzaj + "')";
                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate(dodajSQL);
                        statement.close();

                    } catch (SQLException throwables) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Nie można dodać wpisu!");
                        alert.setContentText(null);
                        alert.show();
                        throwables.printStackTrace();
                    }
                }
                else if(checkChar.equals("e"))
                {
                    rodzaj="wyjscie";

                    String dodajSQL="INSERT INTO `historia` (`osoba`, `data`, `godzina`, `rodzaj`) VALUES ('" + id_p + "','" + data + "', '" + godzina + "','" + rodzaj + "')";
                    try {
                        Statement statement = conn.createStatement();
                        statement.executeUpdate(dodajSQL);
                        statement.close();

                    } catch (SQLException throwables) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setHeaderText("Nie można dodać wpisu!");
                        alert.setContentText(null);
                        alert.show();
                        throwables.printStackTrace();
                    }
                }
            }
            else
            {
                dataToArduino="brak";
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

}






