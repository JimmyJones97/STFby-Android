package  com.xzy.forestSystem.gogisapi.Common;

import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;

public class CommonEvent {
    public XDialogTemplate Dialog = null;
    public EventType mEventType = EventType.None;
    public String returnBackCommand = "";
    public ICallback returnCallback = null;

    public enum EventType {
        None,
        Select
    }
}
