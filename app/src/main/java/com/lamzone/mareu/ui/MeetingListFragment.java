package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.data.di.DI;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.service.MeetingApiService;
import com.lamzone.mareu.databinding.FragmentMeetingListBinding;

import java.util.ArrayList;
import java.util.List;

public class MeetingListFragment extends Fragment {

    private static final MeetingApiService service  = DI.getMeetingApiService();
    private List<Meeting> mMeetings;
    private FragmentMeetingListBinding mBinding;
    private MeetingViewModel mMeetingViewModel;
    private RecyclerView mRecyclerView;

    private void initData() {
        mMeetings = new ArrayList<>();
        mMeetingViewModel = new ViewModelProvider(requireActivity()).get(MeetingViewModel.class);
        mMeetingViewModel.getMutableLiveData().observe(getViewLifecycleOwner(), meetings -> {
            mMeetings.clear();
            mMeetings.addAll(meetings);
            mRecyclerView.getAdapter().notifyDataSetChanged();
        });
    }

    private void initRecyclerView(View root) {
        mRecyclerView = mBinding.recyclerView;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        MeetingListRecylerViewAdapter mAdapter = new MeetingListRecylerViewAdapter(mMeetings);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        mBinding = FragmentMeetingListBinding.inflate(inflater, container, false);
        mBinding.addButton.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigateToMeetingAdd);
        });

        initData();
        initRecyclerView(mBinding.getRoot());
        return mBinding.getRoot();
    }
}
