package ex.guitartest.util;

import android.os.Environment;

import java.io.File;

/**
 * Created by lenovo on 2018/4/23.
 */

public class FileUtils {
    /**
     * 3.获取应用目录下面的指定目录
     *
     * @param name
     * @return
     */
    public static File getDir(String name) {
        File dir = new File(getAppDir(), name);

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    /**
     * 2.获取应用目录
     *
     * @return
     */
    public static File getAppDir() {
        File dir = new File(getSDcardDir(), "GuitarTest");

        if (!dir.exists()) {
            dir.mkdir();
        }

        return dir;
    }

    /**
     * 1.获取sd卡根目录
     *
     * @return
     */
    public static File getSDcardDir() {
        String state = Environment.getExternalStorageState();

        // 如果内存卡已挂载
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File storageDirectory = Environment.getExternalStorageDirectory();

            return storageDirectory;

        }

        throw new RuntimeException("没有找到内存卡");
    }
}
