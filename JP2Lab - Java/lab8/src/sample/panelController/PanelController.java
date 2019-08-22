package sample.panelController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sample.shared.TeamStatistics;
import sample.shared.TeamsStatistics;
import sample.util.MemoryDataKeeper;

import javax.xml.bind.JAXB;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PanelController implements Initializable {

    @FXML
    private TableView<TeamStatistics> tabela;
    @FXML
    TableColumn index;
    @FXML
    TableColumn druzyna;
    @FXML
    TableColumn m;
    @FXML
    TableColumn z;
    @FXML
    TableColumn p;
    @FXML
    TableColumn ppd;
    @FXML
    TableColumn b;
    @FXML
    TableColumn bramki;
    @FXML
    TableColumn pkt;
    @FXML
    TextField nazwaPliku;


    TableController tableViewController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableViewController = new TableController(tabela, index, druzyna, m, z, p, ppd, b, bramki, pkt);
    }

    @FXML
    private void saveFile(ActionEvent event) {

        File file = new File(nazwaPliku.getText().equals("") ? "defaultName" : nazwaPliku.getText() +".xml");
        try {
            file.createNewFile();
            TeamsStatistics teamsStatistics = new TeamsStatistics();
            teamsStatistics.setList(tableViewController.getTeamList());
            JAXB.marshal(teamsStatistics, file);
            System.out.println("Plik zapisany: "+ file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadFile(ActionEvent event) {
        File file = new File(nazwaPliku.getText().equals("") ? "defaultName" : nazwaPliku.getText() +".xml");
        if(file.exists()) {
            TeamsStatistics teamsStatistics = JAXB.unmarshal(file, TeamsStatistics.class);
            tableViewController.clear();
            tableViewController.batchInsert(teamsStatistics.getList());
            System.out.println("Plik wczytany: "+ file.getAbsolutePath());
        }else {
            System.err.println("Plik nie istnieje");
        }
    }

    @FXML
    private void addTeam(ActionEvent event) {
        try {
            Stage stage = (Stage) tabela.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("../popupController/popup.fxml"));
            stage.setScene(new Scene(root));
            MemoryDataKeeper.data.put("tableValues", tableViewController.getTeamList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteTeam(ActionEvent event) {
        tableViewController.removeSelectedRow();
    }

}
