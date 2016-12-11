package biz.clag.ordermanage.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import biz.clag.ordermanage.R;
import biz.clag.ordermanage.model.Food;
import biz.clag.ordermanage.model.OrderedFood;
import biz.clag.ordermanage.model.Table;

public class FoodDeatailActivity extends AppCompatActivity {

    public static final String CURRENT_MEAL = "current_meal";
    public static final String CURRENT_TABLE = "current_table";
    private TextView mName;
    private TextView mPrice;
    private TextView mTotalPrice;
    private Spinner mQuantity;
    private ImageView mImg;
    private EditText mComment;
    private Button mAdd;
    private Button mReturn;
    private int mQuantityValue;
    private float mTotalPriceValue;

    private Food mFood;
    private Table mTable;
    private FoodDeatailActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_deatail);
        mActivity=this;

        mFood = (Food) getIntent().getSerializableExtra(CURRENT_MEAL);
        mTable = (Table) getIntent().getSerializableExtra(CURRENT_TABLE);

        mName = (TextView) findViewById(R.id.meal_description);
        mPrice = (TextView) findViewById(R.id.meal_price);
        mTotalPrice = (TextView) findViewById(R.id.total_meal_price);
        mQuantity = (Spinner) findViewById(R.id.spinner);
        mImg = (ImageView) findViewById(R.id.image_detail);
        mComment = (EditText) findViewById(R.id.commentText);
        mComment.clearFocus();
        mAdd = (Button) findViewById(R.id.add_btn);
        mAdd.setEnabled(false);
        mReturn = (Button) findViewById(R.id.return_btn);
        setValues();

        mQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mAdd.setEnabled(true);
                mQuantityValue = Integer.parseInt(mQuantity.getItemAtPosition(position).toString());
                updateTotalPrice(mQuantityValue);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAdd.setEnabled(false);
            }
        });

        mReturn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mActivity.finish();
            }
        });

        mAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                OrderedFood orderedFood = new OrderedFood();
                orderedFood.setFood(mFood);
                orderedFood.setQuantity(mQuantityValue);
                orderedFood.setComments(mComment.getText().toString());
                orderedFood.setTotalPrice(mTotalPriceValue);
                if ( mTable.getMeal()==null){
                    List<OrderedFood> menu = new ArrayList<OrderedFood>();
                    mTable.setMeal(menu);
                    mTable.setTotalPrice(0);
                }
                mTable.getMeal().add(orderedFood);
                mTable.setTotalPrice(mTable.getTotalPrice()+mTotalPriceValue);
                mTable.setFree(false);
                setResult(RESULT_OK, new Intent().putExtra(CURRENT_TABLE, mTable));
                mActivity.finish();

            }
        });
    }

    private void updateTotalPrice(int value){
        mTotalPriceValue = value * mFood.getPrice();
        mTotalPrice.setText(String.format(getString(R.string.total_price_txt), mTotalPriceValue));
    }

    private void setValues(){
        mName.setText(mFood.getName());
        mPrice.setText(String.format("Price: %.02f", mFood.getPrice()));
        int resID = getResources().getIdentifier(mFood.getImg() , "drawable", getPackageName());
        mImg.setImageResource(resID);

    }

}
