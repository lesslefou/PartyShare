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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ViewActivity extends AppCompatActivity {

    String activityName;
    TextView name,locationView;
    ListView foodView,drinkView,friendView;
    ArrayList<String> listContact,listDrink,listFood;
    Button foodUpdate,drinkUpdate,friendUpdate,locationUpdate,back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        activityName = Objects.requireNonNull(getIntent().getExtras()).getString("NAME_ACTIVITY","error");
        Log.d("ViewActivity", "activity_name récupéré " + activityName);

        name = findViewById(R.id.activityName);
        name.setText(activityName);

        viewFriendList();

        foodUpdate = findViewById(R.id.foodUpdate);
        drinkUpdate = findViewById(R.id.drinkUpdate);
        friendUpdate = findViewById(R.id.friendUpdate);
        locationUpdate = findViewById(R.id.locationUpdate);
        back = findViewById(R.id.btn_back);

        foodUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        drinkUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    private void viewFriendList() {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference("Activities").child(activityName);

        /* friendView = findViewById(R.id.ListViewFriend);
        final ArrayAdapter<String> friendArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listContact);
        friendView.setAdapter(friendArrayAdapter);


        drinkView = findViewById(R.id.ListViewDrink);
        final ArrayAdapter<String> drinkArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listDrink);
        drinkView.setAdapter(friendArrayAdapter);


        foodView = findViewById(R.id.ListViewFood);
        final ArrayAdapter<String> foodArrayAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,listFood);
        foodView.setAdapter(friendArrayAdapter);*/

        locationView = findViewById(R.id.locationAddress);


        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                /*String value = dataSnapshot.child("name").getValue().toString();
                listContact.add(value);
                friendArrayAdapter.notifyDataSetChanged();*/ //Modifier pour afficher la liste des contacts

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
