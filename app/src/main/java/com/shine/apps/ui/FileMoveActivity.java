package com.shine.apps.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.shine.apps.R;
import com.shine.apps.adapter.FolderListAdapter;
import com.shine.apps.consts.FileConst;
import com.shine.apps.utils.ExternalFileUtils;

import java.io.File;
import java.util.List;

/**
 * 移动文件界面
 */
public class FileMoveActivity extends AppCompatActivity {
    private static final String TAG = "MoveFileActivity";
    //文件列表控件
    private RecyclerView rvFileList;
    private FolderListAdapter mFolderListAdapter;
    private List<File> mFileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_file_move);
        initPermission();
        initView();
        initData();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        rvFileList = findViewById(R.id.rvFileList);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rvFileList.setLayoutManager(llm);
        registerForContextMenu(rvFileList);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        ExternalFileUtils externalFileUtils = new ExternalFileUtils();
        mFileList = externalFileUtils.getExternalFolderPathList();
        //绑定文件列表数据
        mFolderListAdapter = new FolderListAdapter(this, getSupportActionBar());
        rvFileList.setAdapter(mFolderListAdapter);
        mFolderListAdapter.setFileList(mFileList);
    }

    private void initPermission() {
        XXPermissions.with(this)
                // 申请安装包权限
                //.permission(Permission.REQUEST_INSTALL_PACKAGES)
                .permission(Permission.READ_EXTERNAL_STORAGE)
                .permission(Permission.WRITE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all) {
                            // toast("获取录音和日历权限成功");

                        } else {
                            // toast("获取部分权限成功，但部分权限未正常授予");
                        }
                        initView();
                        initData();
                    }

                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            //toast("被永久拒绝授权，请手动授予录音和日历权限");
                            // 如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.startPermissionActivity(FileMoveActivity.this, permissions);
                        } else {
                            //toast("获取录音和日历权限失败");
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        if (!mFolderListAdapter.onBack()) {//!false=true  !true=false
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == XXPermissions.REQUEST_CODE) {
            if (XXPermissions.isGranted(this, Permission.WRITE_EXTERNAL_STORAGE) &&
                    XXPermissions.isGranted(this, Permission.READ_EXTERNAL_STORAGE)) {
                //toast("用户已经在权限设置页授予了录音和日历权限");
                initView();
                initData();
            } else {
                //toast("用户没有在权限设置页授予权限");
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.move_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home://back
                onBackPressed();
                return true;
            case R.id.menu_done:
                Intent intent = getIntent();
                String moveFilePath = intent.getStringExtra(FileConst.MOVE_FILE_PATH);
                File src = new File(moveFilePath);
                mFolderListAdapter.moveFile(src);
                finish();
                return true;
            case R.id.menu_new:
                showCreateDirDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示新建文件夹对话框
     */
    private void showCreateDirDialog() {
        View view = View.inflate(this, R.layout.dialog_create_dir, null);
        EditText etFolderName = view.findViewById(R.id.etFolderName);
        showDialog("新建文件夹", view, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mFolderListAdapter.createDir(etFolderName.getText().toString());
            }
        });
    }

    private void showDialog(String title, View view, DialogInterface.OnClickListener positiveButtonClickListener) {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title)
                .setIcon(R.mipmap.ic_launcher)
                .setView(view)
                .setPositiveButton("确定", positiveButtonClickListener)
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create();
        alertDialog.show();
    }

    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}