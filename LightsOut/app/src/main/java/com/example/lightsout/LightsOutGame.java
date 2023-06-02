package com.example.lightsout;

import java.util.Random;

public class LightsOutGame {
    public static final int GRID_SIZE = 3;

    private final boolean[][] mLightsGrid;

    public LightsOutGame() {
        mLightsGrid = new boolean[GRID_SIZE][GRID_SIZE];
    }

    public void newGame() {
        Random randomNumGenerator = new Random();
        do {
            for (int row = 0; row < GRID_SIZE; row++) {
                for (int col = 0; col < GRID_SIZE; col++) {
                    mLightsGrid[row][col] = randomNumGenerator.nextBoolean();
                }
            }
        } while (!isGameOver());
    }

    public boolean isLightOn(int row, int col) {
        return mLightsGrid[row][col];
    }

    public void selectLight(int row, int col) {
        mLightsGrid[row][col] = !mLightsGrid[row][col];
        if (row > 0) { //need to flip light above it
            mLightsGrid[row - 1][col] = !mLightsGrid[row - 1][col];
        }
        if (row < GRID_SIZE - 1) { // need to flip light below it
            mLightsGrid[row + 1][col] = !mLightsGrid[row + 1][col];
        }
        if (col > 0) { //need to flip light to the left
            mLightsGrid[row][col - 1] = !mLightsGrid[row][col - 1];
        }
        if (col < GRID_SIZE - 1) { //need to flip light to the right
            mLightsGrid[row][col + 1] = !mLightsGrid[row][col + 1];
        }
    }

    public boolean isGameOver() {
        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < GRID_SIZE; col++) {
                if (mLightsGrid[row][col]) {
                    return false;
                }
            }
        }
        return true;
    }

    public String getState()
    {
        StringBuilder boardString = new StringBuilder();
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int col = 0; col <GRID_SIZE; col++)
            {
                char value = mLightsGrid[row][col] ? 'T' : 'F';
                boardString.append(value);
            }
        }
        return boardString.toString();
    }

    public void setState(String gameState)
    {
        int index = 0;
        for (int row = 0; row < GRID_SIZE; row++)
        {
            for (int col = 0; col < GRID_SIZE; col++)
            {
                mLightsGrid[row][col] = gameState.charAt(index) == 'T';
                index++;
            }
        }
    }


}
