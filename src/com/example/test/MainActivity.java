package com.example.test;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import cn.jpush.android.api.JPushInterface;
import com.example.test.business.*;
import com.example.test.util.DialogUtil;

/**
 * Created by xs on 2014/12/11.
 */
public class MainActivity extends ListActivity implements IBackToPreview {

    private static final String[] menus = new String[5];
    private boolean isStudentChecked = false;
    private String userName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        menus[0] = getString(R.string.user_info);
        menus[1] = getString(R.string.code_roll);
        menus[2] = getString(R.string.all_roll);
        menus[3] = getString(R.string.arandom_roll);
        menus[4] = getString(R.string.roll_result);
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.main_activity);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.custom_title);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, menus));
        getListView().setTextFilterEnabled(true);

        Intent intent = getIntent();
        isStudentChecked = intent.getBooleanExtra("isStudentChecked", true);
        userName = intent.getStringExtra("username");
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = null;
        switch (position) {
            case 0:
                if (isStudentChecked) {
                    intent = new Intent(MainActivity.this, StudentInfoActivity.class);
                } else {
                    intent = new Intent(MainActivity.this, TeacherInfoActivity.class);
                }
                intent.putExtra("username", userName);
                break;
            case 1:
                intent = new Intent(MainActivity.this, ChooseActivity.class);
                break;
            case 2:
                intent = new Intent(MainActivity.this, RollAllActivity.class);
                break;
            case 3:
                intent = new Intent(MainActivity.this, RandomRoll.class);
                break;
        }
        startActivity(intent);
    }

    public void backToPreview(View view) {
        DialogUtil.logoutDialog(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}