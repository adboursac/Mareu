package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.FragmentMeetingAddBinding;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

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
        initRoomDropMenu(mBinding.roomInput);
        TimePicker.setTimePickerOnTextInput(mBinding.startTimeInput, getChildFragmentManager());
        TimePicker.setTimePickerOnTextInput(mBinding.endTimeInput, getChildFragmentManager());
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    public void initAddButton() {
        mBinding.addButton.setOnClickListener(v -> {
            int validationMessage = mMeetingViewModel.addMeeting(generateMeeting());

            if (validationMessage == 0) {
                Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
            }
            else {
                String message = getResources().getString(validationMessage);
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
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
        return new Meeting(
                System.currentTimeMillis(),
                mBinding.titleInput.getText().toString(),
                mSelectedRoom,
                LocalTime.parse(mBinding.startTimeInput.getText().toString(), DateTimeFormatter.ofPattern("HH:mm")),
                LocalTime.parse(mBinding.endTimeInput.getText().toString(), DateTimeFormatter.ofPattern("HH:mm"))
        );
    }
}
