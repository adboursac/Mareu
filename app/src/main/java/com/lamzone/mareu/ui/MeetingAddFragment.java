package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
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
    private Room mSelectedRoom;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentMeetingAddBinding.inflate(inflater, container, false);

        initData();
        initAddButton();
        initRoomDropMenu(mBinding.roomDropDownMenu);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    public void initAddButton() {
        mBinding.addButton.setOnClickListener(v -> {
            String validationMessage = mMeetingViewModel.addMeeting(generateMeeting());

            if (validationMessage == null) {
                Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
            }
            else {
                Toast.makeText(getContext(), validationMessage,1000);
            }
        });
    }

    private void initRoomDropMenu(AutoCompleteTextView dropMenu) {
        final RoomDropListAdapter adapter = new RoomDropListAdapter(requireContext(), Room.values());
        dropMenu.setAdapter(adapter);
        dropMenu.setOnItemClickListener((parent, view, position, id) ->
                mSelectedRoom = adapter.getItem(position)
        );
    }

    private Meeting generateMeeting() {
        Meeting meeting = new Meeting(
                System.currentTimeMillis(),
                mBinding.title.getText().toString(),
                mSelectedRoom);
        return meeting;
    }
}
