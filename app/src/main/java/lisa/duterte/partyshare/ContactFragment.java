package lisa.duterte.partyshare;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class ContactFragment extends Fragment {

    private DatabaseReference mReference;
    private FirebaseUser fUser;
    private ArrayList<String> contactList = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private Button addContactBtn;
    private String userId;
    private User user = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        addContactBtn = v.findViewById(R.id.createBtn);
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), Add_Contact.class);
                startActivity(i);
            }
        });

        mReference = FirebaseDatabase.getInstance().getReference("user");
        final ListView contactView = v.findViewById(R.id.listContactView);
        arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, contactList);
        contactView.setAdapter(arrayAdapter);

        Query post = mReference.child(userId).child("contactList");
        post.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    String p = child.getValue(String.class);
                    if (p != null) {
                        contactList.add(p);
                    }
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        contactView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user.setName(contactList.get(position));

                String recup_pseudo = contactList.get(position);
                Log.d("ContactFragment ",recup_pseudo);

               showInformationSavedDialog(recup_pseudo);
            }
        });

        return v;
    }

    protected void showInformationSavedDialog(final String recup_pseudo) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()), android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        }
        builder.setMessage(R.string.dialogue_message);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.no_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton(R.string.yes_answer, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteFunction(recup_pseudo);
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void deleteFunction(final String recup_pseudo) {
        int remove=0;

        for (int j=0; j<contactList.size(); j++){
            if (contactList.get(j).equals(recup_pseudo)) {
                remove = j;
            }
        }
        contactList.remove(remove);
        mReference.child(userId).child("contactList").setValue(contactList);

    }
}

