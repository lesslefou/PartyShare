package lisa.duterte.partyshare;

import java.util.ArrayList;

public class Activity {
    private String name,location,DrinkChoice,FoodChoice;
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

    public String getDrinkChoice() {
        return DrinkChoice;
    }

    public void setDrinkChoice(String drinkChoice) {
        DrinkChoice = drinkChoice;
    }

    public String getFoodChoice() {
        return FoodChoice;
    }

    public void setFoodChoice(String foodChoice) {
        FoodChoice = foodChoice;
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
