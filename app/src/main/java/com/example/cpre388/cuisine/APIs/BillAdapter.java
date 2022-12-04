package com.example.cpre388.cuisine.APIs;

import android.content.res.Resources;
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

    private OnRestaurantSelectedListener mListener;

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

        //ImageView View;
        TextView foodView, drinksView, refillsView, numFoodView, numDrinksView, numRefillsView, tipView, totView;
        //MaterialRatingBar ratingBar;
        //TextView numRatingsView;
        //TextView priceView;
        //TextView categoryView;
        //TextView cityView;

        public ViewHolder(View itemView) {
            super(itemView);
            //imageView = itemView.findViewById(R.id.restaurant_item_image);
            foodView = itemView.findViewById(R.id.textViewF);
            drinksView = itemView.findViewById(R.id.textViewD);
            refillsView = itemView.findViewById(R.id.textViewR);
            numFoodView = itemView.findViewById(R.id.textViewNF);
            numDrinksView = itemView.findViewById(R.id.textViewND);
            numRefillsView = itemView.findViewById(R.id.textViewNR);
            tipView = itemView.findViewById(R.id.textViewT);
            totView = itemView.findViewById(R.id.textViewTot);
            //ratingBar = itemView.findViewById(R.id.restaurant_item_rating);
            //numRatingsView = itemView.findViewById(R.id.restaurant_item_num_ratings);
            //priceView = itemView.findViewById(R.id.restaurant_item_price);
            //categoryView = itemView.findViewById(R.id.restaurant_item_category);
            //cityView = itemView.findViewById(R.id.restaurant_item_city);
        }

        public void bind(final DocumentSnapshot snapshot,
                         final OnRestaurantSelectedListener listener) {

            bill_model bill = snapshot.toObject(bill_model.class);
            Resources resources = itemView.getResources();

            // Load image
            /*
            Glide.with(imageView.getContext())
                    .load(restaurant.getPhoto())
                    .into(imageView);
            */



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
            numRefillsView.setText(bill.getNumRefills() + "x Refills");

            int total = Integer.parseInt(bill.getFood()) + Integer.parseInt(bill.getDrinks()) + Integer.parseInt(bill.getRefills()) + Integer.parseInt(bill.getTip());


            String totDollars = String.valueOf(Integer.valueOf(total / 100));
            String totCents = String.valueOf(Integer.valueOf(total % 100));

            if ((totCents.length() < 2) && (((total % 100) > 9) || ((total % 100) == 0))){
                totCents = totCents + "0";

            } else if (totCents.length() < 2){
                totCents = "0" + totCents;

            }

            totView.setText("$" + totDollars + "." + totCents);
            //ratingBar.setRating((float) restaurant.getAvgRating());
            //cityView.setText(restaurant.getCity());
            //categoryView.setText(restaurant.getCategory());
            //numRatingsView.setText(resources.getString(R.string.fmt_num_ratings,
            //restaurant.getNumRatings()));

            // Click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onRestaurantSelected(snapshot);
                    }
                }
            });
        }

    }
}
