package desktopapp.data;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Workers
{
    private final IntegerProperty id_p=new SimpleIntegerProperty();
    private final StringProperty imie=new SimpleStringProperty();
    private final StringProperty nazwisko=new SimpleStringProperty();
    private final StringProperty login=new SimpleStringProperty();
    private final StringProperty haslo=new SimpleStringProperty();
    private final StringProperty id_u=new SimpleStringProperty();
    private final StringProperty uprawnienia=new SimpleStringProperty();

    public int getId_p() {
        return id_p.get();
    }

    public IntegerProperty id_pProperty() {
        return id_p;
    }

    public void setId_p(int id_p) {
        this.id_p.set(id_p);
    }

    public String getImie() {
        return imie.get();
    }

    public StringProperty imieProperty() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie.set(imie);
    }

    public String getNazwisko() {
        return nazwisko.get();
    }

    public StringProperty nazwiskoProperty() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko.set(nazwisko);
    }

    public String getLogin() {
        return login.get();
    }

    public StringProperty loginProperty() {
        return login;
    }

    public void setLogin(String login) {
        this.login.set(login);
    }

    public String getHaslo() {
        return haslo.get();
    }

    public StringProperty hasloProperty() {
        return haslo;
    }

    public void setHaslo(String haslo) {
        this.haslo.set(haslo);
    }

    public String getId_u() {
        return id_u.get();
    }

    public StringProperty id_uProperty() {
        return id_u;
    }

    public void setId_u(String id_u) {
        this.id_u.set(id_u);
    }

    public String getUprawnienia() {
        return uprawnienia.get();
    }

    public StringProperty uprawnieniaProperty() {
        return uprawnienia;
    }

    public void setUprawnienia(String uprawnienia) {
        this.uprawnienia.set(uprawnienia);
    }
}
