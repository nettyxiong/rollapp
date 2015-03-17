package com.example.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.test.util.DialogUtil;
import com.example.test.util.HttpUtil;

/**
 * Created by xs on 2014/12/13.
 */
public class RegisterActivity extends Activity {
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private Button cancleBtn, registerBtn, backBtn;
    private TextView title;
    private EditText name, password;

    private boolean isStudentChecked = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.register_activity);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
                R.layout.custom_title);

        backBtn = (Button) findViewById(R.id.head_TitleBackBtn);
        cancleBtn = (Button) findViewById(R.id.cancelButton);
        registerBtn = (Button) findViewById(R.id.registerButton);
        title = (TextView) findViewById(R.id.head_center_text);
        name = (EditText) findViewById(R.id.userEditText);
        password = (EditText) findViewById(R.id.pwdEditText);

        title.setText(getString(R.string.register));

        Intent intent = getIntent();
        isStudentChecked = intent.getBooleanExtra("logintype", true);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });
        cancleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name.setText(LoginActivity.EMPTY_STRING);
                password.setText(LoginActivity.EMPTY_STRING);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    int status = register();
                    if (status == 1) {
                        showDialog(getString(R.string.regieter_success));
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else if (status == 0) {
                        showDialog(getString(R.string.register_error));
                    } else {
                        showDialog(getString(R.string.register_error_for_duplicate_user));
                    }
                }
            }
        });
    }

    private boolean validate() {
        String username = name.getText().toString();
        if (username.equals(LoginActivity.EMPTY_STRING)) {
            DialogUtil.showDialog(this, getString(R.string.empty_username_error));
            return false;
        } else if (username.length() > MAX_NAME_LENGTH) {
            DialogUtil.showDialog(this, getString(R.string.max_user_length_error));
            return false;
        }
        String pwd = password.getText().toString();
        if (pwd.equals(LoginActivity.EMPTY_STRING)) {
            DialogUtil.showDialog(this, getString(R.string.empty_password_error));
            return false;
        } else if (pwd.length() > MAX_PASSWORD_LENGTH) {
            DialogUtil.showDialog(this, getString(R.string.max_password_length_error));
            return false;
        }
        return true;
    }

    private void showDialog(String message) {
        DialogUtil.showDialog(this, message);
    }

    private void exit() {
        DialogUtil.exitDialog(this);
    }

    private int register() {
        String username = name.getText().toString();
        String pwd = password.getText().toString();
        String result = null;
        if (isStudentChecked) {
            result = query(username, pwd, "true");
        } else {
            result = query(username, pwd, "false");
        }
        if (result != null) {
            return Integer.parseInt(result);
        } else {
            return 0;
        }
    }

    private String query(String username, String password, String logintype) {
        String queryString = "username=" + username + "&password=" + password + "&logintype=" + logintype;
        String url = HttpUtil.getBASE_URL() + "servlet/RegisterServlet?" + queryString;
        return HttpUtil.queryStringForPost(url);
    }
}