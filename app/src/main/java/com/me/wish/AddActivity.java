package com.me.wish;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */
    private EditText dueDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        // date输入框
        dueDateTime = (EditText) findViewById(R.id.dueDateEditText);

        dueDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                            AddActivity.this);
                    dateTimePicKDialog.dateTimePicKDialog(dueDateTime);
                }
            }
        });

        dueDateTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                DateTimePickDialogUtil dateTimePicKDialog = new DateTimePickDialogUtil(
                        AddActivity.this);
                dateTimePicKDialog.dateTimePicKDialog(dueDateTime);
            }
        });
    }
}
