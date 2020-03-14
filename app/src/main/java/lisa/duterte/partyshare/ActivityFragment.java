package lisa.duterte.partyshare;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class ActivityFragment extends Fragment {

    private static final String TAG = "ActivityFragment";

    private DatabaseReference mReference;
    private ArrayList<String> allActivityList = new ArrayList<>(),activityList = new ArrayList<>(), activityRef= new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Activity activity = new Activity();
    private String pseudoUser;
    private String value;
    private ArrayList<Integer> check = new ArrayList<>();
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

        //Get user ID and user Name
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            String userId = firebaseUser.getUid();
            Log.d(TAG , "id user " + userId);

            DatabaseReference uReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            uReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pseudoUser = dataSnapshot.child("pseudo").getValue().toString();
                    Log.d(TAG, "onDataChange name user " + pseudoUser);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {   }
            });
        }

        mReference = FirebaseDatabase.getInstance().getReference("Activities");
        final ListView activityView = v.findViewById(R.id.listActivities);
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,activityList);
        activityView.setAdapter(arrayAdapter);
        mReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot,  String s) {
                //Get name Activity and add it to allActivityList
                value = dataSnapshot.child("name").getValue().toString();
                Log.d(TAG,"activityName : " + value);
                allActivityList.add(value);
                //create an Arraylist of reference of activities
                activityRef.add(dataSnapshot.child("friends").getRef().toString());
                check.add(0);
                Log.d(TAG,"value : " + dataSnapshot.child("friends").getRef());
                Log.d(TAG,"allActivityList : " + allActivityList);

                Log.d(TAG," addActivityUser called");

                //Check if the user is on a listFriend of the activity
                checkIfPseudoCanSee(allActivityList.size()-1);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {  }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {  }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {  }


        });


        //Allows the user to click on the name of the activity and reacts
        activityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Display the name of the activity
                activity.setName(activityList.get(position));
                //Show a dialog information
                showInformationSavedDialog();
            }
        });



        return v;
    }

    protected void showInformationSavedDialog() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()),R.style.MyDialogTheme);
        } else {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
        builder.setMessage(R.string.dialogue_message_activity);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.delete_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Delete the activity chose
                deleteFunction();
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.see_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Get the name of the activity and and launches the activity which allows to show the information of the activity
                String nameActivity = activity.getName();
                Intent i = new Intent(getContext(),View_Info_Activity.class);
                i.putExtra("NAME_ACTIVITY",nameActivity);
                startActivity(i);

                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Delete the activity
    private void deleteFunction() {
        final String str = activity.getName();
        //Check if the string is not null
        if (!str.equals("")){
            mReference.child("Activities").child(str).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReference.child(str).removeValue();
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }

    }

    public void checkIfPseudoCanSee(int i) {
        Log.d(TAG,"checkIfPseudoCanSee called");
        Log.d(TAG,"checkIfPseudoCanSee: activity : " + allActivityList.get(i));

        final DatabaseReference checkRef = FirebaseDatabase.getInstance().getReference("Activities").child(allActivityList.get(i)).child("friends");
        checkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int count = 0, checkNom=0,checkActvity=0;
                Log.d(TAG," checkIfPseudoCanSee:onDataChange : activity : " + dataSnapshot.getRef() );

                for(int j = 0; j<activityRef.size();j++){
                    //Check in the activityRef ArrayList if the activity we actually check on checkRef is equal
                    if(activityRef.get(j).equals(dataSnapshot.getRef().toString())){
                        //get the name of the activity
                        value = allActivityList.get(j);
                        //get the value of check  (=0: activity not already check)
                        checkActvity = check.get(j);
                        break;
                    }
                    //get the position of the activity
                    count=count+1;
                }

                //if activity not already check :
                if(checkActvity != 1) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        //Get name of friendList
                        String p = child.getValue().toString();
                        Log.d(TAG, "checkIfPseudoCanSee:onDataChange : friend = " + p);
                        if (p != null) {
                            //compare the name of the user to the friend name found => if =0: user is on the friendList
                            if (p.compareTo(pseudoUser) == 0) {
                                checkNom = 1;
                                break;
                            }
                        }
                    }

                    //User found on the friendList => can see the activity
                    if (checkNom == 1) {
                        activityList.add(value);
                        Log.d(TAG, "activityList : " + activityList);
                        arrayAdapter.notifyDataSetChanged();
                    }
                    // put 1 to the activity which has been check (count = position on the checkList)
                    check.set(count,1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}