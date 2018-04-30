package ex.guitartest.teacher;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import ex.guitartest.R;

/**
 *
 * Created by qyxlx on 2018/4/6 0006.
 */

public class StudentListAdapter extends BaseAdapter {
    private Context context;
    private String[] data;
    public StudentListAdapter(Context context,String[] data) {
        this.context = context;
        this.data=data;
    }

    @Override
    public int getCount() {
        return 9;
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
            convertView = View.inflate(context, R.layout.item_student, null);
            viewHolder.ll_picture = convertView.findViewById(R.id.ll_picture);
            viewHolder.ll_text=convertView.findViewById(R.id.ll_text);
            viewHolder.ll_text.setText(data[position]);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ll_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,ChatActivity.class);
                context.startActivity(intent);
            }
        });
        return convertView;


    }

    class ViewHolder{
        ImageView ll_picture;
        TextView ll_text;
    }

}
