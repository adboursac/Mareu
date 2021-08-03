package com.lamzone.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MeetingViewModel mMeetingViewModel;

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
    }

    private void initViewModel() {
        mMeetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        mMeetingViewModel.fetchMeetings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initViewModel();
    }
}