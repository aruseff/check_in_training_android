package training.ruseff.com.checkintraining.models;

public class User {

    int id;
    String name;
    boolean isChecked;
    boolean isActive;
    int lastPayment;

    public User(int id, String name, boolean isChecked, boolean isActive, int lastPayment) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        this.isActive = isActive;
        this.lastPayment = lastPayment;
    }

    public User(String name, boolean isChecked, boolean isActive, int lastPayment) {
        this.id = id;
        this.name = name;
        this.isChecked = isChecked;
        this.isActive = isActive;
        this.lastPayment = lastPayment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public int getLastPayment() {
        return lastPayment;
    }

    public void setLastPayment(int lastPayment) {
        this.lastPayment = lastPayment;
    }
}
