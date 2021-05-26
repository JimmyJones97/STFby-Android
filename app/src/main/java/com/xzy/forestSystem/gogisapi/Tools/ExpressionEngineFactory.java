package  com.xzy.forestSystem.gogisapi.Tools;

import  com.xzy.forestSystem.gogisapi.Common.CommonMath;
import java.util.HashMap;
import java.util.Map;

/* compiled from: Calculation */
class ExpressionEngineFactory {
    private ExpressionEngineFactory() {
    }
//JexlEngine
    public static JexlEngine createEngine() {
        Map<String, Object> ns = new HashMap<>();
        ns.put("math", CommonMath.class);
        JexlEngine jexl = new JexlEngine(null, new Arithmetic(), ns, null);
        jexl.setStrict(true);
        return jexl;
    }
}
