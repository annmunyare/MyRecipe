package com.example.nyambura.myrecipe;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by nyambura on 4/12/17.
 */

@Parcel
public class Recipe {
    String label;
    String image;
    String url;
    ArrayList<String> ingredientLines;
    Integer calories;
    Integer yield;
    private String pushId;
    String index;

    public Recipe(){}

    public Recipe(String label, String image, String url, ArrayList<String> ingredientLines, Integer calories, Integer yield) {
        this.label = label;
        this.image = image;
        this.url = url;
        this.ingredientLines = ingredientLines;
        this.calories = calories;
        this.yield = yield;
        this.index = "not_specified";
    }

    public String getLabel() {
        return label;
    }

    public String getImage() {
        return image;
    }

    public String getUrl() {
        return url;
    }

    public ArrayList<String> getIngredientLines() {
        return ingredientLines;
    }

    public Integer getYield() {
        return yield;
    }

    public Integer getCalories(){
        return  calories;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

}
