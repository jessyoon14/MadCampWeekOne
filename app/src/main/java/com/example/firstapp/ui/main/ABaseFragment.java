package com.example.firstapp.ui.main;

import android.app.Activity;
import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

public abstract class ABaseFragment extends Fragment {

    protected IActivityEnabledListener aeListener;

    protected interface IActivityEnabledListener{
        void onActivityEnabled(FragmentActivity activity);
    }

    protected void getAvailableActivity(IActivityEnabledListener listener){
        if (getActivity() == null){
            aeListener = listener;

        } else {
            listener.onActivityEnabled(getActivity());
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (aeListener != null){
            aeListener.onActivityEnabled((FragmentActivity) activity);
            aeListener = null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (aeListener != null){
            aeListener.onActivityEnabled((FragmentActivity) context);
            aeListener = null;
        }
    }
}