package com.admin.huangchuan.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.admin.huangchuan.R;
import com.admin.huangchuan.View.vhtableview.MarkdownView;
import com.admin.huangchuan.activity.EditorActivity1;


/**
 * Created by zk on 2017/12/11.
 */

public class PreviewFragment extends Fragment {

    private LinearLayout main;

    public MarkdownView markdownView;

    private EditorActivity1 activity;

    private static final String TAG = PreviewFragment.class.getSimpleName();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (EditorActivity1) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (main == null) {
            main = (LinearLayout) inflater.inflate(R.layout.fragment_preview, container, false);
            initViews(main);
        }
        return main;
    }

    private void initViews(LinearLayout main) {
        markdownView = (MarkdownView) main.findViewById(R.id.markdownView);

        markdownView.setLinkClickListener(activity.getLinkClickListener());
        markdownView.setImgClickListener(activity.getImgClickListener());
    }

    public void load(String text) {
        Log.d("reg","text:"+text);
        markdownView.setTextInBackground(text);
    }
}
