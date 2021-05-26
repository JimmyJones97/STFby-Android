package com.xzy.forestSystem.baidu.speech;

import android.content.Context;
import android.util.Log;
import  com.xzy.forestSystem.qihoo.jiagutracker.C0246Config;
import com.xzy.forestSystem.swift.sandhook.annotation.MethodReflectParams;
import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Args {
    private static final String TAG = "Args";
    private static final Logger logger = Logger.getLogger(TAG);
    private static final HashMap<String, ArrayList<Def>> sParsedArgDefinesCache = new HashMap<>();
    private static final HashMap<String, HashMap<String, ArgValues>> sParsedArgValuesCache = new HashMap<>();
    private final Context mContext;
    private final ArrayList<Def> mSupportedArgs;
    private final HashMap<String, ArgValues> mSupportedValues;
    private final HashMap<String, Obj> mUsingArgs;

    private static ArrayList<Def> staticInit(String name) throws Exception {
        InputStream in = null;
        try {
            in = Args.class.getResourceAsStream(name);
            return staticInit(in);
        } finally {
            in.close();
        }
    }

    private static ArrayList<Def> staticInit(InputStream in) throws Exception {
        ArrayList<Def> defs = new ArrayList<>();
        NodeList groups = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in).getDocumentElement().getElementsByTagName("group");
        for (int iGrp = 0; iGrp < groups.getLength(); iGrp++) {
            Element g = (Element) groups.item(iGrp);
            String groupName = g.getAttribute("name");
            NodeList notesInGroup = g.getChildNodes();
            for (int iInOneGroup = 0; iInOneGroup < notesInGroup.getLength(); iInOneGroup++) {
                Node oneArgs = notesInGroup.item(iInOneGroup);
                if (oneArgs.getNodeType() == 1) {
                    Element e = (Element) oneArgs;
                    defs.add(new Def(e.getTagName(), groupName, e.getAttribute("name"), e.getAttribute("required"), parseText(e, "rule"), parseText(e, "desc")));
                }
            }
        }
        return defs;
    }

    private static String parseText(Element e, String name) {
        NodeList l = e.getElementsByTagName(name);
        if (l.getLength() > 0) {
            return l.item(0).getFirstChild().getNodeValue();
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    public static class ArgValues {
        private String _rawDepends;
        private String content;
        private LinkedList<ArgValues> depends = new LinkedList<>();
        private String name;

        ArgValues() {
        }

        public LinkedList<ArgValues> getDepends() {
            return this.depends;
        }

        public String getName() {
            return this.name;
        }

        public String getContent() {
            return this.content;
        }

        /* access modifiers changed from: package-private */
        public void setContent(String str) throws IOException {
            BufferedReader r = new BufferedReader(new StringReader(str));
            StringBuilder sb = new StringBuilder();
            while (true) {
                String line = r.readLine();
                if (line != null) {
                    String tmp = line;
                    int index = tmp.indexOf(35);
                    if (index >= 0) {
                        tmp = tmp.substring(0, index);
                    }
                    String tmp2 = tmp.trim();
                    if (tmp2.length() > 0) {
                        sb.append(tmp2).append(' ');
                    }
                } else {
                    r.close();
                    this.content = sb.toString();
                    return;
                }
            }
        }

        public String toString() {
            return String.format("ArgValues:name=%s, depends=%s\n%s\n", getName(), getDepends(), getContent());
        }

        public static final HashMap load(String name2) throws Exception {
            InputStream in = null;
            try {
                in = Args.class.getResourceAsStream(name2);
                return load(in);
            } finally {
                in.close();
            }
        }

        public static final HashMap<String, ArgValues> load(InputStream in) throws Exception {
            NodeList groups = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in).getDocumentElement().getElementsByTagName("cmd");
            HashMap<String, ArgValues> defaults = new HashMap<>();
            for (int iGrp = 0; iGrp < groups.getLength(); iGrp++) {
                Element g = (Element) groups.item(iGrp);
                ArgValues v = new ArgValues();
                v.name = g.getAttribute("name");
                v._rawDepends = g.getAttribute("depends");
                v.setContent(g.getTextContent());
                defaults.put(v.name, v);
            }
            for (ArgValues item : defaults.values()) {
                for (String k : item._rawDepends.split(",")) {
                    ArgValues tmp = defaults.get(k);
                    if (tmp != null) {
                        item.depends.offer(tmp);
                    }
                }
            }
            return defaults;
        }
    }

    public static class Def {
        public String desc;
        public String group;
        public String name;
        public boolean required;
        private Pattern rule;
        public String type;

        public Def(String type2, String group2, String name2, String required2, String rule2, String desc2) {
            String str = null;
            this.type = type2;
            this.group = group2.trim();
            this.name = name2.trim();
            this.required = Boolean.parseBoolean(required2);
            this.rule = rule2 == null ? null : Pattern.compile(rule2.trim());
            this.desc = desc2 != null ? desc2.trim() : str;
        }

        public String getFullName() {
            return this.group + FileSelector_Dialog.sFolder + this.name;
        }

        private void check(String val) {
            if (val == null) {
                if (this.required) {
                    throw new IllegalArgumentException(getFullName() + " 的参数不能为 null ");
                }
            } else if (this.rule != null && !this.rule.matcher(val).find()) {
                throw new IllegalArgumentException(getFullName() + " 的参数不能为 " + val);
            }
        }

        public Object parse(String val) {
            check(val);
            if ("string".equals(this.type)) {
                return val;
            }
            if (MethodReflectParams.INT.equals(this.type)) {
                if (val == null || "".equals(val) || C0246Config.EMPTY_STRING.equalsIgnoreCase(val)) {
                    return null;
                }
                return Integer.valueOf(Integer.parseInt(val));
            } else if (MethodReflectParams.LONG.equals(this.type)) {
                if (val == null || "".equals(val) || C0246Config.EMPTY_STRING.equalsIgnoreCase(val)) {
                    return null;
                }
                return Long.valueOf(Long.parseLong(val));
            } else if (MethodReflectParams.BOOLEAN.equals(this.type)) {
                if (val == null || "".equals(val) || C0246Config.EMPTY_STRING.equalsIgnoreCase(val)) {
                    return null;
                }
                return Boolean.valueOf(Boolean.parseBoolean(val));
            } else if (!"string-arr".equals(this.type)) {
                throw new IllegalArgumentException("不支持的type类型，" + this.type);
            } else if (val == null || "".equals(val)) {
                return null;
            } else {
                return val.trim().split(",");
            }
        }

        public String toString() {
            return "ArgDef [group=" + this.group + ", name=" + this.name + ", required=" + this.required + ", rule=" + this.rule + ", desc=" + this.desc + "]";
        }

        public Obj create() {
            return new Obj(this);
        }
    }

    public static class Obj {
        private final Def key;
        private final LinkedList<String> values = new LinkedList<>();

        public Obj(Def def) {
            this.key = def;
        }

        public void setValue(String tag, String val) {
            this.values.offer(tag);
            this.values.offer(val);
        }

        public Object getValue() {
            return this.key.parse(this.values.size() > 0 ? this.values.getLast() : null);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            getValue();
            int i = 0;
            while (i < this.values.size()) {
                sb.insert(0, String.format("%-12s", "" + this.values.get(i + 1) + "[" + this.values.get(i) + "]" + (i == 0 ? "" : " <- ")));
                i += 2;
            }
            return sb.toString();
        }
    }

    public Args(Context context, String argsDefineRes, String defaultValuesRes) {
        this(context, argsDefineRes, defaultValuesRes, "default");
    }

    public Args(Context context, String argsDefineRes, String defaultValuesRes, String defaultName) {
        this.mContext = context;
        this.mSupportedArgs = getDefinition(argsDefineRes);
        this.mSupportedValues = getDefaults(defaultValuesRes);
        this.mUsingArgs = new HashMap<>();
        Iterator i$ = this.mSupportedArgs.iterator();
        while (i$.hasNext()) {
            Def def = (Def) i$.next();
            this.mUsingArgs.put(def.getFullName(), def.create());
        }
        ArgValues aa = this.mSupportedValues.get(defaultName);
        if (aa == null) {
            Log.d(TAG, defaultName + " not found");
        } else {
            parse(aa);
        }
    }

    private static ArrayList<Def> getDefinition(String name) {
        if (!sParsedArgDefinesCache.containsKey(name)) {
            try {
                sParsedArgDefinesCache.put(name, staticInit(name));
            } catch (Exception e) {
                throw new IllegalArgumentException("can't init arg def list", e);
            }
        }
        return sParsedArgDefinesCache.get(name);
    }

    private static HashMap<String, ArgValues> getDefaults(String name) {
        if (!sParsedArgValuesCache.containsKey(name)) {
            try {
                sParsedArgValuesCache.put(name, ArgValues.load(Args.class.getResourceAsStream(name)));
            } catch (Exception e) {
                throw new IllegalArgumentException("can't init arg def list", e);
            }
        }
        return sParsedArgValuesCache.get(name);
    }

    public Args parse(String name) {
        parse(this.mSupportedValues.get(name));
        return this;
    }

    private String tryFillValue(String input) {
        Object val;
        if (!(input instanceof String) || !input.startsWith("&")) {
            return input;
        }
        try {
            Matcher m = Pattern.compile("^&(.*)[#.](.*?)\\(").matcher(input);
            if (m.find()) {
                String cls = m.group(1);
                String mtd = m.group(2);
                Method methodWithContext = null;
                Method methodWithoutContext = null;
                try {
                    methodWithContext = Class.forName(cls).getMethod(mtd, Context.class);
                } catch (NoSuchMethodException e) {
                }
                try {
                    methodWithoutContext = Class.forName(cls).getMethod(mtd, new Class[0]);
                } catch (NoSuchMethodException e2) {
                }
                if (methodWithContext != null) {
                    val = methodWithContext.invoke(null, this.mContext);
                } else if (methodWithoutContext != null) {
                    val = methodWithoutContext.invoke(null, new Object[0]);
                } else {
                    throw new Exception("method not found, " + input);
                }
                return val + "";
            }
            throw new Exception("method not found, " + input);
        } catch (Exception e3) {
            throw new IllegalArgumentException("" + input + " invoke failed", e3);
        }
    }

    public Args parse(String reason, String strArgs) {
        Matcher m = Pattern.compile("--(\\S+?)[\\s==]+?(\\S+)").matcher(strArgs);
        while (m.find()) {
            String inputKey = m.group(1).trim();
            String inputVal = m.group(2).trim();
            Obj arg = this.mUsingArgs.get(inputKey);
            if (arg != null) {
                arg.setValue(reason + (inputVal.startsWith("&") ? "-" + inputVal : ""), tryFillValue(inputVal));
            }
        }
        return this;
    }

    private void parse(ArgValues a) {
        Iterator i$ = a.getDepends().iterator();
        while (i$.hasNext()) {
            parse((ArgValues) i$.next());
        }
        parse(a.getName(), a.getContent());
    }

    public Map<String, Object> create() {
        HashMap<String, Object> results = new HashMap<>();
        for (Map.Entry<String, Obj> e : this.mUsingArgs.entrySet()) {
            results.put(e.getKey(), e.getValue().getValue());
        }
        return Collections.unmodifiableMap(results);
    }

    private String print() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        Iterator i$ = this.mSupportedArgs.iterator();
        while (i$.hasNext()) {
//            String k = i$.next().getFullName();
//            sb.append(String.format("  --%-24s %s\n", k, this.mUsingArgs.get(k).toString()));
        }
        sb.append("\n");
        return sb.toString();
    }

    public String toString() {
        return print();
    }
}
