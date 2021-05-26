package  com.xzy.forestSystem.mob.commons;

public class SMSSDK implements MobProduct {
    @Override //  com.xzy.forestSystem.mob.commons.MobProduct
    public String getProductTag() {
        return "SMSSDK";
    }

    @Override //  com.xzy.forestSystem.mob.commons.MobProduct
    public int getSdkver() {
        return 30000;
    }
}
