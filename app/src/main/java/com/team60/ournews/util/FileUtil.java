package com.team60.ournews.util;

import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by Misutesu on 2017/4/21 0021.
 */

public class FileUtil {
    public static boolean createDir(@NonNull File file) {
        if (!file.exists()) return file.mkdirs();
        return file.isDirectory();
    }

    public static boolean deleteFile(@NonNull File file) {
        if (file.exists()) return file.delete();
        return true;
    }
}
