package com.bsoft.hospital.pub.suzhoumh.fragment.index;

import com.app.tanklib.bitmap.IndexUrlCache;
import com.app.tanklib.view.BsoftActionBar;
import com.bsoft.hospital.pub.suzhoumh.R;
import com.bsoft.hospital.pub.suzhoumh.AppApplication;
import com.bsoft.hospital.pub.suzhoumh.model.LoginUser;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    View mainView;
    protected boolean isCreated = false;
    protected boolean isLoaded = false;
    protected boolean isReceiver = false;
    AppApplication application;
    public LoginUser loginUser;
    Context baseContext;
    IndexUrlCache urlMap;

    BsoftActionBar actionBar;
    protected Unbinder mUnbinder;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isCreated) {
            return;
        }
        if (isVisibleToUser) {
            isReceiver = true;
            startHint();
        } else {
            endHint();
        }
    }

    public abstract void startHint();

    public abstract void endHint();

    public void findActionBar() {
        actionBar = (BsoftActionBar) mainView.findViewById(R.id.actionbar);
        actionBar.setBackGround(getResources().getColor(R.color.actionbar_bg));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseContext = getActivity();
        isCreated = true;
        application = (AppApplication) getActivity().getApplication();
        loginUser = application.getLoginUser();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }
}
