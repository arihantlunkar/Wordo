package com.wordo.game;

import java.util.Random;

/**
 * Created by Arihant on 03-07-2016.
 */
public class GameOverDictionary {

    String[] success = {"Fabulous !", "Extraordinary !", "Tremendous !", "Stupendous !", "Phenomenal !",
            "Remarkable !", "Exceptional !", "Astounding !", "Amazing !", "Astonishing !",
            "Fantastic !", "Breathtaking !", "Overwhelming !", "Incredible !", "Unbelievable !",
            "Mind Blowing !", "Ming Boggling !", "Very Good !", "Wonderful !", "Excellent !",
            "Superb !", "Marvellous !", "First Class !", "Outstanding !", "Magnificent !",
            "Splendid !", "Awesome !", "Super Duper !", "Terrific !", "Great !",
            "Lovely !", "Bingo !", "Extravagant !", "Legendary !", "Super !",
            "Prodigious !", "Eye opening !", "Professional !", "World Class !", "Mind Altering !",
            "Impressive !", "Smashing !", "Glorious !", "Fantabulous !", "Bang Up !",
            "A-1 !", "Best !", "First Class !", "Matchless !", "Boss Like !"};
    String[] failureIncorrectWord = {"Better luck next time !", "Hard  Luck !", "Wrong !", "Incorrect !", "Inaccurate !",
            "Invalid !", "Unsatisfactory !", "Chance Missed !", "Tough Luck !", "A miss !", "Not Right !",
            "Setback !", "Tough Break !", "Imperfect !", "Unsuitable !", "Not good enough !", "Vague !",
            "Off target !", "Inapt !", "Undesirable !", "Out of order !", "Imprecise !", "Improper !"};
    String[] failureMagnetCollision = {"Beware of Magnets !", "Look out for Magnets !", "Be cautious of Magnets !"
            , "Be careful of Magnets !", "Keep your eyes open before Magnets !", "Avoid Magnets !",
            "Be wary of Magnets !", "Stay away from Magnets !", "Keep an eye on Magnets !", "Be alert of Magnets !",
            "Danger from Magnets !", "Shun Magnets !", "Leave alone Magnets !", "Snub Magnets !", "Don't touch Magnets !"};
    String[] failureTimeOver = {"Time's up !", "Need to catch up Time !", "Hurry Up !", "So Close !", "Speed Up !",
            "Time's Over !", "A bit Slow !", "Too Slow !", "Lose no Time !"};
    String[] failureAngryBallCollision = {"Beware of Angry Balls !", "Look out for Angry Balls !", "Be cautious of Angry Balls !"
            , "Be careful of Angry Balls !", "Keep your eyes open before Angry Balls !", "Avoid Angry Balls !",
            "Be wary of Angry Balls !", "Stay away from Angry Balls !", "Keep an eye on Angry Balls !", "Be alert of Angry Balls !",
            "Danger from Angry Balls !", "Shun Angry Balls !", "Leave alone Angry Balls !", "Snub Angry Balls !", "Don't touch Angry Balls !"};
    String[] failureInsidePit = {"Beware of Holes !", "Look out for Holes !", "Be cautious of Holes !"
            , "Be careful of Holes !", "Keep your eyes open before Holes !", "Avoid Holes !",
            "Be wary of Holes !", "Stay away from Holes !", "Keep an eye on Holes !", "Be alert of Holes !",
            "Danger from Holes !", "Shun Holes !", "Leave alone Holes !", "Snub Holes !", "Mind you don't fall into Holes !"};

    public String getGameOverWord(int reason) {
        switch (reason) {
            case 1:
                return failureTimeOver[new Random().nextInt(failureTimeOver.length)];
            case 2:
                return failureMagnetCollision[new Random().nextInt(failureMagnetCollision.length)];
            case 3:
                return failureAngryBallCollision[new Random().nextInt(failureAngryBallCollision.length)];
            case 4:
                return failureInsidePit[new Random().nextInt(failureInsidePit.length)];
            case 5:
                return failureIncorrectWord[new Random().nextInt(failureIncorrectWord.length)];
            default:
                return success[new Random().nextInt(success.length)];
        }
    }
}
