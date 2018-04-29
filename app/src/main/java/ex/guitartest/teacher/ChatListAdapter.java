package ex.guitartest.teacher;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import ex.guitartest.R;

/**
 *
 * Created by qyxlx on 2018/4/6 .
 */

public class ChatListAdapter extends BaseAdapter {
    private Context context;

    public ChatListAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_chat, null);
            viewHolder.ll_chat = convertView.findViewById(R.id.ll_chat);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ll_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                context.startActivity(intent);
            }
        });
            return convertView;


    }

    class ViewHolder{
        LinearLayout ll_chat;
    }

}
