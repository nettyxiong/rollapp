package com.example.test.business;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.example.test.R;
import com.example.test.entity.StudentInfo;
import com.example.test.util.DBHelper;
import com.example.test.util.HttpUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;

/**
 * Created by xs on 2014/12/11.
 */
public class StudentInfoActivity extends Activity implements IBackToPreview {
    private String userNameString;
    private ImageView portrait;
    private EditText account, userName, sex, classes, telephone, email;
    private Button update;
    private static final String selection_student = "Sid = ?";
    private DBHelper dbHelper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.student_info);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.custom_title);
        portrait = (ImageView) findViewById(R.id.image_portrait);
        account = (EditText) findViewById(R.id.accountInfo);
        userName = (EditText) findViewById(R.id.userNameInfo);
        sex = (EditText) findViewById(R.id.sexInfo);
        classes = (EditText) findViewById(R.id.classInfo);
        telephone = (EditText) findViewById(R.id.telephoneInfo);
        email = (EditText) findViewById(R.id.emailInfo);

        Intent intent = getIntent();
        userNameString = intent.getStringExtra("username");

        dbHelper = new DBHelper(getApplicationContext());
        Cursor studentCursor = dbHelper.query("student", selection_student, new String[]{userNameString});
        if (studentCursor.getCount() != 0) {
            //read info from local SQLite file
            System.out.println("studentInfo from SQLite");
            if (studentCursor.moveToFirst()) {
                account.setText(studentCursor.getString(0));
                userName.setText(studentCursor.getString(1));
                sex.setText(studentCursor.getString(2));
                byte[] imageByte = studentCursor.getBlob(3);
                portrait.setImageBitmap(BitmapFactory.decodeByteArray(imageByte, 0, imageByte.length));
                classes.setText(studentCursor.getString(4));
                telephone.setText(studentCursor.getString(5));
                email.setText(studentCursor.getString(6));
                studentCursor.close();
            }
        } else {
            //read info from server
            String result = query(userNameString);
            StudentInfo studentInfo = JSON.parseObject(result, StudentInfo.class);
            assert studentInfo != null;
            Bitmap bitmapTemp = HttpUtil.getBitMapByUrl(HttpUtil.getBASE_URL() + studentInfo.getImageUrl());
            portrait.setImageBitmap(bitmapTemp);
            account.setText(studentInfo.getSid().toString());
            userName.setText(studentInfo.getName().toString());
            sex.setText(studentInfo.getSex());
            classes.setText(studentInfo.getClasse() + " - " + studentInfo.getDepartment());
            telephone.setText(studentInfo.getTelephone());
            email.setText(studentInfo.getEmail());

            //write into the dbHelper SQLite file
            ContentValues values = new ContentValues();
            values.put("Sid", studentInfo.getSid().toString());
            values.put("Sname", studentInfo.getName().toString());
            values.put("Ssex", studentInfo.getSex().toString());
            values.put("image", HttpUtil.BitmapToBytes(bitmapTemp));
            values.put("classDepartment", studentInfo.getClasse() + " - " + studentInfo.getDepartment());
            values.put("Sphone", studentInfo.getTelephone());
            values.put("Semail", studentInfo.getEmail());
            dbHelper.insert("student", values);
        }
    }

    public void backToPreview(View view) {
        this.finish();
    }

    private String query(String Sid) {
        String queryString = "Sid=" + Sid;
        String url = HttpUtil.getBASE_URL() + "servlet/StudentInfoServlet?" + queryString;
        return HttpUtil.queryStringForPost(url);
    }
}