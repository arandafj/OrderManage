package biz.clag.ordermanage.model;


import java.io.Serializable;
import java.util.List;

public class Table implements Serializable {
    private Integer mNumber;
    private float mTotalPrice = 0;
    private boolean mIsFree=true;
    List<OrderedFood> mMeal;

    public Table(Integer number) {
        mNumber = number;
    }

    public float getTotalPrice() {
        return mTotalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        mTotalPrice = totalPrice;
    }

    public void setFree(boolean free) {
        mIsFree = free;
    }

    public Integer getNumber() {
        return mNumber;
    }


    public List<OrderedFood> getMeal() {
        return mMeal;
    }

    public void setMeal(List<OrderedFood> meal) {
        mMeal = meal;
    }

    @Override
    public String toString() {
        return String.format("Table %d", getNumber());
    }

    public void clear(){
        mTotalPrice=0;
        mIsFree=true;
        mMeal=null;
    }
}
