package com.lamzone.mareu.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.lamzone.mareu.R;
import com.lamzone.mareu.data.meeting.model.Room;
import com.lamzone.mareu.databinding.RoomItemBinding;

public class RoomDropListAdapter extends ArrayAdapter<Room> {

    private Context mContext;
    private RoomItemBinding mRoomDropdownItemBinding;

    public RoomDropListAdapter(@NonNull Context context, Room[] rooms) {
        super(context, R.layout.room_item, rooms);
        mContext = context;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return getCustomView(position, parent);
    }

    @NonNull
    public View getCustomView(int position, @NonNull ViewGroup parent) {
        Room room = getItem(position);

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View row = inflater.inflate(R.layout.room_item, parent, false);

        ImageView circle = row.findViewById(R.id.room_item_circle);
        TextView letter = row.findViewById(R.id.room_item_icon_letter);
        TextView name = row.findViewById(R.id.room_item_name);

        int roomColor = mContext.getResources().getColor(room.getColor());
        String roomName = mContext.getResources().getString(room.getName());

        circle.setColorFilter(roomColor);
        letter.setText(Room.getLetter(roomName));
        name.setText(roomName);

        return row;
    }
}
