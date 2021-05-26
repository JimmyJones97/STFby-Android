package  com.xzy.forestSystem.gogisapi.Tools;

import com.xzy.forestSystem.otherlibs.Waterfall.TransportMediator;

import java.text.DecimalFormat;

public class Calculation {
    public static final String ERROR_PREFIX = "ERROR:";
    private static final DecimalFormat decimalFormat = new DecimalFormat();
    private static final JexlEngine jexl = ExpressionEngineFactory.createEngine();

    static {
        decimalFormat.setDecimalSeparatorAlwaysShown(false);
        decimalFormat.setMaximumFractionDigits(6);
        decimalFormat.setMinimumFractionDigits(0);
        decimalFormat.setGroupingSize(TransportMediator.KEYCODE_MEDIA_PAUSE);
    }

    private Calculation() {
    }

    public static String cal(String express) {
        if (express != null) {
            String express2 = express.trim();
            if (express2.length() != 0) {
                try {
//                    Object tmp = jexl.createExpression(express2).evaluate(null);
//                    if (tmp == null) {
//                        return "ERROR:No Result";
//                    }
//                    try {
//                        return decimalFormat.format(tmp);
//                    } catch (Exception e) {
//                        return tmp.toString();
//                    }
                } catch (Exception e2) {
                    return "ERROR:Wrong Expression";
                }
            }
        }
        return "ERROR:Empty Express";
    }
}
