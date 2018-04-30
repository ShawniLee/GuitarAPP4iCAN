package ex.guitartest.teacher;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ex.guitartest.R;

public class PracticeResultActivity extends AppCompatActivity {


    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView5)
    TextView textView5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prictice_result);
        ButterKnife.bind(this);
        textView3.setText("100分");
        textView5.setText("该部分有0处错误错误，其中0处速度错误，0处音准错误。太棒了！");
    }
}
