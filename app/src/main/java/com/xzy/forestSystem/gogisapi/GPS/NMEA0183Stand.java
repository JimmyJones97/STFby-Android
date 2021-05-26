package  com.xzy.forestSystem.gogisapi.GPS;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

public class NMEA0183Stand {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static String lastString = "";
    private static Hashtable<Integer, SatelliteInfo> satellites;
    public GPSLocationClass gpsLocation = null;

    public void processMsg(String msg) {
        this.gpsLocation.updateRecData(msg);
    }

    public static void ProcessData(GPSLocationClass gpsLocation2, GPSDevice gpsDevice, String msg) {
        String tempStatus;
        try {
            gpsLocation2.updateRecData(msg);
            String[] tempLines = msg.split("\r\n");
            if (tempLines != null && tempLines.length > 0) {
                int length = tempLines.length;
                for (int i = 0; i < length; i++) {
                    String tempLine = tempLines[i];
                    if (!tempLine.startsWith("$GP")) {
                        tempLine = String.valueOf(lastString) + tempLine;
                    }
                    String[] tempStrs = tempLine.split(",");
                    boolean tempBool = true;
                    if (tempStrs != null && tempStrs.length > 0) {
                        int tempLen = tempStrs.length;
                        String tempHead = tempStrs[0];
                        if (tempHead.equals("$GPGGA") || tempHead.equals("$GNGGA") || tempHead.equals("$BDGGA")) {
                            if (tempStrs[tempLen - 1].contains("*")) {
                                tempBool = false;
                            }
                            if (tempLen > 14) {
                                String tempStr = tempStrs[1];
                                Date tempTime2 = new Date();
                                int tmpInt = Integer.parseInt(tempStr.substring(0, 2)) + 8;
                                if (tmpInt > 24) {
                                    tmpInt -= 24;
                                }
                                tempTime2.setHours(tmpInt);
                                tempTime2.setMinutes(Integer.parseInt(tempStr.substring(2, 4)));
                                tempTime2.setSeconds(Integer.parseInt(tempStr.substring(4, 6)));
                                double tempD1 = convertDegree(tempStrs[2]);
                                double tempD2 = convertDegree(tempStrs[4]);
                                String tempStatus2 = tempStrs[6];
                                if (tempStatus2.equals("1")) {
                                    tempStatus = "非差分定位";
                                } else if (tempStatus2.equals("2")) {
                                    tempStatus = "差分定位";
                                } else if (tempStatus2.equals("3")) {
                                    tempStatus = "PPS解";
                                } else if (tempStatus2.equals("4")) {
                                    tempStatus = "RTK固定解";
                                } else if (tempStatus2.equals("5")) {
                                    tempStatus = "RTK浮点解";
                                } else if (tempStatus2.equals("6")) {
                                    tempStatus = "正在估算";
                                } else if (tempStatus2.equals("7")) {
                                    tempStatus = "手工输入模式";
                                } else if (tempStatus2.equals("8")) {
                                    tempStatus = "模拟模式";
                                } else {
                                    tempStatus = "未定位";
                                }
                                double tempD3 = Double.parseDouble(tempStrs[8]);
                                gpsLocation2.UpdateLocation(tempTime2.getTime(), tempD2, tempD1, Double.parseDouble(tempStrs[9]), 5.0d * tempD3, -1.0d, -1.0f);
                                gpsLocation2.HDOP = (float) tempD3;
                                gpsLocation2.status = tempStatus;
                            }
                        } else if (tempHead.equals("$GPGSA") || tempHead.equals("$BDGSA")) {
                            if (tempStrs[tempLen - 1].contains("*")) {
                                tempBool = false;
                                if (tempLen > 17) {
                                    float tempD12 = Float.parseFloat(tempStrs[15]);
                                    float tempD22 = Float.parseFloat(tempStrs[16]);
                                    String tempStr2 = tempStrs[17];
                                    if (tempStr2.contains("*")) {
                                        tempStr2 = tempStr2.substring(0, tempStr2.indexOf("*"));
                                    }
                                    float tempD32 = Float.parseFloat(tempStr2);
                                    gpsLocation2.PDOP = tempD12;
                                    gpsLocation2.HDOP = tempD22;
                                    gpsLocation2.VDOP = tempD32;
                                    int tempCount = 0;
                                    for (int i2 = 3; i2 < 15; i2++) {
                                        if (!tempStrs[i2].equals("")) {
                                            tempCount++;
                                        }
                                    }
                                    gpsLocation2.usedSateCount = tempCount;
                                    if (gpsLocation2.satellitesCount < gpsLocation2.usedSateCount) {
                                        gpsLocation2.satellitesCount = gpsLocation2.usedSateCount;
                                    }
                                }
                            }
                        } else if (tempHead.equals("$GPGSV") || tempHead.equals("$BDGSV")) {
                            if (tempStrs[tempLen - 1].contains("*")) {
                                tempBool = false;
                                if (tempLen > 7) {
                                    tempBool = false;
                                    if (tempStrs[2].equals("1")) {
                                        satellites = new Hashtable<>();
                                    }
                                    try {
                                        if (!tempStrs[4].equals("")) {
                                            int tempPRN = Integer.parseInt(tempStrs[4]);
                                            float tempD13 = Float.parseFloat(tempStrs[5]);
                                            float tempD23 = Float.parseFloat(tempStrs[6]);
                                            float tempD33 = Float.parseFloat(tempStrs[7]);
                                            SatelliteInfo tempSate = new SatelliteInfo();
                                            tempSate.f471ID = tempPRN;
                                            tempSate.Elevation = tempD13;
                                            tempSate.Azimuth = tempD23;
                                            tempSate.Snr = tempD33;
                                            satellites.put(Integer.valueOf(tempPRN), tempSate);
                                        }
                                    } catch (Exception e) {
                                    }
                                    if (tempLen > 11) {
                                        try {
                                            if (!tempStrs[8].equals("")) {
                                                int tempPRN2 = Integer.parseInt(tempStrs[8]);
                                                float tempD14 = Float.parseFloat(tempStrs[9]);
                                                float tempD24 = Float.parseFloat(tempStrs[10]);
                                                float tempD34 = Float.parseFloat(tempStrs[11]);
                                                SatelliteInfo tempSate2 = new SatelliteInfo();
                                                tempSate2.f471ID = tempPRN2;
                                                tempSate2.Elevation = tempD14;
                                                tempSate2.Azimuth = tempD24;
                                                tempSate2.Snr = tempD34;
                                                satellites.put(Integer.valueOf(tempPRN2), tempSate2);
                                            }
                                        } catch (Exception e2) {
                                        }
                                    }
                                    if (tempLen > 15) {
                                        try {
                                            if (!tempStrs[12].equals("")) {
                                                int tempPRN3 = Integer.parseInt(tempStrs[12]);
                                                float tempD15 = Float.parseFloat(tempStrs[13]);
                                                float tempD25 = Float.parseFloat(tempStrs[14]);
                                                float tempD35 = Float.parseFloat(tempStrs[15]);
                                                SatelliteInfo tempSate3 = new SatelliteInfo();
                                                tempSate3.f471ID = tempPRN3;
                                                tempSate3.Elevation = tempD15;
                                                tempSate3.Azimuth = tempD25;
                                                tempSate3.Snr = tempD35;
                                                satellites.put(Integer.valueOf(tempPRN3), tempSate3);
                                            }
                                        } catch (Exception e3) {
                                        }
                                    }
                                    if (tempLen > 19) {
                                        try {
                                            if (!tempStrs[16].equals("")) {
                                                int tempPRN4 = Integer.parseInt(tempStrs[16]);
                                                float tempD16 = Float.parseFloat(tempStrs[17]);
                                                float tempD26 = Float.parseFloat(tempStrs[18]);
                                                String tempStr3 = tempStrs[19];
                                                if (tempStr3.contains("*")) {
                                                    tempStr3 = tempStr3.substring(0, tempStr3.indexOf("*"));
                                                }
                                                float tempD36 = Float.parseFloat(tempStr3);
                                                SatelliteInfo tempSate4 = new SatelliteInfo();
                                                tempSate4.f471ID = tempPRN4;
                                                tempSate4.Elevation = tempD16;
                                                tempSate4.Azimuth = tempD26;
                                                tempSate4.Snr = tempD36;
                                                satellites.put(Integer.valueOf(tempPRN4), tempSate4);
                                            }
                                        } catch (Exception e4) {
                                        }
                                    }
                                    if (tempStrs[1].equals(tempStrs[2])) {
                                        int tempCount2 = satellites.size();
                                        if (tempCount2 < gpsLocation2.usedSateCount) {
                                            tempCount2 = gpsLocation2.usedSateCount;
                                        }
                                        gpsLocation2.satellitesCount = tempCount2;
                                        List<SatelliteInfo> tempSatellites = new ArrayList<>();
                                        for (Integer key : satellites.keySet()) {
                                            tempSatellites.add(satellites.get(key));
                                        }
                                        gpsDevice.satellites = tempSatellites;
                                    }
                                }
                            }
                        } else if ((tempHead.equals("$GPRMC") || tempHead.equals("$GNRMC") || tempHead.equals("$BDRMC")) && tempStrs[tempLen - 1].contains("*")) {
                            tempBool = false;
                            if (tempLen > 12) {
                                tempBool = false;
                                String tempStr4 = tempStrs[9];
                                StringBuilder sb = new StringBuilder();
                                sb.append(20);
                                sb.append(tempStr4.substring(4, 6));
                                sb.append("-");
                                sb.append(tempStr4.substring(2, 4));
                                sb.append("-");
                                sb.append(tempStr4.substring(0, 2));
                                sb.append(" ");
                                String tempStr5 = tempStrs[1];
                                sb.append(tempStr5.substring(0, 2));
                                sb.append(":");
                                sb.append(tempStr5.substring(2, 4));
                                sb.append(":");
                                sb.append(tempStr5.substring(4, 6));
                                Date tempTime22 = formatter.parse(sb.toString());
                                tempTime22.setHours(tempTime22.getHours() + 8);
                                double tempD17 = convertDegree(tempStrs[3]);
                                double tempD27 = convertDegree(tempStrs[5]);
                                double tempD37 = 1.852d * Double.parseDouble(tempStrs[7]);
                                double tempD4 = Double.parseDouble(tempStrs[10]);
                                if (tempStrs[11].toUpperCase().equals("W")) {
                                    tempD4 += 180.0d;
                                }
                                gpsLocation2.UpdateLocation(tempTime22.getTime(), tempD27, tempD17, -10000.0d, -1.0d, tempD37, (float) tempD4);
                            }
                        }
                    }
                    if (tempBool) {
                        lastString = tempLine;
                    }
                }
            }
        } catch (Exception e5) {
        }
    }

    private static double convertDegree(String value) {
        double result = Double.parseDouble(value) / 100.0d;
        int tempHead = (int) result;
        return ((double) tempHead) + (((result - ((double) tempHead)) * 10.0d) / 6.0d);
    }
}
