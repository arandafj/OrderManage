package biz.clag.ordermanage.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import biz.clag.ordermanage.R;
import biz.clag.ordermanage.fragment.TableListFragment;
import biz.clag.ordermanage.fragment.TablePagerFragment;
import biz.clag.ordermanage.model.Table;

public class RestaurantActivity extends AppCompatActivity implements TableListFragment.TableListListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);

        FragmentManager fm  = getFragmentManager();
        if (findViewById(R.id.fragment_table_list) != null) {
            if (fm.findFragmentById(R.id.fragment_table_list) == null) {
                Fragment tp = new TableListFragment();
                fm.beginTransaction().add(R.id.fragment_table_list, tp).commit();
            }
        }
        if (findViewById(R.id.fragment_table_pager)!=null){
            if (fm.findFragmentById(R.id.fragment_table_pager)==null){
                fm.beginTransaction().add(R.id.fragment_table_pager, TablePagerFragment.newInstance(0)).commit();
            }

        }
    }

    @Override
    public void onTableSelected(Table table, int position) {

        FragmentManager fm = getFragmentManager();
        TablePagerFragment tpF = (TablePagerFragment) fm.findFragmentById(R.id.fragment_table_pager);
        if (tpF!=null){
            tpF.showTable(position);
        }else {
            Intent intent = new Intent( this, TablePagerActivity.class);
            intent.putExtra(TablePagerActivity.EXTRA_TABLE_INDEX, position);
            startActivity(intent);
        }
    }

}


