package  com.xzy.forestSystem.gogisapi.Geodatabase;

public class DataTableField {
    private String _Caption = "";
    private String _Name = "";
    private boolean _Type = false;

    public String getCaption() {
        return this._Caption;
    }

    public String getName() {
        return this._Name;
    }

    public boolean getType() {
        return this._Type;
    }

    public void setCaption(String paramString) {
        this._Caption = paramString;
    }

    public void setName(String paramString) {
        this._Name = paramString;
        this._Name = this._Name.toUpperCase();
        if (this._Name.indexOf("SYS_") >= 0) {
            this._Type = true;
        }
    }
}
