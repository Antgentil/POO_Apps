package pt.isel.poo.G2LI21N.covid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class MenuActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        findViewById(R.id.play_game).setOnClickListener(this::onPlayGame);
    }

    /**
     * Handles the play button
     * @param v view
     */
    private void onPlayGame(View v) {
        Intent it = new Intent(this, CovidActivity.class);
        startActivity(it);
    }
}
