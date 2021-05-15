package com.shine.apps.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;

import com.shine.apps.R;
import com.shine.apps.utils.L;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <strong>文件夹列表适配器</strong>
 * <p>
 * 这里写类的详细描述
 *
 * @author wangyang
 * @version 1.0
 * @email wangyang@meiweishenghuo.com
 * @modified 4/20/21
 * @since 4/20/21
 */

public class FolderListAdapter extends BaseAdapter {
    private static final String TAG = "FolderListAdapter";
    private Context mContext;
    private List<File> mFileList;
    private File mParentFile;
    private File mCurrentFile;
    private ActionBar mActionBar;

    public FolderListAdapter(Context context, ActionBar actionBar) {
        mContext = context;
        mActionBar = actionBar;
        mCurrentFile = Environment.getExternalStorageDirectory();
        mParentFile = Environment.getExternalStorageDirectory().getParentFile();//根目录
    }


    /**
     * 设置文件列表
     *
     * @param fileList 文件列表
     */
    public void setFileList(List<File> fileList) {
        mFileList = fileList;
    }


    @Override
    public int getCount() {
        return mFileList != null ? mFileList.size() : 0;
    }

    @Override
    public File getItem(int position) {
        return mFileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FolderListAdapter.ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file, null);
            viewHolder = new FolderListAdapter.ViewHolder();
            viewHolder.tvFileName = convertView.findViewById(R.id.tvFileName);
            viewHolder.tvFileDesc = convertView.findViewById(R.id.tvFileDesc);
            viewHolder.imgIcon = convertView.findViewById(R.id.imgIcon);
            viewHolder.rlItem = convertView.findViewById(R.id.rlItem);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (FolderListAdapter.ViewHolder) convertView.getTag();
        }

        File file = mFileList.get(position);
        viewHolder.tvFileName.setText(file.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String modified = sdf.format(file.lastModified());
        String desc = "";

        if (file.isDirectory()) {
            desc = modified;//2021/03/39 07:08:09
            viewHolder.imgIcon.setImageResource(R.mipmap.file_list);
            viewHolder.tvFileDesc.setText(desc);
        }


        return convertView;
    }

    public void onItemClick(File file) {
        if (file.isDirectory()) {
            mCurrentFile = file;
            mParentFile = file.getParentFile();
            mFileList = Arrays.asList(file.listFiles());

            mActionBar.setDisplayHomeAsUpEnabled(true);

            notifyDataSetChanged();
        }
    }

    public boolean onBack() {
        File rootPath = Environment.getExternalStorageDirectory();
        if (mParentFile == null) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
            return false;
        }

        L.i(TAG, "onBack()", "rootPath=%s mParentFile=%s", rootPath.getAbsolutePath(), mParentFile.getAbsolutePath());
        if (rootPath.getAbsolutePath().contentEquals(mParentFile.getAbsolutePath())) {
            mActionBar.setDisplayHomeAsUpEnabled(false);
        }

        L.i(TAG, "onBack()", "path=%s", mParentFile.getAbsolutePath());

        if (rootPath.getParentFile().getAbsolutePath().contentEquals(mParentFile.getAbsolutePath())) {
            return false;
        }

        mFileList = Arrays.asList(mParentFile.listFiles());//上一级
        mParentFile = mParentFile.getParentFile();
        notifyDataSetChanged();
        return true;
    }

    /**
     * 移动文件
     *
     * @param src 源文件
     */
    public void moveFile(File src) {
        File dest = new File(mCurrentFile, src.getName());
        src.renameTo(dest);
    }

    /**
     * 新建文件夹
     *
     * @param name 新文件夹的名称
     */
    public void createDir(String name) {
        File file = new File(mCurrentFile, name);
        L.i(TAG, "createDir", "path=%s", file.getAbsoluteFile());
        if (!file.exists()) {
            if (file.mkdirs()) {    //mkdir()最多只能新建一层 /a，mkdirs()可以新建多层/a/b/c....
                toast("新建成功");
            } else {
                toast("新建失败");
            }
        } else {
            toast("文件夹已存在");
        }
        mFileList = Arrays.asList(mCurrentFile.listFiles());
        notifyDataSetChanged();
    }

    private boolean isArrayEmpty(File[] array) {
        if (array == null || array.length <= 0) {
            return true;
        }
        return false;
    }

    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    private class ViewHolder {
        TextView tvFileName;
        TextView tvFileDesc;
        ImageView imgIcon;
        RelativeLayout rlItem;
    }
}
