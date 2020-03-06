package lisa.duterte.partyshare;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class GameFragment extends Fragment {
    private static final String TAG = "MainActivity";

    private ArrayList<String> mImageNames = new ArrayList<>(), mInformation = new ArrayList<>();
    private ArrayList<Integer> mImages = new ArrayList<>();
    private RecyclerView recyclerView;
    private GameAdapter adapter;
    private ArrayList<Game> game;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_game, container, false);


        initImageBitmaps(v);
        return v;
    }

    private void initImageBitmaps(View v){
        Log.d(TAG,"initImageBitmaps: preparing bitmaps");
        game = new ArrayList<Game>();

        Game mytho = new Game(
                R.string.mythoGame,
                R.drawable.mytho,
                R.string.mythoGameInfo
        );

        Game galerapagos = new Game(
                R.string.galerapagosGame,
                R.drawable.galerapagos,
                R.string.galerapagosGameInfo
        );

        Game petitMeurtre = new Game(
                R.string.petitMeurtreGame,
                R.drawable.petitmeurtre,
                R.string.petitMeurtreGameInfo
        );

        game.add(mytho);
        game.add(galerapagos);
        game.add(petitMeurtre);

        initRecycleView(v);
    }

    private  void initRecycleView(View v){
        Log.d(TAG,"initRecycleView: init recyclerview");
        recyclerView = v.findViewById(R.id.gameRecycler);
        adapter = new GameAdapter(getContext(),R.layout.activity_game_adapter,game);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

}
