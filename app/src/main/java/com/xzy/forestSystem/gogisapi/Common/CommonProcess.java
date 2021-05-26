package  com.xzy.forestSystem.gogisapi.Common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonProcess {
    public static Bitmap Base64ToBitmap(String paramString) {
        try {
            byte[] arrayOfByte = Base64.decode(paramString, 0);
            return BitmapFactory.decodeByteArray(arrayOfByte, 0, arrayOfByte.length);
        } catch (Exception e) {
            return null;
        }
    }

    public static String BitmapToBase64(Bitmap paramBitmap) {
        String str = "";
        try {
            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
            paramBitmap.compress(Bitmap.CompressFormat.PNG, 100, localByteArrayOutputStream);
            str = Base64.encodeToString(localByteArrayOutputStream.toByteArray(), 0);
            try {
                localByteArrayOutputStream.close();
                return str;
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
                return str;
            }
        } catch (Exception e) {
            return str;
        }
    }

    public static boolean isDigit(String strNum) {
        return strNum.matches("[0-9]{1,}");
    }

    public static String getNumbers(String content) {
        Matcher matcher = Pattern.compile("\\d+").matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }

    public static String splitNotNumber(String content) {
        Matcher matcher = Pattern.compile("\\D+").matcher(content);
        if (matcher.find()) {
            return matcher.group(0);
        }
        return "";
    }
}
