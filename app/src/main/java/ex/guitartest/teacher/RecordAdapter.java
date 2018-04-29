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

public class RecordAdapter extends BaseAdapter {
    private Context context;
    private int RECORD_VIEW = 0;
    private int RECORD_ADD = 1;

    public RecordAdapter(Context context) {
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
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 2){
            return RECORD_ADD;
        }else {
            return RECORD_VIEW;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (position == 2){
            ViewHolderAdd viewHolderAdd;
            if (convertView == null){
                viewHolderAdd = new ViewHolderAdd();
                convertView = View.inflate(context, R.layout.item_add, null);
                viewHolderAdd.ll_add = convertView.findViewById(R.id.ll_add);
                convertView.setTag(viewHolderAdd);
            }else {
                viewHolderAdd = (ViewHolderAdd) convertView.getTag();
            }
            viewHolderAdd.ll_add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,VideoAddActivity.class);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }else {
            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = View.inflate(context, R.layout.item_record, null);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            return convertView;
        }

    }

    class ViewHolder{

    }
    class ViewHolderAdd{
        LinearLayout ll_add;
    }
}
