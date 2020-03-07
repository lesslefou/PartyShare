package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity {

    String activityName,value;
    TextView name,locationView;
    ListView foodView,drinkView,friendView;
    ArrayList<String> listContact= new ArrayList<>(),listDrink= new ArrayList<>(),listFood= new ArrayList<>();
    Button foodUpdate,drinkUpdate,friendUpdate,locationUpdate,back;
    ArrayAdapter<String> arrayAdapterFriend,arrayAdapterDrink,arrayAdapterFood;
    Activity activity = new Activity();
    DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        activityName = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY","error");
        Log.d("ViewActivity", "activity_name récupéré " + activityName);


        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(activityName);

        name = findViewById(R.id.activityName);
        name.setText(activityName);

        viewFriendList();
        viewLocation();
        viewDrinkList();

        foodUpdate = findViewById(R.id.foodUpdate);
        drinkUpdate = findViewById(R.id.drinkUpdate);
        friendUpdate = findViewById(R.id.friendUpdate);
        locationUpdate = findViewById(R.id.locationUpdate);
        back = findViewById(R.id.btn_back);

        foodUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewActivity.this,FoodChoice.class);
                i.putExtra("NAME_ACTIVITY",activityName);
                i.putExtra("UPDATE",1);
                startActivity(i);

            }
        });
        drinkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewActivity.this,DrinkChoice.class);
                i.putExtra("NAME_ACTIVITY",activityName);
                i.putExtra("UPDATE",1);
                startActivity(i);

            }
        });
        friendUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewActivity.this,ContactChoice.class);
                i.putExtra("NAME_ACTIVITY",activityName);
                i.putExtra("UPDATE",1);
                startActivity(i);


            }
        });
        locationUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewActivity.this,LocationChoice.class);
                i.putExtra("NAME_ACTIVITY",activityName);
                i.putExtra("UPDATE",1);
                startActivity(i);

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ViewActivity.this,Welcome.class);
                //FLAG TAG to close everything
                startActivity(i);
            }
        });
    }

    private void viewDrinkList() {
        drinkView = findViewById(R.id.ListViewDrink);
        arrayAdapterDrink = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1, listDrink);
        drinkView.setAdapter(arrayAdapterDrink);
        Query post = mReference.child("DrinkChoice");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               //Si null ne pas afficher !
                value = dataSnapshot.getValue().toString();
                listDrink.add(value);
                arrayAdapterDrink.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void viewFriendList() {
        friendView = findViewById(R.id.ListViewFriend);
        arrayAdapterFriend = new ArrayAdapter<>(ViewActivity.this, android.R.layout.simple_list_item_1, listContact);
        friendView.setAdapter(arrayAdapterFriend);
        Query post = mReference.child("friends");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String p = child.getValue(String.class);
                    if (p != null) {
                        listContact.add(p);
                    }
                }
                arrayAdapterFriend.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void viewLocation() {
        locationView = findViewById(R.id.locationAddress);
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String address = dataSnapshot.child("location").getValue().toString();
                locationView.setText(address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
