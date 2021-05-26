package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import androidx.core.view.ViewCompat;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.SQLiteDBHelper;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.AbstractC0383CoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.ProjectionCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.CoordinateSystem.WKTCoordinateSystem;
import  com.xzy.forestSystem.gogisapi.MyControls.CustomeProgressDialog;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;

import org.gdal.gdal.Band;
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;
import org.gdal.gdalconst.gdalconstConstants;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;


public class ConvertTifRasterClass {
    public static int CompressQuality = 70;

    public static int GetMaxLevel(int width, int height, int tileSize) {
        int side;
        int level = 1;
        if (width > height) {
            side = width;
        } else {
            side = height;
        }
        double tmpR = ((double) side) / ((double) tileSize);
        int tmpCount = 2;
        for (int i = 1; i < 22 && tmpR > ((double) tmpCount); i++) {
            tmpCount *= 2;
            level++;
        }
        return level;
    }

    public static int convertByteToInt(byte data) {
        return (((data >> 4) & 15) * 16) + (data & 15);
    }

    public static int[] convertByteToColor(byte[] rData, byte[] gData, byte[] bData, int len) {
        if (len == 0) {
            return null;
        }
        int[] color = new int[len];
        for (int i = 0; i < color.length; i++) {
            int red = convertByteToInt(rData[i]);
            int green = convertByteToInt(gData[i]);
            color[i] = (red << 16) | (green << 8) | convertByteToInt(bData[i]) | ViewCompat.MEASURED_STATE_MASK;
        }
        return color;
    }

    public static int[] convertByteToColor(byte[] rData, byte[] gData, byte[] bData, int len, int scale) {
        if (len == 0) {
            return null;
        }
        int red = 0;
        int green = 0;
        int blue = 0;
        int tmpTid = 0;
        int[] color = new int[len];
        for (int i = 0; i < color.length; i++) {
            red += convertByteToInt(rData[i]);
            green += convertByteToInt(gData[i]);
            blue += convertByteToInt(bData[i]);
            if (tmpTid == scale) {
                color[i] = (red << 16) | (green << 8) | blue | ViewCompat.MEASURED_STATE_MASK;
                tmpTid = 0;
                red = 0;
            } else {
                tmpTid++;
            }
        }
        return color;
    }

    public static int[] convertByteToColor(byte[] data, int offset, int len) {
        if (len == 0) {
            return null;
        }
        int[] color = new int[len];
        for (int i = 0; i < color.length; i++) {
            int red = convertByteToInt(data[i + offset]);
            color[i] = (red << 16) | (red << 8) | red | ViewCompat.MEASURED_STATE_MASK;
        }
        return color;
    }

    public static int[] convertBytesToColor(byte[] rData, byte[] gData, byte[] bData, int len, int offsetR, int offsetG, int offsetB) {
        if (len == 0) {
            return null;
        }
        int[] color = new int[len];
        for (int i = 0; i < color.length; i++) {
            int red = convertByteToInt(rData[i + offsetR]);
            int green = convertByteToInt(gData[i + offsetG]);
            color[i] = (red << 16) | (green << 8) | convertByteToInt(bData[i + offsetB]) | ViewCompat.MEASURED_STATE_MASK;
        }
        return color;
    }

    public static int[] convertByteToColor(byte[] data, int width, int height, int scale) {
        if (data.length == 0) {
            return null;
        }
        int tmpTid = 0;
        int[] color = new int[(width * height)];
        int tmp2W = (int) Math.pow(2.0d, (double) (scale - 1));
        int tmpOrgW = tmp2W * width;
        int i = 0;
        while (i < width) {
            for (int j = 0; j < height; j++) {
                int tmpX = i * scale;
                int tmpY = j * scale;
                byte b = 0;
                for (int i2 = tmpX; i2 < tmpX + tmp2W; i2++) {
                    for (int j2 = tmpY; j2 < tmpY + tmp2W; j2++) {
                        b = data[(j2 * tmpOrgW) + i2];
                    }
                }
                int red = b / (tmp2W * tmp2W);
                tmpTid++;
                color[tmpTid] = (red << 16) | (red << 8) | red | ViewCompat.MEASURED_STATE_MASK;
            }
            i++;
            tmpTid = tmpTid;
        }
        return color;
    }

