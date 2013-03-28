/**
 *
 */
package de.geymer.je.Gameplay;

import java.io.Serializable;

/**
 * @author babkamen
 */
public class Player implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7705028588946309068L;
	private int score;
    private String name;
    private boolean bot;


    Player(String name, boolean bot) {
        this.name = name;
        this.bot = bot;
        score = 0;
    }

    public int getScore() {
        return score;
    }

    /**
     * @param score the score to set
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    public void adjustScore(int amm) {
        score += amm;
    }

}
