package com.fitbit.sampleandroidoauth2.Home;



public class HMFoodClass {
    private int foodID;
    private String foodName;
    private String foodBrandName;
    private int foodCalories;
    private int foodServingSize;
    private int foodServingSizeUnit;


    public HMFoodClass(int foodID, String foodName, String foodBrandName, int foodCalories, int foodServingSize, int foodServingSizeUnit) {
        this.foodID = foodID;
        this.foodName = foodName;
        this.foodBrandName = foodBrandName;
        this.foodCalories = foodCalories;
        this.foodServingSize = foodServingSize;
        this.foodServingSizeUnit = foodServingSizeUnit;
    }


    public int getFoodID() {
        return foodID;
    }

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodBrandName() {
        return foodBrandName;
    }

    public void setFoodBrandName(String foodBrandName) {
        this.foodBrandName = foodBrandName;
    }

    public int getFoodCalories() {
        return foodCalories;
    }

    public void setFoodCalories(int foodCalories) {
        this.foodCalories = foodCalories;
    }

    public int getFoodServingSize() {
        return foodServingSize;
    }

    public void setFoodServingSize(int foodServingSize) {
        this.foodServingSize = foodServingSize;
    }

    public int getFoodServingSizeUnit() {
        return foodServingSizeUnit;
    }

    public void setFoodServingSizeUnit(int foodServingSizeUnit) {
        this.foodServingSizeUnit = foodServingSizeUnit;
    }
}
