package com.shine.apps.ui;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.shine.apps.R;

/**
 * <strong>主界面</strong>
 * <p>
 * 这里写类的详细描述
 *
 * @author wangyang
 * @version 1.0
 * @email wyhdgx@163.com
 * @modified 4/22/21
 * @since 4/22/21
 */
public class MainActivity extends AppCompatActivity {
    private long firstClickTime = 0, secondClickTime = 0;
    private boolean isFirstClick = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        initView();
    }

    private void initView() {
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_recommend, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onBackPressed() {
        doubleBack();
    }

    /**
     * 双击back退出功能
     */
    private void doubleBack() {
        if (isFirstClick) {
            firstClickTime = System.currentTimeMillis();
            toast("再按一次退出程序");
            isFirstClick = false;
        } else {
            secondClickTime = System.currentTimeMillis();
            if (secondClickTime - firstClickTime < 1500) {
                super.onBackPressed();
            } else {
                firstClickTime = secondClickTime;
                toast("再按一次退出程序");
            }
        }
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}