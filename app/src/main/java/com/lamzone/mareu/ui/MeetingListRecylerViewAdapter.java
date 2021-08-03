package com.lamzone.mareu.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.databinding.MeetingItemBinding;

import java.util.List;

public class MeetingListRecylerViewAdapter extends RecyclerView.Adapter<MeetingListRecylerViewAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;

    public MeetingListRecylerViewAdapter(List<Meeting> meetings) {
        mMeetings = meetings;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MeetingItemBinding meetingItemBinding;

        public ViewHolder(MeetingItemBinding meetingItemBinding) {
            super(meetingItemBinding.getRoot());
            this.meetingItemBinding = meetingItemBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(MeetingItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = mMeetings.get(position);
        holder.meetingItemBinding.itemTitle.setText(meeting.getTitle());
        //holder.itemView.setOnClickListener(v -> activity.navigate(v.getContext(), meeting.getId()));
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }
}
