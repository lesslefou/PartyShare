package lisa.duterte.partyshare;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class GameInformations extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_informations);

        Game game =
                (Game) getIntent().getParcelableExtra("game");


        TextView gameTitle =
                (TextView) findViewById(R.id.game_info_title);
        ImageView gameImage =
                (ImageView) findViewById(R.id.game_info_image);
        TextView gameInfo =
                (TextView) findViewById(R.id.game_info);

        gameTitle.setText(game.getName());
        gameImage.setImageResource(game.getIconId());
        gameInfo.setText(game.getInfo());

        Button backBtn = findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
