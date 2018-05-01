package ex.guitartest;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.suke.widget.SwitchButton;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.BlueTooth.RxBleService;
import ex.guitartest.student.StudentHomeActivity;
import ex.guitartest.teacher.TeacherHomeActivity;

import static android.content.Intent.ACTION_VIEW;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;
    @BindView(R.id.input_email)
    EditText _emailText;
    @BindView(R.id.input_password)
    EditText _passwordText;
    @BindView(R.id.btn_login)
    Button _loginButton;
    @BindView(R.id.link_signup)
    TextView _signupLink;
    @BindView(R.id.link_signup2)
    TextView linkSignup2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        _emailText.setText("s@ustb.com");
        _passwordText.setText("123");
        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Uri uri = Uri.parse("http://www.baidu.com");
                startActivity(new Intent(ACTION_VIEW, uri));
            }
        });
        AndPermission.with(this).permission(
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.RECORD_AUDIO
        ).requestCode(100).callback(permissionListener).start();

        Intent intent = new Intent(this, AlarmService.class);
        intent.setAction("NOTIFICATION");
        PendingIntent pi = PendingIntent.getService(LoginActivity.this, 0, new Intent(LoginActivity.this, AlarmService.class), 0);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int type = AlarmManager.RTC_WAKEUP;
        //new Date()：表示当前日期，可以根据项目需求替换成所求日期
        //getTime()：日期的该方法同样可以表示从1970年1月1日0点至今所经历的毫秒数
        long intervalMillis = System.currentTimeMillis() + 30 * 1000;
        if (manager != null) {
            manager.set(type, intervalMillis, pi);
        } else Log.d("AlarmServer", "Error");
    }

    public void login() {
        //Log.d(TAG, "Login");
        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.Theme_AppCompat_Dialog_Alert);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();
        SwitchButton switchButton = findViewById(R.id.switch_button);
        // TODO: Implement your own authentication logic here.
        boolean a = switchButton.isChecked();
        if (email.equals("t@ustb.com") && password.equals("123") && !switchButton.isChecked() ||
                email.equals("s@ustb.com") && password.equals("123") && switchButton.isChecked())
            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onLoginSuccess or onLoginFailed
                            onLoginSuccess();
                            progressDialog.dismiss();
                        }
                    }, 1000);
        else
            new Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onLoginFailed();
                            progressDialog.dismiss();
                        }
                    }, 1000);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // disable going back to the LoginActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess() {
        _loginButton.setEnabled(true);
        SwitchButton switchButton = findViewById(R.id.switch_button);
        if (switchButton.isChecked()) {
            Intent intent = new Intent(this, TeacherHomeActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, StudentHomeActivity.class);
            startActivity(intent);
        }
        finish();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()||email.length()!=11) {
            _emailText.setError("Email地址或手机号码无效");/*enter a valid email address*/
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 16) {
            _passwordText.setError("密码长度应在3-16位之间");//between 3 and 10 alphanumeric characters
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    private PermissionListener permissionListener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
            if (requestCode == 100) {
                Intent intent = new Intent(getApplicationContext(), RxBleService.class);
                startService(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

        }
    };

    @OnClick(R.id.link_signup2)
    public void onViewClicked() {
        Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
        startActivityForResult(intent, REQUEST_SIGNUP);
    }
}
