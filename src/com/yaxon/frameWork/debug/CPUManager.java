package com.yaxon.frameWork.debug;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 获取应用的CPU占用率并存储到文件中
 *
 * @author guojiaping
 * @version 2016-10-12 创建<br>
 */
public class CPUManager {
    private final static String TAG = "CPUManager";
    private CPUDumper mCpuDumper;
    private static CPUManager INSTANCE = new CPUManager();
    private static String PATH_LOGCAT;
    private SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyyMMdd");
    private SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static CPUManager getInstance() {
        return INSTANCE;
    }

    private void setFolderPath(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("The logcat folder path is not a directory: " + folderPath);
        }

        PATH_LOGCAT = folderPath.endsWith("/") ? folderPath : folderPath + "/";
    }


    public void start(String dir) {
        setFolderPath(dir);
        if (mCpuDumper == null) {
            mCpuDumper = new CPUDumper(PATH_LOGCAT);
        }
        mCpuDumper.start();
    }

    public void stop() {
        if (mCpuDumper != null) {
            mCpuDumper.stopLogs();
            mCpuDumper = null;
        }
    }


    private class CPUDumper extends Thread {
        private Process logcatProc;
        private BufferedReader mReader = null;
        private boolean mRunning = true;
        String[] cmds = null;
        private FileOutputStream out = null;

        public CPUDumper(String dir) {
            try {
                out = new FileOutputStream(new File(dir, "cpu-" + simpleDateFormat1.format(new Date()) + ".log"), true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            cmds = new String[]{"/system/bin/top", "-n", "1"};
        }


        public void stopLogs() {
            mRunning = false;
        }


        @Override
        public void run() {
            try {
                while (mRunning) {
                    logcatProc = Runtime.getRuntime().exec(cmds);
                    mReader = new BufferedReader(new InputStreamReader(logcatProc.getInputStream()), 1024);
                    String line;
                    while ((line = mReader.readLine()) != null) {
                        if (!mRunning) {
                            break;
                        }
                        if (line.length() == 0) {
                            continue;
                        }
                        int index = line.indexOf("%");
                        if (index != -1 && index >= 2) {
                            Pattern pattern = Pattern.compile("[0-9]*");
                            String str = line.substring(index - 2, index).trim();
                            Matcher isNum = pattern.matcher(str);
                            if (isNum.matches()) {
                                int tmp = Integer.parseInt(str);
                                if (out != null && tmp >= 10) {
                                    out.write((simpleDateFormat2.format(new Date()) + "  " + line + "\n").getBytes());
                                    LogUtils.d(TAG, line);
                                }
                            }
                        }
                    }
                    try {
                        Thread.sleep(30000);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (logcatProc != null) {
                    logcatProc.destroy();
                    logcatProc = null;
                }
                if (mReader != null) {
                    try {
                        mReader.close();
                        mReader = null;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    out = null;
                }
            }
        }
    }
}
