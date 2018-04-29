package ex.guitartest.teacher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import butterknife.BindView;
import ex.guitartest.R;

/**
 * Created by qyxlx on 2018/3/12.
 * 聊天列表界面
 */

public class MessageActivity extends AppCompatActivity {
    @BindView(R.id.list_chat)
    ListView list_chat;
    private ChatListAdapter chatListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        list_chat = findViewById(R.id.list_chat);
        initView();
    }
    private void initView(){
        chatListAdapter = new ChatListAdapter(MessageActivity.this);
        list_chat.setAdapter(chatListAdapter);
    }
}
