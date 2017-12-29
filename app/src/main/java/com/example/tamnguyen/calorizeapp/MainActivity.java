package com.example.tamnguyen.calorizeapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.tamnguyen.calorizeapp.Diary.DiaryFoodList;
import com.example.tamnguyen.calorizeapp.Diary.DiaryFragment;
import com.example.tamnguyen.calorizeapp.Diary.OnItemListener;
import com.example.tamnguyen.calorizeapp.FoodList.Food;
import com.example.tamnguyen.calorizeapp.FoodList.FoodListFragment;
import com.example.tamnguyen.calorizeapp.FoodList.OnItemClickListener;
import com.example.tamnguyen.calorizeapp.Profile.ProfileFragment;
import com.example.tamnguyen.calorizeapp.Progress.ProgressFragment;
import com.gigamole.navigationtabstrip.NavigationTabStrip;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.fabFrame)
    public FrameLayout fabSubMenu;
    @BindView(R.id.tab)
    public NavigationTabStrip tabStrip;
    @BindView(R.id.pager)
    public ViewPager pager;
    @BindView(R.id.fabAddBreakfast)
    FloatingActionButton fabAddBreakfast;
    @BindView(R.id.fabAddLunch)
    FloatingActionButton fabAddLunch;
    @BindView(R.id.fabAddDinner)
    FloatingActionButton fabAddDinner;
    @BindView(R.id.fabAddExercise)
    FloatingActionButton fabAddExercise;
    @BindView(R.id.fabShareFB)
    FloatingActionButton fabShareFB;
    @BindView(R.id.fabCaptureImage)
    FloatingActionButton fabCaptureImage;
    @BindView(R.id.fabShowProgress)
    FloatingActionButton fabShowProgress;
    @BindView(R.id.fabCompare)
    FloatingActionButton fabCompare;
    @BindView(R.id.fabShareFBProgress)
    FloatingActionButton fabShareFBProgress;
    @BindView(R.id.fabFrameProgress)
    FrameLayout fabProgressSubMenu;
    private boolean fabExpanded = false;

    @BindView(R.id.fab)
    public FloatingActionButton fabAddItem;

    FoodListFragment foodListFragment;
    ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // prevent auto open keyboard
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        fabSubMenu.setVisibility(View.INVISIBLE);
        fabProgressSubMenu.setVisibility(View.INVISIBLE);

        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fabExpanded) {
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

        if (pager.getCurrentItem() != 2)
            fabSubMenu.setVisibility(View.VISIBLE);
        else
            fabProgressSubMenu.setVisibility(View.VISIBLE);

        fabAddItem.setImageResource(R.drawable.ic_close);
        fabExpanded = true;
    }

    private void closeSubMenuFab() {

        fabSubMenu.setVisibility(View.INVISIBLE);
        fabProgressSubMenu.setVisibility(View.INVISIBLE);
        fabAddItem.setImageResource(R.drawable.ic_menu);
        fabExpanded = false;
    }

    private void setupTab() {
        setupPager();
        tabStrip.setTitles("Diary", "Food", "Progress", "Profile");
        tabStrip.setViewPager(pager);
    }

    private void setupPager() {
        pager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 2:
                        return new ProgressFragment();
                    case 3:
                        return new ProfileFragment();
                    case 1:
                        return FoodListFragment.Companion.newInstance(new OnItemClickListener() {
                            @Override
                            public void onItemClick(@NotNull Food food) {
                                Intent intent = new Intent(MainActivity.this, AddFoodActivity.class);
                                intent.putExtra("food", food);
                                startActivity(intent);
                            }
                        });
                    case 0:
                        return DiaryFragment.newInstance(new OnItemListener() {
                            @Override
                            public void onClick(DiaryFoodList diaryFoodList, int position) {

                            }

                            @Override
                            public void onLongClick(DiaryFoodList diaryFoodList, int position) {

                            }

                            @Override
                            public void onAddClick(DiaryFoodList diaryFoodList) {
                                //TODO: Process when users want to add more items into their's diary
                                int mealChoice = -1;
                                if (diaryFoodList.getTitle().equals("Breakfast")) {
                                    mealChoice = 0;
                                }
                                else if (diaryFoodList.getTitle().equals("Lunch")) {
                                    mealChoice = 1;
                                }
                                else {
                                    mealChoice = 2;
                                }
                                Intent intent = new Intent(MainActivity.this, FoodListActivity.class);
                                intent.putExtra("choice", mealChoice);
                                startActivity(intent);
                            }
                        });
                    default:
                        return new Fragment();
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
