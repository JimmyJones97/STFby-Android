package  com.xzy.forestSystem.gogisapi.Tools;

import java.math.BigDecimal;
import java.math.BigInteger;

/* compiled from: Calculation JexlArithmetic*/
class Arithmetic extends  ArithmeticException{
    public Arithmetic() {
        super("false");
    }

    /* access modifiers changed from: protected */
    public double divideZero(BigDecimal x) {
        int ls = x.signum();
        if (ls < 0) {
            return Double.NEGATIVE_INFINITY;
        }
        if (ls > 0) {
            return Double.POSITIVE_INFINITY;
        }
        return Double.NaN;
    }

    /* access modifiers changed from: protected */
    public double divideZero(BigInteger x) {
        throw new ArithmeticException("divide by zero");
    }

//    @Override // org.apache.commons.jexl2.JexlArithmetic
//    public Object divide(Object left, Object right) {
//        if (left == null && right == null) {
//            return controlNullNullOperands();
//        }
//        if ((left instanceof BigDecimal) || (right instanceof BigDecimal)) {
//            BigDecimal l = toBigDecimal(left);
//            BigDecimal r = toBigDecimal(right);
//            if (BigDecimal.ZERO.equals(r)) {
//                return Double.valueOf(divideZero(l));
//            }
//            return narrowBigDecimal(left, right, l.divide(r, getMathContext()));
//        } else if (isFloatingPointNumber(left) || isFloatingPointNumber(right)) {
//            return new Double(toDouble(left) / toDouble(right));
//        } else {
//            BigInteger l2 = toBigInteger(left);
//            BigInteger r2 = toBigInteger(right);
//            if (BigInteger.ZERO.equals(r2)) {
//                return Double.valueOf(divideZero(l2));
//            }
//            return narrowBigInteger(left, right, l2.divide(r2));
//        }
//    }
//
//    @Override // org.apache.commons.jexl2.JexlArithmetic
//    public Object mod(Object left, Object right) {
//        if (left == null && right == null) {
//            return controlNullNullOperands();
//        }
//        if ((left instanceof BigDecimal) || (right instanceof BigDecimal)) {
//            BigDecimal l = toBigDecimal(left);
//            BigDecimal r = toBigDecimal(right);
//            if (BigDecimal.ZERO.equals(r)) {
//                return Double.valueOf(divideZero(l));
//            }
//            return narrowBigDecimal(left, right, l.remainder(r, getMathContext()));
//        } else if (isFloatingPointNumber(left) || isFloatingPointNumber(right)) {
//            return new Double(toDouble(left) % toDouble(right));
//        } else {
//            BigInteger l2 = toBigInteger(left);
//            BigInteger r2 = toBigInteger(right);
//            BigInteger result = l2.mod(r2);
//            if (BigInteger.ZERO.equals(r2)) {
//                return Double.valueOf(divideZero(l2));
//            }
//            return narrowBigInteger(left, right, result);
//        }
//    }
}
