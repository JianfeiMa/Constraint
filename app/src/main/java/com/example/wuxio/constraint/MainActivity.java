package com.example.wuxio.constraint;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MenuItem;

import com.example.viewskin.ContainerLayout;

/**
 * @author wuxio
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    protected ContainerLayout                    mContainer;
    protected NavigationView                     mNavigationView;
    protected DrawerLayout                       mDrawer;
    private   MainNavigationItemSelectedListener mItemSelectedListener;


    public static void start(Context context) {

        Intent starter = new Intent(context, MainActivity.class);
        context.startActivity(starter);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);
        initView();

        mDrawer.post(new Runnable() {
            @Override
            public void run() {

                mNavigationView.setCheckedItem(R.id.menu00);
                mItemSelectedListener.initFirstPage();
            }
        });
    }


    private void initView() {

        mContainer = findViewById(R.id.container);
        mNavigationView = findViewById(R.id.navigationView);
        mItemSelectedListener = new MainNavigationItemSelectedListener();
        mNavigationView.setNavigationItemSelectedListener(mItemSelectedListener);
        mDrawer = findViewById(R.id.drawer);
    }


    private void closeDrawer() {

        mDrawer.closeDrawer(Gravity.START);
    }

    //============================ 内部类 ============================

    private class MainNavigationItemSelectedListener implements NavigationView
            .OnNavigationItemSelectedListener {

        Fragment[] mFragments = {
                YunFragment.newInstance(),
                TaoFragment.newInstance(),
                RecyclerFragment.newInstance(),
                TestFragment.newInstance(),
        };


        private void changeFragment(Fragment fragment) {

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
        }


        public void initFirstPage() {

            changeFragment(mFragments[0]);
        }


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {

                case R.id.menu00:
                    changeFragment(mFragments[0]);
                    break;

                case R.id.menu01:
                    changeFragment(mFragments[1]);
                    break;

                case R.id.menu02:
                    changeFragment(mFragments[2]);
                    break;

                case R.id.menu03:
                    changeFragment(mFragments[3]);
                    break;

                default:
                    break;
            }

            closeDrawer();
            return true;
        }
    }
}
