package com.me.wish;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class AchieveActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achieve);

        //get achieves from mainactivity
        Intent intent=getIntent();
        String[] achieves=intent.getStringArrayExtra("achieve");

        final ArrayList<Achieve> achieveList = new ArrayList<Achieve>();
        int num=achieves.length;
        switch(num)
        {
            case 10:achieveList.add(new Achieve("银河指挥官",R.mipmap.level_10,"等级到达18级"));
            case 9:achieveList.add(new Achieve("银河守护者",R.mipmap.level_9,"等级到达16级"));
            case 8:achieveList.add(new Achieve("星际推进者",R.mipmap.level_8,"等级到达14级"));
            case 7:achieveList.add(new Achieve("星际冒险家",R.mipmap.level_7,"等级到达12级"));
            case 6:achieveList.add(new Achieve("星际先锋",R.mipmap.level_6,"等级到达10级"));
            case 5:achieveList.add(new Achieve("太空驾驶员",R.mipmap.level_5,"等级到达8级"));
            case 4:achieveList.add(new Achieve("太空浪人",R.mipmap.level_4,"等级到达6级"));
            case 3:achieveList.add(new Achieve("太空旅行者",R.mipmap.level_3,"等级到达4级"));
            case 2:achieveList.add(new Achieve("太空实习生",R.mipmap.level_2,"等级到达2级"));
            case 1:achieveList.add(new Achieve("太空菜鸟",R.mipmap.level_1,"初始奖励"));
        }
        AchieveAdapter adapter = new AchieveAdapter(this,R.layout.achieve_item,achieveList);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }
}
