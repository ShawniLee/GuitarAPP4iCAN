package ex.guitartest.teacher;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ex.guitartest.R;
import ex.guitartest.bean.SpectrumBean;
import ex.guitartest.util.PinyinUtils;

/**
 * author zaaach on 2016/1/26.
 */
public class SpectrumListAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater inflater;
    private List<SpectrumBean> mSpectrumBeans;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnItemClickListener onItemClickListener;


    public SpectrumListAdapter(Context mContext, List<SpectrumBean> mSpectrumBeans) {
        this.mContext = mContext;
        this.mSpectrumBeans = mSpectrumBeans;
        this.inflater = LayoutInflater.from(mContext);
        if (mSpectrumBeans == null){
            mSpectrumBeans = new ArrayList<>();
        }
        int size = mSpectrumBeans.size();
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++){
            //当前item拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mSpectrumBeans.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mSpectrumBeans.get(index - 1).getPinyin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)){
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }


    /**
     * 获取字母索引的位置
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter){
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }


    @Override
    public int getCount() {
        return mSpectrumBeans == null ? 0: mSpectrumBeans.size();
    }

    @Override
    public SpectrumBean getItem(int position) {
        return mSpectrumBeans == null ? null : mSpectrumBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        int viewType = getItemViewType(position);

                if (view == null){
                    view = inflater.inflate(R.layout.item_spectrum, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_name);
                    holder.view_divider =  view.findViewById(R.id.view_divider);
                    view.setTag(holder);
                }else{
                    holder = (CityViewHolder) view.getTag();
                }
                    final String name = mSpectrumBeans.get(position).getName();
                    holder.name.setText(name);
                    String currentLetter = PinyinUtils.getFirstLetter(mSpectrumBeans.get(position).getPinyin());
                    String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mSpectrumBeans.get(position-1).getPinyin()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)){
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.letter.setText(currentLetter);
                        holder.view_divider.setVisibility(View.VISIBLE);
                    }else{
                        holder.letter.setVisibility(View.GONE);
                        holder.view_divider.setVisibility(View.GONE);
                    }
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onItemClickListener != null){
                                onItemClickListener.onClick(name);
                            }
                        }
                    });


        return view;
    }

    public static class CityViewHolder{
        TextView letter;
        TextView name;
        View view_divider;
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.onItemClickListener = listener;
    }

    public interface OnItemClickListener{
        void onClick(String name);

    }
}
