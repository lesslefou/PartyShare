package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Create_Activity_Adapter extends RecyclerView.Adapter<Create_Activity_Adapter.ViewHolder> {
    private static final String TAG = "CreateAdapter";

    private ArrayList<String> mImageNames = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private Context mContext;
    private String nameActivity;

    public Create_Activity_Adapter(ArrayList<String> mImageNames, ArrayList<Integer> mImages, Context mContext,String nameActivity) {
        this.mImageNames = mImageNames;
        this.mImages = mImages;
        this.mContext = mContext;
        this.nameActivity = nameActivity;
    }

    @NonNull
    @Override
    public Create_Activity_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_create___adapter, parent,false);

        Create_Activity_Adapter.ViewHolder holder = new Create_Activity_Adapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Create_Activity_Adapter.ViewHolder holder, final int position) {
        Log.d(TAG,"onBindViewHolder: called");

        holder.image.setImageDrawable(ResourcesCompat.getDrawable(mContext.getResources(), mImages.get(position), null));
        holder.imageName.setText(mImageNames.get(position));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"onClick:clicked on: " + mImageNames.get(position));
                Toast.makeText(mContext,mImageNames.get(position),Toast.LENGTH_SHORT).show();

                switch (position) {
                    case 0:
                        Intent i = new Intent(mContext, ContactChoice.class);
                        i.putExtra("NAME_ACTIVITY",nameActivity);
                        mContext.startActivity(i);
                        break;
                    case 1:
                        Intent i1 = new Intent(mContext, FoodChoice.class);
                        i1.putExtra("NAME_ACTIVITY",nameActivity);
                        mContext.startActivity(i1);
                        break;
                    case 2:
                        Intent i2 = new Intent(mContext, DrinkChoice.class);
                        i2.putExtra("NAME_ACTIVITY",nameActivity);
                        mContext.startActivity(i2);
                        break;
                    case 3:
                        Intent i3 = new Intent(mContext, LocationChoice.class);
                        i3.putExtra("NAME_ACTIVITY",nameActivity);
                        i3.putExtra("UPDATE",0);
                        mContext.startActivity(i3);
                        break;

                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mImageNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView imageName;
        RelativeLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageCreate);
            imageName = itemView.findViewById(R.id.textCreate);
            parentLayout = itemView.findViewById(R.id.parent_layout_create);
        }
    }
}
