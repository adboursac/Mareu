package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.lamzone.mareu.R;
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
        initHourPickers();
        initHoursFilterObserver();
        initApplyButton();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    private void initHourPickers() {
        mBinding.fromTimeInput.setText(mMeetingViewModel.getHourFilterFromTimeString(getContext()));
        mBinding.fromTimeInput.addTextChangedListener(createTextWatcher(mBinding.fromTimeInput, mBinding.toTimeInput));
        TimePicker.setTimePickerOnTextInput(mBinding.fromTimeInput, getParentFragmentManager());

        mBinding.toTimeInput.setText(mMeetingViewModel.getHourFilterToTimeString(getContext()));
        mBinding.toTimeInput.addTextChangedListener(createTextWatcher(mBinding.fromTimeInput, mBinding.toTimeInput));
        TimePicker.setTimePickerOnTextInput(mBinding.toTimeInput, getParentFragmentManager());
    }

    private void initHoursFilterObserver() {
        mMeetingViewModel.getTimeFilter().observe(getViewLifecycleOwner(), hourRange -> {
            mMeetingViewModel.applyFilters();
        });
    }

    public void initApplyButton() {
        mBinding.applyButton.setOnClickListener(v -> {
            mMeetingViewModel.applyFilters();
            Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
        });
    }

    private TextWatcher createTextWatcher(TextInputEditText from, TextInputEditText to) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMeetingViewModel.setHourRange(from.getText().toString(), to.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.hour_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        mBinding.fromTimeInput.setText("");
        mBinding.toTimeInput.setText("");
        return true;
    }
}
