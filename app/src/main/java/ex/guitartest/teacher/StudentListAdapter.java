package ex.guitartest.teacher;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.assistne.icondottextview.IconDotTextView;

import ex.guitartest.R;

/**
 *
 * Created by qyxlx on 2018/4/6 0006.
 */

public class StudentListAdapter extends BaseAdapter {
    private Context context;
    private String[] data;
    private ViewHolder viewHolder;
    private boolean haveMessage[];
    public StudentListAdapter(Context context,String[] data,boolean notify[]) {
        this.context = context;
        this.data=data;
        this.haveMessage=notify;

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
    public View getView(int position, View convertView, final ViewGroup viewGroup) {

        if (convertView == null){

            viewHolder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_student, null);
            viewHolder.ll_IconDot = convertView.findViewById(R.id.iconDot);
            viewHolder.ll_IconDot.setText(data[position]);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (haveMessage[0]&&position==0)
            viewHolder.ll_IconDot.setDotVisibility(true);
//        viewHolder.ll_IconDot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                viewHolder.ll_IconDot.setDotVisibility(false);
//            }
//        });
        return convertView;


    }

    class ViewHolder{
        IconDotTextView ll_IconDot;
    }

}
