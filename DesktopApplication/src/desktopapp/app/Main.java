package desktopapp.app;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;

public class Main extends Application {
    public static String dataArduino = "";
    public static String checkChar = "";
    public static String wejscieData = "";
    public static String wyjscieData = "";
    public static String bladData = "";
    public static String dataToArduino = "";
    final CheckAndSend check = new CheckAndSend();

    @Override
    public void start(Stage primaryStage) throws Exception {
        conn();
        Parent root = FXMLLoader.load(getClass().getResource("../fxml/loginscreen.fxml"));
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.setTitle("Aplikacja - logowanie");
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(e ->
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
                    primaryStage.close();
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }

    private void conn() {
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                SerialPort serialPort = SerialPort.getCommPort("COM8");
                serialPort.setComPortParameters(115200, Byte.SIZE, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
                serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
                serialPort.openPort();
                serialPort.addDataListener(new SerialPortDataListener() {
                    @Override
                    public int getListeningEvents() {
                        return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
                    }

                    @Override
                    public void serialEvent(SerialPortEvent event) {
                        byte[] newData = event.getReceivedData();
                        dataArduino = new String(newData);
                        dataArduino = dataArduino.trim();
                        System.out.println("Odczytany id: " + dataArduino);
                        checkChar = dataArduino.substring(0, 1);
                        dataArduino = dataArduino.substring(1, dataArduino.length());
                        if (checkChar.equals("w")) {
                            wejscieData = dataArduino;
                        } else if (checkChar.equals("e")) {
                            wyjscieData = dataArduino;
                        }
                        check.check();
                        dataToArduino = CheckAndSend.dataToArduino;
                        serialPort.writeBytes(dataToArduino.getBytes(), dataToArduino.length());
                    }
                });
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
