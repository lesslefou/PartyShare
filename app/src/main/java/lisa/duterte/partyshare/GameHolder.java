package lisa.duterte.partyshare;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class GameHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final ImageView gameIcon;
    private final TextView gameName;

    private Game game;
    private Context context;

    public GameHolder(Context context, View itemView) {
        super(itemView);

        this.context = context;

        this.gameIcon = (ImageView) itemView.findViewById(R.id.imageGame);
        this.gameName = (TextView) itemView.findViewById(R.id.nameGame);

        itemView.setOnClickListener(this);
    }

    public void bindPlace(Game game) {
        // Bind the data to the ViewHolder
        this.game = game;
        this.gameName.setText(game.getName());
        this.gameIcon.setImageDrawable(
                this.context.getResources().getDrawable(game.getIconId()));
    }

    @Override
    public void onClick(View v) {
        if (this.game != null) {
            Intent intent = new Intent(itemView.getContext(), GameInformations.class);
            intent.putExtra("game", this.game);
            itemView.getContext().startActivity(intent);
        }
    }
}
