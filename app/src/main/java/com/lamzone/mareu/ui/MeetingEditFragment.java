package com.lamzone.mareu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.lamzone.mareu.databinding.FragmentMeetingEditBinding;

public class MeetingEditFragment extends Fragment {

    @Override
    public View onCreateView (LayoutInflater inflater,
                              ViewGroup container,
                              Bundle savedInstanceState) {
        FragmentMeetingEditBinding binding = FragmentMeetingEditBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;
    }
}
