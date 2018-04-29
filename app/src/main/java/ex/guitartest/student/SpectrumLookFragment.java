package ex.guitartest.student;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ex.guitartest.R;

/**
 *
 * Created by qyxlx on 2018/4/14.
 */

public class SpectrumLookFragment extends Fragment {
    @BindView(R.id.list_spectrum)
    ListView list_spectrum;
    private  SpectrumLookAdapter spectrumLookAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_spectrum_look,null);
        ButterKnife.bind(this,view);
        initView();
        return view;
    }

    private void initView(){
        spectrumLookAdapter = new SpectrumLookAdapter(getActivity());
        list_spectrum.setAdapter(spectrumLookAdapter);
    }
}
