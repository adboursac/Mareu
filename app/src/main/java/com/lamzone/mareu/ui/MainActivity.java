package com.lamzone.mareu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.databinding.ActivityMainBinding;

import static androidx.navigation.Navigation.findNavController;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private MeetingViewModel mMeetingViewModel;

    private void initUI() {
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        setSupportActionBar(mBinding.toolbar);
    }

    private void initNavigationBar() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        AppBarConfiguration appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupWithNavController(
                mBinding.toolbar, navController, appBarConfiguration);
    }

    private void initViewModel() {
        mMeetingViewModel = new ViewModelProvider(this).get(MeetingViewModel.class);
        mMeetingViewModel.getMeetings();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initNavigationBar();
        initViewModel();
    }
}