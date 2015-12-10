package com.score.senz.chatz;

import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.score.senz.chatz.exceptions.NoUserException;
import com.score.senz.chatz.ui.DrawerAdapter;
import com.score.senz.chatz.utils.PreferenceUtils;
import com.score.senzc.pojos.DrawerItem;
import com.score.senzc.pojos.User;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    private static final String TAG = MainActivity.class.getName();

    // Ui components
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private RelativeLayout drawerContainer;
    private HomeActionBarDrawerToggle homeActionBarDrawerToggle;
    private ArrayList<DrawerItem> drawerItemList;
    // type face
    private Typeface typeface;
    private DrawerAdapter drawerAdapter;

    // user components
    private CircularImageView userImage;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initDrawer();
        initDrawerUser();
        initDrawerList();
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

    private void initDrawer() {
        // enable ActionBar app icon to behave as action to toggle nav drawer
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerContainer = (RelativeLayout) findViewById(R.id.drawer_container);

        // set a custom shadow that overlays the senz_map_layout content when the drawer opens
        // set up drawer listener
        //drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        homeActionBarDrawerToggle = new HomeActionBarDrawerToggle(this, drawerLayout);
        drawerLayout.setDrawerListener(homeActionBarDrawerToggle);
    }

    private void initDrawerUser() {
        userImage = (CircularImageView) findViewById(R.id.contact_image);
        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.default_user_icon);
        userImage.setImageBitmap(largeIcon);

//        typeface = Typeface.createFromAsset(getAssets(), "fonts/vegur_2.otf");
        //typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");

        try {
            User user = PreferenceUtils.getUser(this);
            username = (TextView) findViewById(R.id.home_user_text);
            username.setText("@" + user.getUsername());
            username.setTextColor(Color.parseColor("#eada00"));
            //username.setTextColor(Color.parseColor("#4a4a4a"));
            username.setTypeface(typeface, Typeface.BOLD);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        homeActionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        homeActionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void initDrawerList() {
        // initialize drawer content
        // need to determine selected item according to the currently selected sensor type
        drawerItemList = new ArrayList();
        drawerItemList.add(new DrawerItem("#Senz", R.drawable.my_sensz_normal, R.drawable.my_sensz_selected, true));
        drawerItemList.add(new DrawerItem("#Friend", R.drawable.friends_normal, R.drawable.friends_selected, false));
        drawerItemList.add(new DrawerItem("#Share", R.drawable.friends_normal, R.drawable.friends_selected, false));

        drawerAdapter = new DrawerAdapter(MainActivity.this, drawerItemList);
        drawerListView = (ListView) findViewById(R.id.drawer);

        if (drawerListView != null)
            drawerListView.setAdapter(drawerAdapter);

        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class HomeActionBarDrawerToggle extends ActionBarDrawerToggle {

        public HomeActionBarDrawerToggle(Activity mActivity, DrawerLayout mDrawerLayout) {
            super(mActivity, mDrawerLayout, R.drawable.my_sensz_normal, R.string.ns_menu_open, R.string.ns_menu_close);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDrawerClosed(View view) {
            invalidateOptionsMenu();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onDrawerOpened(View drawerView) {
            invalidateOptionsMenu();
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Highlight the selected item, update the title, and close the drawer
            // update selected item and title, then close the drawer
            drawerLayout.closeDrawer(drawerContainer);

            //  reset content in drawer list
            for (DrawerItem drawerItem : drawerItemList) {
                drawerItem.setSelected(false);
            }

            if (position == 0) {
                drawerItemList.get(0).setSelected(true);
                //loadSensors();
            } else if (position == 1) {
                drawerItemList.get(1).setSelected(true);
                //loadFriends();
            } else if (position == 2) {
                drawerItemList.get(2).setSelected(true);
                //loadShare();
            }

            drawerAdapter.notifyDataSetChanged();
        }
    }


}
