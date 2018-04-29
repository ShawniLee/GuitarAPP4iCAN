package ex.guitartest.teacher;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import ex.guitartest.R;
import ex.guitartest.VideoActivity;

/**
 * Created by qyxlx on 2018/4/6.
 * 首页adapter
 */

public class WorksAdapter extends BaseAdapter {
    private Context context;

    public WorksAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_work, null);
            viewHolder.ll_player = convertView.findViewById(R.id.ll_player);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ll_player.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, VideoActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    class ViewHolder{
        LinearLayout ll_player;

    }
}
