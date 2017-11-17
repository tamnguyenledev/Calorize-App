package com.example.tamnguyen.calorizeapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.llFabAddBreakfast)
    LinearLayout fabAddBreakfast;
    @BindView(R.id.llFabAddLunch)
    LinearLayout fabAddLunch;
    @BindView(R.id.llFabAddDinner)
    LinearLayout fabAddDinner;
    @BindView(R.id.llFabAddExercise)
    LinearLayout fabAddExercise;
    @BindView(R.id.fabFrame)
    FrameLayout fabSubMenu;

    private boolean fabExpanded = false;

    @BindView(R.id.fab)
    FloatingActionButton fabAddItem;

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
