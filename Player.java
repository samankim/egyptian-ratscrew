


import java.util.*;
import acm.program.*;
import acm.graphics.*;

public class Player extends ConsoleProgram {
	
	/* Constructor: Player */
	public Player(String name) {
		playerName = name;
	}	
	
	
	/* Method: getName */
	/** Returns player's name */
	public String getName() {
		return playerName;
	}	
	
	public void setPlayerNum(int num) {
		playerNum = num;
	}
	
	
	public int getPlayerNum() {
		return playerNum;
	}
	
	public void setProfilePic(GImage img) {
		playerProfilePic = img;
	}
	
	public GImage getProfilePic() {
		return playerProfilePic;
	}
	
	public String toString() {
		return playerName;
	}
	
	
	
	/* Wheel of Fortune methods */
	
	/* Method: getBalance */
	/** Returns player's balance */
	public int getBalance() {
		return balance;
	}
	
	
	/* Method: setBalance */
	/** Sets player's balance */
	public int setBalance(int newBalance) {
		balance = newBalance;
		return balance;	
	}
	
	/* Method: changeBalance */
	/** Changes the players balance by amount changeInBalance */
	public void changeBalance(int changeInBalance) {
		balance += changeInBalance;
	}
	
	
	/* Egyptian Ratscrew methods */
	
	/* Method: setDeck */
	/** Sets player's deck of cards*/
	public void setDeck(ArrayList <Card> deck) {
		playerdeck = deck;
	}

	/* Method: getCard */
	/** Returns card at specified index */
	public Card getCard(int index) {
		return playerdeck.get(index);
	}
	
	/* Method: removeCard */
	/** Removes card at specified index and returns the removed card*/
	public Card removeCard(int index) {
		return playerdeck.remove(index);
	}
	
	/* Method: addPile */
	/** Adds pile of cards to the bottom of the player's deck */
	public void addPile(ArrayList <Card> pile) {
		for (int i = 0; i < pile.size(); i++) {
			Card card = pile.get(i);
			playerdeck.add(card);
		}
	}
	
	/* Method: getDeck */
	/** Returns player's deck */
	public ArrayList <Card> getDeck() {
		return playerdeck;
	}
	
	public int getDeckSize() {
		return playerdeck.size();
	}
	
	
	
	public void setPlayerX(double x) {
		playerX = x;
	}
	
	public void setPlayerY(double y) {
		playerY = y;
	}
	
	public double getPlayerX() {
		return playerX;
	}
	
	public double getPlayerY() {
		return playerY;
	}
	
	public boolean isComputerPlayer() {
		return false;
	}

	
	
	/** Private instance variables */
	private String playerName;
	private int playerNum;
	private GImage playerProfilePic;
	
	/** Wheel of Fortune private instance variables */
	private int balance;
	
	/** Egyptian Ratscrew private instance variables */
	private ArrayList <Card> playerdeck;
	private double playerX;
	private double playerY;
	private double playerDeckX;
	private double playerDeckY;
}

