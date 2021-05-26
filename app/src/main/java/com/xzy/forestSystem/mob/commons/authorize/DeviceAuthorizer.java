package  com.xzy.forestSystem.mob.commons.authorize;

import  com.xzy.forestSystem.mob.commons.CommonConfig;
import  com.xzy.forestSystem.mob.commons.MobProduct;
import  com.xzy.forestSystem.mob.commons.MobProductCollector;
import  com.xzy.forestSystem.mob.tools.proguard.PublicMemberKeeper;

public final class DeviceAuthorizer implements PublicMemberKeeper {
    public static synchronized String authorize(MobProduct mobProduct) {
        String a;
        synchronized (DeviceAuthorizer.class) {
            MobProductCollector.registerProduct(mobProduct);
            Authorizer aVar = new Authorizer();
            if (mobProduct == null || !CommonConfig.m236h()) {
                a = aVar.m210a(false);
            } else {
                a = aVar.m217a(mobProduct);
            }
        }
        return a;
    }
}
