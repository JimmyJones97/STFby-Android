package  com.xzy.forestSystem.gogisapi.Common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    public static String compress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gzip = new GZIPOutputStream(out);
        gzip.write(str.getBytes());
        gzip.close();
        return out.toString("ISO-8859-1");
    }

    public static String uncompress(String str) throws IOException {
        if (str == null || str.length() == 0) {
            return str;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPInputStream gunzip = new GZIPInputStream(new ByteArrayInputStream(str.getBytes("ISO-8859-1")));
        byte[] buffer = new byte[256];
        while (true) {
            int n = gunzip.read(buffer);
            if (n < 0) {
                return out.toString();
            }
            out.write(buffer, 0, n);
        }
    }

    public static List<File> getFileList(String zipFileString, boolean bContainFolder, boolean bContainFile) throws Exception {
        List<File> fileList = new ArrayList<>();
        ZipInputStream inZip = new ZipInputStream(new FileInputStream(zipFileString));
        while (true) {
            ZipEntry zipEntry = inZip.getNextEntry();
            if (zipEntry == null) {
                inZip.close();
                return fileList;
            }
            String szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                File folder = new File(szName.substring(0, szName.length() - 1));
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
    }

    public static InputStream upZip(String zipFilePath, String fileString) throws Exception {
        ZipFile zipFile = new ZipFile(zipFilePath);
        return zipFile.getInputStream(zipFile.getEntry(fileString));
    }

    public static void unZipFolder(InputStream input, String outPathString) throws Exception {
        ZipInputStream inZip = new ZipInputStream(input);
        while (true) {
            ZipEntry zipEntry = inZip.getNextEntry();
            if (zipEntry == null) {
                inZip.close();
                return;
            }
            String szName = zipEntry.getName();
            if (zipEntry.isDirectory()) {
                new File(String.valueOf(outPathString) + File.separator + szName.substring(0, szName.length() - 1)).mkdirs();
            } else {
                File file = new File(String.valueOf(outPathString) + File.separator + szName);
                file.createNewFile();
                FileOutputStream out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                while (true) {
                    int len = inZip.read(buffer);
                    if (len == -1) {
                        break;
                    }
                    out.write(buffer, 0, len);
                    out.flush();
                }
                out.close();
            }
        }
    }

    public static void unZipFolder(String zipFileString, String outPathString) throws Exception {
        unZipFolder(new FileInputStream(zipFileString), outPathString);
    }

    public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
        ZipOutputStream outZip = new ZipOutputStream(new FileOutputStream(zipFilePath));
        File file = new File(srcFilePath);
        zipFiles(String.valueOf(file.getParent()) + File.separator, file.getName(), outZip);
        outZip.finish();
        outZip.close();
    }

    private static void zipFiles(String folderPath, String filePath, ZipOutputStream zipOut) throws Exception {
        if (zipOut != null) {
            File file = new File(String.valueOf(folderPath) + filePath);
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(filePath);
                FileInputStream inputStream = new FileInputStream(file);
                zipOut.putNextEntry(zipEntry);
                byte[] buffer = new byte[4096];
                while (true) {
                    int len = inputStream.read(buffer);
                    if (len == -1) {
                        zipOut.closeEntry();
                        return;
                    }
                    zipOut.write(buffer, 0, len);
                }
            } else {
                String[] fileList = file.list();
                if (fileList.length <= 0) {
                    zipOut.putNextEntry(new ZipEntry(String.valueOf(filePath) + File.separator));
                    zipOut.closeEntry();
                }
                for (int i = 0; i < fileList.length; i++) {
                    zipFiles(folderPath, String.valueOf(filePath) + File.separator + fileList[i], zipOut);
                }
            }
        }
    }
}
