package com.xzy.forestSystem.mob.tools.utils;

import android.text.TextUtils;
import  com.xzy.forestSystem.mob.tools.MobLog;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Hashon {
    public <T> HashMap<String, T> fromJson(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return new HashMap<>();
        }
        try {
            if (jsonStr.startsWith("[") && jsonStr.endsWith("]")) {
                jsonStr = "{\"fakelist\":" + jsonStr + "}";
            }
            return fromJson(new JSONObject(jsonStr));
        } catch (Throwable t) {
            MobLog.getInstance().m58w(jsonStr, new Object[0]);
            MobLog.getInstance().m57w(t);
            return new HashMap<>();
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.util.HashMap<java.lang.String, T> */
    /* JADX WARN: Multi-variable type inference failed */
    private <T> HashMap<String, T> fromJson(JSONObject json) throws JSONException {
        HashMap<String, T> map = (HashMap<String, T>) new HashMap();
        Iterator<String> iKey = json.keys();
        while (iKey.hasNext()) {
            String key = iKey.next();
            Object value = json.opt(key);
            if (JSONObject.NULL.equals(value)) {
                value = null;
            }
            if (value != null) {
                if (value instanceof JSONObject) {
                    value = fromJson((JSONObject) value);
                } else if (value instanceof JSONArray) {
                    value = fromJson((JSONArray) value);
                }
                map.put(key, (T) value);
            }
        }
        return map;
    }

    private ArrayList<Object> fromJson(JSONArray array) throws JSONException {
        ArrayList<Object> list = new ArrayList<>();
        int size = array.length();
        for (int i = 0; i < size; i++) {
            Object value = array.opt(i);
            if (value instanceof JSONObject) {
                value = fromJson((JSONObject) value);
            } else if (value instanceof JSONArray) {
                value = fromJson((JSONArray) value);
            }
            list.add(value);
        }
        return list;
    }

    public <T> String fromHashMap(HashMap<String, T> map) {
        try {
            JSONObject obj = getJSONObject(map);
            if (obj == null) {
                return "";
            }
            return obj.toString();
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return "";
        }
    }

    private <T> JSONObject getJSONObject(HashMap<String, T> map) throws JSONException {
        JSONObject json = new JSONObject();
        for (Map.Entry<String, T> entry : map.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                value = getJSONObject((HashMap) value);
            } else if (value instanceof ArrayList) {
                value = getJSONArray((ArrayList) value);
            } else if (isBasicArray(value)) {
                value = getJSONArray((ArrayList<Object>) arrayToList(value));
            }
            json.put(entry.getKey(), value);
        }
        return json;
    }

    private boolean isBasicArray(Object value) {
        return (value instanceof byte[]) || (value instanceof short[]) || (value instanceof int[]) || (value instanceof long[]) || (value instanceof float[]) || (value instanceof double[]) || (value instanceof char[]) || (value instanceof boolean[]) || (value instanceof String[]);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: ModVisitor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't change immutable type byte[] to java.lang.Object for r15v18 byte[]
        	at jadx.core.dex.instructions.args.SSAVar.setType(SSAVar.java:100)
        	at jadx.core.dex.instructions.args.RegisterArg.setType(RegisterArg.java:52)
        	at jadx.core.dex.visitors.ModVisitor.removeCheckCast(ModVisitor.java:358)
        	at jadx.core.dex.visitors.ModVisitor.replaceStep(ModVisitor.java:144)
        	at jadx.core.dex.visitors.ModVisitor.visit(ModVisitor.java:93)
        */
    private java.util.ArrayList<?> arrayToList(java.lang.Object r15) {
        /*
        // Method dump skipped, instructions count: 271
        */
        throw new UnsupportedOperationException("Method not decompiled:  com.xzy.forestSystem.mob.tools.utils.Hashon.arrayToList(java.lang.Object):java.util.ArrayList");
    }

    private JSONArray getJSONArray(ArrayList<Object> list) throws JSONException {
        JSONArray array = new JSONArray();
        Iterator<Object> it = list.iterator();
        while (it.hasNext()) {
            Object value = it.next();
            if (value instanceof HashMap) {
                value = getJSONObject((HashMap) value);
            } else if (value instanceof ArrayList) {
                value = getJSONArray((ArrayList) value);
            }
            array.put(value);
        }
        return array;
    }

    public String format(String jsonStr) {
        try {
            return format("", fromJson(jsonStr));
        } catch (Throwable t) {
            MobLog.getInstance().m57w(t);
            return "";
        }
    }

    private String format(String sepStr, HashMap<String, Object> map) {
        StringBuffer sb = new StringBuffer();
        sb.append("{\n");
        String mySepStr = sepStr + "\t";
        int i = 0;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(mySepStr).append('\"').append(entry.getKey()).append("\":");
            Object value = entry.getValue();
            if (value instanceof HashMap) {
                sb.append(format(mySepStr, (HashMap) value));
            } else if (value instanceof ArrayList) {
                sb.append(format(mySepStr, (ArrayList) value));
            } else if (value instanceof String) {
                sb.append('\"').append(value).append('\"');
            } else {
                sb.append(value);
            }
            i++;
        }
        sb.append('\n').append(sepStr).append('}');
        return sb.toString();
    }

    private String format(String sepStr, ArrayList<Object> list) {
        StringBuffer sb = new StringBuffer();
        sb.append("[\n");
        String mySepStr = sepStr + "\t";
        int i = 0;
        Iterator<Object> it = list.iterator();
        while (it.hasNext()) {
            Object value = it.next();
            if (i > 0) {
                sb.append(",\n");
            }
            sb.append(mySepStr);
            if (value instanceof HashMap) {
                sb.append(format(mySepStr, (HashMap) value));
            } else if (value instanceof ArrayList) {
                sb.append(format(mySepStr, (ArrayList) value));
            } else if (value instanceof String) {
                sb.append('\"').append(value).append('\"');
            } else {
                sb.append(value);
            }
            i++;
        }
        sb.append('\n').append(sepStr).append(']');
        return sb.toString();
    }
}
