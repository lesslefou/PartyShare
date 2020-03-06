package lisa.duterte.partyshare;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;



public class GameAdapter extends RecyclerView.Adapter<GameHolder> {
    private static final String TAG = "GameAdapter";

    private final List<Game> games;
    private Context mContext;
    private int itemResource;

    public GameAdapter (Context context, int itemResource, List<Game> games) {
        this.games = games;
        this.mContext = context;
        this.itemResource = itemResource;
    }

    @NonNull
    @Override
    public GameHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(itemResource,parent,false);
        return new GameHolder(this.mContext,view);
    }
    @Override
    public void onBindViewHolder(@NonNull GameHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder: called");

        Game g = this.games.get(position);
        holder.bindPlace(g);

    }

    @Override
    public int getItemCount() {
        return this.games.size();
    }

}
