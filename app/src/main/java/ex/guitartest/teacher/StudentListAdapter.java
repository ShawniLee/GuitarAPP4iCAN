package ex.guitartest.teacher;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import ex.guitartest.R;

/**
 *
 * Created by qyxlx on 2018/4/6 0006.
 */

public class StudentListAdapter extends BaseAdapter {
    private Context context;

    public StudentListAdapter(Context context) {
        this.context = context;
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
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            return convertView;


    }

    class ViewHolder{

    }
    class ViewHolderAdd{

    }
}
