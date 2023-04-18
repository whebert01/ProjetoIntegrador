package com.dataflair.fooddeliveryapp.Adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Fragments.HomeFragment;
import com.dataflair.fooddeliveryapp.Fragments.MyOrdersFragment;
import com.dataflair.fooddeliveryapp.MainActivity;
import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MyOrdersAdapter extends FirebaseRecyclerAdapter<Model, MyOrdersAdapter.Viewholder> {


    public MyOrdersAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull MyOrdersAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting the data from the database and setting the values to appropriate view
        holder.foodTitle.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        holder.hostelLocation.setText(model.getHotelLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);


        //Implementing the OnClick Listener to delete the data from the database
        holder.cancelOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Getting user id from the gmail sing in
                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();
                //Path to the database
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("myOrders").child(userId);
                reference.orderByChild("itemName").equalTo(model.getItemName()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()) {

                            //getting the parent node of the data
                            String key = ds.getKey();

                            //removing the data from the database
                            reference.child(key).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase.getInstance().getReference().child("totalOrders")
                                                .child(key).removeValue()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            //Showing the Toast message to the user
                                                            Toast.makeText(view.getContext(), "Order Canceled Successfully", Toast.LENGTH_SHORT).show();

                                                        }
                                                    }
                                                });
                                    }
                                }
                            });


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

    }


    @NonNull
    @Override
    public MyOrdersAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_food_orders, parent, false);
        return new MyOrdersAdapter.Viewholder(view);

    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        TextView foodTitle, foodPrice, hostelLocation;
        Button cancelOrderBtn;
        ImageView imageView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //Assigning the address of the Android Materials to perform appropriate action
            foodTitle = (TextView) itemView.findViewById(R.id.FoodNameMyOrders);
            foodPrice = (TextView) itemView.findViewById(R.id.FoodPriceMyOrders);
            hostelLocation = (TextView) itemView.findViewById(R.id.HotelLocationMyOrders);

            imageView = (ImageView) itemView.findViewById(R.id.FoodImageMyOrders);
            cancelOrderBtn = (Button) itemView.findViewById(R.id.CancelOrderNowBtnMyOrders);
        }
    }


}


