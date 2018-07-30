package com.ken.expressquery.mainui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ken.expressquery.R;

/**
 * 主界面的第fragment,暂时弃用
 *
 * @author by ken on 2017/9/10.
 */

public class SendFragment extends Fragment implements
        View.OnClickListener {

    private View view;

    public static SendFragment newInstance(String param1) {
        SendFragment fragment = new SendFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_send, container, false);

        return view;
    }

    @Override
    public void onClick(View v) {

    }
}
