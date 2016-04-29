package com.me.wish;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
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

    private WishDBManager wishMgr;

    private List<Wish> childWish = new ArrayList<Wish>();
    private List<String> childWishTitle = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        wishMgr = new WishDBManager(this);

        childWishListView = (ListView) findViewById(R.id.addChildWishListView);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_checked,
                childWishTitle);
        childWishListView.setAdapter(adapter);
        childWishListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        childWishListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                childWish.get(position).isDaily = childWishListView.isItemChecked(position);
            }
        });

        ImageButton addChildWishImgBtn = (ImageButton) findViewById(R.id.addChildWishImgBtn);
        childWishEditTxt = (EditText) findViewById(R.id.childWishEditText);
        if (addChildWishImgBtn != null) {
            addChildWishImgBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    String extraWishTitle = childWishEditTxt.getText().toString();
                    if (extraWishTitle.equals("")) {
                        Toast noEmptyHint = Toast.makeText(AddActivity.this, "心愿不能为空", Toast.LENGTH_SHORT);
                        noEmptyHint.show();
                    } else {
                        childWishTitle.add(extraWishTitle);
                        childWish.add(new Wish(extraWishTitle));
                        adapter.notifyDataSetChanged();
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

        Button addWishBtn = (Button) findViewById(R.id.addWishBtn);
        titleEditTxt = (EditText) findViewById(R.id.titleEditText);
        descriptionEditTxt = (EditText) findViewById(R.id.descriptionEditText);
        commentEditTxt = (EditText) findViewById(R.id.commentEditText);
        if (addWishBtn != null) {
            addWishBtn.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        if (titleEditTxt.getText().toString().equals("")) {
                            Toast.makeText(AddActivity.this, "标题不能为空", Toast.LENGTH_SHORT).show();
                        } else {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
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
                            for (Wish child : childWish) {
                                child.parent_id = parentId;
                                childrenWish.add(child);
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
                            sChildrenWishIds = (sChildrenWishIds.equals("")) ? "" : sChildrenWishIds.substring(1);
                            wishMgr.updateChildIdByParentId(sChildrenWishIds, parentId);

                            wishMgr.closeDB();
                            finish();
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}
