package com.example.cpre388.cuisine.APIs;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.cpre388.cuisine.Models.bill_model;
import com.example.cpre388.cuisine.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;


/**
 * RecyclerView adapter for a list of Bills. -Adapted from the Firebase lab restaurant adapter
 */
public class BillAdapter extends FirestoreAdapter<BillAdapter.ViewHolder> {

    public interface OnBillSelectedListener {

        void onBillSelected(DocumentSnapshot bill);

    }

    final OnBillSelectedListener mListener;

    public BillAdapter(Query query, OnBillSelectedListener listener) {
        super(query);
        mListener = listener;
    }

    // Methods for getting the layout and viewfinder stuff set up
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ViewHolder(inflater.inflate(R.layout.item_bill, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getSnapshot(position), mListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        //  Defining appropriate textviews
        TextView foodView, drinksView, refillsView, numFoodView, numDrinksView, numRefillsView, tipView, totView;

        public ViewHolder(View itemView) {
            super(itemView);

            // For linking appropriate textviews
            foodView = itemView.findViewById(R.id.textViewF);
            drinksView = itemView.findViewById(R.id.textViewD);
            refillsView = itemView.findViewById(R.id.textViewR);
            numFoodView = itemView.findViewById(R.id.textViewNF);
            numDrinksView = itemView.findViewById(R.id.textViewND);
            numRefillsView = itemView.findViewById(R.id.textViewNR);
            tipView = itemView.findViewById(R.id.textViewT);
            totView = itemView.findViewById(R.id.textViewTot);

        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnBillSelectedListener listener) {

            bill_model bill = snapshot.toObject(bill_model.class);

            // Setting up food cost strings
            String foodDollars = String.valueOf(Integer.valueOf(bill.getFood()) / 100);
            String foodCents = String.valueOf(Integer.valueOf(bill.getFood()) % 100);

            if ((foodCents.length() < 2) && (((Integer.valueOf(bill.getFood()) % 100) > 9) || ((Integer.valueOf(bill.getFood()) % 100) == 0))){
                foodCents = foodCents + "0";

            } else if (foodCents.length() < 2){
                foodCents = "0" + foodCents;

            }

            // Setting up drink cost strings
            String drinkDollars = String.valueOf(Integer.valueOf(bill.getDrinks()) / 100);
            String drinkCents = String.valueOf(Integer.valueOf(bill.getDrinks()) % 100);

            if ((drinkCents.length() < 2) && (((Integer.valueOf(bill.getDrinks()) % 100) > 9) || ((Integer.valueOf(bill.getDrinks()) % 100) == 0))){
                drinkCents = drinkCents + "0";

            } else if (drinkCents.length() < 2){
                drinkCents = "0" + drinkCents;

            }

            // Setting up refill cost strings
            String refDollars = String.valueOf(Integer.valueOf(bill.getRefills()) / 100);
            String refCents = String.valueOf(Integer.valueOf(bill.getRefills()) % 100);

            if ((refCents.length() < 2) && (((Integer.valueOf(bill.getRefills()) % 100) > 9) || ((Integer.valueOf(bill.getRefills()) % 100) == 0))){
                refCents = refCents + "0";

            } else if (refCents.length() < 2){
                refCents = "0" + refCents;

            }

            // Setting up tip strings
            String tipDollars = String.valueOf(Integer.valueOf(bill.getTip()) / 100);
            String tipCents = String.valueOf(Integer.valueOf(bill.getTip()) % 100);

            if ((tipCents.length() < 2) && (((Integer.valueOf(bill.getTip()) % 100) > 9) || ((Integer.valueOf(bill.getTip()) % 100) == 0))){
                tipCents = tipCents + "0";

            } else if (tipCents.length() < 2){
                tipCents = "0" + tipCents;

            }


            // Setting the strings for the receipt Firestore documents
            foodView.setText("$" + foodDollars + "." + foodCents);
            drinksView.setText("$" + drinkDollars + "." + drinkCents);
            refillsView.setText("$" + refDollars + "." + refCents);
            tipView.setText("$" + tipDollars + "." + tipCents);


            numFoodView.setText(bill.getNumFood() + "x Food Order");
            numDrinksView.setText(bill.getNumDrinks() + "x Drink Order");
            numRefillsView.setText(bill.getNumRefills() + "x Refill");


            // Add up the bill total
            int total = Integer.parseInt(bill.getFood()) + Integer.parseInt(bill.getDrinks()) + Integer.parseInt(bill.getRefills()) + Integer.parseInt(bill.getTip());

            // Format the strings
            String totDollars = String.valueOf(Integer.valueOf(total / 100));
            String totCents = String.valueOf(Integer.valueOf(total % 100));

            // Make the output nice
            if ((totCents.length() < 2) && (((total % 100) > 9) || ((total % 100) == 0))){
                totCents = totCents + "0";

            } else if (totCents.length() < 2){
                totCents = "0" + totCents;

            }

            // And set the total
            totView.setText("$" + totDollars + "." + totCents);


            // Click listener
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onBillSelected(snapshot);
                }
            });
        }

    }
}
