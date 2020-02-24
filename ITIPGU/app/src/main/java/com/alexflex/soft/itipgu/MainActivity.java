package com.alexflex.soft.itipgu;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import com.alexflex.soft.itipgu.fragments.MessengerFragment;
import com.alexflex.soft.itipgu.fragments.NewsFeedFragment;
import com.alexflex.soft.itipgu.logic.CommonMethods;
import com.alexflex.soft.itipgu.logic.mPagerAdapter;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //наше меню навигационного вьюва
    private NavigationView nav_view;
    //тулбар
    private Toolbar toolbar;
    private ConstraintLayout main_layout;
    private DrawerLayout drawer;
    private ViewPager pager;
    private mPagerAdapter adapter;
    private boolean isNightNow;
    private int quarter;
    private static long back_pressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        nav_view = findViewById(R.id.nav_view);
        main_layout = findViewById(R.id.main_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        nav_view.setNavigationItemSelectedListener(this);
        isNightNow = CommonMethods.isNightHere();
        quarter = CommonMethods.getQuarter();
        pager = findViewById(R.id.pager);
        main_layout.removeAllViews();
        //если на часах больше 17:00 или меньше 8:00 ...
        if(isNightNow) {
            //...переключаемся на ночную тему
            changeToNight();
        } else {
            //иначе меянем цветовую тему в зависимости от сезона
            changeForSeason(quarter);
        }
        main_layout.addView(pager);
        adapter = new mPagerAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        pager.setAdapter(adapter);
        pager.setCurrentItem(0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //выход только при двойном нажатии
            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
                Toast.makeText(getBaseContext(), R.string.are_u_sure, Toast.LENGTH_SHORT).show();

            back_pressed = System.currentTimeMillis();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_activity_actionbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.open_in_browser){
            String url;
            if (adapter.currentItem == 1) {
                url = CommonMethods.urlITI;
            } else {
                url = CommonMethods.urlITK;
            }
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.news:
                main_layout.removeAllViews();
                main_layout.addView(pager);
                pager.setCurrentItem(0);
                break;
            case R.id.messaging:
                main_layout.removeAllViews();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.main_layout, new MessengerFragment());
                fragmentTransaction.commit();
                break;
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.about_app_item:
                startActivity(new Intent(this, AboutAppActivity.class));
        }

        drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //переключение на ночную тему
    public void changeToNight(){
        toolbar.setBackgroundColor(getResources().getColor(R.color.night_lighter));
        nav_view.setBackgroundColor(getResources().getColor(R.color.night1));
        nav_view.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
        nav_view.getHeaderView(0).setBackgroundResource(R.drawable.night_theme);
        main_layout.setBackgroundColor(Color.BLACK);
        this.setTheme(R.style.night);
    }

    //меняем тему в зависимости от сезона
    public void changeForSeason(int quarter){
        switch (quarter){
            //зима
            case 1:
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                nav_view.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                nav_view.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
                nav_view.getHeaderView(0).setBackgroundResource(R.drawable.winter);
                break;
            //весна
            case 2:
                toolbar.setBackgroundColor(getResources().getColor(R.color.colorSpring2));
                nav_view.setBackgroundColor(getResources().getColor(R.color.colorSpring1));
                nav_view.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                nav_view.getHeaderView(0).setBackgroundResource(R.drawable.spring);
                break;
            //лето
            case 3:
                toolbar.setBackgroundColor(getResources().getColor(R.color.Summer2));
                nav_view.setBackgroundColor(getResources().getColor(R.color.Summer1));
                nav_view.setItemTextColor(ColorStateList.valueOf(Color.WHITE));
                nav_view.getHeaderView(0).setBackgroundResource(R.drawable.summer);
                break;
            //осень
            case 4:
                toolbar.setBackgroundColor(getResources().getColor(R.color.Autumn2));
                nav_view.setBackgroundColor(getResources().getColor(R.color.Autumn1));
                nav_view.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
                nav_view.getHeaderView(0).setBackgroundResource(R.drawable.autumn);
                break;
        }
    }
}