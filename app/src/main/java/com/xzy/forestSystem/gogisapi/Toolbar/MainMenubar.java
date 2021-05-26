package  com.xzy.forestSystem.gogisapi.Toolbar;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Navigation.Navigation_Dialog;
import com.stczh.gzforestSystem.R;

public class MainMenubar {
    private View.OnClickListener MenuItemClickListener = new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.Toolbar.MainMenubar.1
        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            Object tempObj = view.getTag();
            if (tempObj != null) {
                String tempTag = tempObj.toString();
                if (tempTag.equals("选择")) {
                    PubVar._PubCommand.ShowToolbar("选择工具栏");
                } else if (tempTag.equals("足迹")) {
                    PubVar._PubCommand.ShowToolbar("足迹工具栏");
                } else if (tempTag.equals("导航")) {
                    new Navigation_Dialog().ShowDialog();
                } else if (tempTag.equals("工具")) {
                    PubVar._PubCommand.ShowToolbar("工具工具栏");
                } else {
                    PubVar._PubCommand.ProcessCommand(tempTag);
                }
            }
        }
    };
    private Context m_MainContext = null;
    private View m_View = null;
    private TextView selectCountTxtView = null;

    public MainMenubar(Context context) {
        this.m_MainContext = context;
    }

    public boolean IsVisible() {
        if (this.m_View.getVisibility() == 0) {
            return true;
        }
        return false;
    }

    public void SetVisible(boolean visible) {
        if (visible) {
            this.m_View.setVisibility(0);
        } else {
            this.m_View.setVisibility(8);
        }
    }

    public void UpdateSeletedCount(int count) {
        if (this.selectCountTxtView == null) {
            return;
        }
        if (count == 0) {
            this.selectCountTxtView.setVisibility(4);
            return;
        }
        this.selectCountTxtView.setVisibility(0);
        this.selectCountTxtView.setText(String.valueOf(count));
    }

    public void UpdateSeletedCount() {
        UpdateSeletedCount(Common.GetSelectObjectsCount(PubVar._Map));
    }

    public void LoadBottomToolBar(View view) {
        PubVar._ComHashMap.GetValue("ModuleName");
        this.m_View = view;
        view.findViewById(R.id.buttonLayer).setOnClickListener(this.MenuItemClickListener);
        view.findViewById(R.id.buttonSelect).setOnClickListener(this.MenuItemClickListener);
        this.selectCountTxtView = (TextView) view.findViewById(R.id.textViewSelectCount);
        this.selectCountTxtView.setVisibility(8);
        view.findViewById(R.id.btn_MainMenu_Tools).setOnClickListener(this.MenuItemClickListener);
        view.findViewById(R.id.btn_MainMenu_Query).setOnClickListener(this.MenuItemClickListener);
        view.findViewById(R.id.btn_MainMenu_Track).setOnClickListener(this.MenuItemClickListener);
        view.findViewById(R.id.btn_MainMenu_Navigation).setOnClickListener(this.MenuItemClickListener);
        view.findViewById(R.id.buttonTools).setOnClickListener(this.MenuItemClickListener);
        view.findViewById(R.id.rly_select).setOnClickListener(this.MenuItemClickListener);
    }
}
