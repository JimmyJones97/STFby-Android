package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.ViewCompat;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class BaseDialog extends Dialog {
    private TextView msgTextView = null;
    private boolean myAlertColor = false;
    private Runnable myTask = new Runnable() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.BaseDialog.1
        @Override // java.lang.Runnable
        public void run() {
            if (BaseDialog.this.myAlertColor) {
                if (BaseDialog.this.msgTextView != null) {
                    BaseDialog.this.msgTextView.setTextColor(ViewCompat.MEASURED_STATE_MASK);
                }
                BaseDialog.this.myAlertColor = false;
            } else {
                if (BaseDialog.this.msgTextView != null) {
                    BaseDialog.this.msgTextView.setTextColor(SupportMenu.CATEGORY_MASK);
                }
                BaseDialog.this.myAlertColor = true;
            }
            BaseDialog.this.myTickHandler.postDelayed(BaseDialog.this.myTask, 500);
        }
    };
    private Handler myTickHandler = new Handler();

    public BaseDialog(Context context) {
        super(context);
        requestWindowFeature(1);
        setCanceledOnTouchOutside(false);
    }

    public BaseDialog(Context context, int theme) {
        super(context, theme);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void SetAlert(TextView MsgTextView) {
        this.msgTextView = MsgTextView;
        this.msgTextView.getPaint().setFakeBoldText(true);
        this.myTickHandler.postDelayed(this.myTask, 500);
    }

    public static class Builder {
        TextView MsgTextView = null;
        private View contentView;
        private Context context;
        private boolean isAlert = false;
        private String message;
        private int messageColor = R.color.myBlackColor;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private String title;

        public Builder(Context context2) {
            this.context = context2;
        }

        public Builder setMessage(String message2) {
            this.message = message2;
            return this;
        }

        public Builder setMessage(int message2) {
            this.message = (String) this.context.getText(message2);
            return this;
        }

        public Builder setTitle(int title2) {
            this.title = (String) this.context.getText(title2);
            return this;
        }

        public Builder setTitle(String title2) {
            this.title = title2;
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setMessageColor(int color) {
            this.messageColor = color;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = (String) this.context.getText(positiveButtonText2);
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setPositiveButton(String positiveButtonText2, DialogInterface.OnClickListener listener) {
            this.positiveButtonText = positiveButtonText2;
            this.positiveButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = (String) this.context.getText(negativeButtonText2);
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setNegativeButton(String negativeButtonText2, DialogInterface.OnClickListener listener) {
            this.negativeButtonText = negativeButtonText2;
            this.negativeButtonClickListener = listener;
            return this;
        }

        public Builder setIsAlert() {
            this.isAlert = true;
            return this;
        }

        public BaseDialog create() {
            final BaseDialog dialog = new BaseDialog(this.context);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.simpledialog, (ViewGroup) null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            ((TextView) layout.findViewById(R.id.tv_simpleDialog_title)).setText(this.title);
            if (this.positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_simpleDialog_Posi)).setText(this.positiveButtonText);
                if (this.positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_simpleDialog_Posi)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.BaseDialog.Builder.1
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.btn_simpleDialog_Posi).setVisibility(8);
            }
            if (this.negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_simpleDialog_Nagv)).setText(this.negativeButtonText);
                if (this.negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_simpleDialog_Nagv)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.BaseDialog.Builder.2
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.btn_simpleDialog_Nagv).setVisibility(8);
            }
            if (this.message != null) {
                this.MsgTextView = (TextView) layout.findViewById(R.id.tv_simpleDialog_msg);
                this.MsgTextView.setText(this.message);
                this.MsgTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
                this.MsgTextView.setTextColor(this.messageColor);
                if (this.isAlert) {
                    dialog.SetAlert(this.MsgTextView);
                }
            } else if (this.contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.ll_simpleDialog_msg)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.ll_simpleDialog_msg)).addView(this.contentView, new ViewGroup.LayoutParams(-1, -1));
            }
            dialog.setContentView(layout);
            return dialog;
        }

        public void SetLongClickTxt(final ICallback iCallback) {
            if (iCallback != null && this.MsgTextView != null) {
                this.MsgTextView.setOnLongClickListener(new View.OnLongClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.BaseDialog.Builder.3
                    @Override // android.view.View.OnLongClickListener
                    public boolean onLongClick(View v) {
                        iCallback.OnClick("点击对话框窗体文字返回", Builder.this.MsgTextView.getText().toString());
                        return false;
                    }
                });
            }
        }
    }
}
