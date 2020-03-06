package lisa.duterte.partyshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ActivityFragment extends Fragment {
    private DatabaseReference mReference;
    private DatabaseReference uReference;
    private FirebaseUser firebaseUser;
    private ArrayList<String> activityList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Activity activity = new Activity();
    private String userId,pseudoUser,value;
    private Integer check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_activity, container, false);

        Button createBtn = v.findViewById(R.id.createBtn);
        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), NameChoiceActivity.class);
                startActivity(i);
            }
        });

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            userId = firebaseUser.getUid();

            Log.d("ActivityFragment" , "id user " + userId);
            uReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            uReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pseudoUser = dataSnapshot.child("pseudo").getValue().toString();
                    Log.d("ActivityFragment", "onDataChange name user " + pseudoUser);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        mReference = FirebaseDatabase.getInstance().getReference("Activities");
        final ListView activityView = v.findViewById(R.id.listActivities);
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,activityList);
        activityView.setAdapter(arrayAdapter);
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                value = dataSnapshot.child("name").getValue().toString();
                Log.d("ActivityFragment","value : " + value);

                Query q = mReference.child(value).child("friends");
                q.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot child : dataSnapshot.getChildren()) {
                            String p = child.getValue(String.class);
                            if (p != null ) {
                                Log.d("ActivityFragment","p : " + p);
                                //check if the contact is or not already on his contactList
                                if (p.equals(pseudoUser)){
                                    check = 1;
                                    break;
                                }
                            }
                        }

                        if (check == 1) {
                            activityList.add(value);
                            Log.d("ActivityFragment","activityList : "+ activityList);
                            arrayAdapter.notifyDataSetChanged();
                            check =0;
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


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

        activityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                activity.setName(activityList.get(position));

                showInformationSavedDialog();
            }
        });

        return v;
    }

    protected void showInformationSavedDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
        builder.setMessage(R.string.dialogue_message_activity);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.delete_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFunction();

                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.see_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nameActivity = activity.getName();
                Intent i = new Intent(getContext(),ViewActivity.class);
                i.putExtra("NAME_ACTIVITY",nameActivity);
                startActivity(i);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteFunction() {
        final String str = activity.getName();
        if (!str.equals("")){
            mReference.child("Activities").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReference.child(str).removeValue();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}