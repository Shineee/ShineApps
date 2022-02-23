package com.shine.apps.ui

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.shine.apps.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.shine.apps.ui.ui.theme.ShineAppsTheme
import com.shine.apps.utils.L

/**
 * 主界面
 *
 * @author wangyang
 * @version 1.0
 * @email wyhdgx@163.com
 * @modified 4/22/21
 * @since 4/22/21
 */
class MainActivity : AppCompatActivity() {
    private val TAG: String = "MainActivity"
    private var firstClickTime: Long = 0
    private var secondClickTime: Long = 0
    private var isFirstClick = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_main)
        initView()
        L.d(TAG, "onCreate");
    }

    override fun onStart() {
        super.onStart()
        L.d(TAG, "onStart");
    }

    override fun onRestart() {
        super.onRestart()
        L.d(TAG, "onRestart");
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        L.d(TAG, "onSaveInstanceState(Bundle,PersistableBundle)");
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        L.d(TAG, "onConfigurationChanged(Configuration)");
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        L.d(TAG, "onRestoreInstanceState(Bundle)");
    }

    override fun onRestoreInstanceState(
        savedInstanceState: Bundle?,
        persistentState: PersistableBundle?
    ) {
        super.onRestoreInstanceState(savedInstanceState, persistentState)
        L.d(TAG, "onRestoreInstanceState(Bundle,PersistableBundle)");
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        L.d(TAG, "onSaveInstanceState(Bundle)");
    }

    override fun onResume() {
        super.onResume()
        L.d(TAG, "onResume");
    }

    override fun onPause() {
        super.onPause()
        L.d(TAG, "onPause");
    }

    override fun onStop() {
        super.onStop()
        L.d(TAG, "onStop");
    }

    override fun onDestroy() {
        super.onDestroy()
        L.d(TAG, "onDestroy");
    }

    private fun initView() {
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_recommend, R.id.navigation_profile
        )
            .build()
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        //NavigationUI.setupWithNavController(navView, navController)
    }

    override fun onBackPressed() {
        doubleBack()
    }

    /**
     * 双击back退出功能
     */
    private fun doubleBack() {
        if (isFirstClick) {
            firstClickTime = System.currentTimeMillis()
            toast("再按一次退出程序")
            isFirstClick = false
        } else {
            secondClickTime = System.currentTimeMillis()
            if (secondClickTime - firstClickTime < 1500) {
                super.onBackPressed()
            } else {
                firstClickTime = secondClickTime
                toast("再按一次退出程序")
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}