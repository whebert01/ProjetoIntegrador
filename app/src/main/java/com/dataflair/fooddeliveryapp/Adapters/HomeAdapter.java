package com.dataflair.fooddeliveryapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dataflair.fooddeliveryapp.Model.Model;
import com.dataflair.fooddeliveryapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class HomeAdapter extends FirebaseRecyclerAdapter<Model, HomeAdapter.Viewholder> {


    public HomeAdapter(@NonNull FirebaseRecyclerOptions<Model> options) {

        super(options);

    }

    @Override
    protected void onBindViewHolder(@NonNull HomeAdapter.Viewholder holder, int position, @NonNull Model model) {

        //Getting the data from the database using the Model class and setting the data
        holder.foodTitle.setText(model.getItemName());
        holder.foodPrice.setText(model.getItemPrice());
        holder.hostelLocation.setText(model.getHotelLocation());
        Picasso.get().load(model.getImageUrl()).into(holder.imageView);

        String hotelLocation = model.getHotelLocation();
        String foodPrice = model.getItemPrice();
        String foodName = model.getItemName();
        String foodImage = model.getImageUrl();

        //Implementing the Onclick Listener to add the order and user details to database
        holder.orderNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Generating the unique key
                String key = FirebaseDatabase.getInstance().getReference().child("myOrders").push().getKey().toString();

                //Getting the user id from google sign in
                String userId = GoogleSignIn.getLastSignedInAccount(view.getContext()).getId();
                //Database Path to add the details
                FirebaseDatabase.getInstance().getReference().child("users").child(userId)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                Model model = snapshot.getValue(Model.class);

                                //Hash Map to store the values
                                HashMap orderDetails = new HashMap();

                                //Adding the details to hashmap
                                orderDetails.put("name", model.getName());
                                orderDetails.put("phoneNumber", model.getPhoneNumber());
                                orderDetails.put("address", model.getAddress());
                                orderDetails.put("cityName", model.getCityName());
                                orderDetails.put("pinCode", model.getPinCode());

                                orderDetails.put("hotelLocation", hotelLocation);
                                orderDetails.put("itemPrice", foodPrice);
                                orderDetails.put("itemName", foodName);
                                orderDetails.put("imageUrl", foodImage);
                                orderDetails.put("userId", userId);


                                //Adding the hash map to the database
                                FirebaseDatabase.getInstance().getReference().child("myOrders").child(userId).child(key)
                                        .setValue(orderDetails)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {

                                                if (task.isSuccessful()) {
                                                    FirebaseDatabase.getInstance().getReference().child("totalOrders").child(key)
                                                            .setValue(orderDetails)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        //Showing the toast to user for confirmation
                                                                        Toast.makeText(view.getContext(), "Order Placed Successfully", Toast.LENGTH_SHORT).show();
                                                                    }
                                                                }
                                                            });

                                                }

                                            }
                                        });


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
    public HomeAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //the data objects are inflated into the xml file single_data_item
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_food_item, parent, false);
        return new HomeAdapter.Viewholder(view);

    }

    //we need view holder to hold each objet form recyclerview and to show it in recyclerview
    class Viewholder extends RecyclerView.ViewHolder {


        TextView foodTitle, foodPrice, hostelLocation;
        Button orderNowBtn;
        ImageView imageView;


        public Viewholder(@NonNull View itemView) {
            super(itemView);

            //Assigning the address of the Android Materials to perform appropriate action
            foodTitle = (TextView) itemView.findViewById(R.id.FoodName);
            foodPrice = (TextView) itemView.findViewById(R.id.FoodPrice);
            hostelLocation = (TextView) itemView.findViewById(R.id.HotelLocation);

            imageView = (ImageView) itemView.findViewById(R.id.FoodImage);
            orderNowBtn = (Button) itemView.findViewById(R.id.OrderNowBtn);
        }
    }


}


