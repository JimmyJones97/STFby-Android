package  com.xzy.forestSystem.mob.commons;

import  com.xzy.forestSystem.mob.MobSDK;
import  com.xzy.forestSystem.mob.tools.MobLog;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;
import  com.xzy.forestSystem.mob.tools.utils.DeviceHelper;
import  com.xzy.forestSystem.mob.tools.utils.ReflectHelper;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class MobProductCollector implements PublicMemberKeeper {

    /* renamed from: a */
    private static final String[] f311a = {"SHARESDK", "SMSSDK", "SHAREREC", "MOBAPI", "MOBLINK", "UMSSDK", "CMSSDK", "BBSSDK"};

    /* renamed from: b */
    private static final HashMap<String, MobProduct> f312b = new HashMap<>();

    public static final synchronized void registerProduct(MobProduct mobProduct) {
        synchronized (MobProductCollector.class) {
            if (mobProduct != null) {
                if (!f312b.containsKey(mobProduct.getProductTag())) {
                    f312b.put(mobProduct.getProductTag(), mobProduct);
                }
            }
        }
    }

    public static final synchronized ArrayList<MobProduct> getProducts() {
        ArrayList<MobProduct> arrayList;
        synchronized (MobProductCollector.class) {
            try {
                ReflectHelper.importClass(" com.xzy.forestSystem.mob.commons.*");
                for (String str : f311a) {
                    try {
                        MobProduct mobProduct = (MobProduct) ReflectHelper.newInstance(str, new Object[0]);
                        if (mobProduct != null) {
                            f312b.put(mobProduct.getProductTag(), mobProduct);
                        }
                    } catch (Throwable th) {
                    }
                }
                for (int i = 1; i <= 128; i++) {
                    try {
                        MobProduct mobProduct2 = (MobProduct) ReflectHelper.newInstance("MobProduct" + i, new Object[0]);
                        if (mobProduct2 != null) {
                            f312b.put(mobProduct2.getProductTag(), mobProduct2);
                        }
                    } catch (Throwable th2) {
                    }
                }
            } catch (Throwable th3) {
                MobLog.getInstance().m57w(th3);
            }
            arrayList = new ArrayList<>();
            for (Map.Entry<String, MobProduct> entry : f312b.entrySet()) {
                arrayList.add(entry.getValue());
            }
        }
        return arrayList;
    }

    public static final synchronized String getUserIdentity(ArrayList<MobProduct> arrayList) {
        String str;
        String str2;
        synchronized (MobProductCollector.class) {
            try {
                DeviceHelper instance = DeviceHelper.getInstance(MobSDK.getContext());
                String str3 = instance.getPackageName() + FileSelector_Dialog.sRoot + instance.getAppVersionName();
                String str4 = "CLV/1";
                int size = arrayList.size();
                int i = 0;
                while (i < size) {
                    try {
                        MobProduct mobProduct = arrayList.get(i);
                        str2 = str4 + " " + mobProduct.getProductTag() + FileSelector_Dialog.sRoot + mobProduct.getSdkver();
                    } catch (Throwable th) {
                        str2 = str4;
                    }
                    i++;
                    str4 = str2;
                }
                str = str3 + " " + str4 + " " + ("Android/" + instance.getOSVersionInt()) + " " + TimeZone.getDefault().getID() + " " + ("Lang/" + Locale.getDefault().toString().replace("-r", "-"));
            } catch (Throwable th2) {
                MobLog.getInstance().m57w(th2);
                str = "";
            }
        }
        return str;
    }
}
