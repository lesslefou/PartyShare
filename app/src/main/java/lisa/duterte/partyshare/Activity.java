package lisa.duterte.partyshare;

import java.util.ArrayList;

public class Activity {
    private String name,location,DrinkName,DrinkFood;
    private ArrayList<String> Friends;
    private Integer quantity;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public ArrayList<String> getFriends() {
        return Friends;
    }

    public void setFriends(ArrayList<String> friends) {
        Friends = friends;
    }

    public String getDrinkName() {
        return DrinkName;
    }

    public void setDrinkName(String drinkName) {
        DrinkName = drinkName;
    }

    public String getDrinkFood() {
        return DrinkFood;
    }

    public void setDrinkFood(String drinkFood) {
        DrinkFood = drinkFood;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String toString() {
        return " " + this.name + " ";
    }
}
