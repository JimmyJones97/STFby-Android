package  com.xzy.forestSystem.gogisapi.Carto;

import android.view.MotionEvent;

public interface ICommand {
    void MouseDown(MotionEvent motionEvent);

    void MouseMove(MotionEvent motionEvent);

    void MouseUp(MotionEvent motionEvent);
}