    public static int[] getTileColors(Dataset dataset, int offsetX, int offsetY, int width, int height) {
        int[] result = null;
        byte[] tmpRBytes = null;
        byte[] tmpGBytes = null;
        byte[] tmpBBytes = null;
        try {
            ByteBuffer tmpByteBuffer = dataset.GetRasterBand(1).ReadRaster_Direct(offsetX, offsetY, width, height);
            if (tmpByteBuffer != null) {
                tmpRBytes = tmpByteBuffer.array();
                if (dataset.GetRasterCount() == 1) {
                    return convertByteToColor(tmpRBytes, tmpByteBuffer.arrayOffset(), width * height);
                }
            }
            ByteBuffer tmpByteBuffer2 = dataset.GetRasterBand(2).ReadRaster_Direct(offsetX, offsetY, width, height);
            if (tmpByteBuffer2 != null) {
                tmpGBytes = tmpByteBuffer2.array();
            }
            ByteBuffer tmpByteBuffer3 = dataset.GetRasterBand(3).ReadRaster_Direct(offsetX, offsetY, width, height);
            if (tmpByteBuffer3 != null) {
                tmpBBytes = tmpByteBuffer3.array();
            }
            if (tmpGBytes == null) {
                tmpGBytes = tmpRBytes;
            }
            if (tmpBBytes == null) {
                tmpBBytes = tmpRBytes;
            }
            result = convertByteToColor(tmpRBytes, tmpGBytes, tmpBBytes, width * height);
        } catch (Exception e) {
        }
        return result;
    }

    public static int[] getTileColorsWithScale(Dataset dataset, int offsetX, int offsetY, int orgwidth, int orgheight, int destwidth, int destheight) {
        int tmpOffset02 = 0;
        byte[] tmpGBytes;
        int tmpOffset03 = 0;
        byte[] tmpBBytes;
        int[] result = null;
        byte[] tmpRBytes = null;
        int tmpOffset01 = 0;
        try {
            Band tmpBand = dataset.GetRasterBand(1);
            ByteBuffer tmpByteBuffer = ByteBuffer.allocateDirect(destwidth * destheight);
            tmpBand.ReadRaster_Direct(offsetX, offsetY, orgwidth, orgheight, destwidth, destheight, tmpByteBuffer);
            if (tmpByteBuffer != null) {
                tmpRBytes = tmpByteBuffer.array();
                tmpOffset01 = tmpByteBuffer.arrayOffset();
                if (dataset.GetRasterCount() == 1) {
                    return convertByteToColor(tmpRBytes, tmpOffset01, destwidth * destheight);
                }
            }
            Band tmpBand2 = dataset.GetRasterBand(2);
            ByteBuffer tmpByteBuffer2 = ByteBuffer.allocateDirect(destwidth * destheight);
            tmpBand2.ReadRaster_Direct(offsetX, offsetY, orgwidth, orgheight, destwidth, destheight, tmpByteBuffer2);
            if (tmpByteBuffer2 != null) {
                tmpGBytes = tmpByteBuffer2.array();
                try {
                    tmpOffset02 = tmpByteBuffer2.arrayOffset();
                } catch (Exception e) {
                }
            } else {
                tmpOffset02 = 0;
                tmpGBytes = null;
            }
            try {
                Band tmpBand3 = dataset.GetRasterBand(3);
                ByteBuffer tmpByteBuffer3 = ByteBuffer.allocateDirect(destwidth * destheight);
                tmpBand3.ReadRaster_Direct(offsetX, offsetY, orgwidth, orgheight, destwidth, destheight, tmpByteBuffer3);
                if (tmpByteBuffer3 != null) {
                    tmpBBytes = tmpByteBuffer3.array();
                    try {
                        tmpOffset03 = tmpByteBuffer3.arrayOffset();
                    } catch (Exception e2) {
                    }
                } else {
                    tmpOffset03 = 0;
                    tmpBBytes = null;
                }
                if (tmpGBytes == null) {
                    tmpGBytes = tmpRBytes;
                }
                if (tmpBBytes == null) {
                    tmpBBytes = tmpRBytes;
                }
                try {
                    result = convertBytesToColor(tmpRBytes, tmpGBytes, tmpBBytes, destwidth * destheight, tmpOffset01, tmpOffset02, tmpOffset03);
                } catch (Exception e3) {
                }
            } catch (Exception e4) {
            }
        } catch (Exception e5) {
        }
        return result;
    }

