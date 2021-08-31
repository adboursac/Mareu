package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.data.meeting.MeetingDeleteCommand;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.databinding.FragmentMeetingListBinding;

import java.util.ArrayList;
import java.util.List;

public class MeetingListFragment extends Fragment implements MeetingDeleteCommand {

    private List<Meeting> mMeetings = new ArrayList<>();
    private MeetingViewModel mMeetingViewModel;

    private FragmentMeetingListBinding mBinding;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentMeetingListBinding.inflate(inflater, container, false);

        initRecyclerView(mBinding.getRoot());
        initData();
        initAddButton();
        setHasOptionsMenu(true);
        mMeetingViewModel.applyFilters();

        return mBinding.getRoot();
    }

    private void initRecyclerView(View root) {
        mRecyclerView = mBinding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        MeetingListRecyclerViewAdapter mAdapter = new MeetingListRecyclerViewAdapter(mMeetings, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
        mMeetingViewModel.getMeetingsLiveData().observe(getViewLifecycleOwner(), meetings -> {
            mMeetings.clear();
            mMeetings.addAll(meetings);
            if (meetings.size() == 0) {
                Toast.makeText(getContext(), R.string.emptyMeetingList, Toast.LENGTH_LONG).show();
            }
            mRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void initAddButton() {
        mBinding.addButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigateToMeetingAdd);
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.meeting_list_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.meeting_menu_filter_room:
                Navigation.findNavController(getView()).navigate(R.id.navigateToRoomFilter);
                break;
            case R.id.meeting_menu_filter_date:
                Navigation.findNavController(getView()).navigate(R.id.navigateToDateFilter);
                break;
            default:
                Log.w("MeetingListFragment", "onOptionsItemSelected: didn't match any menu item");
        }
        return true;
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        mMeetingViewModel.deleteMeeting(meeting);
    }
}