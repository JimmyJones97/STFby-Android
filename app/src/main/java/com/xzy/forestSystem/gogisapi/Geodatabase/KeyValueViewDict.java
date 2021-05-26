package  com.xzy.forestSystem.gogisapi.Geodatabase;

import android.view.View;

public class KeyValueViewDict {
    public String DataKey = "";
    public String Key = "";
    public String Value = "";
    public View ViewControl = null;

    public KeyValueViewDict(String paramString, View paramView) {
        this.DataKey = paramString;
        this.Key = paramString;
        this.Value = "";
        this.ViewControl = paramView;
    }

    public KeyValueViewDict(String paramString1, String paramString2, View paramView) {
        this.DataKey = paramString1;
        this.Key = paramString1;
        this.Value = paramString2;
        this.ViewControl = paramView;
    }

    public KeyValueViewDict(String dataKey, String key, String value, View paramView) {
        this.DataKey = dataKey;
        this.Key = key;
        this.Value = value;
        this.ViewControl = paramView;
    }
}
