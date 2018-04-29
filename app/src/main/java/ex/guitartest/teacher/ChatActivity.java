package ex.guitartest.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ex.guitartest.R;

/**
 * Created by qyxlx on 2018/3/12.
 * 聊天界面
 */

public class ChatActivity extends AppCompatActivity {
    @BindView(R.id.list_chat)
    ListView list_chat;
    @BindView(R.id.et_text)
    EditText et_text;
    private ChatAdapter chatAdapter;
    private List<String> datas = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        initView();
    }
    private void initView(){
        chatAdapter = new ChatAdapter(ChatActivity.this,datas);
        list_chat.setAdapter(chatAdapter);
    }
    @OnClick(R.id.btn_send)
    public void sendText(){
        if (et_text.getText().toString()!= null){
            datas.add(et_text.getText().toString());
            et_text.setText("");
            chatAdapter.notifyDataSetChanged();
        }
    }
}
