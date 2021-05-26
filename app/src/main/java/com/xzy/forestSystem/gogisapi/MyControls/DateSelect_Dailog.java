package  com.xzy.forestSystem.gogisapi.MyControls;

import android.content.DialogInterface;
import android.widget.DatePicker;
import android.widget.TimePicker;
import com.xzy.forestSystem.PubVar;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;
import java.util.Calendar;
import java.util.Date;

public class DateSelect_Dailog {
    private XDialogTemplate _Dialog;
    private ICallback m_Callback;
    private DatePicker m_DatePicker;
    private String m_ReturnTag;
    private Date m_SelectDate;
    private int m_SelectType;
    private TimePicker m_TimePicker;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public DateSelect_Dailog() {
        this._Dialog = null;
        this.m_Callback = null;
        this.m_DatePicker = null;
        this.m_TimePicker = null;
        this.m_SelectType = 0;
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.DateSelect_Dailog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String command, Object object) {
                if (command.equals("确定")) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(DateSelect_Dailog.this.m_DatePicker.getYear(), DateSelect_Dailog.this.m_DatePicker.getMonth(), DateSelect_Dailog.this.m_DatePicker.getDayOfMonth(), DateSelect_Dailog.this.m_TimePicker.getCurrentHour().intValue(), DateSelect_Dailog.this.m_TimePicker.getCurrentMinute().intValue());
                    if (DateSelect_Dailog.this.m_Callback != null) {
                        DateSelect_Dailog.this.m_Callback.OnClick("时间选择返回", new Object[]{DateSelect_Dailog.this.m_ReturnTag, calendar.getTime()});
                    }
                    DateSelect_Dailog.this._Dialog.dismiss();
                }
            }
        };
        this.m_SelectDate = null;
        this.m_ReturnTag = "";
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.dateselect_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("选择时间");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this.m_DatePicker = (DatePicker) this._Dialog.findViewById(R.id.mydatePicker01);
        this.m_TimePicker = (TimePicker) this._Dialog.findViewById(R.id.mytimePicker01);
        this.m_TimePicker.setIs24HourView(true);
    }

    public void SetSelectType(int type) {
        this.m_SelectType = type;
    }

    public void SetReturnTag(String value) {
        this.m_ReturnTag = value;
    }

    public void SetDate(Date value) {
        this.m_SelectDate = value;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void intialLayout() {
        if (this.m_SelectType == 1) {
            this._Dialog.findViewById(R.id.tv_dateLabel).setVisibility(8);
            this._Dialog.findViewById(R.id.ll_timeSelect).setVisibility(8);
        } else if (this.m_SelectType == 2) {
            this._Dialog.findViewById(R.id.tv_timeLabel).setVisibility(8);
            this._Dialog.findViewById(R.id.ll_dateSelect).setVisibility(8);
        }
        try {
            if (this.m_SelectDate == null) {
                this.m_SelectDate = new Date();
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(this.m_SelectDate);
            int tmpYear = calendar.get(1);
            int tmpMon = calendar.get(2);
            int tmpDays = calendar.get(5);
            this.m_DatePicker.init(tmpYear, tmpMon, tmpDays, null);
            this.m_DatePicker.updateDate(tmpYear, tmpMon, tmpDays);
            this.m_TimePicker.setCurrentHour(Integer.valueOf(this.m_SelectDate.getHours()));
            this.m_TimePicker.setCurrentMinute(Integer.valueOf(this.m_SelectDate.getMinutes()));
        } catch (Exception e) {
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.DateSelect_Dailog.2
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                DateSelect_Dailog.this.intialLayout();
            }
        });
        this._Dialog.show();
    }
}
