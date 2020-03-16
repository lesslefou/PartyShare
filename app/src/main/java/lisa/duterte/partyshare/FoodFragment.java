package lisa.duterte.partyshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class FoodFragment extends Fragment {
    private static final String TAG = "FoodFragment";

    private DatabaseReference mReference;
    private TextView foodView,extraFood;
    private String food="";
    private Button update,back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_food, container, false);

        Bundle b = getArguments();
        final String nameActivity = Objects.requireNonNull(b).getString("NAME_ACTIVITY","ERROR");
        Log.d(TAG, "nameActivity " + nameActivity);

        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);

        foodView = v.findViewById(R.id.ListViewFood);
        extraFood = v.findViewById(R.id.extraShow);

        viewFoodList();


        //go on the DrinkChoice.java to update the information
        update = v.findViewById(R.id.updateBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),FoodChoice.class);
                i.putExtra("NAME_ACTIVITY",nameActivity);
                i.putExtra("UPDATE",1);
                startActivity(i);
            }
        });

        //Go back on the previous page and close this activity
        back = v.findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Welcome.class);
                startActivity(i);
                getActivity().finish();
            }
        });


        return v;
    }

    //Display from the database the drinkList
    private void viewFoodList() {
        Query post = mReference.child("foodChoice");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String quantity = child.child("quantity").getValue(String.class);
                    String name = child.getKey();
                    Log.d(TAG,"name : "+name + " quantity = "+quantity);
                    if (!name.equals("extra")) {
                        food = food + name + " : " + quantity + "\n\n";
                    }
                }
                Log.d(TAG,"food = " + food);
                foodView.setText(food);

                String extra = dataSnapshot.child("extra").getValue(String.class);
                extraFood.setText(extra);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
