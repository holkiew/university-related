package sample.shared;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.*;

/**
 * Created by DZONI on 12.12.2016.
 */

@XmlRootElement(name = "Tabela")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeamsStatistics{

    @XmlElement(name = "Team")
    private ObservableList<TeamStatistics> list = FXCollections.observableArrayList();

    public TeamsStatistics(){
    }

    public ObservableList<TeamStatistics> getList() {
        return list;
    }

    public void setList(ObservableList<TeamStatistics> list) {
        this.list = list;
    }
}
