package com.example.tamnguyen.calorizeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodListFragment;

import com.example.tamnguyen.calorizeapp.FoodList.OnItemClickListener;
import com.example.tamnguyen.calorizeapp.Profile.ProfileFragment;
import com.gigamole.navigationtabstrip.NavigationTabStrip;


import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;


import com.example.tamnguyen.calorizeapp.Progress.ProgressFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.llFabAddBreakfast)
    public LinearLayout fabAddBreakfast;
    @BindView(R.id.llFabAddLunch)
    public LinearLayout fabAddLunch;
    @BindView(R.id.llFabAddDinner)
    public LinearLayout fabAddDinner;
    @BindView(R.id.llFabAddExercise)
    public LinearLayout fabAddExercise;
    @BindView(R.id.fabFrame)
    public FrameLayout fabSubMenu;
    @BindView(R.id.tab)
    public NavigationTabStrip tabStrip;
    @BindView(R.id.pager)
    public ViewPager pager;
    private boolean fabExpanded = false;

    @BindView(R.id.fab)
    public FloatingActionButton fabAddItem;

    FoodListFragment foodListFragment;
    ProfileFragment profileFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        fabSubMenu.setVisibility(View.INVISIBLE);

        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fabExpanded == true) {
                    closeSubMenuFab();
                }
                else {
                    openSubMenuFab();
                }
            }
        });

        //Set up Tabstrip and Viewpager
        setupTab();
    }

    private void openSubMenuFab() {

        /*
        fabAddBreakfast.setVisibility(View.VISIBLE);
        fabAddLunch.setVisibility(View.VISIBLE);
        fabAddDinner.setVisibility(View.VISIBLE);
        fabAddExercise.setVisibility(View.VISIBLE);
        */
        fabSubMenu.setVisibility(View.VISIBLE);
        fabAddItem.setImageResource(R.drawable.ic_close);
        fabExpanded = true;
    }

    private void closeSubMenuFab() {

        /*
        fabAddBreakfast.setVisibility(View.INVISIBLE);
        fabAddLunch.setVisibility(View.INVISIBLE);
        fabAddDinner.setVisibility(View.INVISIBLE);
        fabAddExercise.setVisibility(View.INVISIBLE);
        */
        fabSubMenu.setVisibility(View.INVISIBLE);
        fabAddItem.setImageResource(R.drawable.ic_add);
        fabExpanded = false;
    }

    private void setupTab(){
        setupPager();
        tabStrip.setTitles("Diary","Food","Progress","Profile");
        tabStrip.setViewPager(pager);
    }
    private void setupPager(){
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position){
                    case 2:
                        return new ProgressFragment();
                    case 3:
                        return new ProfileFragment();
                    default:
                        return FoodListFragment.Companion.newInstance(new OnItemClickListener() {
                            @Override
                            public void onItemClick(@NotNull Food food) {
                                //TODO: Do whatever when the item in Food List Fragment is clicked
                            }
                        });
                }
            }

            @Override
            public int getCount() {
                return 4;
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
