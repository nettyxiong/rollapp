package com.example.test.business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.example.test.R;
import com.example.test.entity.TeacherInfo;
import com.example.test.util.DBHelper;
import com.example.test.util.HttpUtil;

/**
 * Created by xs on 2014/12/20.
 */
public class TeacherInfoActivity extends Activity implements IBackToPreview {
    private String userNameString;
    private ImageView portrait;
    private EditText account, userName, sex, position, telephone, email;
    private DBHelper dbHelper = null;
    private static final String selection_teacher = "Tid = ?";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.teacher_info);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.custom_title);
        portrait = (ImageView) findViewById(R.id.image_portrait);
        account = (EditText) findViewById(R.id.accountInfo);
        userName = (EditText) findViewById(R.id.userNameInfo);
        sex = (EditText) findViewById(R.id.sexInfo);
        position = (EditText) findViewById(R.id.positionInfo);
        telephone = (EditText) findViewById(R.id.telephoneInfo);
        email = (EditText) findViewById(R.id.emailInfo);

        Intent intent = getIntent();
        userNameString = intent.getStringExtra("username");
        
        dbHelper = new DBHelper(getApplicationContext());
        Cursor teacherCursor = dbHelper.query("teacher", selection_teacher, new String[]{userNameString});
        if (teacherCursor.getCount() != 0){
            System.out.println("teacherInfo from SQLite");
            //read info from local SQLite file
            if (teacherCursor.moveToFirst()){
                account.setText(teacherCursor.getString(0));
                userName.setText(teacherCursor.getString(1));
                sex.setText(teacherCursor.getString(2));
                position.setText(teacherCursor.getString(3));
                telephone.setText(teacherCursor.getString(4));
                email.setText(teacherCursor.getString(5));
                byte[] imageByte = teacherCursor.getBlob(6);
                portrait.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
                teacherCursor.close();
            }
        }else {
            //read info from server
            String result = query(userNameString);
            TeacherInfo teacherInfo = JSON.parseObject(result, TeacherInfo.class);
            assert teacherInfo != null;
            Bitmap bitmapTemp = HttpUtil.getBitMapByUrl(HttpUtil.getBASE_URL() + teacherInfo.getImageUrl());
            portrait.setImageBitmap(bitmapTemp);
            account.setText(teacherInfo.getTid().toString());
            userName.setText(teacherInfo.getName().toString());
            sex.setText(teacherInfo.getSex().toString());
            position.setText(teacherInfo.getPosition().toString());
            telephone.setText(teacherInfo.getTelephone().toString());
            email.setText(teacherInfo.getEmail().toString());

            //write into the dbHelper SQLite file
            ContentValues values = new ContentValues();
            values.put("Tid", teacherInfo.getTid().toString());
            values.put("Tname", teacherInfo.getName().toString());
            values.put("Tsex", teacherInfo.getSex().toString());
            values.put("Tposition",teacherInfo.getPosition());
            values.put("Tphone", teacherInfo.getTelephone());
            values.put("Temail", teacherInfo.getEmail());
            values.put("image", HttpUtil.BitmapToBytes(bitmapTemp));
            dbHelper.insert("teacher", values);
        }

    }

    public void backToPreview(View view) {
        this.finish();
    }

    private String query(String Sid) {
        String queryString = "Tid=" + Sid;
        String url = HttpUtil.getBASE_URL() + "servlet/TeacherInfoServlet?" + queryString;
        return HttpUtil.queryStringForPost(url);
    }
}