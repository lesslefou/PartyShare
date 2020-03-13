package lisa.duterte.partyshare;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
    private ListView foodView,quantityView;
    private ArrayAdapter<String> arrayAdapterFood,arrayAdapterFoodQuantity;
    private ArrayList<String> listFood = new ArrayList<>(),listFoodQuantity = new ArrayList<>();
    private String food="",quantityFood = "";
    private Button update;

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
        quantityView = v.findViewById(R.id.ListViewFoodQuantity);

        viewFoodList();

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
        return v;
    }

    private void viewFoodList() {
        arrayAdapterFood = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listFood);
        arrayAdapterFoodQuantity = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listFoodQuantity);
        foodView.setAdapter(arrayAdapterFood);
        quantityView.setAdapter(arrayAdapterFoodQuantity);
        Query post = mReference.child("foodChoice");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String quantity = child.child("quantity").getValue(String.class);
                    String name = child.getKey();
                    Log.d(TAG,"name : "+name + " quantity = "+quantity);
                    food = food + name + "\n";
                    quantityFood = quantityFood + quantity + "\n";
                }
                Log.d(TAG,"food = " + food);
                Log.d(TAG,"quantity = " + quantityFood);
                listFood.add(food);
                listFoodQuantity.add(quantityFood);
                arrayAdapterFood.notifyDataSetChanged();
                arrayAdapterFoodQuantity.notifyDataSetChanged();
                //Quantity never shown...
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}