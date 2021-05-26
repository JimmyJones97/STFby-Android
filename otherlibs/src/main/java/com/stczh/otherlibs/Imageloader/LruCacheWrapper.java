package com.stczh.otherlibs.Imageloader;

import android.graphics.Bitmap;
import android.util.LruCache;

public class LruCacheWrapper {
    private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(((int) Runtime.getRuntime().maxMemory()) / 4) { // from class: com.stczh.otherlibs.Imageloader.LruCacheWrapper.1
        /* access modifiers changed from: protected */
        public int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight();
        }
    };

    public Bitmap get(String key) {
        return this.cache.get(key);
    }

    public void put(String key, Bitmap bitmap) {
        if (this.cache.get(key) != null && bitmap != null) {
            this.cache.put(key, bitmap);
        }
    }

    public void clear() {
    }
}
