package  com.xzy.forestSystem.gogisapi.GPS;

import android.content.Context;
import com.xzy.forestSystem.PubVar;
import java.util.HashMap;
import java.util.List;

public class GPSDevice {
    public String deviceName = "";
    public GPSLocationClass gpsLocation = null;
    public boolean isDisposed = false;
    public List<SatelliteInfo> satellites = null;

    public GPSDevice(GPSLocationClass gpsLocation2) {
        this.gpsLocation = gpsLocation2;
        if (this.gpsLocation != null) {
            this.gpsLocation.setGPSOpenClose(true);
        }
    }

    public void startConnect() {
    }

    public void StopGPSDevice() {
        if (this.gpsLocation != null) {
            this.gpsLocation.setGPSOpenClose(false);
        }
    }

    public static GPSDevice OpenGPSDevice(GPSLocationClass gpsLocation2) {
        String tempStr;
        String tempStr2;
        GPSDevice result = null;
        boolean tempBool = true;
        HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
        if (!(tempHashMap == null || (tempStr = tempHashMap.get("F3")) == null || !tempStr.equals("1") || (tempStr2 = tempHashMap.get("F4")) == null || tempStr2.equals(""))) {
            tempBool = false;
            result = new BluetoothGPSDevice(PubVar._PubCommand.m_Context, gpsLocation2);
        }
        if (tempBool) {
            result = new LocalGPSDevice(PubVar._PubCommand.m_Context, gpsLocation2);
        }
        if (result != null) {
            result.startConnect();
        }
        return result;
    }

    public static GPSDevice OpenGPSDevice(Context context, GPSLocationClass gpsLocation2) {
        String tempStr;
        String tempStr2;
        GPSDevice result = null;
        boolean tempBool = true;
        HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_GPS");
        if (!(tempHashMap == null || (tempStr = tempHashMap.get("F3")) == null || !tempStr.equals("1") || (tempStr2 = tempHashMap.get("F4")) == null || tempStr2.equals(""))) {
            tempBool = false;
            result = new BluetoothGPSDevice(context, gpsLocation2);
        }
        if (tempBool) {
            result = new LocalGPSDevice(context, gpsLocation2);
        }
        if (result != null) {
            result.startConnect();
        }
        return result;
    }
}
