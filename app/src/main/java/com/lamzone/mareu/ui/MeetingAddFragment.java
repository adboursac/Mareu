package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.FragmentMeetingAddBinding;
import com.lamzone.mareu.databinding.FragmentMeetingListBinding;

import java.util.ArrayList;
import java.util.List;

public class MeetingAddFragment extends Fragment {

    private MeetingViewModel mMeetingViewModel;

    private FragmentMeetingAddBinding mBinding;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentMeetingAddBinding.inflate(inflater, container, false);

        initData();
        initAddButton();

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    public void initAddButton() {
        mBinding.addButton.setOnClickListener(v -> {
            mMeetingViewModel.addMeeting(generateMeeting());
            Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
        });
    }

    public Meeting generateMeeting() {
        Meeting meeting = new Meeting(
                System.currentTimeMillis(),
                mBinding.title.getText().toString(),
                Room.Kirby);
        return meeting;
    }
}
