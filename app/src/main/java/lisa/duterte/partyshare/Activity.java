package lisa.duterte.partyshare;

import java.util.ArrayList;

public class Activity {
    private String name,location;
    private ArrayList<String> Foods,Drinks,Friends;
    private Integer foodQuantity, drinkQuantity;


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

    public ArrayList<String> getFoods() {
        return Foods;
    }

    public void setFoods(ArrayList<String> foods) {
        Foods = foods;
    }

    public ArrayList<String> getDrinks() {
        return Drinks;
    }

    public void setDrinks(ArrayList<String> drinks) {
        Drinks = drinks;
    }

    public ArrayList<String> getFriends() {
        return Friends;
    }

    public void setFriends(ArrayList<String> friends) {
        Friends = friends;
    }

    public Integer getFoodQuantity() {
        return foodQuantity;
    }

    public void setFoodQuantity(Integer foodQuantity) {
        this.foodQuantity = foodQuantity;
    }

    public Integer getDrinkQuantity() {
        return drinkQuantity;
    }

    public void setDrinkQuantity(Integer drinkQuantity) {
        this.drinkQuantity = drinkQuantity;
    }

    public String toString() {
        return " " + this.name + " ";
    }
}
