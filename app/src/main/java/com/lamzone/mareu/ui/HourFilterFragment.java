package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.databinding.FragmentHourFilterBinding;

public class HourFilterFragment extends Fragment {

    private MeetingViewModel mMeetingViewModel;
    private FragmentHourFilterBinding mBinding;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentHourFilterBinding.inflate(inflater, container, false);

        initData();
        initApplyButton();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    public void initApplyButton() {
        mBinding.applyButton.setOnClickListener(v -> {

        });
    }
}
