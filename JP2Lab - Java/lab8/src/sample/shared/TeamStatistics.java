package sample.shared;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by DZONI on 11.12.2016.
 */

public class TeamStatistics {
    IntegerProperty index;
    StringProperty druzyna;
    IntegerProperty m;
    IntegerProperty z;
    IntegerProperty p;
    IntegerProperty ppd;
    IntegerProperty b;
    StringProperty bramki;
    IntegerProperty pkt;

    public TeamStatistics(String druzyna, Integer m, Integer z, Integer p, Integer ppd, Integer b, String bramki, Integer pkt) {
        this.druzyna = new SimpleStringProperty(druzyna);
        this.m = new SimpleIntegerProperty(m);
        this.z = new SimpleIntegerProperty(z);
        this.p = new SimpleIntegerProperty(p);
        this.ppd = new SimpleIntegerProperty(ppd);
        this.b = new SimpleIntegerProperty(b);
        this.bramki = new SimpleStringProperty(bramki);
        this.pkt = new SimpleIntegerProperty(pkt);
        this.index = new SimpleIntegerProperty(new Integer(0));
    }

    public TeamStatistics() {
        index = new SimpleIntegerProperty(new Integer(0));
        druzyna = new SimpleStringProperty(new String());
        m = new SimpleIntegerProperty(new Integer(0));
        z = new SimpleIntegerProperty(new Integer(0));
        p = new SimpleIntegerProperty(new Integer(0));
        ppd = new SimpleIntegerProperty(new Integer(0));
        b = new SimpleIntegerProperty(new Integer(0));
        bramki = new SimpleStringProperty(new String());
        pkt = new SimpleIntegerProperty(new Integer(0));
    }

    public int getIndex() {
        return index.get();
    }

    public IntegerProperty indexProperty() {
        return index;
    }
    @XmlTransient
    public void setIndex(int index) {
        this.index.set(index);
    }

    @XmlAttribute
    public String getDruzyna() {
        return druzyna.get();
    }

    public StringProperty druzynaProperty() {
        return druzyna;
    }

    public void setDruzyna(String druzyna) {
        this.druzyna.set(druzyna);
    }

    public int getM() {
        return m.get();
    }

    public IntegerProperty mProperty() {
        return m;
    }

    public void setM(int m) {
        this.m.set(m);
    }

    public int getZ() {
        return z.get();
    }

    public IntegerProperty zProperty() {
        return z;
    }

    public void setZ(int z) {
        this.z.set(z);
    }

    public int getP() {
        return p.get();
    }

    public IntegerProperty pProperty() {
        return p;
    }

    public void setP(int p) {
        this.p.set(p);
    }

    public int getPpd() {
        return ppd.get();
    }

    public IntegerProperty ppdProperty() {
        return ppd;
    }

    public void setPpd(int ppd) {
        this.ppd.set(ppd);
    }

    @XmlElement
    public int getB() {
        return b.get();
    }

    public IntegerProperty bProperty() {
        return b;
    }

    public void setB(int b) {
        this.b.set(b);
    }

    public String getBramki() {
        return bramki.get();
    }

    public StringProperty bramkiProperty() {
        return bramki;
    }

    public void setBramki(String bramki) {
        this.bramki.set(bramki);
    }

    public int getPkt() {
        return pkt.get();
    }

    public IntegerProperty pktProperty() {
        return pkt;
    }

    public void setPkt(int pkt) {
        this.pkt.set(pkt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TeamStatistics that = (TeamStatistics) o;

        if (index != null ? !index.equals(that.index) : that.index != null) return false;
        if (druzyna != null ? !druzyna.equals(that.druzyna) : that.druzyna != null) return false;
        if (m != null ? !m.equals(that.m) : that.m != null) return false;
        if (z != null ? !z.equals(that.z) : that.z != null) return false;
        if (p != null ? !p.equals(that.p) : that.p != null) return false;
        if (ppd != null ? !ppd.equals(that.ppd) : that.ppd != null) return false;
        if (b != null ? !b.equals(that.b) : that.b != null) return false;
        if (bramki != null ? !bramki.equals(that.bramki) : that.bramki != null) return false;
        return pkt != null ? pkt.equals(that.pkt) : that.pkt == null;

    }

    @Override
    public int hashCode() {
        int result = index != null ? index.hashCode() : 0;
        result = 31 * result + (druzyna != null ? druzyna.hashCode() : 0);
        result = 31 * result + (m != null ? m.hashCode() : 0);
        result = 31 * result + (z != null ? z.hashCode() : 0);
        result = 31 * result + (p != null ? p.hashCode() : 0);
        result = 31 * result + (ppd != null ? ppd.hashCode() : 0);
        result = 31 * result + (b != null ? b.hashCode() : 0);
        result = 31 * result + (bramki != null ? bramki.hashCode() : 0);
        result = 31 * result + (pkt != null ? pkt.hashCode() : 0);
        return result;
    }
}
