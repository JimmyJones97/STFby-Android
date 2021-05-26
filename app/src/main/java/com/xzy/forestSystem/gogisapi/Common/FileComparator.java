package  com.xzy.forestSystem.gogisapi.Common;

import java.util.Comparator;

public class FileComparator implements Comparator<String> {
    public int compare(String paramString1, String paramString2) {
        if (Long.parseLong(paramString1.split(",")[1]) > Long.parseLong(paramString2.split(",")[1])) {
            return -1;
        }
        return 1;
    }
}
