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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.RoomFilterSelectCommand;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.FragmentRoomFilterBinding;

import java.util.Arrays;

public class RoomFilterFragment extends Fragment implements RoomFilterSelectCommand {

    private Room[] mRooms;
    private boolean[] mRoomFilter;
    private boolean mEmptyRoomFilter;
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
        initApplyButton();
        setHasOptionsMenu(true);

        return mBinding.getRoot();
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
        mRooms = mMeetingViewModel.getRooms();
        mRoomFilter = mMeetingViewModel.getRoomFilter().clone();
        mEmptyRoomFilter = mMeetingViewModel.isEmptyRoomFilter();
    }

    private void initRecyclerView(View root) {
        mRecyclerView = mBinding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        RoomFilterRecyclerViewAdapter mAdapter = new RoomFilterRecyclerViewAdapter(mRooms, mRoomFilter, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initApplyButton() {
        mBinding.applyButton.setOnClickListener(v -> {
            mMeetingViewModel.setRoomFilter(mRoomFilter);
            mMeetingViewModel.setEmptyRoomFilter(mEmptyRoomFilter);
            mMeetingViewModel.applyFilters();
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
                Arrays.fill(mRoomFilter, true);
                mEmptyRoomFilter = true;
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            case R.id.room_filter_cancel:
                Arrays.fill(mRoomFilter, false);
                mEmptyRoomFilter = true;
                mRecyclerView.getAdapter().notifyDataSetChanged();
                break;
            default:
                Log.w("RoomFilterFragment","onOptionsItemSelected: didn't match any menu item");
        }
        return true;
    }

    @Override
    public void selectRoom(int position) {
        mRoomFilter[position] = ! mRoomFilter[position];
        mEmptyRoomFilter = false;
        mRecyclerView.getAdapter().notifyDataSetChanged();
    }
}



