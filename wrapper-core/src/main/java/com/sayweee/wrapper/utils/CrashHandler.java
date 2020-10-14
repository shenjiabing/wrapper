package com.sayweee.wrapper.utils;

import com.sayweee.logger.Logger;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:  winds
 * Email:   heardown@163.com
 * Date:    2020/5/27.
 * Desc:    异常捕获
 */
public class CrashHandler implements UncaughtExceptionHandler {

    private UncaughtExceptionHandler handler;
    private String fileName = "crash/crash";

    private SimpleDateFormat dateFormat;
    private static CrashHandler crashHandler;

    public static CrashHandler install() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    private CrashHandler() {
        handler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
    }

    public CrashHandler setWriteFile(String fileName) {
        this.fileName = fileName;
        return this;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        StringBuilder builder = new StringBuilder();

        builder.append("Date: ").append(dateFormat.format(new Date())).append("\n");
        // 崩溃的堆栈信息
        builder.append("Stacktrace:\n\n");
        StringWriter stringwriter = new StringWriter();
        PrintWriter printwriter = new PrintWriter(stringwriter);
        throwable.printStackTrace(printwriter);
        builder.append(stringwriter.toString());
        builder.append("===========\n");
        printwriter.close();

        write(builder.toString());

        if (handler != null) {
            // 交给还给系统处理
            handler.uncaughtException(thread, throwable);
        }

    }

    protected void write(String content) {
        Logger.f(fileName).i(content);
    }

}
