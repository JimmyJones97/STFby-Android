package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import java.util.HashMap;

public class WKTCoordinateSystem {
    public EWKTCoorSystemType CoorSystemType = EWKTCoorSystemType.GEOCCS;
    public String Name = "";
    public HashMap<String, String> ParamsHashMap = new HashMap<>();

    public enum EWKTCoorSystemType {
        GEOGCS,
        PROJCS,
        GEOCCS
    }

    public AbstractC0383CoordinateSystem convertToCoordinateSystem() {
        String tmpString;
        String[] tmpStrs;
        float tmpFenDu;
        try {
            if (this.CoorSystemType == EWKTCoorSystemType.GEOCCS) {
                return new Coordinate_WGS1984();
            }
            if (this.CoorSystemType != EWKTCoorSystemType.PROJCS || (tmpStrs = (tmpString = this.Name.toLowerCase().trim()).split("_")) == null || tmpStrs.length <= 0) {
                return null;
            }
            float tmpCM = 120.0f;
            float tmpQuHao = 40.0f;
            boolean tmpWithDaihao = false;
            if (tmpString.contains("_3_")) {
                tmpFenDu = 3.0f;
            } else {
                tmpFenDu = 6.0f;
            }
            int tmpI = tmpString.indexOf("_cm_");
            if (tmpI > 0) {
                String tmpString2 = tmpString.substring(tmpI + 4);
                if (tmpString2.endsWith("e")) {
                    tmpString2 = tmpString2.substring(0, tmpString2.length() - 1);
                }
                tmpCM = Float.parseFloat(tmpString2);
                if (tmpFenDu == 6.0f) {
                    tmpQuHao = (tmpCM + 3.0f) / tmpFenDu;
                } else {
                    tmpQuHao = tmpCM / tmpFenDu;
                }
            }
            int tmpI2 = tmpString.indexOf("_zone_");
            if (tmpI2 > 0) {
                String tmpString22 = tmpString.substring(tmpI2 + 6);
                if (tmpString22.endsWith("e")) {
                    tmpString22 = tmpString22.substring(0, tmpString22.length() - 1);
                }
                tmpWithDaihao = true;
                if (tmpString22.endsWith("n")) {
                    tmpWithDaihao = false;
                    tmpString22 = tmpString22.substring(0, tmpString22.length() - 1);
                }
                tmpQuHao = Float.parseFloat(tmpString22);
                tmpCM = tmpQuHao * tmpFenDu;
                if (tmpFenDu == 6.0f) {
                    tmpCM -= 3.0f;
                }
            }
            if (tmpStrs[0].equals("xian")) {
                Coordinate_XiAn1980 result2 = new Coordinate_XiAn1980();
                result2._CenterMeridian = tmpCM;
                result2._DaiHao = tmpFenDu;
                result2._FenQu = (int) tmpQuHao;
                result2._WithDaiHao = tmpWithDaihao;
                return result2;
            } else if (tmpStrs[0].equals("beijing")) {
                Coordinate_BeiJing1954 result22 = new Coordinate_BeiJing1954();
                result22._CenterMeridian = tmpCM;
                result22._DaiHao = tmpFenDu;
                result22._FenQu = (int) tmpQuHao;
                result22._WithDaiHao = tmpWithDaihao;
                return result22;
            } else if (!tmpStrs[0].equals("cgcs2000")) {
                return null;
            } else {
                Coordinate_CGCS2000 result23 = new Coordinate_CGCS2000();
                result23._CenterMeridian = tmpCM;
                result23._DaiHao = tmpFenDu;
                result23._FenQu = (int) tmpQuHao;
                result23._WithDaiHao = tmpWithDaihao;
                return result23;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public void readWKTString(String content) {
        String[] tmpStrs01;
        String[] tmpStrs012;
        try {
            int tmpI = content.indexOf("[");
            if (tmpI >= 0) {
                String tmpString01 = content.substring(0, tmpI).toUpperCase().trim();
                if (tmpString01.equals("GEOGCS")) {
                    this.CoorSystemType = EWKTCoorSystemType.GEOCCS;
                    String tmpString02 = content.substring(tmpI + 1);
                    int tmpI2 = tmpString02.lastIndexOf("]");
                    if (tmpI2 > 0 && (tmpStrs012 = tmpString02.substring(0, tmpI2).split(",")) != null && tmpStrs012.length > 0) {
                        this.Name = tmpStrs012[0].trim();
                    }
                } else if (tmpString01.equals("PROJCS")) {
                    this.CoorSystemType = EWKTCoorSystemType.PROJCS;
                    String tmpString022 = content.substring(tmpI + 1);
                    int tmpI3 = tmpString022.lastIndexOf("]");
                    if (tmpI3 > 0 && (tmpStrs01 = tmpString022.substring(0, tmpI3).split(",")) != null && tmpStrs01.length > 0) {
                        this.Name = tmpStrs01[0].trim();
                    }
                }
            }
        } catch (Exception e) {
        }
        if (this.Name != null && this.Name.length() > 0) {
            if (this.Name.startsWith("\"")) {
                this.Name = this.Name.substring(1);
            }
            if (this.Name.endsWith("\"")) {
                this.Name = this.Name.substring(0, this.Name.length() - 1);
            }
        }
    }
}
