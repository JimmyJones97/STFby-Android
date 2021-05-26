package  com.xzy.forestSystem.gogisapi.MyControls;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import com.stczh.gzforestSystem.R;

public class SeekbarDialog extends Dialog {
    public SeekbarDialog(Context context) {
        super(context);
        requestWindowFeature(1);
        setCanceledOnTouchOutside(false);
    }

    public SeekbarDialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private int _Index = -1;
        private View contentView;
        private Context context;
        private int defaultValue = 0;
        private ICallback m_Callback = null;
        private Object m_TagObject = null;
        private String message;
        private DialogInterface.OnClickListener negativeButtonClickListener;
        private String negativeButtonText;
        private DialogInterface.OnClickListener positiveButtonClickListener;
        private String positiveButtonText;
        private int seekbarMaxValue = 100;
        private EditText tempEditTxt;
        private SeekBar tempSeekBar;
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

        public void SetCallback(ICallback tmpICallback) {
            this.m_Callback = tmpICallback;
        }

        public void SetTag(Object value) {
            this.m_TagObject = value;
        }

        public void SetIndex(int index) {
            this._Index = index;
        }

        public void SetDefaultValue(int value) {
            this.defaultValue = value;
        }

        public void SetSeekbarMaxValue(int value) {
            this.seekbarMaxValue = value;
        }

        public SeekbarDialog create() {
            final SeekbarDialog dialog = new SeekbarDialog(this.context);
            View layout = ((LayoutInflater) this.context.getSystemService("layout_inflater")).inflate(R.layout.seekbar_dialog, (ViewGroup) null);
            dialog.addContentView(layout, new ViewGroup.LayoutParams(-1, -2));
            ((TextView) layout.findViewById(R.id.tv_seekbarDialog_title)).setText(this.title);
            this.tempEditTxt = (EditText) layout.findViewById(R.id.editTextSeekbar);
            this.tempEditTxt.setText(String.valueOf(this.defaultValue));
            this.tempSeekBar = (SeekBar) layout.findViewById(R.id.seekBarValue);
            this.tempSeekBar.setMax(this.seekbarMaxValue);
            this.tempSeekBar.setProgress(this.defaultValue);
            this.tempSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.SeekbarDialog.Builder.1
                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                    if (arg2) {
                        Builder.this.tempEditTxt.setText(String.valueOf(Builder.this.tempSeekBar.getProgress()));
                    }
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStartTrackingTouch(SeekBar arg0) {
                }

                @Override // android.widget.SeekBar.OnSeekBarChangeListener
                public void onStopTrackingTouch(SeekBar arg0) {
                }
            });
            this.tempEditTxt.addTextChangedListener(new TextWatcher() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.SeekbarDialog.Builder.2
                @Override // android.text.TextWatcher
                public void afterTextChanged(Editable arg0) {
                    String tempStr = arg0.toString();
                    if (tempStr.length() > 0) {
                        int tempInt = Integer.parseInt(tempStr);
                        if (tempInt < 0) {
                            Builder.this.tempEditTxt.setText("0");
                        }
                        if (tempInt > Builder.this.seekbarMaxValue) {
                            Builder.this.tempEditTxt.setText(String.valueOf(Builder.this.seekbarMaxValue));
                            return;
                        }
                        return;
                    }
                    Builder.this.tempEditTxt.setText("0");
                }

                @Override // android.text.TextWatcher
                public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }

                @Override // android.text.TextWatcher
                public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                }
            });
            if (this.positiveButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_seekbarDialog_Posi)).setText(this.positiveButtonText);
                if (this.positiveButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_seekbarDialog_Posi)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.SeekbarDialog.Builder.3
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            String tempStr = Builder.this.tempEditTxt.getText().toString();
                            if (Builder.this.m_Callback != null) {
                                Builder.this.m_Callback.OnClick("对话框返回-Seekbar-" + Builder.this._Index + "-" + tempStr, Builder.this.m_TagObject);
                            }
                            Builder.this.positiveButtonClickListener.onClick(dialog, -1);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.btn_seekbarDialog_Posi).setVisibility(8);
            }
            if (this.negativeButtonText != null) {
                ((Button) layout.findViewById(R.id.btn_seekbarDialog_Nagv)).setText(this.negativeButtonText);
                if (this.negativeButtonClickListener != null) {
                    ((Button) layout.findViewById(R.id.btn_seekbarDialog_Nagv)).setOnClickListener(new View.OnClickListener() { // from class:  com.xzy.forestSystem.gogisapi.MyControls.SeekbarDialog.Builder.4
                        @Override // android.view.View.OnClickListener
                        public void onClick(View v) {
                            Builder.this.negativeButtonClickListener.onClick(dialog, -2);
                        }
                    });
                }
            } else {
                layout.findViewById(R.id.btn_seekbarDialog_Nagv).setVisibility(8);
            }
            if (this.message != null) {
                ((TextView) layout.findViewById(R.id.textview_msg)).setText(this.message);
            } else if (this.contentView != null) {
                ((LinearLayout) layout.findViewById(R.id.ll_seekbarDialog_msg)).removeAllViews();
                ((LinearLayout) layout.findViewById(R.id.ll_seekbarDialog_msg)).addView(this.contentView, new ViewGroup.LayoutParams(-1, -1));
            }
            dialog.setContentView(layout);
            return dialog;
        }
    }
}
