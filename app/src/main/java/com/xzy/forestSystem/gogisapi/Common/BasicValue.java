package  com.xzy.forestSystem.gogisapi.Common;

public class BasicValue {
    private String _value;

    public boolean getBoolean() {
        return Boolean.getBoolean(this._value);
    }

    public double getDouble() {
        return Double.parseDouble(this._value);
    }

    public int getInt() {
        return Integer.parseInt(this._value);
    }

    public String getString() {
        return this._value;
    }

    public void addValue(double paramDouble) {
        this._value = String.valueOf(Double.parseDouble(this._value) + paramDouble);
    }

    public void setValue(double paramDouble) {
        this._value = String.valueOf(paramDouble);
    }

    public void setValue(int paramInt) {
        this._value = String.valueOf(paramInt);
    }

    public void setValue(String paramString) {
        this._value = paramString;
    }

    public void setValue(boolean paramBoolean) {
        this._value = String.valueOf(paramBoolean);
    }
}
