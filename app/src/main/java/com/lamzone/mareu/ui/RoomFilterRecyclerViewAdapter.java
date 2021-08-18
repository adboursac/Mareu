package com.lamzone.mareu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.RoomFilterSelectCommand;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.RoomItemBinding;

public class RoomFilterRecyclerViewAdapter extends RecyclerView.Adapter<RoomFilterRecyclerViewAdapter.ViewHolder> {

    private final Room[] mRooms;
    private boolean[] mSelected;
    private RoomFilterSelectCommand mSelectCommand;
    private Context mContext;

    public RoomFilterRecyclerViewAdapter(Room[] rooms, boolean[] selected, RoomFilterSelectCommand selectCommand) {
        mRooms = rooms;
        mSelected = selected;
        mSelectCommand = selectCommand;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private RoomItemBinding mRoomItemBinding;

        public ViewHolder(RoomItemBinding roomItemBinding) {
            super(roomItemBinding.getRoot());
            mRoomItemBinding = roomItemBinding;
        }
    }

    @NonNull
    @Override
    public RoomFilterRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new RoomFilterRecyclerViewAdapter.ViewHolder(RoomItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    @Override
    public void onBindViewHolder(final RoomFilterRecyclerViewAdapter.ViewHolder holder, int position) {
        Room room = mRooms[position];
        boolean selected = mSelected[position];

        String roomName = mContext.getResources().getString(room.getName());
        String roomLetter = Room.getLetter(roomName);
        int roomColor = mContext.getResources().getColor(room.getColor());
        int selectColor = mContext.getResources().getColor(R.color.blue_200);
        int whiteColor = mContext.getResources().getColor(R.color.white);

        holder.mRoomItemBinding.roomItemName.setText(roomName);
        holder.mRoomItemBinding.roomItemIconLetter.setText(roomLetter);
        holder.mRoomItemBinding.roomItemCircle.setColorFilter(roomColor);

        if (selected) {
            holder.itemView.setBackgroundColor(selectColor);
        }
        else {
            holder.itemView.setBackgroundColor(whiteColor);
        }

        holder.itemView.setOnClickListener(v -> mSelectCommand.selectRoom(position));
    }

    @Override
    public int getItemCount() {
        return mRooms.length;
    }
}
