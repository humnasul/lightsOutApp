package com.example.lightsout;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private LightsOutGame mGame;
    private GridLayout mLightGrid;
    private final String GAME_STATE = "gameState";
    private final String BOX_COLOR = "boxColor";
    private int mLightOnColor;
    private int mLightOffColor;
    private int mLightOnColorId;

    public int finalColorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLightOnColorId = R.color.yellow;

        mLightGrid = findViewById(R.id.light_grid);

        mLightOnColor = ContextCompat.getColor(this, R.color.yellow);
        mLightOffColor = ContextCompat.getColor(this, R.color.black);

        mGame = new LightsOutGame();

        if(savedInstanceState == null)
        {
            startGame();
        }
        else
        {
            String gameState = savedInstanceState.getString(GAME_STATE);
            mGame.setState(gameState);

            int boxColor = savedInstanceState.getInt(BOX_COLOR);
            newButtonColor(boxColor);
            setButtonColors();
        }
        //used to check if app is just starting or if it is continuing from before
        //"else" restores old state from before if appropriate
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(GAME_STATE, mGame.getState());
        outState.putInt(BOX_COLOR, mLightOnColor);
    }

    private void startGame() {
        mGame.newGame();
        setButtonColors();
    }

    public void onLightButtonClick(View view) {

        // Find the button's row and col
        int buttonIndex = mLightGrid.indexOfChild(view);
        int row = buttonIndex / LightsOutGame.GRID_SIZE;
        int col = buttonIndex % LightsOutGame.GRID_SIZE;

        mGame.selectLight(row, col);
        setButtonColors();

        // Congratulate the user if the game is over
        if (mGame.isGameOver()) {
            Toast.makeText(this, R.string.congrats, Toast.LENGTH_SHORT).show();
        }
    }

    private void setButtonColors() {

        // Set all buttons' background color
        for (int row = 0; row < LightsOutGame.GRID_SIZE; row++) {
            for (int col = 0; col < LightsOutGame.GRID_SIZE; col++) {

                // Find the button in the grid layout at this row and col
                int buttonIndex = row * LightsOutGame.GRID_SIZE + col;
                Button gridButton = (Button) mLightGrid.getChildAt(buttonIndex);

                if (mGame.isLightOn(row, col)) {
                    gridButton.setBackgroundColor(mLightOnColor);
                } else {
                    gridButton.setBackgroundColor(mLightOffColor);
                }
            }
        }
    }

    private void newButtonColor(int color)
    {
        mLightOnColor = color;
    }

    public void onNewGameClick(View view) {
        startGame();
    }

    public void onChangeColorClick(View view){
        Intent intent = new Intent(this, ColorActivity.class);
        intent.putExtra(ColorActivity.EXTRA_COLOR, mLightOnColorId);
        mColorResultLauncher.launch(intent);
    }

    ActivityResultLauncher<Intent> mColorResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>(){
        @Override
        public void onActivityResult (ActivityResult result){
        if (result.getResultCode() == Activity.RESULT_OK) {
            Intent data = result.getData();
            if (data != null) {
                finalColorId = data.getIntExtra(ColorActivity.EXTRA_COLOR, mLightOnColorId);
                mLightOnColor = ContextCompat.getColor(MainActivity.this, finalColorId);
                setButtonColors();
            }
        }
    }
    });
}