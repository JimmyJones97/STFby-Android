package  com.xzy.forestSystem.gogisapi.CoordinateSystem;

import android.content.DialogInterface;
import android.view.View;
import com.xzy.forestSystem.PubVar;
import com.xzy.forestSystem.gogisapi.Common.Common;
import  com.xzy.forestSystem.gogisapi.Common.ICallback;
import  com.xzy.forestSystem.gogisapi.MyControls.XDialogTemplate;
import  com.xzy.forestSystem.gogisapi.XProject.CheckControlPtnCal_Dialog;
import com.stczh.gzforestSystem.R;

public class TranslateSetting_Dialog {
    private XDialogTemplate _Dialog;
    private PMTranslate _PMTranslate;
    private ICallback m_Callback;
    private ICallback pCallback;

    public void SetCallback(ICallback pCallback2) {
        this.m_Callback = pCallback2;
    }

    public TranslateSetting_Dialog() {
        this._Dialog = null;
        this.m_Callback = null;
        this._PMTranslate = new PMTranslate();
        this.pCallback = new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.TranslateSetting_Dialog.1
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                double[] tempParas;
                try {
                    if (paramString.equals("确定")) {
                        String tmpMethod = Common.GetSpinnerValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.sp_translateMethods);
                        if (tmpMethod.equals("无")) {
                            TranslateSetting_Dialog.this._PMTranslate.SetPMCoorTransMethodName(tmpMethod);
                            PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                            TranslateSetting_Dialog.this._Dialog.dismiss();
                        } else if (tmpMethod.indexOf("平移") >= 0) {
                            String tempPara01 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p1);
                            Common.ShowYesNoDialog(TranslateSetting_Dialog.this._Dialog.getContext(), "是否按如下参数进行平移转换?\r\nX偏移(m):" + tempPara01 + "\r\nY偏移(m):" + Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p2) + "\r\nZ偏移(m):" + Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p3), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.TranslateSetting_Dialog.1.1
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    if (paramString2.equals("YES")) {
                                        String tempPara012 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p1);
                                        String tempPara02 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p2);
                                        String tempPara03 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p3);
                                        if (TranslateSetting_Dialog.this._PMTranslate == null) {
                                            TranslateSetting_Dialog.this._PMTranslate = new PMTranslate();
                                        }
                                        TranslateSetting_Dialog.this._PMTranslate.SetPMCoorTransMethodName("平移转换");
                                        TranslateSetting_Dialog.this._PMTranslate.SetBiasX(tempPara012);
                                        TranslateSetting_Dialog.this._PMTranslate.SetBiasY(tempPara02);
                                        TranslateSetting_Dialog.this._PMTranslate.SetBiasZ(tempPara03);
                                        if (TranslateSetting_Dialog.this.m_Callback != null) {
                                            TranslateSetting_Dialog.this.m_Callback.OnClick("平面参数转换返回", TranslateSetting_Dialog.this._PMTranslate);
                                        }
                                        PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                                        TranslateSetting_Dialog.this._Dialog.dismiss();
                                    }
                                }
                            });
                        } else if (tmpMethod.indexOf("三参") >= 0) {
                            String tempPara012 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p1);
                            Common.ShowYesNoDialog(TranslateSetting_Dialog.this._Dialog.getContext(), "是否按如下参数进行平面转换?\r\nX平移(m):" + tempPara012 + "\r\nY平移(m):" + Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p2) + "\r\nZ平移(m):" + Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p3), new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.TranslateSetting_Dialog.1.2
                                @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
                                public void OnClick(String paramString2, Object pObject2) {
                                    if (paramString2.equals("YES")) {
                                        String tempPara013 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p1);
                                        String tempPara02 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p2);
                                        String tempPara03 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p3);
                                        if (TranslateSetting_Dialog.this._PMTranslate == null) {
                                            TranslateSetting_Dialog.this._PMTranslate = new PMTranslate();
                                        }
                                        TranslateSetting_Dialog.this._PMTranslate.SetPMCoorTransMethodName("三参转换");
                                        TranslateSetting_Dialog.this._PMTranslate.SetTransToP31(tempPara013);
                                        TranslateSetting_Dialog.this._PMTranslate.SetTransToP32(tempPara02);
                                        TranslateSetting_Dialog.this._PMTranslate.SetTransToP33(tempPara03);
                                        if (TranslateSetting_Dialog.this.m_Callback != null) {
                                            TranslateSetting_Dialog.this.m_Callback.OnClick("平面参数转换返回", TranslateSetting_Dialog.this._PMTranslate);
                                        }
                                        PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                                        TranslateSetting_Dialog.this._Dialog.dismiss();
                                    }
                                }
                            });
                        } else if (tmpMethod.indexOf("四参") >= 0) {
                            String tempPara013 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp4_p1);
                            String tempPara02 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp4_p2);
                            String tempPara03 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp4_p3);
                            String tempPara04 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp4_p4);
                            if (TranslateSetting_Dialog.this._PMTranslate == null) {
                                TranslateSetting_Dialog.this._PMTranslate = new PMTranslate();
                            }
                            TranslateSetting_Dialog.this._PMTranslate.SetPMCoorTransMethodName("四参转换");
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP41(tempPara013);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP42(tempPara02);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP43(tempPara03);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP44(tempPara04);
                            if (TranslateSetting_Dialog.this.m_Callback != null) {
                                TranslateSetting_Dialog.this.m_Callback.OnClick("平面参数转换返回", TranslateSetting_Dialog.this._PMTranslate);
                            }
                            PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                            TranslateSetting_Dialog.this._Dialog.dismiss();
                        } else if (tmpMethod.indexOf("七参") >= 0) {
                            String tempPara014 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p1);
                            String tempPara022 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p2);
                            String tempPara032 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p3);
                            String tempPara042 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p4);
                            String tempPara05 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p5);
                            String tempPara06 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p6);
                            String tempPara07 = Common.GetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp7_p7);
                            if (TranslateSetting_Dialog.this._PMTranslate == null) {
                                TranslateSetting_Dialog.this._PMTranslate = new PMTranslate();
                            }
                            TranslateSetting_Dialog.this._PMTranslate.SetPMCoorTransMethodName("七参转换");
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP71(tempPara014);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP72(tempPara022);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP73(tempPara032);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP74(tempPara042);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP75(tempPara05);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP76(tempPara06);
                            TranslateSetting_Dialog.this._PMTranslate.SetTransToP77(tempPara07);
                            if (TranslateSetting_Dialog.this.m_Callback != null) {
                                TranslateSetting_Dialog.this.m_Callback.OnClick("平面参数转换返回", TranslateSetting_Dialog.this._PMTranslate);
                            }
                            PubVar._PubCommand.m_ProjectDB.GetProjectManage().SaveProjectInfo();
                            TranslateSetting_Dialog.this._Dialog.dismiss();
                        } else {
                            TranslateSetting_Dialog.this._Dialog.dismiss();
                        }
                    } else if (paramString.equals("校准点") && (tempParas = (double[]) pObject) != null && tempParas.length > 1) {
                        double tempX = tempParas[0];
                        double tempY = tempParas[1];
                        Common.SetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p1, String.valueOf(tempX));
                        Common.SetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.pp3_p2, String.valueOf(tempY));
                        Common.SetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p1, String.valueOf(tempX));
                        Common.SetEditTextValueOnID(TranslateSetting_Dialog.this._Dialog, R.id.ppmove_p2, String.valueOf(tempY));
                    }
                } catch (Exception e) {
                }
            }
        };
        this._Dialog = new XDialogTemplate(PubVar.MainContext);
        this._Dialog.SetLayoutView(R.layout.translatesetting_dialog);
        this._Dialog.Resize(1.0f, 0.96f);
        this._Dialog.SetHeightWrapContent();
        this._Dialog.SetCaption("参数校正");
        this._Dialog.SetHeadButtons("1,2130837858,确定,确定", this.pCallback);
        this._Dialog.findViewById(R.id.buttonCheckBtn01).setOnClickListener(new ViewClick());
        this._Dialog.findViewById(R.id.buttonCheckBtn02).setOnClickListener(new ViewClick());
        Common.SetSpinnerListData(this._Dialog, "参数校正方法", PubVar._PubCommand.m_ConfigDB.getTranslateList(), (int) R.id.sp_translateMethods, new ICallback() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.TranslateSetting_Dialog.2
            @Override //  com.xzy.forestSystem.gogisapi.Common.ICallback
            public void OnClick(String paramString, Object pObject) {
                if (pObject != null) {
                    TranslateSetting_Dialog.this._PMTranslate.SetPMCoorTransMethodName(pObject.toString());
                    TranslateSetting_Dialog.this.refreshLayout(TranslateSetting_Dialog.this._PMTranslate);
                }
            }
        });
    }

    public void SetTitle(String title) {
        this._Dialog.SetCaption(title);
    }

    public void SetPMTranslate(PMTranslate PMTranslate) {
        this._PMTranslate = PMTranslate;
        Common.SetValueToView(this._PMTranslate.GetPMCoorTransMethodName(), this._Dialog.findViewById(R.id.sp_translateMethods));
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void DoCommand(String command) {
        if (command.equals("单点校正")) {
            CheckControlPtnCal_Dialog tempDialog = new CheckControlPtnCal_Dialog();
            tempDialog.SetCallback(this.pCallback);
            tempDialog.ShowDialog();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void refreshLayout(PMTranslate translate) {
        if (translate.GetPMCoorTransMethod() == ECoorTransMethod.enNull) {
            this._Dialog.findViewById(R.id.ll_translateParams).setVisibility(8);
            return;
        }
        this._Dialog.findViewById(R.id.ll_translateParams).setVisibility(0);
        if (translate.GetPMCoorTransMethod() == ECoorTransMethod.enXYZMove) {
            this._Dialog.findViewById(R.id.ll_translate_param_move).setVisibility(0);
            this._Dialog.findViewById(R.id.translate_param_3).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_4).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_7).setVisibility(8);
            Common.SetEditTextValueOnID(this._Dialog, R.id.ppmove_p1, String.valueOf(translate.getBiasX()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.ppmove_p2, String.valueOf(translate.getBiasY()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.ppmove_p3, String.valueOf(translate.getBiasZ()));
        } else if (translate.GetPMCoorTransMethod() == ECoorTransMethod.enThreePara) {
            this._Dialog.findViewById(R.id.ll_translate_param_move).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_3).setVisibility(0);
            this._Dialog.findViewById(R.id.translate_param_4).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_7).setVisibility(8);
            this._Dialog.findViewById(R.id.ll_translate3_extend).setVisibility(8);
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp3_p1, String.valueOf(translate.GetTransToP31()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp3_p2, String.valueOf(translate.GetTransToP32()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp3_p3, String.valueOf(translate.GetTransToP33()));
        } else if (translate.GetPMCoorTransMethod() == ECoorTransMethod.enFourPara) {
            this._Dialog.findViewById(R.id.ll_translate_param_move).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_3).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_4).setVisibility(0);
            this._Dialog.findViewById(R.id.translate_param_7).setVisibility(8);
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp4_p1, String.valueOf(translate.GetTransToP41()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp4_p2, String.valueOf(translate.GetTransToP42()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp4_p3, String.valueOf(translate.GetTransToP43()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp4_p4, String.valueOf(translate.GetTransToP44()));
        } else if (translate.GetPMCoorTransMethod() == ECoorTransMethod.enServenPara) {
            this._Dialog.findViewById(R.id.ll_translate_param_move).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_3).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_4).setVisibility(8);
            this._Dialog.findViewById(R.id.translate_param_7).setVisibility(0);
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p1, String.valueOf(translate.GetTransToP71()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p2, String.valueOf(translate.GetTransToP72()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p3, String.valueOf(translate.GetTransToP73()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p4, String.valueOf(translate.GetTransToP74()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p5, String.valueOf(translate.GetTransToP75()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p6, String.valueOf(translate.GetTransToP76()));
            Common.SetEditTextValueOnID(this._Dialog, R.id.pp7_p7, String.valueOf(translate.GetTransToP77()));
        }
    }

    public void ShowDialog() {
        this._Dialog.setOnShowListener(new DialogInterface.OnShowListener() { // from class:  com.xzy.forestSystem.gogisapi.CoordinateSystem.TranslateSetting_Dialog.3
            @Override // android.content.DialogInterface.OnShowListener
            public void onShow(DialogInterface paramDialogInterface) {
                TranslateSetting_Dialog.this.refreshLayout(TranslateSetting_Dialog.this._PMTranslate);
            }
        });
        this._Dialog.show();
    }

    class ViewClick implements View.OnClickListener {
        ViewClick() {
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            if (view.getTag() != null) {
                TranslateSetting_Dialog.this.DoCommand(view.getTag().toString());
            }
        }
    }
}
