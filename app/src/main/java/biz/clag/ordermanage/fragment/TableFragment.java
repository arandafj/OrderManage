package biz.clag.ordermanage.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import biz.clag.ordermanage.R;
import biz.clag.ordermanage.activity.MenuListActivity;
import biz.clag.ordermanage.model.Food;
import biz.clag.ordermanage.model.Table;
import biz.clag.ordermanage.model.Tables;
import biz.clag.ordermanage.model.OrderedFood;

/**
 * A simple {@link Fragment} subclass.
 */

public class TableFragment extends Fragment{

    public static final String ARG_TABLE = "table";
    public static final Integer RESULT_REQUEST = 1;
    private TextView mTableDescription;
    private TextView mTableTotalPrice;
    private Button mClearButton;
    private Button mAddMealButton;
    private Table mTable;
    private Tables mTables;
    private int mTable_position;
    private RecyclerView mRecycler;


    public static TableFragment newInstance(Table table){
        TableFragment fragment = new TableFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ARG_TABLE, table);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            mTable = (Table) getArguments().getSerializable(ARG_TABLE);
            String infoJson = PreferenceManager.getDefaultSharedPreferences(getActivity())
                                                .getString(String.format("TABLE%s", mTable.getNumber()), "");
            if (infoJson!=""){
                JSONObject json = null;
                try {
                    json = new JSONObject(infoJson);
                    mTable.setTotalPrice(Float.parseFloat(json.getString("priceTotal")));
                    JSONArray meals = json.getJSONArray("meals");
                    OrderedFood om = new OrderedFood();
                    List<OrderedFood> lo = new ArrayList<>();
                    for (int i = 0; i<meals.length();i++){
                        JSONObject mealJson = meals.getJSONObject(i);
                        om.setTotalPrice(Float.parseFloat(mealJson.getString("priceTotalMeal")));
                        om.setComments(mealJson.getString("comments"));
                        om.setQuantity(Integer.parseInt(mealJson.getString("quantity")));;
                        Food food = new Food();
                        food.setName(mealJson.getString("name"));
                        food.setPrice(Float.parseFloat(mealJson.getString("price")));
                        om.setFood(food);
                        lo.add(om);
                    }
                    mTable.setMeal(lo);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root =  inflater.inflate(R.layout.fragment_table, container, false);
        mTableDescription = (TextView) root.findViewById(R.id.table_description);
        mTableTotalPrice = (TextView) root.findViewById(R.id.table_price);
        mClearButton = (Button) root.findViewById(R.id.clear_btn);
        mAddMealButton = (Button) root.findViewById(R.id.add_meal_btn);
        mRecycler = (RecyclerView) root.findViewById(R.id.recycler_view);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        if (mTable.getMeal()==null) {
            mRecycler.setAdapter(new FoodRecyclerAdapter(new ArrayList<OrderedFood>(), getActivity()));
        } else{
            mRecycler.setAdapter(new FoodRecyclerAdapter(mTable.getMeal(), getActivity()));
        }

        mClearButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                mTable.clear();
                updateTableInfo();
                mRecycler.setAdapter(new FoodRecyclerAdapter(new ArrayList<OrderedFood>(), getActivity()));
                PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                        .remove(String.format("TABLE%s", mTable.getNumber())).commit();
            }
        });

        mAddMealButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MenuListActivity.class);
                intent.putExtra( MenuListActivity.EXTRA_TABLE  , mTable);
                startActivityForResult(intent, RESULT_REQUEST);
            }
        });


        updateTableInfo();

        return root;

    }

    public void updateTableInfo(){
        mTableDescription.setText(String.format("Table %d", mTable.getNumber()));
        mTableTotalPrice.setText(String.format("Total price %.2f", mTable.getTotalPrice()));

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK){

            mTable=(Table)data.getSerializableExtra(MenuListActivity.EXTRA_TABLE);
            updateTableInfo();
            if (mTable.getMeal()!=null){
                mRecycler.setAdapter(new FoodRecyclerAdapter(mTable.getMeal(), getActivity()));
            }

            String json=String.format("{ \"priceTotal\":\"%.2f\",", mTable.getTotalPrice());
            json+=String.format(" \"meals\": ");
            for (int i=0; i<mTable.getMeal().size(); i++){
                if (i==0){
                    json+=String.format("[{ \"priceTotalMeal\":\"%.2f\",", mTable.getMeal().get(i).getTotalPrice());
                }else{
                    json+=String.format("{ \"priceTotalMeal\":\"%.2f\",", mTable.getMeal().get(i).getTotalPrice());
                }
                json+=String.format(" \"comments\":\"%s\",", mTable.getMeal().get(i).getComments());
                json+=String.format(" \"quantity\":\"%d\",", mTable.getMeal().get(i).getQuantity());
                json+=String.format(" \"price\":\"%.2f\",", mTable.getMeal().get(i).getFood().getPrice());
                if (i<mTable.getMeal().size()-1){
                    json+=String.format(" \"name\":\"%s\"},", mTable.getMeal().get(i).getFood().getName());
                }else{
                    json+=String.format(" \"name\":\"%s\"}", mTable.getMeal().get(i).getFood().getName());
                }

            }
            json+="]}";
            PreferenceManager.getDefaultSharedPreferences(getActivity()).edit()
                    .putString(String.format("TABLE%s", mTable.getNumber()), json).commit();

        }

    }

}

class FoodRecyclerAdapter extends RecyclerView.Adapter<FoodRecyclerAdapter.MealsViewHolder>{

    private List<OrderedFood> mOrderedFoods;
    private Context mContext;

    public FoodRecyclerAdapter(List<OrderedFood> orderedFoods, Context context){
        super();
        mOrderedFoods = orderedFoods;
        mContext = context;
    }

    @Override
    public MealsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_order_meal, parent, false);
        return new MealsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MealsViewHolder holder, int position) {
        holder.bingMeals(mOrderedFoods.get(position), mContext);

    }

    @Override
    public int getItemCount() {
        return mOrderedFoods.size();
    }

    public class  MealsViewHolder extends RecyclerView.ViewHolder{

        private TextView mMealName;
        private TextView mPrice;
        private TextView mPriceT;
        private TextView mQuantity;
        private TextView mComments;

        public MealsViewHolder(View itemView) {
            super(itemView);
            mMealName = (TextView) itemView.findViewById(R.id.name_unit);
            mPrice = (TextView) itemView.findViewById(R.id.price_unit);
            mPriceT = (TextView) itemView.findViewById(R.id.price_total);
            mQuantity = (TextView) itemView.findViewById(R.id.quantity_unit);
            mComments = (TextView) itemView.findViewById(R.id.comments_unity);

        }
        public void bingMeals(OrderedFood meal, Context context){
            mMealName.setText(meal.getFood().getName());
            mQuantity.setText(String.format(context.getString(R.string.quantity_text), meal.getQuantity()));
            mComments.setText(String.format("Comments: %s", meal.getComments()));
            mPrice.setText(String.format("Price unit: %.2f", meal.getFood().getPrice()));
            mPriceT.setText(String.format("Price total: %.2f", meal.getTotalPrice()));

        }
    }
}

