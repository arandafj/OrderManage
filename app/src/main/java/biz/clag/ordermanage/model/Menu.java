package biz.clag.ordermanage.model;


import java.io.Serializable;
import java.util.ArrayList;


public class Menu implements Serializable {
    private ArrayList<Food> mListFoods;

    public Menu() {
    }

    public Menu(ArrayList<Food> listFoods) {
        this.mListFoods = listFoods;
    }

    public ArrayList<Food> getListFoods() {
        return mListFoods;
    }

    public void setListFoods(ArrayList<Food> listFoods) {
        this.mListFoods = listFoods;
    }

}
