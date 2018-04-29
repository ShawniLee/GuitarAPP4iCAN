package ex.guitartest.teacher;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ex.guitartest.R;

/**
 *
 * Created by qyxlxr on 2018/4/30 0006.
 */

public class ChatAdapter extends BaseAdapter {
    private Context context;
    private List<String> datas = new ArrayList<>();

    public ChatAdapter(Context context, List<String> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int i) {
        return datas.get(i);
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
                convertView = View.inflate(context, R.layout.item_chat_me, null);
                viewHolder.tv_send = convertView.findViewById(R.id.tv_send);
                viewHolder.tv_send.setText(datas.get(position));
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tv_send = convertView.findViewById(R.id.tv_send);
            return convertView;


    }

    class ViewHolder{
       TextView tv_send;
    }

}
