package  com.xzy.forestSystem.gogisapi.IOData.DXF.bean;

import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.Entity;
import  com.xzy.forestSystem.gogisapi.IOData.DXF.pojo.UnexpectedElement;

public class SeqEnd extends Entity {
    public SeqEnd(String layer) throws UnexpectedElement {
        super("SEQEND", layer);
    }
}
