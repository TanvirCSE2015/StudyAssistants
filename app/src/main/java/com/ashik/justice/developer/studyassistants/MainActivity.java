package com.ashik.justice.developer.studyassistants;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;

import com.nex3z.notificationbadge.NotificationBadge;

public class MainActivity extends AppCompatActivity  {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ImageView imageHome,imageR,imageN,imageF;
    private NotificationBadge badge,badgeF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar=(Toolbar)findViewById(R.id.tool_bar);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        toggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        viewPager=(ViewPager)findViewById(R.id.view_pager);
        setUpViewPager(viewPager);

        tabLayout=(TabLayout)findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpIcons();


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        imageHome.setImageResource(R.drawable.home_red);
                        imageR.setImageResource(R.drawable.routine_black);
                        imageF.setImageResource(R.drawable.friend_black);
                        imageN.setImageResource(R.drawable.notification_black);
                        badgeF.setNumber(SharedPrefManager.getIntance(getApplicationContext()).getRequestBadgeCouont());

                        //  badge.setNumber(SharedPrefManager.getInstance(getApplicationContext()).getBadgeCouont());
                        break;
                    case 1:
                        imageHome.setImageResource(R.drawable.home_black);
                        imageR.setImageResource(R.drawable.routine_red);
                        imageF.setImageResource(R.drawable.friend_black);
                        imageN.setImageResource(R.drawable.notification_black);
                      badgeF.setNumber(SharedPrefManager.getIntance(getApplicationContext()).getRequestBadgeCouont());
                        break;
                    case 2:
                        imageF.setImageResource(R.drawable.home_black);
                        imageR.setImageResource(R.drawable.routine_black);
                        imageF.setImageResource(R.drawable.friend_red);
                        imageN.setImageResource(R.drawable.notification_black);
                        badgeF.setNumber(0);
                        SharedPrefManager.getIntance(getApplicationContext()).clearRequestBadge();

                        break;
                    case 3:
                        imageHome.setImageResource(R.drawable.home_black);
                        imageR.setImageResource(R.drawable.routine_black);
                        imageF.setImageResource(R.drawable.friend_black);
                        imageN.setImageResource(R.drawable.notification_red);
                        badgeF.setNumber(SharedPrefManager.getIntance(getApplicationContext()).getRequestBadgeCouont());

                        //  badge.setNumber(0);
                        // SharedPrefManager.getInstance(getApplicationContext()).deleteValue();
                        break;



                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });




    }
    private void setUpIcons() {
        View vHome=getLayoutInflater().inflate(R.layout.custom_tabs,null);
        imageHome=(ImageView)vHome.findViewById(R.id.image_view);
        imageHome.setImageResource(R.drawable.home_red);
        tabLayout.getTabAt(0).setCustomView(vHome);

        View vRoutine=getLayoutInflater().inflate(R.layout.custom_tabs,null);
        imageR=(ImageView)vRoutine.findViewById(R.id.image_view);
        imageR.setImageResource(R.drawable.routine_black);
        tabLayout.getTabAt(1).setCustomView(vRoutine);


        View vFriend=getLayoutInflater().inflate(R.layout.custom_notification,null);
        imageF=(ImageView)vFriend.findViewById(R.id.icon_badge);
        imageF.setImageResource(R.drawable.friend_black);
        badgeF=(NotificationBadge)vFriend.findViewById(R.id.badge);
        badgeF.setNumber(SharedPrefManager.getIntance(getApplicationContext()).getRequestBadgeCouont());
        tabLayout.getTabAt(2).setCustomView(vFriend);


        View vNotification=getLayoutInflater().inflate(R.layout.custom_notification,null);
        imageN=(ImageView)vNotification.findViewById(R.id.icon_badge);
        badge=(NotificationBadge)vNotification.findViewById(R.id.badge);
        imageN.setImageResource(R.drawable.notification_black);
        // badge.setNumber(SharedPrefManager.getInstance(getApplicationContext()).getBadgeCouont());
       // badge.setNumber(2);
        tabLayout.getTabAt(3).setCustomView(vNotification);

    }


    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Home());
        adapter.addFragment(new ClassRoutine());
        adapter.addFragment(new FriendRequest());
        adapter.addFragment(new Notification());
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateBadge(int count, String type) {
        if (type.equals("Request")){
            tabLayout=(TabLayout) findViewById(R.id.tabs);
            if (tabLayout.getSelectedTabPosition()!=2){
                badgeF=tabLayout.getTabAt(2).getCustomView().findViewById(R.id.badge);
                badgeF.setNumber(count);
            }else{
                SharedPrefManager.getIntance(getApplicationContext()).clearRequestBadge();
            }
        }
    }
private class  MyBroadcastReceoiver extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {
       Bundle extras=intent.getExtras();
       int count=extras.getInt("count");
       String type=extras.getString("type");
       updateBadge(count,type);

    }


}

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter();
        filter.addAction("com.ashik.justice.developer.studyassistants");
        MyBroadcastReceoiver receoiver=new MyBroadcastReceoiver();
        registerReceiver(receoiver,filter);
    }
}
