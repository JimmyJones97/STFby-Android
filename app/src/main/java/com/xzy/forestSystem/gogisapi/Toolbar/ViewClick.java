package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Button;
import com.xzy.forestSystem.PubVar;
import com.stczh.gzforestSystem.R;

public class ViewClick implements View.OnClickListener {
    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view.getTag() != null) {
            String tempTag = view.getTag().toString();
            PubVar._PubCommand.ProcessCommand(tempTag);
            if (tempTag.equals("解锁GPS")) {
                Button localButton2 = (Button) view;
                Drawable localDrawable2 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.m_zoom_gps_unlock);
                localDrawable2.setBounds(0, 0, localDrawable2.getMinimumWidth(), localDrawable2.getMinimumHeight());
                localButton2.setCompoundDrawables(localDrawable2, null, null, null);
                localButton2.setTag("锁定GPS");
                PubVar.AutoPan = false;
            } else if (tempTag.equals("锁定GPS")) {
                Button localButton1 = (Button) view;
                Drawable localDrawable1 = PubVar._PubCommand.m_Context.getResources().getDrawable(R.drawable.m_zoom_gps_lock);
                localDrawable1.setBounds(0, 0, localDrawable1.getMinimumWidth(), localDrawable1.getMinimumHeight());
                localButton1.setCompoundDrawables(localDrawable1, null, null, null);
                localButton1.setTag("解锁GPS");
                PubVar.AutoPan = true;
            }
        }
    }
}
