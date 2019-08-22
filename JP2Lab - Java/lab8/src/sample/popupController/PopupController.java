package sample.popupController;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.shared.TeamStatistics;
import sample.util.MemoryDataKeeper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created by DZONI on 13.12.2016.
 */
public class PopupController implements Initializable {

    @FXML
    Button zatwierdz;
    @FXML
    Button anuluj;
    @FXML
    TextField punkty;
    @FXML
    TextField nazwaD;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config();
    }
    private void config(){
        punkty.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!newValue.matches("\\d*")) {
                    punkty.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    @FXML
    private void submitAdding(ActionEvent event) {
        if(!(punkty.getText().equals("") || nazwaD.getText().equals(""))){
            ((ObservableList<TeamStatistics>) MemoryDataKeeper.data.get("tableValues")).add(
                    new TeamStatistics(nazwaD.getText(), 11, 22, 33, 44, 51, "bramki", Integer.parseInt(punkty.getText())));
            changeScene();
        }else
        {
            System.err.println("Wypelnij wszystkie pola!");
        }

    }

    @FXML
    private void cancelAdding(ActionEvent event) {
        changeScene();
    }
    private void changeScene(){
        try {
            Stage stage = (Stage) zatwierdz.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../panelController/sample.fxml"));
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