    public static int[] getTileColorsWithScale(Dataset dataset, int bandIndex, int offsetX, int offsetY, int orgwidth, int orgheight, int destwidth, int destheight) {
        try {
            Band tmpBand = dataset.GetRasterBand(bandIndex);
            ByteBuffer tmpByteBuffer = ByteBuffer.allocateDirect(destwidth * destheight);
            tmpBand.ReadRaster_Direct(offsetX, offsetY, orgwidth, orgheight, destwidth, destheight, tmpByteBuffer);
            if (tmpByteBuffer != null) {
                return convertByteToColor(tmpByteBuffer.array(), tmpByteBuffer.arrayOffset(), destwidth * destheight);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean Convert(String filePath, String saveFilePath, CustomeProgressDialog progressDialog) {
        Bitmap tmpBitmap;
        Bitmap tmpBitmap2;
        Bitmap tmpBitmap3;
        Bitmap tmpBitmap4;
        boolean result = false;
        gdal.SetConfigOption("GDAL_FILENAME_IS_GBK", "YES");
        gdal.AllRegister();
        Dataset dataset = gdal.Open(filePath, gdalconstConstants.GA_ReadOnly);
        if (dataset == null) {
            if (progressDialog != null) {
                Message tmpMsg = progressDialog.myHandler.obtainMessage();
                tmpMsg.what = 5;
                tmpMsg.obj = "数据文件错误.";
                progressDialog.myHandler.sendMessage(tmpMsg);
            }
            return false;
        }
        double[] geomatrix = dataset.GetGeoTransform();
        int iXsize = dataset.GetRasterXSize();
        int iYsize = dataset.GetRasterYSize();
        if (dataset.GetRasterCount() > 0) {
            String tmpPrjInfoString = dataset.GetProjectionRef();
            WKTCoordinateSystem tmpWKTCoordinateSystem = new WKTCoordinateSystem();
            tmpWKTCoordinateSystem.readWKTString(tmpPrjInfoString);
            AbstractC0383CoordinateSystem tmpCoordinateSystem = tmpWKTCoordinateSystem.convertToCoordinateSystem();
            if ((tmpCoordinateSystem instanceof ProjectionCoordinateSystem) && ((ProjectionCoordinateSystem) tmpCoordinateSystem).IsWithDaiHao()) {
                geomatrix[0] = geomatrix[0] + (((double) (-((ProjectionCoordinateSystem) tmpCoordinateSystem).getFenQu())) * 1000000.0d);
            }
            double tmpXMin = geomatrix[0];
            double tmpXMax = geomatrix[0] + (geomatrix[1] * ((double) iXsize));
            double tmpYMax = geomatrix[3];
            double tmpYMin = geomatrix[3] + (geomatrix[5] * ((double) iYsize));
            int tmpMaxLevel = GetMaxLevel(iXsize, iYsize, 256);
            SQLiteDBHelper tmpSQLiteDBHelper = new SQLiteDBHelper();
            tmpSQLiteDBHelper.CreateDatabase(saveFilePath);
            tmpSQLiteDBHelper.ExecuteSQL("CREATE TABLE MapInfo (SYS_ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL DEFAULT (0),CoorType varchar(50),CenterJX double,TileSize double,MaxLevel int,Scale double,Min_X double,Min_Y double,Max_X double,Max_Y double)");
            String tmpCM = "0";
            String tmpScale = String.valueOf((tmpXMax - tmpXMin) / 256.0d);
            if (tmpCoordinateSystem instanceof ProjectionCoordinateSystem) {
                tmpCM = String.valueOf(((ProjectionCoordinateSystem) tmpCoordinateSystem).GetCenterMeridian());
            }
            tmpSQLiteDBHelper.ExecuteSQL("Insert Into MapInfo (CoorType,CenterJX,TileSize,MaxLevel,Scale,Min_X,Min_Y,Max_X,Max_Y) Values ('" + tmpCoordinateSystem.GetName() + "'," + tmpCM + ",256," + String.valueOf(tmpMaxLevel) + "," + String.valueOf(tmpScale) + "," + String.valueOf(tmpXMin) + "," + String.valueOf(tmpYMin) + "," + String.valueOf(tmpXMax) + "," + String.valueOf(tmpYMax) + ")");
            int tmpTotalTile = 0;
            int tmpCurrentTid = 0;
            for (int i = 1; i <= tmpMaxLevel; i++) {
                tmpSQLiteDBHelper.ExecuteSQL("CREATE TABLE L" + String.valueOf(i) + " (SYS_ID INTEGER,SYS_GEO Blob,SYS_RC varchar(50) PRIMARY KEY, MapType Text,LT_X double,LT_Y double,RB_X double,RB_Y double)");
                int tmpScan = ((int) Math.pow(2.0d, (double) (tmpMaxLevel - i))) * 256;
                tmpTotalTile += (((iXsize + tmpScan) - 1) / tmpScan) * (((iYsize + tmpScan) - 1) / tmpScan);
            }
            if (tmpTotalTile < 1) {
                tmpTotalTile = 1;
            }
            int i2 = iXsize / 256;
            int i3 = iYsize / 256;
            Handler myHandler = null;
            if (progressDialog != null) {
                myHandler = progressDialog.myHandler;
            }
            if (myHandler != null) {
                Message msg = myHandler.obtainMessage();
                msg.what = 5;
                msg.obj = new Object[]{"开始压缩数据 [0" + FileSelector_Dialog.sRoot + tmpTotalTile + "]", Integer.valueOf(0 / tmpTotalTile)};
                myHandler.sendMessage(msg);
                if (progressDialog.isCancel) {
                    return false;
                }
            }
            int tmpLastRatio = 0;
            tmpSQLiteDBHelper.BeginTransaction();
            if (tmpMaxLevel > 0) {
                if (CompressQuality <= 0 || CompressQuality > 100) {
                    CompressQuality = 70;
                }
                long tmpStartTime = System.currentTimeMillis();
                for (int k = tmpMaxLevel; k > 0; k--) {
                    int tmpScan2 = ((int) Math.pow(2.0d, (double) (tmpMaxLevel - k))) * 256;
                    int tmpXCount = iXsize / tmpScan2;
                    int tmpYCount = iYsize / tmpScan2;
                    for (int i4 = 0; i4 < tmpXCount; i4++) {
                        for (int j = 0; j < tmpYCount; j++) {
                            tmpCurrentTid++;
                            double tmpXgeo01 = geomatrix[0] + (geomatrix[1] * ((double) i4) * ((double) tmpScan2)) + (geomatrix[2] * ((double) j) * ((double) tmpScan2));
                            double tmpYgeo01 = geomatrix[3] + (geomatrix[4] * ((double) i4) * ((double) tmpScan2)) + (geomatrix[5] * ((double) j) * ((double) tmpScan2));
                            double tmpXgeo02 = geomatrix[0] + (geomatrix[1] * ((double) (i4 + 1)) * ((double) tmpScan2)) + (geomatrix[2] * ((double) (j + 1)) * ((double) tmpScan2));
                            double tmpYgeo02 = geomatrix[3] + (geomatrix[4] * ((double) (i4 + 1)) * ((double) tmpScan2)) + (geomatrix[5] * ((double) (j + 1)) * ((double) tmpScan2));
                            if (tmpXgeo02 > tmpXMax) {
                                tmpXgeo02 = tmpXMax;
                            }
                            if (tmpYgeo02 < tmpYMin) {
                                tmpYgeo02 = tmpYMin;
                            }
                            int[] tmpColors = getTileColorsWithScale(dataset, i4 * tmpScan2, j * tmpScan2, tmpScan2, tmpScan2, 256, 256);
                            if (!(tmpColors == null || tmpColors.length <= 0 || (tmpBitmap4 = Bitmap.createBitmap(tmpColors, 256, 256, Bitmap.Config.ARGB_8888)) == null)) {
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                tmpBitmap4.compress(Bitmap.CompressFormat.JPEG, CompressQuality, baos);
                                tmpSQLiteDBHelper.ExecuteSQL("insert into L" + String.valueOf(k) + " (SYS_GEO,SYS_RC,MAPTYPE,LT_X,LT_Y,RB_X,RB_Y) values (?,'" + String.valueOf(j) + "-" + String.valueOf(i4) + "','0'," + String.valueOf(tmpXgeo01) + "," + String.valueOf(tmpYgeo01) + "," + String.valueOf(tmpXgeo02) + "," + String.valueOf(tmpYgeo02) + ")", new Object[]{baos.toByteArray()});
                            }
                            int tmpCurrentRatio = (tmpCurrentTid * 100) / tmpTotalTile;
                            if (!(myHandler == null || tmpCurrentRatio == tmpLastRatio || tmpCurrentRatio == 0 || tmpCurrentRatio >= 100)) {
                                Message msg2 = myHandler.obtainMessage();
                                msg2.what = 5;
                                long tmpNeedTime = (long) (((float) (System.currentTimeMillis() - tmpStartTime)) * ((100.0f / ((float) tmpCurrentRatio)) - 1.0f));
                                if (tmpNeedTime < 0) {
                                    tmpNeedTime = 0;
                                }
                                msg2.obj = new Object[]{"开始压缩数据 [" + tmpCurrentTid + FileSelector_Dialog.sRoot + tmpTotalTile + "],剩余时间约" + Common.SimplifyTime(tmpNeedTime), Integer.valueOf(tmpCurrentRatio)};
                                myHandler.sendMessage(msg2);
                                if (progressDialog.isCancel) {
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                    dataset.delete();
                                    return true;
                                }
                                tmpLastRatio = tmpCurrentRatio;
                            }
                        }
                    }
                    if (tmpXCount * 256 < iXsize) {
                        int tmpXWidth = iXsize - (tmpXCount * tmpScan2);
                        int tmpImgWidth = (tmpXWidth * 256) / tmpScan2;
                        for (int j2 = 0; j2 < tmpYCount; j2++) {
                            tmpCurrentTid++;
                            double tmpXgeo012 = geomatrix[0] + (geomatrix[1] * ((double) tmpXCount) * ((double) tmpScan2)) + (geomatrix[2] * ((double) j2) * ((double) tmpScan2));
                            double tmpYgeo012 = geomatrix[3] + (geomatrix[4] * ((double) tmpXCount) * ((double) tmpScan2)) + (geomatrix[5] * ((double) j2) * ((double) tmpScan2));
                            double tmpXgeo022 = geomatrix[0] + (geomatrix[1] * ((double) (tmpXCount + 1)) * ((double) tmpScan2)) + (geomatrix[2] * ((double) (j2 + 1)) * ((double) tmpScan2));
                            double tmpYgeo022 = geomatrix[3] + (geomatrix[4] * ((double) (tmpXCount + 1)) * ((double) tmpScan2)) + (geomatrix[5] * ((double) (j2 + 1)) * ((double) tmpScan2));
                            if (tmpXgeo022 > tmpXMax) {
                                tmpXgeo022 = tmpXMax;
                            }
                            if (tmpYgeo022 < tmpYMin) {
                                tmpYgeo022 = tmpYMin;
                            }
                            int[] tmpColors2 = getTileColorsWithScale(dataset, tmpXCount * tmpScan2, j2 * tmpScan2, tmpXWidth, tmpScan2, tmpImgWidth, 256);
                            if (!(tmpColors2 == null || tmpColors2.length <= 0 || (tmpBitmap3 = Bitmap.createBitmap(tmpColors2, tmpImgWidth, 256, Bitmap.Config.ARGB_8888)) == null)) {
                                ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
                                tmpBitmap3.compress(Bitmap.CompressFormat.JPEG, CompressQuality, baos2);
                                tmpSQLiteDBHelper.ExecuteSQL("insert into L" + String.valueOf(k) + " (SYS_GEO,SYS_RC,MAPTYPE,LT_X,LT_Y,RB_X,RB_Y) values (?,'" + String.valueOf(j2) + "-" + String.valueOf(tmpXCount) + "','0'," + String.valueOf(tmpXgeo012) + "," + String.valueOf(tmpYgeo012) + "," + String.valueOf(tmpXgeo022) + "," + String.valueOf(tmpYgeo022) + ")", new Object[]{baos2.toByteArray()});
                            }
                            int tmpCurrentRatio2 = (tmpCurrentTid * 100) / tmpTotalTile;
                            if (!(myHandler == null || tmpCurrentRatio2 == tmpLastRatio || tmpCurrentRatio2 == 0 || tmpCurrentRatio2 >= 100)) {
                                Message msg3 = myHandler.obtainMessage();
                                msg3.what = 5;
                                long tmpNeedTime2 = (long) (((float) (System.currentTimeMillis() - tmpStartTime)) * ((100.0f / ((float) tmpCurrentRatio2)) - 1.0f));
                                if (tmpNeedTime2 < 0) {
                                    tmpNeedTime2 = 0;
                                }
                                msg3.obj = new Object[]{"开始压缩数据 [" + tmpCurrentTid + FileSelector_Dialog.sRoot + tmpTotalTile + "],剩余时间约" + Common.SimplifyTime(tmpNeedTime2), Integer.valueOf(tmpCurrentRatio2)};
                                myHandler.sendMessage(msg3);
                                if (progressDialog.isCancel) {
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                    dataset.delete();
                                    return true;
                                }
                                tmpLastRatio = tmpCurrentRatio2;
                            }
                        }
                    }
                    if (tmpYCount * 256 < iYsize) {
                        int tmpYWidth = iYsize - (tmpYCount * tmpScan2);
                        int tmpImgHeight = (tmpYWidth * 256) / tmpScan2;
                        for (int i5 = 0; i5 < tmpXCount; i5++) {
                            tmpCurrentTid++;
                            double tmpXgeo013 = geomatrix[0] + (geomatrix[1] * ((double) i5) * ((double) tmpScan2)) + (geomatrix[2] * ((double) tmpYCount) * ((double) tmpScan2));
                            double tmpYgeo013 = geomatrix[3] + (geomatrix[4] * ((double) i5) * ((double) tmpScan2)) + (geomatrix[5] * ((double) tmpYCount) * ((double) tmpScan2));
                            double tmpXgeo023 = geomatrix[0] + (geomatrix[1] * ((double) (i5 + 1)) * ((double) tmpScan2)) + (geomatrix[2] * ((double) (tmpYCount + 1)) * ((double) tmpScan2));
                            double tmpYgeo023 = geomatrix[3] + (geomatrix[4] * ((double) (i5 + 1)) * ((double) tmpScan2)) + (geomatrix[5] * ((double) (tmpYCount + 1)) * ((double) tmpScan2));
                            if (tmpXgeo023 > tmpXMax) {
                                tmpXgeo023 = tmpXMax;
                            }
                            if (tmpYgeo023 < tmpYMin) {
                                tmpYgeo023 = tmpYMin;
                            }
                            int[] tmpColors3 = getTileColorsWithScale(dataset, i5 * tmpScan2, tmpYCount * tmpScan2, tmpScan2, tmpYWidth, 256, tmpImgHeight);
                            if (!(tmpColors3 == null || tmpColors3.length <= 0 || (tmpBitmap2 = Bitmap.createBitmap(tmpColors3, 256, tmpImgHeight, Bitmap.Config.ARGB_8888)) == null)) {
                                ByteArrayOutputStream baos3 = new ByteArrayOutputStream();
                                tmpBitmap2.compress(Bitmap.CompressFormat.JPEG, CompressQuality, baos3);
                                tmpSQLiteDBHelper.ExecuteSQL("insert into L" + String.valueOf(k) + " (SYS_GEO,SYS_RC,MAPTYPE,LT_X,LT_Y,RB_X,RB_Y) values (?,'" + String.valueOf(tmpYCount) + "-" + String.valueOf(i5) + "','0'," + String.valueOf(tmpXgeo013) + "," + String.valueOf(tmpYgeo013) + "," + String.valueOf(tmpXgeo023) + "," + String.valueOf(tmpYgeo023) + ")", new Object[]{baos3.toByteArray()});
                            }
                            int tmpCurrentRatio3 = (tmpCurrentTid * 100) / tmpTotalTile;
                            if (!(myHandler == null || tmpCurrentRatio3 == tmpLastRatio || tmpCurrentRatio3 == 0 || tmpCurrentRatio3 >= 100)) {
                                Message msg4 = myHandler.obtainMessage();
                                msg4.what = 5;
                                long tmpNeedTime3 = (long) (((float) (System.currentTimeMillis() - tmpStartTime)) * ((100.0f / ((float) tmpCurrentRatio3)) - 1.0f));
                                if (tmpNeedTime3 < 0) {
                                    tmpNeedTime3 = 0;
                                }
                                msg4.obj = new Object[]{"开始压缩数据 [" + tmpCurrentTid + FileSelector_Dialog.sRoot + tmpTotalTile + "],剩余时间约" + Common.SimplifyTime(tmpNeedTime3), Integer.valueOf(tmpCurrentRatio3)};
                                myHandler.sendMessage(msg4);
                                if (progressDialog.isCancel) {
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                    dataset.delete();
                                    return true;
                                }
                                tmpLastRatio = tmpCurrentRatio3;
                            }
                        }
                    }
                    if (tmpXCount * 256 < iXsize && tmpYCount * 256 < iYsize) {
                        tmpCurrentTid++;
                        int tmpXWidth2 = iXsize - (tmpXCount * tmpScan2);
                        int tmpYWidth2 = iYsize - (tmpYCount * tmpScan2);
                        int tmpImgWidth2 = (tmpXWidth2 * 256) / tmpScan2;
                        int tmpImgHeight2 = (tmpYWidth2 * 256) / tmpScan2;
                        double tmpXgeo014 = geomatrix[0] + (geomatrix[1] * ((double) tmpXCount) * ((double) tmpScan2)) + (geomatrix[2] * ((double) tmpYCount) * ((double) tmpScan2));
                        double tmpYgeo014 = geomatrix[3] + (geomatrix[4] * ((double) tmpXCount) * ((double) tmpScan2)) + (geomatrix[5] * ((double) tmpYCount) * ((double) tmpScan2));
                        double tmpXgeo024 = geomatrix[0] + (geomatrix[1] * ((double) (tmpXCount + 1)) * ((double) tmpScan2)) + (geomatrix[2] * ((double) (tmpYCount + 1)) * ((double) tmpScan2));
                        double tmpYgeo024 = geomatrix[3] + (geomatrix[4] * ((double) (tmpXCount + 1)) * ((double) tmpScan2)) + (geomatrix[5] * ((double) (tmpYCount + 1)) * ((double) tmpScan2));
                        if (tmpXgeo024 > tmpXMax) {
                            tmpXgeo024 = tmpXMax;
                        }
                        if (tmpYgeo024 < tmpYMin) {
                            tmpYgeo024 = tmpYMin;
                        }
                        int[] tmpColors4 = getTileColorsWithScale(dataset, tmpXCount * tmpScan2, tmpYCount * tmpScan2, tmpXWidth2, tmpYWidth2, tmpImgWidth2, tmpImgHeight2);
                        if (!(tmpColors4 == null || tmpColors4.length <= 0 || (tmpBitmap = Bitmap.createBitmap(tmpColors4, tmpImgWidth2, tmpImgHeight2, Bitmap.Config.ARGB_8888)) == null)) {
                            ByteArrayOutputStream baos4 = new ByteArrayOutputStream();
                            tmpBitmap.compress(Bitmap.CompressFormat.JPEG, CompressQuality, baos4);
                            tmpSQLiteDBHelper.ExecuteSQL("insert into L" + String.valueOf(k) + " (SYS_GEO,SYS_RC,MAPTYPE,LT_X,LT_Y,RB_X,RB_Y) values (?,'" + String.valueOf(tmpYCount) + "-" + String.valueOf(tmpXCount) + "','0'," + String.valueOf(tmpXgeo014) + "," + String.valueOf(tmpYgeo014) + "," + String.valueOf(tmpXgeo024) + "," + String.valueOf(tmpYgeo024) + ")", new Object[]{baos4.toByteArray()});
                            int tmpCurrentRatio4 = (tmpCurrentTid * 100) / tmpTotalTile;
                            if (!(myHandler == null || tmpCurrentRatio4 == tmpLastRatio || tmpCurrentRatio4 == 0 || tmpCurrentRatio4 >= 100)) {
                                Message msg5 = myHandler.obtainMessage();
                                msg5.what = 5;
                                long tmpNeedTime4 = (long) (((float) (System.currentTimeMillis() - tmpStartTime)) * ((100.0f / ((float) tmpCurrentRatio4)) - 1.0f));
                                if (tmpNeedTime4 < 0) {
                                    tmpNeedTime4 = 0;
                                }
                                msg5.obj = new Object[]{"开始压缩数据 [" + tmpCurrentTid + FileSelector_Dialog.sRoot + tmpTotalTile + "],剩余时间约" + Common.SimplifyTime(tmpNeedTime4), Integer.valueOf(tmpCurrentRatio4)};
                                myHandler.sendMessage(msg5);
                                if (progressDialog.isCancel) {
                                    tmpSQLiteDBHelper.SetTransactionSuccessful();
                                    tmpSQLiteDBHelper.EndTransaction();
                                    dataset.delete();
                                    return true;
                                }
                                tmpLastRatio = tmpCurrentRatio4;
                            }
                        }
                    }
                }
            }
            tmpSQLiteDBHelper.SetTransactionSuccessful();
            tmpSQLiteDBHelper.EndTransaction();
            result = true;
        }
        dataset.delete();
        return result;
    }

    public static boolean ReadRasterFileInfo(String filePath, String[] outInfo) {
        boolean result = false;
        StringBuilder tmpStringBuilder = new StringBuilder();
        try {
            gdal.SetConfigOption("GDAL_FILENAME_IS_GBK", "YES");
            gdal.AllRegister();
            Dataset dataset = gdal.Open(filePath, gdalconstConstants.GA_ReadOnly);
            if (dataset == null) {
                if (outInfo != null && outInfo.length > 1) {
                    outInfo[0] = "数据文件错误.";
                }
                return false;
            }
            double[] geomatrix = dataset.GetGeoTransform();
            int iXsize = dataset.GetRasterXSize();
            int iYsize = dataset.GetRasterYSize();
            tmpStringBuilder.append("行数:" + String.valueOf(iXsize));
            tmpStringBuilder.append("  列数:" + String.valueOf(iYsize));
            int tmpBandCount = dataset.GetRasterCount();
            tmpStringBuilder.append("\r\n波段数:" + String.valueOf(tmpBandCount));
            if (tmpBandCount > 0) {
                String tmpPrjInfoString = dataset.GetProjectionRef();
                if (tmpPrjInfoString == null || tmpPrjInfoString.trim().length() <= 0) {
                    tmpStringBuilder.append("\r\n无坐标系信息.");
                } else {
                    WKTCoordinateSystem tmpWKTCoordinateSystem = new WKTCoordinateSystem();
                    tmpWKTCoordinateSystem.readWKTString(tmpPrjInfoString);
                    tmpStringBuilder.append("\r\n坐标系:" + tmpWKTCoordinateSystem.Name);
                    double tmpXMin = geomatrix[0];
                    double tmpXMax = geomatrix[0] + (geomatrix[1] * ((double) iXsize));
                    double tmpYMax = geomatrix[3];
                    double tmpYMin = geomatrix[3] + (geomatrix[5] * ((double) iYsize));
                    tmpStringBuilder.append("\r\nXMin:" + String.valueOf(tmpXMin));
                    tmpStringBuilder.append("\r\nXMax:" + String.valueOf(tmpXMax));
                    tmpStringBuilder.append("\r\nYMin:" + String.valueOf(tmpYMin));
                    tmpStringBuilder.append("\r\nYMax:" + String.valueOf(tmpYMax));
                    result = true;
                }
            }
            if (outInfo != null && outInfo.length > 0) {
                outInfo[0] = tmpStringBuilder.toString();
            }
            return result;
        } catch (Exception e) {
        }
        return result;
    }
}
