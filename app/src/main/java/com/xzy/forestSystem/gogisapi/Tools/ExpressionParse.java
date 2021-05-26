package  com.xzy.forestSystem.gogisapi.Tools;

import  com.xzy.forestSystem.gogisapi.MyControls.FileSelector_Dialog;

/* compiled from: Calculation */
class ExpressionParse {
    ExpressionParse() {
    }

    public static String parse(String origExp) {
        if (origExp == null || origExp.trim().length() == 0) {
            return origExp;
        }
        return origExp.replaceAll("×", "*").replaceAll("π", "PI").replaceAll("[eE]", "E").replaceAll("(?<!\\d)(\\.\\d+)", "0$1").replaceAll("(?<!math:get)(PI|E)", "math:get$1()").replaceAll("÷\\s*0+(?:(?=[^.])|$)", "/0").replaceAll("[÷/]\\s*(?!0+)(\\d+)(?:(?=[^.0-9])|$)", "/$1.0").replaceAll("÷", FileSelector_Dialog.sRoot).replaceAll("(?<!math:)(sin|cos|tan|pow|sqrt|abs|asin|acos|atan|cbrt|exp|ln|log)", "math:$1");
    }
}
