package  com.xzy.forestSystem.gogisapi.Carto;

import  com.xzy.forestSystem.gogisapi.Display.ISymbol;
import  com.xzy.forestSystem.gogisapi.Edit.EDrawType;
import com.xzy.forestSystem.gogisapi.Geometry.AbstractGeometry;
import java.util.HashMap;
import java.util.UUID;

public class GraphicSymbolGeometry {
    public HashMap<String, Object> _AttributeHashMap = null;
    public EDrawType _DrawMode = EDrawType.NONE;
    public AbstractGeometry _Geoemtry;
    public String _GeometryType = "";
    public ISymbol _Symbol;
    public String _UID = ("G" + UUID.randomUUID().toString().replace("-", "").toUpperCase());
}
