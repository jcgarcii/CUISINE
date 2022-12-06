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
 * RecyclerView adapter for a list of Restaurants.
 */
public class BillAdapter extends FirestoreAdapter<BillAdapter.ViewHolder> {

    public interface OnRestaurantSelectedListener {

        void onRestaurantSelected(DocumentSnapshot bill);

    }

    final OnRestaurantSelectedListener mListener;

    public BillAdapter(Query query, OnRestaurantSelectedListener listener) {
        super(query);
        mListener = listener;
    }

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

        TextView foodView, drinksView, refillsView, numFoodView, numDrinksView, numRefillsView, tipView, totView;

        public ViewHolder(View itemView) {
            super(itemView);

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
                         final OnRestaurantSelectedListener listener) {

            bill_model bill = snapshot.toObject(bill_model.class);

            String foodDollars = String.valueOf(Integer.valueOf(bill.getFood()) / 100);
            String foodCents = String.valueOf(Integer.valueOf(bill.getFood()) % 100);

            if ((foodCents.length() < 2) && (((Integer.valueOf(bill.getFood()) % 100) > 9) || ((Integer.valueOf(bill.getFood()) % 100) == 0))){
                foodCents = foodCents + "0";

            } else if (foodCents.length() < 2){
                foodCents = "0" + foodCents;

            }


            String drinkDollars = String.valueOf(Integer.valueOf(bill.getDrinks()) / 100);
            String drinkCents = String.valueOf(Integer.valueOf(bill.getDrinks()) % 100);

            if ((drinkCents.length() < 2) && (((Integer.valueOf(bill.getDrinks()) % 100) > 9) || ((Integer.valueOf(bill.getDrinks()) % 100) == 0))){
                drinkCents = drinkCents + "0";

            } else if (drinkCents.length() < 2){
                drinkCents = "0" + drinkCents;

            }


            String refDollars = String.valueOf(Integer.valueOf(bill.getRefills()) / 100);
            String refCents = String.valueOf(Integer.valueOf(bill.getRefills()) % 100);

            if ((refCents.length() < 2) && (((Integer.valueOf(bill.getRefills()) % 100) > 9) || ((Integer.valueOf(bill.getRefills()) % 100) == 0))){
                refCents = refCents + "0";

            } else if (refCents.length() < 2){
                refCents = "0" + refCents;

            }


            String tipDollars = String.valueOf(Integer.valueOf(bill.getTip()) / 100);
            String tipCents = String.valueOf(Integer.valueOf(bill.getTip()) % 100);

            if ((tipCents.length() < 2) && (((Integer.valueOf(bill.getTip()) % 100) > 9) || ((Integer.valueOf(bill.getTip()) % 100) == 0))){
                tipCents = tipCents + "0";

            } else if (tipCents.length() < 2){
                tipCents = "0" + tipCents;

            }

            foodView.setText("$" + foodDollars + "." + foodCents);
            drinksView.setText("$" + drinkDollars + "." + drinkCents);
            refillsView.setText("$" + refDollars + "." + refCents);
            tipView.setText("$" + tipDollars + "." + tipCents);


            numFoodView.setText(bill.getNumFood() + "x Food Order");
            numDrinksView.setText(bill.getNumDrinks() + "x Drink Order");
            numRefillsView.setText(bill.getNumRefills() + "x Refill");

            int total = Integer.parseInt(bill.getFood()) + Integer.parseInt(bill.getDrinks()) + Integer.parseInt(bill.getRefills()) + Integer.parseInt(bill.getTip());

            String totDollars = String.valueOf(Integer.valueOf(total / 100));
            String totCents = String.valueOf(Integer.valueOf(total % 100));

            if ((totCents.length() < 2) && (((total % 100) > 9) || ((total % 100) == 0))){
                totCents = totCents + "0";

            } else if (totCents.length() < 2){
                totCents = "0" + totCents;

            }

            totView.setText("$" + totDollars + "." + totCents);


            // Click listener
            itemView.setOnClickListener(view -> {
                if (listener != null) {
                    listener.onRestaurantSelected(snapshot);
                }
            });
        }

    }
}
