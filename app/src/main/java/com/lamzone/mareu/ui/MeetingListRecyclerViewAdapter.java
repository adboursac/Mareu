package com.lamzone.mareu.ui;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.data.meeting.MeetingDeleteCommand;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.MeetingItemBinding;

import java.util.List;

public class MeetingListRecyclerViewAdapter extends RecyclerView.Adapter<MeetingListRecyclerViewAdapter.ViewHolder> {

    private final List<Meeting> mMeetings;
    private MeetingDeleteCommand mDeleteCommand;
    private Context mContext;

    public MeetingListRecyclerViewAdapter(List<Meeting> meetings, MeetingDeleteCommand deleteCommand) {
        mMeetings = meetings;
        mDeleteCommand = deleteCommand;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private MeetingItemBinding mMeetingItemBinding;

        public ViewHolder(MeetingItemBinding meetingItemBinding) {
            super(meetingItemBinding.getRoot());
            mMeetingItemBinding = meetingItemBinding;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new ViewHolder(MeetingItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Meeting meeting = mMeetings.get(position);
        Resources resources = mContext.getResources();
        Room room = meeting.getRoom();

        holder.mMeetingItemBinding.itemTitle.setText(meeting.shortDescription(resources));
        holder.mMeetingItemBinding.itemMembers.setText(meeting.memberListToString(meeting.getMemberList(), ", "));
        holder.mMeetingItemBinding.itemRoomIcon.setColorFilter(room.getColor(resources));
        holder.mMeetingItemBinding.itemIconLetter.setText(room.getLetter(resources));
        holder.mMeetingItemBinding.itemDeleteButton.setOnClickListener(v -> mDeleteCommand.deleteMeeting(meeting));
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }
}
