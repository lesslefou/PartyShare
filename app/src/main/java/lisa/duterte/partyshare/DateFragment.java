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
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class DateFragment extends Fragment {
    private static final String TAG = "FoodFragment";

    private DatabaseReference mReference;
    private TextView dateView;
    private Button update;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_date, container, false);

        Bundle b = getArguments();
        final String nameActivity = Objects.requireNonNull(b).getString("NAME_ACTIVITY","ERROR");
        Log.d(TAG, "nameActivity " + nameActivity);

        mReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity);

        dateView = v.findViewById(R.id.dateTextView);

        viewDateList();

        update = v.findViewById(R.id.updateBtn);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(),DateChoice.class);
                i.putExtra("NAME_ACTIVITY",nameActivity);
                i.putExtra("UPDATE",1);
                startActivity(i);
            }
        });
        return v;
    }

    private void viewDateList() {
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String dateString = dataSnapshot.child("date").getValue().toString();
                Log.d("ViewActivity","date " + dateString);
                dateView.setText(dateString);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
