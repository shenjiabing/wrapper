package com.sayweee.app2;

import android.text.TextUtils;

import com.sayweee.logger.Logger;
import com.sayweee.scheduler.TaskScheduler;

import java.io.File;
import java.io.FileFilter;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by wyn on 2020/5/19.
 */
public class LogConfig {
    /**
     * 日志设置
     *
     * @param path    日志地址
     * @param logEnable   是否打印
     * @param writeEnable 是否写入文件
     * @param keepDays    需要保存的天数
     */
    public static void setLogConfig(String path, boolean logEnable, boolean writeEnable, int keepDays) {
        AppConfig.PATH_LOG = path;
        Logger.config(path, logEnable, writeEnable);
        if (keepDays > 0) {
            setAutoDelete(path, keepDays);
        }
    }

    public static void deleteAllLog(final String path) {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(path);
                    if(file != null) {
                        deleteAll(file);
                    }
                } catch (Exception e) {
                }
            }
        });
    }

    private static void deleteAll(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteAll(f);
            }
            file.delete();
        }
    }

    /**
     * 删除日志
     *
     * @param day
     */
    private static void setAutoDelete(final String basePath, final int day) {
        TaskScheduler.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    File file = new File(basePath);
                    final long limit = System.currentTimeMillis() - day * 24 * 60 * 60 * 1000;
                    deleteFile(file, limit);
                } catch (Exception e) {
                }
            }
        });
    }


    private static void deleteFile(File dir, final long limit) {
        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    deleteFile(file, limit);
                    return false;
                }
                long date = parseFileDate(file.getName());
                if (date <= 0 || limit - date > 0) {
                    return true;
                }
                return false;
            }
        });

        if (files != null && files.length > 0) {
            for (File f : files) {
                if (f != null) {
                    f.delete();
                }
            }
        }
    }

    private static long parseFileDate(String name) {
        if (!TextUtils.isEmpty(name)) {
            try {
                int i = name.lastIndexOf(".");
                String s = name.substring(i - 10, i);
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(s);
                return date.getTime();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }
}
