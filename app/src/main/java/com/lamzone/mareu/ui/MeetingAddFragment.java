package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.FragmentMeetingAddBinding;

import java.util.ArrayList;
import java.util.List;

public class MeetingAddFragment extends Fragment {

    private MeetingViewModel mMeetingViewModel;
    private FragmentMeetingAddBinding mBinding;
    private Room mSelectedRoom;
    private List<String> mMemberList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMeetingAddBinding.inflate(inflater, container, false);

        initData();
        initAddButton();
        initRoomDropMenu(mBinding.roomInput);
        TimePicker.setTimePickerOnTextInput(mBinding.startTimeInput, getChildFragmentManager());
        TimePicker.setTimePickerOnTextInput(mBinding.endTimeInput, getChildFragmentManager());
        initMemberListItems();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
    }

    public void initAddButton() {
        mBinding.addButton.setOnClickListener(v -> {
            Meeting meeting = generateMeeting();
            String resultMessage = mMeetingViewModel.checkMeetingValidity(meeting, getContext());

            if (resultMessage == "") {
                mMeetingViewModel.addMeeting(meeting);
                Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
            } else {
                Toast.makeText(getContext(), resultMessage, Toast.LENGTH_LONG).show();
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

    private void initMemberListItems() {
        mBinding.memberMailLayout.setEndIconVisible(false);

        mBinding.memberMailLayout.setEndIconOnClickListener(v -> {
            mMemberList.add(0, mBinding.memberMailInput.getText().toString());
            mBinding.memberMailInput.setText("");
            String memberList = MeetingViewModel.listToString(mMemberList, "\n");
            mBinding.membersListInput.setText(memberList);
        });

        mBinding.memberMailInput.addTextChangedListener(createTextWatcher());

        mBinding.membersListLayout.setEndIconOnClickListener(v -> {
            mMemberList.clear();
            mBinding.membersListInput.setText("");
        });
    }

    private TextWatcher createTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isEmailValid = mMeetingViewModel.isEmailValid(s.toString());
                if (isEmailValid) mBinding.memberMailLayout.setEndIconVisible(true);
                else mBinding.memberMailLayout.setEndIconVisible(false);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
    }

    private Meeting generateMeeting() {
        return new Meeting(
                System.currentTimeMillis(),
                mBinding.titleInput.getText().toString(),
                mSelectedRoom,
                mMeetingViewModel.stringToLocalTime(mBinding.startTimeInput.getText().toString()),
                mMeetingViewModel.stringToLocalTime(mBinding.endTimeInput.getText().toString()),
                mMemberList
        );
    }
}
