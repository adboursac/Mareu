package com.lamzone.mareu.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lamzone.mareu.data.meeting.MeetingDateTimeHelper;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.databinding.FragmentMeetingDetailsBinding;

public class MeetingDetailsFragment extends Fragment {

    private MeetingViewModel mMeetingViewModel;
    private FragmentMeetingDetailsBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMeetingDetailsBinding.inflate(inflater, container, false);

        initData();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
        long meetingId = MeetingDetailsFragmentArgs.fromBundle(getArguments()).getMeetingId();
        Meeting meeting = mMeetingViewModel.getMeeting(meetingId);

        mBinding.titleInput.setText(meeting.getTitle());
        mBinding.roomInput.setText(meeting.getRoom().getName(getResources()));
        mBinding.startTimeInput.setText(MeetingDateTimeHelper.toString(meeting.getStart()));
        mBinding.endTimeInput.setText(MeetingDateTimeHelper.toString(meeting.getEnd()));
        mBinding.membersListInput.setText(Meeting.memberListToString(meeting.getMemberList(), "\n"));
    }
}