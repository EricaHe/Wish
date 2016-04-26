package com.me.wish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {

    /**
     * Called when the activity is first created.
     */
    private ListView childWishListView;
    private EditText dueDateTime;
    private EditText childWishEditTxt;
    private EditText titleEditTxt;
    private EditText descriptionEditTxt;
    private EditText commentEditTxt;
    private Button addWishBtn;

    private WishDBManager wishMgr;

    private List<String> childWish = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        wishMgr = new WishDBManager(this);

        childWishListView = (ListView) findViewById(R.id.addChildWishListView);
        childWishListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                childWish));

        ImageButton addChildWishImgBtn = (ImageButton) findViewById(R.id.addChildWishImgBtn);
        childWishEditTxt = (EditText) findViewById(R.id.childWishEditText);
        if (addChildWishImgBtn != null) {
            addChildWishImgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String extraWish = childWishEditTxt.getText().toString();
                    if (extraWish.equals("")) {
                        Toast noEmptyHint = Toast.makeText(AddActivity.this, "心愿不能为空", Toast.LENGTH_SHORT);
                        noEmptyHint.show();
                    } else {
                        childWish.add(extraWish);
                        childWishListView.setAdapter(new ArrayAdapter<String>(AddActivity.this,
                                android.R.layout.simple_list_item_checked, childWish));
                        childWishEditTxt.setText("");
                    }
                }
            });
        }

        // date输入框
        dueDateTime = (EditText) findViewById(R.id.dueDateEditText);

        dueDateTime.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
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

        addWishBtn = (Button) findViewById(R.id.addWishBtn);
        titleEditTxt = (EditText) findViewById(R.id.titleEditText);
        descriptionEditTxt = (EditText) findViewById(R.id.descriptionEditText);
        commentEditTxt = (EditText) findViewById(R.id.commentEditText);
        addWishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm", Locale.CHINA);
                    String title = titleEditTxt.getText().toString();
                    String description = descriptionEditTxt.getText().toString();
                    String comment = commentEditTxt.getText().toString();
                    Date dueDate = (dueDateTime.getText().toString().equals("")) ?
                            null : sdf.parse(dueDateTime.getText().toString());
                    Date createDate = new Date();
                    Wish parentWish = new Wish(title, description, dueDate, createDate);
                    parentWish.comment = comment;

                    // add parent wish into database(without children ids)
                    int parentId = wishMgr.addParentWishReturnId(parentWish);

                    List<Wish> childrenWish = new ArrayList<Wish>();
                    // add parent id to children wishes
                    for (String childTitle : childWish) {
                        Wish tempWish = new Wish(childTitle);
                        tempWish.parent_id = parentId;
                        childrenWish.add(tempWish);
                    }
                    // add children wishes into database(with parent id)
                    wishMgr.addChildWishes(childrenWish);

                    // update parent wish's children ids
                    childrenWish.clear();
                    childrenWish = wishMgr.queryChildWishesByParentId(parentId);
                    String sChildrenWishIds = "";
                    for (Wish child : childrenWish) {
                        sChildrenWishIds = sChildrenWishIds + "," + Integer.toString(child.id);
                    }
                    sChildrenWishIds = sChildrenWishIds.substring(1);
                    wishMgr.updateChildIdByParentId(sChildrenWishIds, parentId);

                    wishMgr.closeDB();
                    finish();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
