package  com.xzy.forestSystem.stub.plugin;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import java.util.HashMap;
import java.util.Map;

public class Stub04 extends ContentProvider {
    private Map<String, BusiItem> delegates = new HashMap();

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        Object invoke;
        try {
            BusiItem busiItem = new BusiItem();
            busiItem.setDelegateClz(Class.forName(" com.xzy.forestSystem.stub.stub02.ImplProvider"));
            if (busiItem.getDelegateClz() != null) {
                try {
                    busiItem.setDelegateImpl(busiItem.getDelegateClz().newInstance());
                    this.delegates.put("pull", busiItem);
                    if (!(busiItem.getDelegateImpl() == null || busiItem.getDelegateClz() == null || (invoke = ReflectionUtil.invoke(busiItem.getDelegateImpl(), ReflectionUtil.getMethod(busiItem.getDelegateClz(), "onCreate", ContentProvider.class), this)) == null)) {
                        return ((Boolean) invoke).booleanValue();
                    }
                } catch (IllegalAccessException | InstantiationException e) {
                }
            }
        } catch (Throwable th) {
        }
        return false;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        try {
            for (BusiItem busiItem : this.delegates.values()) {
                ReflectionUtil.invoke(busiItem.getDelegateImpl(), ReflectionUtil.getMethod(busiItem.getDelegateClz(), "getType", Uri.class), uri);
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        try {
            for (BusiItem busiItem : this.delegates.values()) {
                ReflectionUtil.invoke(busiItem.getDelegateImpl(), ReflectionUtil.getMethod(busiItem.getDelegateClz(), "insert", Uri.class, ContentValues.class), uri, contentValues);
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
