package com.example.cpre388.cuisine.APIs;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cpre388.cuisine.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

/**
 * RecyclerView adapter for a list of Restaurants.
 */
public class ReservationAdapter extends FirestoreAdapter<ReservationAdapter.ViewHolder> {

    public interface OnReservationSelectedListener {

        void onReservationSelected(DocumentSnapshot reservation);

    }

    private OnReservationSelectedListener mListener;

    public ReservationAdapter(Query query, OnReservationSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_reservation, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView nameView;
        TextView contactView;
        TextView numView;
        TextView timeView;

        public ViewHolder(View itemView) {
            super(itemView);
            nameView = itemView.findViewById(R.id.reservation_item_name);
            numView = itemView.findViewById(R.id.reservation_item_amount);
            timeView = itemView.findViewById(R.id.reservation_item_time);
            contactView = itemView.findViewById(R.id.reservation_item_contact);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnReservationSelectedListener listener) {

            String name = snapshot.getString("reservation_for");
            String time = snapshot.getString("reservation_time");
            String guests = snapshot.getString("num_guests");
            String contact = snapshot.getString("contact_information");

            Resources resources = itemView.getResources();

            nameView.setText(String.format("Guest: %s", name));
            timeView.setText(String.format("Arrival: %s", time));
            numView.setText(String.format("Table for: %s", guests));
            contactView.setText(String.format("Contact information: %s", contact));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onReservationSelected(snapshot);
                    }
                }
            });
        }

    }
}
