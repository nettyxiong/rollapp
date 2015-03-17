package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.*;
import com.example.test.business.IBackToPreview;
import com.example.test.util.DialogUtil;
import com.example.test.util.HttpUtil;

/**
 * Created by xs on 2014/12/11.
 */
public class LoginActivity extends Activity implements IBackToPreview {
    public static final String EMPTY_STRING = "";
    private static final String IS_Student_Checked = "isStudentChecked";
    private static final String USER_NAME_TEMP = "username";
    private static final String PASSWORD_TEMP = "password";
    private static final String SAVE_STUDENT_FILE_NAME = "save_student";
    private static final String SAVE_TEACHER_FILE_NAME = "save_teacher";
    private RadioButton student,teacher;
    private RadioGroup loginType;
    private Button cancelBtn, loginBtn, registerBtn, backBtn;
    private EditText userEditText, pwdEditText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.login_system);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.custom_title);

        loginType = (RadioGroup) findViewById(R.id.LoginradioGroup);
        student = (RadioButton) findViewById(R.id.radioStudent);
        teacher = (RadioButton) findViewById(R.id.radioTeacher);

        cancelBtn = (Button) findViewById(R.id.cancelButton);
        loginBtn = (Button) findViewById(R.id.loginButton);
        registerBtn = (Button) findViewById(R.id.registerButton);
        backBtn = (Button) findViewById(R.id.head_TitleBackBtn);

        userEditText = (EditText) findViewById(R.id.userEditText);
        pwdEditText = (EditText) findViewById(R.id.pwdEditText);

        SharedPreferences sharedPreferences = getSharedPreferences(SAVE_STUDENT_FILE_NAME, Context.MODE_PRIVATE);
        String usernameContent = sharedPreferences.getString(USER_NAME_TEMP, EMPTY_STRING);
        String passwordContent = sharedPreferences.getString(PASSWORD_TEMP, EMPTY_STRING);
        boolean isStudentChecked = sharedPreferences.getBoolean(IS_Student_Checked, true);
        if (isStudentChecked == true) {
            loginType.check(student.getId());
        } else {
            loginType.check(teacher.getId());
        }
        if (usernameContent != null && !EMPTY_STRING.equals(usernameContent))
            userEditText.setText(usernameContent);
        if (passwordContent != null && !EMPTY_STRING.equals(passwordContent))
            pwdEditText.setText(passwordContent);

        student.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPreferences = null;
                if (student.isChecked()) {
                    sharedPreferences = getSharedPreferences(SAVE_STUDENT_FILE_NAME, Context.MODE_PRIVATE);
                } else {
                    sharedPreferences = getSharedPreferences(SAVE_TEACHER_FILE_NAME, Context.MODE_PRIVATE);
                }
                String usernameContent = sharedPreferences.getString(USER_NAME_TEMP, EMPTY_STRING);
                String passwordContent = sharedPreferences.getString(PASSWORD_TEMP, EMPTY_STRING);
                boolean isStudentChecked = sharedPreferences.getBoolean(IS_Student_Checked, true);
                if (usernameContent != null && !EMPTY_STRING.equals(usernameContent))
                    userEditText.setText(usernameContent);
                if (passwordContent != null && !EMPTY_STRING.equals(passwordContent))
                    pwdEditText.setText(passwordContent);
                if (isStudentChecked == true) {
                    loginType.check(student.getId());
                } else {
                    loginType.check(teacher.getId());
                }
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEditText.setText(EMPTY_STRING);
                pwdEditText.setText(EMPTY_STRING);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    if (login()) {
                        //show(getString(R.string.login_success));
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("isStudentChecked", student.isChecked());
                        intent.putExtra("username", userEditText.getText().toString());
                        startActivity(intent);
                    } else {
                        show(getString(R.string.login_fail));
                    }
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                intent.putExtra("logintype", student.isChecked());
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        String usernameContent = userEditText.getText().toString();
        String passwordContent = pwdEditText.getText().toString();
        boolean isStudentChecked = student.isChecked();
        SharedPreferences sharedPreferences = null;
        if (isStudentChecked) {
            sharedPreferences = getSharedPreferences(SAVE_STUDENT_FILE_NAME, Context.MODE_PRIVATE);
        } else {
            sharedPreferences = getSharedPreferences(SAVE_TEACHER_FILE_NAME, Context.MODE_PRIVATE);
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(IS_Student_Checked, isStudentChecked);
        editor.putString(USER_NAME_TEMP, usernameContent);
        editor.putString(PASSWORD_TEMP, passwordContent);
        editor.commit();
    }

    private void show(String message) {
        DialogUtil.showDialog(this, message);
    }

    private boolean login() {
        String username = userEditText.getText().toString();
        String pwd = pwdEditText.getText().toString();
        String type = student.isChecked() ? "true" : "false";

        String result = query(username, pwd, type);
        if (result != null && result.equals("1")) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validate() {
        String username = userEditText.getText().toString();
        if (username.equals(EMPTY_STRING)) {
            DialogUtil.showDialog(this, getString(R.string.empty_username_error));
            return false;
        }
        String pwd = pwdEditText.getText().toString();
        if (pwd.equals(EMPTY_STRING)) {
            DialogUtil.showDialog(this, getString(R.string.empty_password_error));
            return false;
        }
        return true;
    }

    private String query(String username, String password, String logintype) {
        String queryString = "username=" + username + "&password=" + password + "&logintype=" + logintype;
        String url = HttpUtil.getBASE_URL() + "servlet/LoginServlet?" + queryString;
        return HttpUtil.queryStringForPost(url);
    }

    public void backToPreview(View view) {
        DialogUtil.exitDialog(this);
    }
}