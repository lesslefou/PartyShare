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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class DrinkFragment extends Fragment {
    private static final String TAG = "DrinkFragment";

    private DatabaseReference mReference;
    private ListView drinkView,drinkQuantityView;
    private ArrayAdapter<String> arrayAdapterDrink,arrayAdapterDrinkQuantity;
    private ArrayList<String> listDrink= new ArrayList<>(),listDrinkQuantity= new ArrayList<>();
    private String drink="",quantityDrink="";
    private Button update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_drink, container, false);

        Bundle b = getArguments();
        final String nameActivity = Objects.requireNonNull(b).getString("NAME_ACTIVITY","ERROR");
        Log.d(TAG, "nameActivity " + nameActivity);

        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);

        drinkView = v.findViewById(R.id.ListViewDrink);
        drinkQuantityView = v.findViewById(R.id.ListViewDrinkQuantity);

        viewDrinkList();

        update = v.findViewById(R.id.updateBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),DrinkChoice.class);
                i.putExtra("NAME_ACTIVITY",nameActivity);
                i.putExtra("UPDATE",1);
                startActivity(i);
            }
        });

        return v;
    }

    private void viewDrinkList() {
        arrayAdapterDrink = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listDrink);
        arrayAdapterDrinkQuantity = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, listDrinkQuantity);
        drinkView.setAdapter(arrayAdapterDrink);
        drinkQuantityView.setAdapter(arrayAdapterDrinkQuantity);
        DatabaseReference post = mReference.child("drinkChoice");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot child : dataSnapshot.getChildren()) {
                    String quantity = child.child("quantity").getValue(String.class);
                    String name = child.getKey();
                    Log.d(TAG,"name : "+name + " quantity = "+quantity);
                    drink= drink + name + "\n";
                    quantityDrink= quantityDrink + quantity + "\n";
                }
                Log.d(TAG,"drink = " + drink);
                Log.d(TAG,"quantity = " + quantityDrink);
                listDrink.add(drink);
                listDrinkQuantity.add(quantityDrink);
                arrayAdapterDrink.notifyDataSetChanged();
                arrayAdapterDrinkQuantity.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}