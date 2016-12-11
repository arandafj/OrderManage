package biz.clag.ordermanage.model;

import java.io.Serializable;

public class Food implements Serializable{
    private String name;
    private float price;
    private String img;
    private String alergics;

    public Food(String name, float price, String img, String alergics) {
        this.name = name;
        this.price = price;
        this.img = img;
        this.alergics = alergics;
    }

    public Food() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getImg() {
        return img;
    }

}
