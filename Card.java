import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import acm.io.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Card {
	
	public Card (String card, double height) {
		parseLine(card);
		setImage(height);
	}
	
	/** Reads through the string from the file describing the card and stores in class variables. */
	private void parseLine (String line) {
		int kindIndex = 1;
		if (line.indexOf("0") != -1) {
			kindIndex = 2;
		}

		kind = line.substring(0, kindIndex);
		
		int suitIndex = kindIndex + 1;
		suit = line.substring(suitIndex - 1, suitIndex);
		
		int colorIndex = suitIndex + 1;
		color = line.substring(colorIndex - 1, colorIndex);
	}
	
	/** Sets the front and back images for the card. */
	private void setImage(double height) {
		cardBack = new GImage ("Playing Cards/card_back.jpg");
		cardHeight = height;
		origHeight = height;
		cardWidth = cardBack.getWidth() / cardBack.getHeight() * cardHeight;
		origWidth = cardWidth;
		cardBack.setSize(cardWidth, cardHeight);
		
		cardFront = new GImage ("Playing Cards/Card Front Imgs/" + toString() + ".png");
		cardFront.setSize(cardWidth, cardHeight);
	}
	
	public String toString() {
		return "" + kind + suit + color;
	}
	
	public GImage getCardFrontImg() {
		return cardFront;
	}
	
	public GImage getCardBackImg() {
		return cardBack;
	}

	/** Returns the image of the card rotated by the specified angle */
	public GImage rotateCard(int theta) {
		if (theta != 0) {
			GImage rotatedImg = new GImage ("Playing Cards/Card Front Imgs/" + toString() + " " + theta + ".png");
			cardFront = rotatedImg;
			
			//If the card is rotated 90 degrees, switch dimensions.
			if (theta == 90) {
				cardWidth = origHeight;
				cardHeight = origWidth;
				
			//If the card is rotated 45 or 135 degrees, set the dimensions such that the size of the card image stays the same.
			} else {
				cardWidth = calculateNewWidth();
				cardHeight = cardWidth;
			}
			cardFront.setSize(cardWidth, cardHeight);
		} else {
			GImage origImg = new GImage ("Playing Cards/Card Front Imgs/" + toString() + ".png");
			cardFront = origImg;
			cardWidth = origWidth;
			cardHeight = origHeight;
			cardFront.setSize(cardWidth, cardHeight);
		}
		return cardFront;
	}
	
	/** Calculates the exact value of the width when the card is rotated 45 or 135 degrees */
	private double calculateNewWidth() {
		double y = origHeight / 2;
		double x = origWidth / 2;
		double theta2 = Math.atan(y / x) - Math.toRadians(45);
		
		double x2 = x * x;
		double y2 = y * y;
		double z = Math.sqrt(x2 + y2);
		
		return 2 * (Math.cos(theta2) * z);
	}
	
	public double getWidth() {
		return cardWidth;
	}
	
	
	public void setCardBackHeight(double height) {
		cardFront.setSize(cardWidth, height);
	}
	
	public double getCardBackHeight() {
		return cardBack.getHeight();
	}
	
	public void setCardBackWidth(double width) {
		cardBack.setSize(width, cardHeight);
	}
	
	public double getCardBackWidth() {
		return cardBack.getWidth();
	}
	
	public void setCardFrontHeight(double height) {
		cardFront.setSize(cardWidth, height);
	}
	
	public double getCardFrontHeight() {
		return cardFront.getHeight();
	}
	
	public void setCardFrontWidth(double width) {
		cardFront.setSize(width, cardHeight);
	}
	
	public double getCardFrontWidth() {
		return cardFront.getHeight();
	}
	
	public void resetCardImgDimensions() {
		rotateCard(0);
		setCardFrontHeight(origHeight);
		setCardFrontWidth(origWidth);
		setCardBackHeight(origHeight);
		setCardBackWidth(origWidth);
	}
	
	public double getOrigHeight() {
		return origHeight;
	}
	
	public double getOrigWidth() {
		return origWidth;
	}
	
	public double getHeight() {
		return cardHeight;
	}
	
	public String getKind() {
		return kind;
	}
	
	public String getSuit() {
		return suit;
	}
	
	public String getColor() {
		return color;
	}
	
	private GImage cardBack;
	private GImage cardFront;
	private double cardWidth;
	private double cardHeight;
	private String kind;
	private String suit;
	private String color;
	
	private double origWidth;
	private double origHeight;
	
}
