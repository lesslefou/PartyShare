package lisa.duterte.partyshare;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FoodHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    private static final String TAG = "FoodHolder";

    private DatabaseReference aReference;

    private final ImageView foodIcon;
    private final TextView foodName;
    private final Spinner foodQuantity;

    private Food food;
    private Context context;

    public FoodHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;

        this.foodIcon = itemView.findViewById(R.id.image);
        this.foodName = itemView.findViewById(R.id.drinkText);
        this.foodQuantity = itemView.findViewById(R.id.quantity);

        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.quantityNumbers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        foodQuantity.setAdapter(adapter);
        foodQuantity.setSelection(adapter.getPosition("2"));*/ //Fonctionne en dehors d'un recyclerView

        itemView.setOnClickListener(this);
    }

    public void bindPlace(Food food,String nameActivity) {
        // Bind the data to the ViewHolder
        this.food = food;
        this.foodName.setText(food.getName());
        this.foodIcon.setImageDrawable(
                this.context.getResources().getDrawable(food.getIconId()));
        Log.d(TAG,"name activity = "+nameActivity);
        aReference = FirebaseDatabase.getInstance().getReference("Activities").child(nameActivity).child("foodChoice");

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
