/**
 * Copyright (C) 2013 XiaMen Yaxon NetWorks Co.,LTD.
 */
package com.yaxon.frameWork.file;

import android.util.Log;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

/**
 * ѹ����ѹ���㷨
 *
 * @author: zzh 2013-5-18 ����<br>
 */
public class Zip {
    private static final int BUFF_SIZE = 1024;

    /**
     * ��ѹ����
     *
     * @param zipData :����ѹ����
     * @return byte[] ��ѹ������
     * @throws IOException
     */
    public static byte[] unZip(byte[] zipData) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(zipData);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // ����ѹ��������
        ZipInputStream zis = new ZipInputStream(bais);
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((zis.getNextEntry()) != null) {
            while ((len = zis.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
        }
        zis.closeEntry();
        zis.close();
        return baos.toByteArray();
    }

    /**
     * ��ѹ��һ���ļ�
     *
     * @param zipFile    ��ѹ���ļ�
     * @param folderPath �� ��ѹ����Ŀ��Ŀ¼
     * @throws IOException ������ѹ�����̳���ʱ�׳�
     */
    public static void upZipFile(File zipFile, String folderPath)
            throws IOException {
        File desDir = new File(folderPath);

        if (!desDir.exists()) {
            desDir.mkdirs();
        }

        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            if (entry.isDirectory()) {
                // ���ļ������ȡ��һ���ļ�
                continue;
            }
            InputStream is = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }

            OutputStream os = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int realLength;
            while ((realLength = is.read(buffer)) > 0) {
                os.write(buffer, 0, realLength);
            }
            is.close();
            os.close();
        }
    }

    /**
     * ��ѹ��
     *
     * @param zipFile
     * @param targetDir
     */
    public static void Unzip(String zipFile, String targetDir) {
        // ���ﻺ��������ʹ��4KB��
        int BUFFER = 4096;

        // ����ÿ��zip����Ŀ����
        String strEntry;
        try {
            // ���������
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(zipFile);
            ZipInputStream zis = new ZipInputStream(
                    new BufferedInputStream(fis));

            // ÿ��zip��Ŀ��ʵ��
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                try {
                    Log.i("Unzip: ", "=" + entry);
                    int count;
                    byte data[] = new byte[BUFFER];
                    strEntry = entry.getName();

                    File entryFile = new File(targetDir + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(entryFile);
                    dest = new BufferedOutputStream(fos, BUFFER);
                    while ((count = zis.read(data, 0, BUFFER)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            zis.close();
        } catch (Exception cwj) {
            cwj.printStackTrace();
            Log.i("Unzip: ", cwj.toString());
        }
    }
}