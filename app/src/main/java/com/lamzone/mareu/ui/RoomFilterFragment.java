package com.lamzone.mareu.ui;

import android.os.Bundle;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.RoomSelectCommand;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.FragmentMeetingListBinding;
import com.lamzone.mareu.databinding.FragmentRoomFilterBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RoomFilterFragment extends Fragment implements RoomSelectCommand {

    private Room[] mRooms;
    private boolean[] mSelected;
    private MeetingViewModel mMeetingViewModel;

    private FragmentRoomFilterBinding mBinding;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentRoomFilterBinding.inflate(inflater, container, false);

        initData();
        initRecyclerView(mBinding.getRoot());
        initRoomFilter();
        initApplyButton();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initRecyclerView(View root) {
        mRecyclerView = mBinding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        RoomFilterRecyclerViewAdapter mAdapter = new RoomFilterRecyclerViewAdapter(mRooms, mSelected, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
        mRooms = mMeetingViewModel.getRooms();
        mSelected = mMeetingViewModel.getSelectedRoomsLiveData().getValue();
    }

    private void initRoomFilter() {
        mMeetingViewModel.getSelectedRoomsLiveData().observe(getViewLifecycleOwner(), selected -> {
            mSelected = selected;
            mRecyclerView.getAdapter().notifyDataSetChanged();
            mMeetingViewModel.updateFilteredMeetings();
        });
    }

    private void initApplyButton() {
        mBinding.applyButton.setOnClickListener(v -> {
            mMeetingViewModel.updateFilteredMeetings();
            Navigation.findNavController(v).navigate(R.id.navigateToMeetingList);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.room_filter_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.room_filter_select_all:
                mMeetingViewModel.filterSelectAll();
                break;
            default:
                mMeetingViewModel.filterClear();
        }
        return true;
    }

    @Override
    public void selectRoom(Room room) {
        mMeetingViewModel.toggleRoomState(room);
    }
}



