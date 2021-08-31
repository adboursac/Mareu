package com.lamzone.mareu.ui;

import android.os.Bundle;
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

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.databinding.FragmentDateFilterBinding;

public class DateFilterFragment extends Fragment {

    private MeetingViewModel mMeetingViewModel;
    private FragmentDateFilterBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentDateFilterBinding.inflate(inflater, container, false);

        initData();
        initDatePickers();
        initApplyButton();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    private void initDatePickers() {
        mBinding.dateInput.setText(mMeetingViewModel.getDateFilterString(getContext()));
        DatePicker.setDatePickerOnTextInput(mBinding.dateInput, getParentFragmentManager());
    }

    public void initApplyButton() {
        mBinding.applyButton.setOnClickListener(v -> {
            mMeetingViewModel.setDateFilter(mBinding.dateInput.getText().toString());
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
                mBinding.dateInput.setText(R.string.meetingDate);
                break;
            default:
                Log.w("DateFilterFragment", "onOptionsItemSelected: didn't match any menu item");
        }
        return true;
    }
}
