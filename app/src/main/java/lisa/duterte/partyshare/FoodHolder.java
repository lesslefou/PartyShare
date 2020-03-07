package lisa.duterte.partyshare;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "FoodHolder";

    private DatabaseReference aReference;

    private final ImageView foodIcon;
    private final TextView foodName;
    private final Spinner foodQuantity;
    private String quantity;
    private Food food;
    private Context context;

    public FoodHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;

        this.foodIcon = itemView.findViewById(R.id.image);
        this.foodName = itemView.findViewById(R.id.drinkText);
        this.foodQuantity = itemView.findViewById(R.id.quantity);

        itemView.setOnClickListener(this);
    }

    public void bindPlace(final Food food, final String nameActivity, Integer update,Integer choice) {
        // Bind the data to the ViewHolder
        this.food = food;
        this.foodName.setText(food.getName());
        this.foodIcon.setImageDrawable(
                this.context.getResources().getDrawable(food.getIconId()));

        if (choice == 0) {
            aReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity).child("foodChoice");
        }
        else if (choice == 1) {
            aReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity).child("drinkChoice");
        }

        if (update == 1) {
            aReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String foodNameString = foodName.getText().toString();
                    Log.d(TAG,"foodNameString = " + foodNameString);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Log.d(TAG, "checkIfFoodExist: datasnapshot " + ds);

                        String nameRecup = ds.getKey();
                        Log.d(TAG,"name Recup = " + nameRecup);

                        //Check if the food exist
                        if (nameRecup.equals(foodNameString)) {
                            Log.d(TAG, "Food " + nameRecup + " exist");

                            quantity = dataSnapshot.child(foodNameString).child("quantity").getValue().toString();
                            foodQuantity.setSelection(ArrayAdapter.createFromResource(context,
                                    R.array.quantityNumbers, android.R.layout.simple_spinner_item).getPosition(quantity));

                            break;
                        }
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    public void onClick(View v) {
        if (this.food != null) {
            foodQuantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                   final String item = parent.getItemAtPosition(position).toString();
                   Log.d(TAG,"name " + foodName.getText() + " and quantity = " + item);
                   String foodNameString = foodName.getText().toString();

                   aReference.child(foodNameString).child("quantity").setValue(item);
                   Toast.makeText(context,"Save",Toast.LENGTH_SHORT).show();
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });

        }
    }
}
