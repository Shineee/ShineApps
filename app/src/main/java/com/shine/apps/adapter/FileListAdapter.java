package com.shine.apps.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.shine.apps.R;
import com.shine.apps.utils.L;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * <strong>文件列表适配器</strong>
 * <p>
 * 文件管理主界面
 *
 * @author wangyang
 * @version 1.0
 * @email wangyang@meiweishenghuo.com
 * @modified 3/21/21
 * @since 3/21/21
 */
public class FileListAdapter extends RecyclerView.Adapter<FileListAdapter.ViewHolder> {
    private static final String TAG = "FileListAdapter";
    private Context mContext;
    private List<File> mFileList;
    private SparseIntArray mCheckArray = new SparseIntArray();
    private File mParentFile;
    private File mCurrentFile;
    private ActionBar mActionBar;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean isEditMode = false;

    public FileListAdapter(Context context, ActionBar actionBar) {
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
        String length = convertFileSize(file.length());//2.34KB
        String desc = "";

        if (file.isDirectory()) {
            desc = modified;//2021/03/39 07:08:09
            holder.imgIcon.setImageResource(R.mipmap.file_list);
        } else {
            desc = modified + " - " + length;//2021/03/39 07:08:09 - 2.34KB
            holder.imgIcon.setImageResource(R.mipmap.file);
        }

        holder.tvFileDesc.setText(desc);
        holder.rlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(file);
            }
        });
        holder.rlItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //进入编辑模式
                isEditMode = true;
                notifyDataSetChanged();
                return true;
            }
        });
        if (isEditMode) {
            mCheckArray.put(position, 1);
            holder.cbCheck.setVisibility(View.VISIBLE);
        } else {
            mCheckArray.put(position, 0);
            holder.cbCheck.setVisibility(View.GONE);
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return mFileList != null ? mFileList.size() : 0;
    }

    public void onItemClick(File file) {
        if (file.isDirectory()) {
            if (isArrayEmpty(file.listFiles())) {
                Toast.makeText(mContext, "这个目录是空的", Toast.LENGTH_SHORT).show();
                return;
            }
            mCurrentFile = file;
            mParentFile = file.getParentFile();
            mFileList = Arrays.asList(file.listFiles());

            mActionBar.setDisplayHomeAsUpEnabled(true);

            notifyDataSetChanged();
        } else {
            //打开文件
            openFile(file);
        }
    }

    private void openFile(File f) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);


        /* 设置intent的file与MimeType */
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".FileProvider", f);
            // content://com.shine.apps.FileProvider/external_storage_root/Pictures/1561101910916.jpg
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(f);
            // file:///storage/emulated/0/Pictures/-1633807496.png
        }
        L.i(TAG, "openFile()", "uri=" + uri);


        /* 调用getMIMEType()来取得MimeType */
        String type = getMIMEType(f);
        intent.setDataAndType(uri, type);//type= image/*
        mContext.startActivity(intent);
    }

    /* 判断文件MimeType的method */
    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* 取得扩展名 */
        String end = fName.substring(fName.lastIndexOf(".")
                + 1, fName.length()).toLowerCase();

        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        /*如果无法直接打开，就跳出软件列表给用户选择 */
        if (!end.equals("apk")) {
            type += "/*";
        }
        return type;
    }

    public boolean onBack() {
        //退出编辑模式
        if (isEditMode) {
            isEditMode = false;
            notifyDataSetChanged();
            return true;
        }

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

    /**
     * 删除动作
     *
     * @param file 待删除的根目录或文件
     */
    public void delete(File file) {
        File parent = file.getParentFile();
        new Thread(new Runnable() {
            @Override
            public void run() {
                deleteFile(file);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mFileList = Arrays.asList(parent.listFiles());
                        notifyDataSetChanged();
                        toast("删除成功");
                    }
                });
            }
        }).start();
    }

    /**
     * 删除文件夹和文件
     *
     * @param file 待删除的根目录或文件
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteFile(files[i]);
                }
                file.delete();
            } else {
                file.delete();
            }
        }
    }

    /**
     * 编辑文件夹
     *
     * @param name 修改名称
     */
    public void editDir(File src, String name) {
        File dest = new File(src.getParent(), name);
        if (src.exists()) {
            src.renameTo(dest);
            toast("编辑成功");
        } else {
            toast("文件夹不存在");
        }
        mFileList = Arrays.asList(src.getParentFile().listFiles());
        notifyDataSetChanged();
    }

    private boolean isArrayEmpty(File[] array) {
        if (array == null || array.length <= 0) {
            return true;
        }
        return false;
    }

    /**
     * @param size
     * @return
     */
    private String convertFileSize(long size) {
        //Log.i("FileListAdapter", "convertFileSize() size=" + size);
        double length = size;//0->0.00
        String units[] = {"B", "KB", "MB", "GB", "TB", "PB", "EB"};
        String value = "";//2.34KB|3.45MB|4.67GB
        DecimalFormat df = new DecimalFormat("#.00");

        for (int i = 0; i < units.length; i++) {
            if (length < 1024) {
                value = df.format(length) + units[i];
                break;
            }
            length = length / 1024;//1025/1024=1.x
        }
        return value;
    }

    private void toast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvFileName;
        TextView tvFileDesc;
        ImageView imgIcon;
        CheckBox cbCheck;
        RelativeLayout rlItem;

        public ViewHolder(View view) {
            super(view);
            tvFileName = view.findViewById(R.id.tvFileName);
            tvFileDesc = view.findViewById(R.id.tvFileDesc);
            imgIcon = view.findViewById(R.id.imgIcon);
            cbCheck = view.findViewById(R.id.cbCheck);
            rlItem = view.findViewById(R.id.rlItem);
        }

    }
}
