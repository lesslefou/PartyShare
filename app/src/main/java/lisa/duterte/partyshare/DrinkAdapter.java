package lisa.duterte.partyshare;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.ViewHolder> implements AdapterView.OnItemSelectedListener {
    private static final String TAG = "DrinkAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;
    private String nameDrink,nameActivity;
    private DatabaseReference aReference;



    DrinkAdapter(ArrayList<String> mImageNames, ArrayList<Integer> mImages, Context mContext,String nameActivity) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.nameActivity = nameActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_drink_adapter, parent,false);

        Spinner spinner = view.findViewById(R.id.quantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(mContext,
                R.array.quantityNumbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        aReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity).child("drinkChoice");


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");

        holder.image.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), mImages.get(position), null));
        holder.imageName.setText(mImageNames.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameDrink = mImageNames.get(position);
                holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        final String item = parent.getItemAtPosition(position).toString();
                        Log.d(TAG,"onClick: name " + nameDrink + ", quantity : " + item);

                        aReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                aReference.child(nameDrink).child("quantity").setValue(item);

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView image;
        TextView imageName;
        RelativeLayout parentLayout;
        Spinner spinner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            imageName = itemView.findViewById(R.id.drinkText);
            parentLayout = itemView.findViewById(R.id.parent_layout);
            spinner = itemView.findViewById(R.id.quantity);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
