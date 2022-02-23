package com.shine.apps.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.recyclerview.widget.RecyclerView;

import com.shine.apps.R;
import com.shine.apps.utils.L;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * <strong>文件夹列表适配器</strong>
 * <p>
 * 用于展示
 *
 * @author wangyang
 * @version 1.0
 * @email wangyang@meiweishenghuo.com
 * @modified 4/20/21
 * @since 4/20/21
 */

public class FolderListAdapter extends RecyclerView.Adapter<FolderListAdapter.ViewHolder> {
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

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        File file = mFileList.get(position);
        holder.tvFileName.setText(file.getName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String modified = sdf.format(file.lastModified());
        String desc = "";

        if (file.isDirectory()) {
            desc = modified;//2021/03/39 07:08:09
            holder.imgIcon.setImageResource(R.mipmap.file_list);
            holder.tvFileDesc.setText(desc);
        }

        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(file);
            }
        });
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


    @Override
    public int getItemCount() {
        return mFileList != null ? mFileList.size() : 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        TextView tvFileDesc;
        ImageView imgIcon;
        RelativeLayout rlItem;

        public ViewHolder(View view) {
            super(view);
            tvFileName = view.findViewById(R.id.tvFileName);
            tvFileDesc = view.findViewById(R.id.tvFileDesc);
            imgIcon = view.findViewById(R.id.imgIcon);
            rlItem = view.findViewById(R.id.rlItem);
        }

    }
}
