package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodHolder> {
private static final String TAG = "FoodAdapter";

private final List<Food> food;
private Context mContext;
private Integer itemResource,update,choice;
private String nameActivity;

public FoodAdapter (Context context, Integer itemResource, List<Food> food, String nameActivity, Integer update,Integer choice) {
        this.food = food;
        this.mContext = context;
        this.itemResource = itemResource;
        this.nameActivity = nameActivity;
        this.update = update;
        this.choice = choice;
        }

@NonNull
@Override
public FoodHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemResource,parent,false);
        return new FoodHolder(this.mContext,view);
        }

@Override
public void onBindViewHolder(@NonNull FoodHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called");

        Food f = this.food.get(position);
        holder.bindPlace(f,nameActivity,update,choice);

        }

@Override
public int getItemCount() {
        return this.food.size();
        }

        }
