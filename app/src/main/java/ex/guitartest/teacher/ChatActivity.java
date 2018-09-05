package ex.guitartest.teacher;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.R;
import ex.guitartest.teacher.fragment.MyFragment;
import ex.guitartest.teacher.fragment.MyStudentFragment;

/**
 * Created by qyxlx on 2018/3/12.
 * 聊天界面
 */

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.list_chat)
    ListView list_chat;
    @BindView(R.id.et_text)
    EditText et_text;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.ll_student)
    LinearLayout llStudent;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.iv_me)
    ImageView ivMe;
    @BindView(R.id.relativeLayout)
    RelativeLayout relativeLayout;
    @BindView(R.id.btn_send)
    Button btnSend;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private ChatAdapter chatAdapter;
    private List<String> datas = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        chatAdapter = new ChatAdapter(ChatActivity.this, datas);
        list_chat.setAdapter(chatAdapter);
    }

    @OnClick(R.id.btn_send)
    public void sendText() {
        if (et_text.getText().toString() != null) {
            datas.add(et_text.getText().toString());
            et_text.setText("");
            chatAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
       onBackPressed();
    }
    @Override
    public void onBackPressed(){
        Intent intent=new Intent();
        intent.putExtra("have_been_clicked",true);
        setResult(RESULT_OK,intent);

        finish();
    }

}
