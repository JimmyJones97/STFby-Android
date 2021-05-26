package  com.xzy.forestSystem.gogisapi.System;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.BasicValue;
import com.xzy.forestSystem.gogisapi.Common.Common;
import java.io.File;
import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class AuthorizeTools {
    private static final String ENCRYPT_KEY = "XZYWHU2014MILLIONTEC2015XZY";
    static final String EXPAND_LAYER_KEY = "XZYWEARETHEWORLD2015";
    private static final String HEX = "0123456789ABCDEF";
    static final String REGISTERCIDE2 = "uGd3A8d!phX";
    static final String REGISTERCODE = "DhZ@igry0234X!hS";
    private static int m_PromptCount = 6;
    boolean isFirst = true;
    long m_ExpireDate = 0;
    public boolean m_HasNetReg = false;
    private boolean m_IsExpired = false;
    private Handler m_LSUserHandler = null;
    private Handler m_MyHandler = new Handler() { // from class:  com.xzy.forestSystem.gogisapi.System.AuthorizeTools.2
        @Override // android.os.Handler
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                Common.ShowToast(String.valueOf(msg.obj));
            } else if (msg.what == 1) {
                Common.ShowDialog(String.valueOf(msg.obj));
            }
        }
    };
    private int m_NetRegLimitHour = 0;
    private int m_PromptPerTimer = 300000;
    private String m_RegisterCode = "";
    private String m_RegisterInfo = "";
    protected int m_RegisterMode = 0;
    private String m_UserAndroidID = null;
    private Runnable m_runnable = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.System.AuthorizeTools.1
        /* JADX WARNING: Code restructure failed: missing block: B:20:0x0049, code lost:
            if (r18.this$0.isFirst != false) goto L_0x004b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:22:0x004d, code lost:
            if (com.xzy.forestSystem.PubVar.m_Callback != null) goto L_0x004f;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:23:0x004f, code lost:
            com.xzy.forestSystem.PubVar.m_Callback.OnClick("正常进入", null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:24:0x0057, code lost:
            r18.this$0.isFirst = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:72:0x016c, code lost:
            r12 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:76:0x0173, code lost:
            if (r18.this$0.isFirst != false) goto L_0x0175;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:78:0x0177, code lost:
            if (com.xzy.forestSystem.PubVar.m_Callback != null) goto L_0x0179;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x0179, code lost:
            com.xzy.forestSystem.PubVar.m_Callback.OnClick("正常进入", null);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:80:0x0181, code lost:
            r18.this$0.isFirst = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:87:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:91:?, code lost:
            return;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:92:?, code lost:
            return;
         */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Removed duplicated region for block: B:72:0x016c A[ExcHandler: all (r12v0 'th' java.lang.Throwable A[CUSTOM_DECLARE]), Splitter:B:1:0x0002] */
        @Override // java.lang.Runnable
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            /*
            // Method dump skipped, instructions count: 401
            */
            throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.gogisapi.System.AuthorizeTools.RunnableC08031.run():void");
        }
    };

    public AuthorizeTools() {
        String tempKeys = "";
        try {
            SharedPreferences preParas = PreferenceManager.getDefaultSharedPreferences(PubVar.MainContext);
            tempKeys = preParas.getString("Values", "");
            if (tempKeys == null || tempKeys.equals("")) {
                tempKeys = preParas.getString("NetRegInfo", "");
            }
        } catch (Exception e) {
        }
        if (tempKeys == null || tempKeys.equals("")) {
            try {
                HashMap<String, String> tempHashMap = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_LIC");
                if (tempHashMap != null) {
                    tempKeys = tempHashMap.get("F2");
                }
            } catch (Exception e2) {
            }
            if (tempKeys == null || tempKeys.equals("")) {
                try {
                    HashMap<String, String> tempHashMap2 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_LIC");
                    if (tempHashMap2 != null) {
                        tempKeys = tempHashMap2.get("F2");
                    }
                } catch (Exception e3) {
                }
            }
            if (tempKeys == null || tempKeys.equals("")) {
                try {
                    HashMap<String, String> tempHashMap3 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_LIC");
                    if (tempHashMap3 != null) {
                        tempKeys = tempHashMap3.get("F2");
                    }
                } catch (Exception e4) {
                }
            }
            if (tempKeys == null || tempKeys.equals("")) {
                try {
                    HashMap<String, String> tempHashMap4 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_UserInfo");
                    if (tempHashMap4 != null) {
                        tempKeys = tempHashMap4.get("F2");
                    }
                } catch (Exception e5) {
                }
            }
            if (tempKeys == null || tempKeys.equals("")) {
                try {
                    HashMap<String, String> tempHashMap5 = PubVar._PubCommand.m_UserConfigDB.GetUserParam().GetUserPara("Tag_System_UserInfo");
                    if (tempHashMap5 != null) {
                        tempKeys = tempHashMap5.get("F2");
                    }
                } catch (Exception e6) {
                }
            }
        }
        if (tempKeys != null && !tempKeys.equals("")) {
            String[] tempOutMsg = new String[1];
            CheckRegister(tempKeys, tempOutMsg);
            if (this.m_RegisterMode == 0 && !this.m_HasNetReg) {
                Common.ShowToast("提示:" + tempOutMsg[0]);
            }
        }
        this.m_LSUserHandler = new Handler();
        this.m_LSUserHandler.postDelayed(this.m_runnable, 1000);
    }

    public void CheckRegister(String registerKey, String[] outMsg) {
        this.m_RegisterMode = 0;
        outMsg[0] = "";
        if (registerKey != null && registerKey.length() > 0) {
            this.m_HasNetReg = false;
            if (registerKey.contains("NETREGISTER")) {
                NetUserConnectServer();
                this.m_HasNetReg = true;
            }
            if (!this.m_HasNetReg) {
                try {
                    String[] tempStrs001 = new String[2];
                    if (DeCollapseString(registerKey, tempStrs001)) {
                        String[] tempStrs = new String[2];
                        if (DeCollapseString(new StringBuilder(decrypt(REGISTERCIDE2 + tempStrs001[1], tempStrs001[0])).reverse().toString(), tempStrs)) {
                            String tempCode = decrypt(REGISTERCODE + tempStrs[1], tempStrs[0]);
                            String tempRegName = GetRegisterInfo();
                            String[] tempStrs01 = tempCode.split(",");
                            String[] tempStrs02 = tempRegName.split(",");
                            if (tempStrs01 == null || tempStrs02 == null || tempStrs01.length <= 5 || tempStrs02.length <= 4 || !tempStrs01[0].equals(tempStrs02[0]) || !tempStrs01[1].equals(tempStrs02[1]) || !tempStrs01[2].equals(tempStrs02[2]) || !tempStrs01[3].equals(tempStrs02[3])) {
                                outMsg[0] = "注册信息不正确.";
                                return;
                            }
                            this.m_ExpireDate = Long.parseLong(tempStrs01[4]);
                            long date = PubVar.CurrentNetTime.getTime();
                            long tmpLastLogTime = Common.GetLastRecordTime();
                            if (this.m_ExpireDate <= date || this.m_ExpireDate <= tmpLastLogTime) {
                                this.m_IsExpired = true;
                                outMsg[0] = "超过使用期限.";
                                return;
                            }
                            String tempAllowVersion = tempStrs01[5];
                            if (tempAllowVersion.contains(PubVar.Version) || tempAllowVersion.contains("MYALLVERSION")) {
                                this.m_RegisterMode = 2;
                                outMsg[0] = Common.dateFormat.format(new Date(this.m_ExpireDate));
                                return;
                            }
                            outMsg[0] = "授权码不能用于该版本,请与开发商联系.";
                        }
                    }
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    public void setRegisterMode(int mode) {
        this.m_RegisterMode = mode;
    }

    public int getRegisterMode() {
        return this.m_RegisterMode;
    }

    public long GetExpireDate() {
        return this.m_ExpireDate;
    }

    public String GetRegisterInfo() {
        try {
            if (this.m_RegisterInfo.equals("") || this.m_RegisterCode.equals("")) {
                long date = 0;
                try {
                    List<ApplicationInfo> installedAppList = PubVar.MainContext.getPackageManager().getInstalledApplications(8192);
                    if (installedAppList != null) {
                        Iterator<ApplicationInfo> it = installedAppList.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                ApplicationInfo appInfo = it.next();
                                if (appInfo.processName.contains("com.stczh.")) {
                                    date = new Date(new File(appInfo.sourceDir).lastModified()).getTime();
                                    break;
                                }
                            } else {
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                }
                if (date == 0) {
                    date = new Date().getTime();
                }
                StringBuilder tempSB = new StringBuilder();
                tempSB.append(String.valueOf(Build.BRAND));
                tempSB.append(",");
                tempSB.append(String.valueOf(Build.CPU_ABI));
                tempSB.append(",");
                tempSB.append(String.valueOf(Build.MANUFACTURER));
                try {
                    this.m_UserAndroidID = Settings.Secure.getString(PubVar.MainContext.getContentResolver(), "android_id");
                    if (this.m_UserAndroidID == null) {
                        this.m_UserAndroidID = "ABCDEGHIJKLMNOP";
                    }
                    ((TelephonyManager) PubVar.MainContext.getSystemService("phone")).getDeviceId();
                    tempSB.append(",");
                    tempSB.append(this.m_UserAndroidID);
                } catch (Exception e2) {
                }
                tempSB.append(",");
                tempSB.append(date);
                tempSB.append(",");
                tempSB.append(getDeviceIMEI());
                tempSB.append(",");
                tempSB.append(String.valueOf(Build.VERSION.SDK_INT));
                String tempKey = REGISTERCODE + this.m_UserAndroidID;
                try {
                    this.m_RegisterInfo = tempSB.toString();
                    this.m_RegisterCode = encrypt(tempKey, this.m_RegisterInfo);
                    this.m_RegisterCode = String.valueOf(CollapseString(this.m_RegisterCode, this.m_UserAndroidID)) + String.valueOf(Build.VERSION.SDK_INT);
                } catch (Exception e3) {
                }
            }
        } catch (Exception e4) {
        }
        return this.m_RegisterInfo;
    }

    public String GetRegisterCode() {
        GetRegisterInfo();
        return this.m_RegisterCode;
    }

    public String getUserAndroidID() {
        if (this.m_UserAndroidID == null) {
            this.m_UserAndroidID = Settings.Secure.getString(PubVar.MainContext.getContentResolver(), "android_id");
            if (this.m_UserAndroidID == null) {
                this.m_UserAndroidID = "ABCDEGHIJKLMNOP";
            }
        }
        return this.m_UserAndroidID;
    }

    public String getDeviceIMEI() {
        try {
            return ((TelephonyManager) PubVar.MainContext.getSystemService("phone")).getDeviceId();
        } catch (Exception e) {
            return "";
        }
    }

    private String CollapseString(String value, String value2) {
        StringBuilder tempSB = new StringBuilder();
        int tempLen = value.length() / value2.length();
        int i = 0;
        int tid01 = 0;
        int tid02 = 0;
        for (int tmpTotoal = value.length() + value2.length(); tmpTotoal > 0; tmpTotoal--) {
            if (i == tempLen) {
                tempSB.append(value2.charAt(tid02));
                tid02++;
                i = 0;
            } else {
                tempSB.append(value.charAt(tid01));
                tid01++;
                i++;
            }
        }
        tempSB.append(String.format("%03x", Integer.valueOf(value.length())));
        return tempSB.toString();
    }

    private boolean DeCollapseString(String code, String[] outValues) {
        try {
            int tempTotalLen = code.length();
            int tempValueLen = Integer.valueOf(String.valueOf(String.valueOf(code.charAt(tempTotalLen - 3))) + String.valueOf(code.charAt(tempTotalLen - 2)) + String.valueOf(code.charAt(tempTotalLen - 1)), 16).intValue();
            int tempTotalLen2 = tempTotalLen - 3;
            StringBuilder tempSB01 = new StringBuilder();
            StringBuilder tempSB02 = new StringBuilder();
            int tempLen = tempValueLen / (tempTotalLen2 - tempValueLen);
            int i = 0;
            int j = 0;
            while (tempTotalLen2 > 0) {
                if (i == tempLen) {
                    tempSB02.append(code.charAt(j));
                    i = 0;
                } else {
                    tempSB01.append(code.charAt(j));
                    i++;
                }
                tempTotalLen2--;
                j++;
            }
            outValues[0] = tempSB01.toString();
            outValues[1] = tempSB02.toString();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String encrypt(String seed, String cleartext) throws Exception {
        return AESUtils.des(cleartext, seed, 1);
    }

    public static String decrypt(String seed, String encrypted) throws Exception {
        return AESUtils.des(encrypted, seed, 2);
    }

    public static String DecryptExpandLayerKey(String layerPassword) {
        try {
            return decrypt(EXPAND_LAYER_KEY, layerPassword);
        } catch (Exception e) {
            return "";
        }
    }

    public static String EncryptExpandLayerKey(String layerPassword) {
        try {
            return encrypt(EXPAND_LAYER_KEY, layerPassword);
        } catch (Exception e) {
            return "";
        }
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        SecureRandom sr;
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        if (Build.VERSION.SDK_INT >= 17) {
            sr = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        } else {
            sr = SecureRandom.getInstance("SHA1PRNG");
        }
        sr.setSeed(seed);
        kgen.init(128, sr);
        return kgen.generateKey().getEncoded();
    }

    private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(1, skeySpec);
        return cipher.doFinal(clear);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(2, skeySpec);
        return cipher.doFinal(encrypted);
    }

    public static String toHex(String txt) {
        return toHex(txt.getBytes());
    }

    public static String fromHex(String hex) {
        return new String(toByte(hex));
    }

    public static byte[] toByte(String hexString) {
        int len = hexString.length() / 2;
        byte[] result = new byte[len];
        for (int i = 0; i < len; i++) {
            result[i] = Integer.valueOf(hexString.substring(i * 2, (i * 2) + 2), 16).byteValue();
        }
        return result;
    }

    public static String toHex(byte[] buf) {
        if (buf == null) {
            return "";
        }
        StringBuffer result = new StringBuffer(buf.length * 2);
        for (byte b : buf) {
            appendHex(result, b);
        }
        return result.toString();
    }

    private static void appendHex(StringBuffer sb, byte b) {
        sb.append(HEX.charAt((b >> 4) & 15)).append(HEX.charAt(b & 15));
    }

    public static String EncryptPrjPwd(String pwd, boolean isLocal) {
        if (!isLocal) {
//            return encrypt(ENCRYPT_KEY, "A" + pwd);
        }
        try {
            return encrypt(ENCRYPT_KEY, "B[" + PubVar.m_AuthorizeTools.getUserAndroidID() + "];" + pwd);
        } catch (Exception e) {
            e.printStackTrace();
            return pwd;
        }
    }

    public static boolean DecryptPrjPwd(String code, BasicValue outMsg, BasicValue pwd) {
        try {
            String tmpKey = decrypt(ENCRYPT_KEY, code);
            if (tmpKey != null && !tmpKey.equals("")) {
                if (tmpKey.substring(0, 1).equals("A")) {
                    pwd.setValue(tmpKey.substring(1));
                    outMsg.setValue("PUBLIC");
                    return true;
                } else if (tmpKey.substring(0, 1).equals("B")) {
                    String tmpStr = tmpKey.substring(1);
                    int tmpInt = tmpStr.indexOf("];");
                    String tmpDevice = "";
                    if (tmpInt >= 0) {
                        tmpDevice = tmpStr.substring(0, tmpInt);
                    }
                    if (tmpDevice.contains(PubVar.m_AuthorizeTools.getUserAndroidID())) {
                        pwd.setValue(tmpStr.substring(tmpInt + 2));
                        outMsg.setValue("LOCAL");
                        return true;
                    }
                    outMsg.setValue("该项目数据不能在本设备上使用.");
                }
            }
        } catch (Exception e) {
            Common.Log("错误", "项目数据解密时:" + e.getMessage());
        }
        return false;
    }

    public void NetUserConnectServer() {
        try {
            new Thread() { // from class:  com.xzy.forestSystem.gogisapi.System.AuthorizeTools.3
                @Override // java.lang.Thread, java.lang.Runnable
                public void run() {
                    try {
                        HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(String.valueOf(PubVar.ServerURL) + "RegisterHandler.ashx?method=vertify&device=" + PubVar.m_AuthorizeTools.getUserAndroidID()));
                        if (httpResponse.getStatusLine().getStatusCode() == 200) {
                            JSONObject jsonObject = new JSONObject(EntityUtils.toString(httpResponse.getEntity()));
                            boolean result = jsonObject.getBoolean("success");
                            String tmpMsg = jsonObject.getString("msg");
                            if (result) {
                                AuthorizeTools.this.setRegisterMode(1);
                                String[] tmpStrs = tmpMsg.split(";");
                                if (tmpStrs == null || tmpStrs.length <= 1) {
                                    Message msg = AuthorizeTools.this.m_MyHandler.obtainMessage();
                                    msg.what = 0;
                                    msg.obj = tmpMsg;
                                    AuthorizeTools.this.m_MyHandler.sendMessage(msg);
                                    AuthorizeTools.this.m_NetRegLimitHour = 1;
                                } else {
                                    Message msg2 = AuthorizeTools.this.m_MyHandler.obtainMessage();
                                    msg2.what = 0;
                                    msg2.obj = tmpStrs[0];
                                    AuthorizeTools.this.m_MyHandler.sendMessage(msg2);
                                    AuthorizeTools.this.m_NetRegLimitHour = Integer.parseInt(tmpStrs[1]);
                                }
                                PubVar._PubCommand.m_UserConfigDB.GetUserParam().SaveUserPara("Tag_System_UserInfo_ConnectTime", String.valueOf(new Date().getTime()));
                                return;
                            }
                            AuthorizeTools.this.setRegisterMode(0);
                            Message msg3 = AuthorizeTools.this.m_MyHandler.obtainMessage();
                            msg3.what = 1;
                            msg3.obj = tmpMsg;
                            AuthorizeTools.this.m_MyHandler.sendMessage(msg3);
                            AuthorizeTools.this.m_NetRegLimitHour = 0;
                        }
                    } catch (Exception e) {
                    }
                }
            }.start();
        } catch (Exception e) {
        }
    }

    public int getNetRegLimitHour() {
        return this.m_NetRegLimitHour;
    }
}
