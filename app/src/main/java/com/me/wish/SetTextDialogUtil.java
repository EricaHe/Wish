package com.me.wish;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Erica on 2016/4/29.
 */
public class SetTextDialogUtil {
    private EditText txtSetter;
    private AlertDialog ad;
    private Activity activity;
    private String dialogTitle;
    private WishDBManager wishMgr;
    private Wish modifiedWish;

    public SetTextDialogUtil(Activity activity){
        this.activity = activity;
    }

    public void init(EditText txtSet){
        txtSet.setText("");
        wishMgr = new WishDBManager(activity);
    }

    public AlertDialog setTextDialog(final TextView inputTxt){
        LinearLayout txtSetterLayout = (LinearLayout) activity
                .getLayoutInflater().inflate(R.layout.common_txt_setter, null);
        txtSetter = (EditText) txtSetterLayout.findViewById(R.id.textSetter);
        init(txtSetter);

        ad = new AlertDialog.Builder(activity)
                .setTitle("修改设置")
                .setView(txtSetterLayout)
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        inputTxt.setText(txtSetter.getText().toString());
//                        if (dialogTitle.equals("标题")){
//                            modifiedWish.title = txtSetter.getText().toString();
//                            wishMgr.updateTitle("parent_wish",modifiedWish);
//                        } else if (dialogTitle.equals("描述")){
//                            modifiedWish.description = txtSetter.getText().toString();
//                            wishMgr.updateDescription("parent_wish", modifiedWish);
//                        } else if (dialogTitle.equals("截止日期")){
//
//                        }
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {}
                }).show();
        return ad;
    }
}
