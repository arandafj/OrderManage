package biz.clag.ordermanage.activity;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import biz.clag.ordermanage.R;
import biz.clag.ordermanage.model.Food;
import biz.clag.ordermanage.model.Menu;
import biz.clag.ordermanage.model.Table;

public class MenuListActivity extends AppCompatActivity {
    public static final String EXTRA_TABLE ="extra_table";
    public static final Integer RESULT_CODE=101;
    private ListView mListViewMenu;

    private Menu mMenu;
    private MenuListActivity mActivity = this;
    private Table mTable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);

        if (mMenu == null) {
            downlaodMenu();
        }
        mTable = (Table) getIntent().getSerializableExtra(EXTRA_TABLE);
        mListViewMenu = (ListView) findViewById(R.id.menu_list);

        mListViewMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(mActivity, FoodDeatailActivity.class);
                intent.putExtra(FoodDeatailActivity.CURRENT_MEAL, mMenu.getListFoods().get(position));
                intent.putExtra(FoodDeatailActivity.CURRENT_TABLE, mTable);
                mActivity.startActivityForResult(intent, RESULT_CODE);

            }
        });
    }

    private void downlaodMenu() {
        AsyncTask<Void, Integer, ArrayList<Food> > mealsDownloader = new AsyncTask<Void, Integer, ArrayList<Food> >() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected ArrayList<Food>  doInBackground(Void... voids) {
                InputStream input = null;
                URL url = null;

                try {
                    url = new URL("http://www.mocky.io/v2/58454341110000631e0e6c1c");
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.connect();
                    int responseLength = con.getContentLength();
                    byte data[] = new byte[1024];
                    long currentBytes = 0;
                    int downloadedBytes;
                    input = con.getInputStream();
                    StringBuilder sb = new StringBuilder();
                    while ((downloadedBytes = input.read(data)) != -1) {
                        sb.append(new String(data, 0, downloadedBytes));
                        if (responseLength > 0) {
                            currentBytes += downloadedBytes;
                            publishProgress((int) (currentBytes * 100) / responseLength);

                        }
                    }
                    JSONObject jsonRoot = new JSONObject(sb.toString());
                    JSONArray menu = jsonRoot.getJSONArray("menu");
                    ArrayList<Food> menuFoods = new ArrayList<Food>();
                    for (int i = 0; i<menu.length(); i++){
                        JSONObject mealJson = menu.getJSONObject(i);
                        String name = mealJson.getString("name");
                        float price = (float) mealJson.getDouble("price");
                        String image = mealJson.getString("image");
                        String alergics = mealJson.getString("alergics");
                        Food food =  new Food(name,price,image, alergics);
                        menuFoods.add(food);

                    }

                    return menuFoods;

                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    if (input != null) {
                        try {
                            input.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                return null;
            }


            @Override
            protected void onProgressUpdate(Integer... values) {
                super.onProgressUpdate(values);

            }


            @Override
            protected void onPostExecute(ArrayList<Food> meals) {
                super.onPostExecute(meals);
                if (meals != null) {
                    mMenu = new Menu();
                    mMenu.setListFoods(meals);
                    MenuListAdapter adapter = new MenuListAdapter(MenuListActivity.this, mMenu);
                    mListViewMenu.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(MenuListActivity.this);
                    alert.setTitle("Error");
                    alert.setMessage("No se pudo descargar la información");
                    alert.setPositiveButton("Reintentar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            downlaodMenu();
                        }
                    });
                    alert.setPositiveButton("Regresar", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            MenuListActivity.this.finish();
                        }
                    });
                    alert.show();
                }
            }

        };

        mealsDownloader.execute();

    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_OK, new Intent().putExtra(EXTRA_TABLE, mTable));
        super.onBackPressed();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==RESULT_CODE && resultCode == Activity.RESULT_OK){
            mTable=(Table)data.getSerializableExtra(FoodDeatailActivity.CURRENT_TABLE);
        }

    }
}

class MenuListAdapter extends ArrayAdapter<String> {
    private final String TAG = "MenuListAdapter";
    private final Activity context;
    private final Menu menu;

    public MenuListAdapter(Activity context, Menu menu) {
        super(context, R.layout.list_menu_single);
        this.context = context;
        this.menu = menu;

    }

    @Override
    public int getCount() {
        return this.menu.getListFoods().size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_menu_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt_menu_description);
        TextView txtPrice= (TextView) rowView.findViewById(R.id.txt_menu_price);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img_menu);
        txtTitle.setText(menu.getListFoods().get(position).getName());
        txtPrice.setText(String.format("%.2f",menu.getListFoods().get(position).getPrice()));
        int resID = context.getResources().getIdentifier(menu.getListFoods().get(position).getImg() , "drawable", context.getPackageName());
        imageView.setImageResource(resID);
        return rowView;
    }

}
