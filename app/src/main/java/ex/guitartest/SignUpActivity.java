package ex.guitartest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @BindView(R.id.input_name)
    EditText _nameText;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_signup)
    Button _signupButton;
    @BindView(R.id.link_login)
    TextView _loginLink;

    boolean signUpResult = false;
    OkHttpClient client = new OkHttpClient();

    String name;
    String email;
    String password;
    @BindView(R.id.ClientChoose)
    RadioGroup ClientChoose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
        ClientChoose.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i){
                    case R.id.Radio_parents:
                        _signupButton.setText("创建家长账户");
                        break;
                    case R.id.Radio_student:
                        _signupButton.setText("创建学生账户");
                        break;
                    case R.id.Radio_teacher:
                        _signupButton.setText("创建教师账户");
                        break;
                }
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUpActivity.this,
                R.style.Theme_AppCompat_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        password = _passwordText.getText().toString();
        // TODO: Implement your own signup logic here.

        RequestBody requestBody = new FormBody.Builder()
                .add("a", "signup")
                .add("username", email)
                .add("password", password)
                .add("nickname", name)
                .build();
        Request request = new Request.Builder()
                .url(" http://sguitar.mybeike.com/api/user.php")
                .post(requestBody)
                .build();

        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure:Sign Up");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                signUpResult = response.body().string().equals("1");
            }
        });

        new Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success

                        // On complete call either onLoginSuccess or onLoginFailed
                        progressDialog.dismiss();

                        if (signUpResult) {
                            onSignupSuccess();
                        } else {
                            onSignupFailed();
                        }
                    }
                }, 2000);

    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        Intent intent = new Intent();
        intent.putExtra("SignUpEmail", _emailText.getText().toString());
        setResult(RESULT_OK, intent);
        Toast.makeText(SignUpActivity.this, "注册成功", Toast.LENGTH_SHORT);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "注册失败", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (/*name.isEmpty() || name.length() !=11*/false) {
            _nameText.setError("请输入正确的手机号");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("请输入正确的邮箱地址");
            valid = false;
        } else if (email.contains("\"") || email.contains("\\") || email.contains("\'")) {
            _emailText.setError("不能包含单、双引号或反斜杠");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            _passwordText.setError("密码应在3至10位之间");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}
