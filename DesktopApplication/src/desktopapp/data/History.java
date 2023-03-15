package desktopapp.data;

import javafx.beans.property.*;

public class History
{
    private final IntegerProperty id_h=new SimpleIntegerProperty();
    private final StringProperty osoba=new SimpleStringProperty();
    private final StringProperty data=new SimpleStringProperty();
    private final StringProperty godzina=new SimpleStringProperty();
    private final StringProperty rodzaj =new SimpleStringProperty();

    public String getGodzina() {
        return godzina.get();
    }

    public StringProperty godzinaProperty() {
        return godzina;
    }

    public void setGodzina(String godzina) {
        this.godzina.set(godzina);
    }

    public int getId_h() {
        return id_h.get();
    }

    public IntegerProperty id_hProperty() {
        return id_h;
    }

    public void setId_h(int id_h) {
        this.id_h.set(id_h);
    }

    public String getOsoba() {
        return osoba.get();
    }

    public StringProperty osobaProperty() {
        return osoba;
    }

    public void setOsoba(String osoba) {
        this.osoba.set(osoba);
    }

    public String getData() {
        return data.get();
    }

    public StringProperty dataProperty() {
        return data;
    }

    public void setData(String data) {
        this.data.set(data);
    }

    public String getRodzaj() {
        return rodzaj.get();
    }

    public StringProperty rodzajProperty() {
        return rodzaj;
    }

    public void setRodzaj(String rodzaj) {
        this.rodzaj.set(rodzaj);
    }
}
