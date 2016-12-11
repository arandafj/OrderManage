package biz.clag.ordermanage.model;

import java.io.Serializable;

public class OrderedFood implements Serializable {
    private Food mFood;
    private float mTotalPrice;
    private Integer mQuantity;
    private String mComments;

    public OrderedFood(Food food, float priceTotal, Integer quantity, String comments) {
        mFood = food;
        mTotalPrice = priceTotal;
        mQuantity = quantity;
        mComments = comments;
    }

    public OrderedFood() {}

    public String getComments() {
        return mComments;
    }

    public void setComments(String comments) {
        mComments = comments;
    }

    public Food getFood() {
        return mFood;
    }

    public void setFood(Food food) {
        mFood = food;
    }

    public float getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        mTotalPrice = totalPrice;
    }

    public Integer getQuantity() {
        return mQuantity;
    }

    public void setQuantity(Integer quantity) {
        mQuantity = quantity;
    }

}
