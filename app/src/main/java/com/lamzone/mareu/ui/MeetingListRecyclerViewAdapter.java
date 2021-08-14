package com.lamzone.mareu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.data.meeting.MeetingDeleteCommand;
import com.lamzone.mareu.data.meeting.MeetingViewModel;
import com.lamzone.mareu.data.meeting.model.Meeting;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.MeetingItemBinding;

import java.text.BreakIterator;
import java.time.format.DateTimeFormatter;
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
        String roomName = mContext.getResources().getString(meeting.getRoom().getName());
        String roomLetter = Room.getLetter(roomName);
        String time = meeting.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"));
        int roomColor = mContext.getResources().getColor(meeting.getRoom().getColor());

        String title = meeting.getTitle() + " • " + time + " • " + roomName;
        String memberList =  MeetingViewModel.listToString(meeting.getMemberList(), ", ");
        holder.mMeetingItemBinding.itemTitle.setText(title);
        holder.mMeetingItemBinding.itemMembers.setText(memberList);
        holder.mMeetingItemBinding.itemRoomIcon.setColorFilter(roomColor);
        holder.mMeetingItemBinding.itemIconLetter.setText(roomLetter);
        holder.mMeetingItemBinding.itemDeleteButton.setOnClickListener(v -> mDeleteCommand.deleteMeeting(meeting));
        //holder.itemView.setOnClickListener(v -> activity.navigate(v.getContext(), meeting.getId()));
    }

    @Override
    public int getItemCount() {
        return mMeetings.size();
    }
}
