package  com.xzy.forestSystem.gogisapi.Geodatabase;

import  com.xzy.forestSystem.gogisapi.Common.BitConverter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

public class IndexFile {
    RandomAccessFile m_BinaryRead = null;

    public IndexFile(String paramString) {
        try {
            this.m_BinaryRead = new RandomAccessFile(new File(paramString), "r");
        } catch (FileNotFoundException e) {
            try {
                this.m_BinaryRead.seek(0);
            } catch (IOException localIOException) {
                localIOException.printStackTrace();
            }
        }
    }

    public void Dispose() throws IOException {
        getReader().close();
    }

    public List<String> ReadIndex() {
        ArrayList localArrayList = new ArrayList();
        try {
            getReader().seek(0);
            if (getReader().getFilePointer() < getReader().length()) {
                byte[] arrayOfByte1 = new byte[getReader().read()];
                getReader().read(arrayOfByte1);
                String str = new String(arrayOfByte1);
                byte[] arrayOfByte2 = new byte[4];
                getReader().read(arrayOfByte2);
                int i = BitConverter.ToInt(BitConverter.Reverse(arrayOfByte2), 0);
                byte[] arrayOfByte3 = new byte[4];
                getReader().read(arrayOfByte3);
                int j = BitConverter.ToInt(BitConverter.Reverse(arrayOfByte3), 0);
                getReader().seek((long) j);
                localArrayList.add(String.valueOf(str) + "," + String.valueOf(i) + "," + String.valueOf(j));
            }
        } catch (Exception localException) {
            localException.getMessage();
        }
        return localArrayList;
    }

    public RandomAccessFile getReader() {
        return this.m_BinaryRead;
    }
}
