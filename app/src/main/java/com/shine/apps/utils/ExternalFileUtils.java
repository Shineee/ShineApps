package com.shine.apps.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <strong>外部文件系统工具类</strong>
 * <p>
 * 获取手机系统中的文件列表（SDCard中的文件目录）
 *
 * @author wangyang
 * @version 1.0
 * @email wyhdgx@163.com
 * @modified 3/25/21
 * @since 3/25/21
 */

public class ExternalFileUtils {
    public List<File> getExternalPathList() {
        File externalPath = Environment.getExternalStorageDirectory();//  /storage/emulated/0
        if (externalPath == null) {
            Log.e("ExternalFileUtils", "getExternalPath() externalPath=null");
            return null;
        }
        if (externalPath.listFiles() == null) {
            Log.e("ExternalFileUtils", "getExternalPath() externalPath.listFiles=null");
            return null;
        }
        List<File> fileList;
        boolean showFile = SharedPreferenceUtils.getInstance().getBoolean("showFile");
        if (showFile) {
            fileList = Arrays.asList(externalPath.listFiles());
            return fileList;
        }

        fileList = new ArrayList<>();
        for (File file : externalPath.listFiles()) {
            if (!file.getName().startsWith(".")) {//.a.txt
                fileList.add(file);
            }
        }

        return fileList;
    }

    public List<File> getExternalFolderPathList() {
        File externalPath = Environment.getExternalStorageDirectory();//  /storage/emulated/0
        if (externalPath == null) {
            Log.e("ExternalFileUtils", "getExternalPath() externalPath=null");
            return null;
        }
        if (externalPath.listFiles() == null) {
            Log.e("ExternalFileUtils", "getExternalPath() externalPath.listFiles=null");
            return null;
        }
        List<File> folderList = new ArrayList<>();
        for (File file : externalPath.listFiles()) {
            if (file.isDirectory()) {
                folderList.add(file);
            }
        }
        return folderList;
    }
}
