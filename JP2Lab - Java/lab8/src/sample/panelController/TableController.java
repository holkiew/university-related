package sample.panelController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import sample.shared.TeamStatistics;
import sample.util.MemoryDataKeeper;

/**
 * Created by DZONI on 12.12.2016.
 */
public class TableController {
    private TableColumnsConfig tableColumnsController;
    private TableView table;
    private ObservableList<TeamStatistics> teamsList = FXCollections.observableArrayList();

    public TableController(TableView table, TableColumn index, TableColumn druzyna, TableColumn m, TableColumn z, TableColumn p, TableColumn ppd, TableColumn b, TableColumn bramki, TableColumn pkt) {
        this.table = table;
        if(MemoryDataKeeper.data.containsKey("tableValues")){
            this.teamsList =(ObservableList<TeamStatistics>) MemoryDataKeeper.data.get("tableValues");
        }
        this.table.setItems(teamsList);
        this.tableColumnsController = new TableColumnsConfig(index, druzyna, m, z, p, ppd, b, bramki, pkt, teamsList);

        if(!MemoryDataKeeper.data.containsKey("testData")) {
            tableColumnsController.setTestData();
        }
        MemoryDataKeeper.data.put("testData", true);
        setIndexes();

    }

    public void addRow(TeamStatistics teamStatistics) {
        teamsList.add(teamStatistics);
        setIndexes();
    }

    public void clear() {
        teamsList.clear();
    }

    public void removeSelectedRow() {
        Object selectedRow = table.getSelectionModel().getSelectedItem();
        teamsList.remove(selectedRow);
        setIndexes();
    }

    public void batchInsert(ObservableList<TeamStatistics> teamsStatistics) {
        teamsStatistics.forEach(s -> teamsList.add(s));
        setIndexes();
    }
    public ObservableList<TeamStatistics> getTeamList(){
        return teamsList;
    }

    private void setIndexes() {
        teamsList.sort((o1, o2) -> Integer.compare(o1.getPkt(), o2.getPkt()));
        Integer idx = 0;
        for (TeamStatistics ts : teamsList) {
            ts.setIndex(++idx);
        }
    }

    private class TableColumnsConfig {
        private TableColumn index;
        private TableColumn druzyna;
        private TableColumn m;
        private TableColumn z;
        private TableColumn p;
        private TableColumn ppd;
        private TableColumn b;
        private TableColumn bramki;
        private TableColumn pkt;

        private ObservableList<TeamStatistics> observableList;

        public TableColumnsConfig(TableColumn index, TableColumn druzyna, TableColumn m, TableColumn z, TableColumn p, TableColumn ppd, TableColumn b, TableColumn bramki, TableColumn pkt, ObservableList<TeamStatistics> observableList) {
            this.index = index;
            this.druzyna = druzyna;
            this.m = m;
            this.z = z;
            this.p = p;
            this.ppd = ppd;
            this.b = b;
            this.bramki = bramki;
            this.pkt = pkt;
            this.observableList = observableList;

            configView();
        }

        private void configView() {
            index.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("index"));
            druzyna.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, String>("druzyna"));
            m.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("m"));
            z.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("z"));
            p.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("p"));
            ppd.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("ppd"));
            b.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("b"));
            bramki.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, String>("bramki"));
            pkt.setCellValueFactory(
                    new PropertyValueFactory<TeamStatistics, Integer>("pkt"));
        }

        public void setTestData() {
            observableList.add(new TeamStatistics("druzyna", 35, 4, 3, 42, 1, "bramki", 10));
            observableList.add(new TeamStatistics("druzyna", 15, 44, 33, 2, 11, "bramki", 15));
            observableList.add(new TeamStatistics("druzyna", 25, 4, 3, 22, 21, "bramki", 23));
            observableList.add(new TeamStatistics("druzyna", 5, 24, 33, 2, 31, "bramki", 11));
        }
    }
}
