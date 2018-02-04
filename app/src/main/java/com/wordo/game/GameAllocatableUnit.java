package com.wordo.game;

/**
 * Created by Arihant on 19-06-2016.
 */
public class GameAllocatableUnit {

    public String word;

    public String gloss;

    public String wordID;

    public String difficultyLevel;

    public int levelNo;

    public int scoreNeeded;

    public int gameTime;

    public int noOfPits;

    public int noOfAngryBalls;

    public int noOfMagnetBalls;

    private int lockType;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getGloss() {
        return gloss;
    }

    public void setGloss(String gloss) {
        this.gloss = gloss;
    }

    public String getWordID() {
        return wordID;
    }

    public void setWordID(String wordID) {
        this.wordID = wordID;
    }

    public String getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getLevelNo() {
        return levelNo;
    }

    public void setLevelNo(int levelNo) {
        this.levelNo = levelNo;
    }

    public int getScoreNeeded() {
        return scoreNeeded;
    }

    public void setScoreNeeded(int scoreNeeded) {
        this.scoreNeeded = scoreNeeded;
    }

    public int getGameTime() {
        return gameTime;
    }

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getNoOfPits() {
        return noOfPits;
    }

    public void setNoOfPits(int noOfPits) {
        this.noOfPits = noOfPits;
    }

    public int getNoOfAngryBalls() {
        return noOfAngryBalls;
    }

    public void setNoOfAngryBalls(int noOfAngryBalls) {
        this.noOfAngryBalls = noOfAngryBalls;
    }

    public int getNoOfMagnetBalls() {
        return noOfMagnetBalls;
    }

    public void setNoOfMagnetBalls(int noOfMagnetBalls) {
        this.noOfMagnetBalls = noOfMagnetBalls;
    }

    public int getLockType() {
        return lockType;
    }

    public void setLockType(int lockType) {
        this.lockType = lockType;
    }

}
