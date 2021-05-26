package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Carto.Layer.EGeoLayerType;
import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import  com.xzy.forestSystem.gogisapi.Geometry.ExtraGeometry;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.IOException;
import java.util.Iterator;

public class IndexFileLoader {
    DataSource m_ToDataSource = null;

    public IndexFileLoader(DataSource paramDataSource) {
        this.m_ToDataSource = paramDataSource;
    }

    private void ExtraByteData(byte[] paramArrayOfByte, String paramString) {
        int i = paramArrayOfByte.length;
        DataSet localDataset = new DataSet(this.m_ToDataSource);
        this.m_ToDataSource.getDatasets().add(localDataset);
        localDataset.setName(this.m_ToDataSource + FileSelector_Dialog.sFolder + paramString);
        String str = paramString.substring(paramString.length() - 1, paramString.length());
        if (str.equals("0")) {
            localDataset.setType(EGeoLayerType.POINT);
        }
        if (str.equals("1")) {
            localDataset.setType(EGeoLayerType.POLYLINE);
        }
        if (str.equals("2")) {
            localDataset.setType(EGeoLayerType.POLYGON);
        }
        if (0 != i) {
            ExtraGeometry localExtraGeometry = new ExtraGeometry();
            localDataset.AddExtraGeometry(localExtraGeometry);
            localExtraGeometry.setDIndex(BitConverter.ToInt(BitConverter.Reverse(paramArrayOfByte, 0, 4)));
            int k = 0 + 4;
            int j = k + 4;
            for (int i1 = 1; i1 <= BitConverter.ToInt(BitConverter.Reverse(paramArrayOfByte, k, 4)); i1++) {
                j += 4;
            }
            if (localDataset.getType() != EGeoLayerType.POINT) {
                int i3 = 1;
                while (true) {
                    if (i3 <= 4) {
                    }
                    double d1 = BitConverter.ToDouble(paramArrayOfByte, j);
                    j += 8;
                    if (i3 == 1) {
                        localExtraGeometry.setMinX(d1);
                    }
                    if (i3 == 2) {
                        localExtraGeometry.setMinY(d1);
                    }
                    if (i3 == 3) {
                        localExtraGeometry.setMaxX(d1);
                    }
                    if (i3 == 4) {
                        localExtraGeometry.setMaxY(d1);
                    }
                    i3++;
                }
            } else {
                int i4 = 1;
                while (true) {
                    if (i4 <= 2) {
                    }
                    double d2 = BitConverter.ToDouble(paramArrayOfByte, j);
                    j += 8;
                    if (i4 == 1) {
                        localExtraGeometry.setMinX(d2);
                    }
                    if (i4 == 2) {
                        localExtraGeometry.setMinY(d2);
                    }
                    i4++;
                }
            }
        }
    }

    public boolean ReadIndexData(String paramString) {
        try {
            IndexFile localFilePack = new IndexFile(paramString);
            Iterator localIterator = localFilePack.ReadIndex().iterator();
            if (!localIterator.hasNext()) {
                localFilePack.Dispose();
                return true;
            }
            String[] arrayOfString = localIterator.toString().split(",");
            int i = Integer.valueOf(arrayOfString[1]).intValue();
            int j = Integer.valueOf(arrayOfString[2]).intValue();
            localFilePack.getReader().seek((long) i);
            byte[] arrayOfByte = new byte[(j - i)];
            localFilePack.getReader().read(arrayOfByte);
            ExtraByteData(arrayOfByte, arrayOfString[0]);
            System.gc();
            return false;
        } catch (IOException e) {
        }
        return false;
    }
}
