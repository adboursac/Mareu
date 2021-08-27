package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import java.util.Arrays;

public class TimeFilterFragment extends Fragment {

    private MeetingViewModel mMeetingViewModel;
    private FragmentHourFilterBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentHourFilterBinding.inflate(inflater, container, false);

        initData();
        initHourPickers();
        initApplyButton();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    private void initHourPickers() {
        mBinding.fromTimeInput.setText(mMeetingViewModel.getFromTimeString(getContext()));
        TimePicker.setTimePickerOnTextInput(mBinding.fromTimeInput, getParentFragmentManager());

        mBinding.toTimeInput.setText(mMeetingViewModel.getToTimeString(getContext()));
        TimePicker.setTimePickerOnTextInput(mBinding.toTimeInput, getParentFragmentManager());
    }

    private void updateFilterDescription(String input, String output) {
        if (input=="" && output=="") {
            mBinding.filterDescriptionInput.setText("PAS de filtre");
        }
        else {

        }
    }

    public void initApplyButton() {
        mBinding.applyButton.setOnClickListener(v -> {
            mMeetingViewModel.setTimeFilter(
                    mBinding.fromTimeInput.getText().toString(),
                    mBinding.toTimeInput.getText().toString()
            );
            mMeetingViewModel.applyFilters();
            Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.hour_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hour_filter_cancel:
                mBinding.fromTimeInput.setText("");
                mBinding.toTimeInput.setText("");
                break;
            default:
                Log.w("TimeFilterFragment", "onOptionsItemSelected: didn't match any menu item");
        }
        return true;
    }
}
