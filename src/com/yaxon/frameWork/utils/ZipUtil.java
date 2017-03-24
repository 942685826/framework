package com.yaxon.frameWork.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author guojiaping
 * @version 2016/11/25 创建<br>.
 */
public class ZipUtil {
    public ZipUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 文件解压
     *
     * @param zipFileString 待压缩的文件
     * @param outPathString 解压完目录
     * @throws Exception
     */
    public static void upZipFolder(String zipFileString, String outPathString) throws Exception {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(outPathString + File.separator + szName);
                if (!folder.exists()) {
                    folder.mkdirs();
                }
            } else {
                String str = outPathString + File.separator + szName;
//                str = new String(str.getBytes("8859_1"), "GB2312");
                File file = new File(str);
                if (!file.exists()) {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    file.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(file);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = zipInputStream.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
        zipInputStream.close();
    }

    public static void zipFolder(String srcFileString, String zipFileString) throws Exception {
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFileString));
        File file = new File(srcFileString);
        zipFiles(file.getParent() + File.separator, file.getName(), outZip);
        outZip.finish();
        outZip.close();
    }

    private static void zipFiles(String folderString, String fileString, ZipOutputStream
            zipOutputString) throws Exception {
        if (zipOutputString == null) {
            return;
        }
        File file = new File(folderString + fileString);
//        final File file = new File(folderString + File.separator + fileString);
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(fileString);
            FileInputStream inputStream = new FileInputStream(file);
            zipOutputString.putNextEntry(zipEntry);
            int len;
            byte[] buffer = new byte[4096];
            while ((len = inputStream.read(buffer)) != -1) {
                zipOutputString.write(buffer, 0, len);
            }
        } else {
            String[] fileList = file.list();
            if (fileList.length <= 0) {
                ZipEntry zipEntry = new ZipEntry(fileString + File.separator);
                zipOutputString.putNextEntry(zipEntry);
                zipOutputString.closeEntry();
            }

            for (int i = 0; i < fileList.length; i++) {
                zipFiles(folderString, fileString + File.separator + fileList[i], zipOutputString);
            }
        }
    }

    public static List<File> getFileList(String zipFileString, boolean bContainFolder,
                                         boolean bContainFile) throws Exception {
        List<File> fileList = new ArrayList<>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        ZipEntry zipEntry;
        String szName = "";
        while ((zipEntry = inZip.getNextEntry()) != null) {
            szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                szName = szName.substring(0, szName.length() - 1);
                File folder = new File(szName);
                if (bContainFolder) {
                    fileList.add(folder);
                }
            } else {
                File file = new File(szName);
                if (bContainFile) {
                    fileList.add(file);
                }
            }
        }
        inZip.close();
        return fileList;
    }
}
