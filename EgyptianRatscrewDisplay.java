import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import acm.io.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


public class EgyptianRatscrewDisplay extends GraphicsProgram {

	
	private static final Color TABLE_COLOR = new Color(0, 128, 0); //?????
	
	private static final double FACE_HEIGHT = 75;
	private static final double FACE_SPACE = 25;
	private static final double PLAYER_OFFSET = 30;
	private static final double NAME_SPACE = 8;
	
	private static final double HAND_HEIGHT = 100;
	private static final double HAND_WIDTH = 100;
	
	private static final double CARD_HEIGHT = 150;
	private static final Color DECK_LABEL_COLOR = Color.WHITE;
	
	private static final double BUTTON_SPACE = 20;
	private static final double BUTTON_WIDTH = 100;
	private static final double BUTTON_HEIGHT = 20;
	
	private static final Color COLLECT_BUTTON_COLOR = Color.WHITE;
	private static final Color COLLECT_BUTTON_FLASH_COLOR = Color.YELLOW;
	
	private static final double CHECK_BUTTON_SPACE = 40;
	private static final Color CHECK_BUTTON_COLOR = Color.YELLOW;
	
	private static final Color SLAP_ALERT_COLOR = Color.WHITE;
	private static final Color SLAP_ALERT_FLASH_COLOR = Color.RED;
	public static final double SLAP_ALERT_HEIGHT = 25;
	public static final double SLAP_ALERT_WIDTH = 100;
	private static final double SLAP_ALERT_OFFSET = 20;
	public static final double SLAP_ALERT_X = SLAP_ALERT_OFFSET;
	public static final double SLAP_ALERT_Y = SLAP_ALERT_OFFSET;
	
	private static final Color PLAYER_HIGHLIGHT_COLOR = Color.YELLOW;
	private static final double PLAYER_HIGHLIGHT_WIDTH = 5;
	
	private static final double COLLECT_ANIM_PAUSE = 10;
	
	
	
	public EgyptianRatscrewDisplay (GCanvas canvas) {
		displayCanvas = canvas;
		displayCanvas.setBackground(TABLE_COLOR);
	}
	
	
	public void add(GObject obj) {
		displayCanvas.add(obj);
	}
	
	public void remove(GObject obj) {
		displayCanvas.remove(obj); //something wrong with this??
	}
	
	public void removeAll() {
		displayCanvas.removeAll();
	}
	
	public double getSlapAlertX() {
		return SLAP_ALERT_X;
	}
	
	public double getSlapAlertY() {
		return SLAP_ALERT_Y;
	}
	
	public double getSlapAlertWidth() {
		return SLAP_ALERT_WIDTH;
	}
	
	public double getSlapAlertHeight() {
		return SLAP_ALERT_HEIGHT;
	}
	
	
	
	
	
	
	
	
	public void init (ArrayList <Player> gamePlayers, Card card) {
		players = gamePlayers;
		cardTemplateWidth = card.getWidth();
		drawProfiles();
		theta = 0;
		addCheckButton();
		noShowCheckButton();
		setUpSlapAlert();
		setUpMidpoints();
	}
	
	private void setUpMidpoints() {
		centerPileLeftMidpoint = new GPoint (getWidth() / 2 - getCardWidth() / 2, getHeight() / 2);
		centerPileRightMidpoint = new GPoint (getWidth() / 2 + getCardWidth(), getHeight() / 2);
		centerPileUpperMidpoint = new GPoint (getWidth() / 2, getHeight() / 2 - getCardHeight() / 2);
		centerPileLowerMidpoint = new GPoint (getWidth() / 2, getHeight() / 2 + getCardHeight() / 2);
	}
	
	public GPoint getMidpoint (String location) {
		switch(location) {
		case "left": return centerPileLeftMidpoint;
		case "right": return centerPileRightMidpoint;
		case "upper": return centerPileUpperMidpoint;
		case "lower": return centerPileLowerMidpoint;	
		default: return null;
		}
	}
	
	private void addCheckButton() {
		double x = displayCanvas.getWidth() / 2;
		double y = displayCanvas.getHeight() / 2 + getCardHeight() / 2 + CHECK_BUTTON_SPACE;
		checkButton = new Button("Check Pile", x, y, BUTTON_WIDTH, BUTTON_HEIGHT, CHECK_BUTTON_COLOR);
		add(checkButton);
	}
	
	public Button getCheckButton() {
		return checkButton;
	}
	
	public void clickCheckButton() {
		checkButton.clickButton();
		add(checkButton);
	}
	
	public void releaseCheckButton() {
		checkButton.releaseButton();
		add(checkButton);
	}
	
	public boolean isCheckButtonRaised() {
		return checkButton.isRaised();
	}
	
	public double getCardHeight() {
		return CARD_HEIGHT;
	}
	
	public double getCardWidth() {
		return cardTemplateWidth;
	}
	
	/** Creates the profile picture image, names, deck images, deck labels, and buttons */
	private void drawProfiles() {
		setPlayerLocations();
		displayPlayers();
		setUpHandImgs();
		setUpPlayerDecks();
		setUpCollectButtons();
		Iterator <Player> itPlayer = players.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			displayPlayerDeckImg(player);
			updateDeckLabel(player);
		}
	}

	/** Sets the locations of each player based on where they should be seated on the screen */
	private void setPlayerLocations() {
		double bottomSeatX = displayCanvas.getWidth() / 2 - FACE_HEIGHT / 2;
		double bottomSeatY = displayCanvas.getHeight() - (FACE_HEIGHT + PLAYER_OFFSET);
		double bottomSeatDeckX = bottomSeatX + FACE_HEIGHT / 2  - getCardWidth() / 2;
		double bottomSeatDeckY = bottomSeatY - (getCardHeight() + BUTTON_SPACE);
		double bottomSeatButtonX = bottomSeatX + FACE_HEIGHT / 2;
		double bottomSeatButtonY = bottomSeatDeckY - BUTTON_SPACE;
		char bottomSeat = 'b';
		
		double topSeatX = displayCanvas.getWidth() / 2 - FACE_HEIGHT / 2;
		double topSeatY = PLAYER_OFFSET;
		double topSeatDeckX = topSeatX + FACE_HEIGHT / 2 - getCardWidth() / 2;
		double topSeatDeckY = topSeatY + FACE_SPACE + FACE_HEIGHT;
		double topSeatButtonX = topSeatX + FACE_HEIGHT / 2;
		double topSeatButtonY = topSeatDeckY + getCardHeight() + BUTTON_SPACE;
		char topSeat = 't';
		
		double rightSeatX = displayCanvas.getWidth() - (FACE_HEIGHT + PLAYER_OFFSET);
		double rightSeatY = displayCanvas.getHeight() / 2 - FACE_HEIGHT / 2;
		double rightSeatDeckX = rightSeatX - (FACE_SPACE + getCardWidth());
		double rightSeatDeckY = rightSeatY;
		double rightSeatButtonX = rightSeatDeckX + getCardWidth() / 2;
		double rightSeatButtonY = rightSeatDeckY + getCardHeight() + BUTTON_SPACE;
		char rightSeat = 'r';
		
		double leftSeatX = PLAYER_OFFSET;
		double leftSeatY = displayCanvas.getHeight() / 2 - FACE_HEIGHT / 2;
		double leftSeatDeckX = leftSeatX + FACE_SPACE + FACE_HEIGHT;
		double leftSeatDeckY = leftSeatY;
		double leftSeatButtonX = leftSeatDeckX + getCardWidth() / 2;
		double leftSeatButtonY = leftSeatDeckY + getCardHeight() + BUTTON_SPACE;
		char leftSeat = 'l';
		
		double x = 0;
		double y = 0;
		
		double deckX = 0;
		double deckY = 0;
		
		double buttonX = 0;
		double buttonY = 0;
		
		char seat = 0;
		
		for (int i = 0; i < players.size(); i++) {
			switch(i) {
			case 0: x = bottomSeatX;
					y = bottomSeatY;
					deckX = bottomSeatDeckX;
					deckY = bottomSeatDeckY;
					buttonX = bottomSeatButtonX;
					buttonY = bottomSeatButtonY;
					seat = bottomSeat;
					break;
			case 1: if (players.size() < 3) {
						x = topSeatX;
						y = topSeatY;
						deckX = topSeatDeckX;
						deckY = topSeatDeckY;
						buttonX = topSeatButtonX;
						buttonY = topSeatButtonY;
						seat = topSeat;
					} else {
						x = rightSeatX;
						y = rightSeatY;
						deckX = rightSeatDeckX;
						deckY = rightSeatDeckY;
						buttonX = rightSeatButtonX;
						buttonY = rightSeatButtonY;
						seat = rightSeat;
					}
					break;
			case 2: x = topSeatX;
					y = topSeatY;
					deckX = topSeatDeckX;
					deckY = topSeatDeckY;
					buttonX = topSeatButtonX;
					buttonY = topSeatButtonY;
					seat = topSeat;
					break;
			case 3: x = leftSeatX;
					y = leftSeatY;
					deckX = leftSeatDeckX;
					deckY = leftSeatDeckY;
					buttonX = leftSeatButtonX;
					buttonY = leftSeatButtonY;
					seat = leftSeat;
					break;
			}
		players.get(i).setPlayerX(x);
		players.get(i).setPlayerY(y);
		GPoint deckLocation = new GPoint (deckX, deckY);
		playerDeckLocations.put(players.get(i), deckLocation);
		GPoint buttonLocation = new GPoint (buttonX, buttonY);
		playerButtonLocations.put(players.get(i), buttonLocation);
		playerSeatLocations.put(players.get(i), seat);
		}
		
	}
	
	
	/** Displays the profile picture and name of the player */
	private void displayPlayers() {
		Iterator <Player> itPlayer = players.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			GImage profilePic = player.getProfilePic();
			profilePic.setSize(FACE_HEIGHT, FACE_HEIGHT);
			profilePic.setLocation(player.getPlayerX(), player.getPlayerY());
			add(profilePic);
			
			GRect highlight = new GRect(profilePic.getWidth() + PLAYER_HIGHLIGHT_WIDTH * 2, profilePic.getHeight() + PLAYER_HIGHLIGHT_WIDTH * 2);
			highlight.setLocation(profilePic.getX() - PLAYER_HIGHLIGHT_WIDTH, profilePic.getY() - PLAYER_HIGHLIGHT_WIDTH);
			highlight.setColor(PLAYER_HIGHLIGHT_COLOR);
			highlight.setFilled(true);
			highlight.setFillColor(PLAYER_HIGHLIGHT_COLOR);
			highlight.setVisible(false);
			add(highlight);
			highlight.sendToBack();
			highlightMap.put(player, highlight);
			
			GLabel playerName = new GLabel (player.getName());
			double x = player.getPlayerX() + FACE_HEIGHT / 2 - playerName.getWidth() / 2;
			double y = player.getPlayerY() + FACE_HEIGHT + playerName.getAscent() + NAME_SPACE;
			playerName.setLocation(x, y);
			add(playerName);
		}
	}
	
	/** Sets each player's hand image and stores in a map */
	private void setUpHandImgs() {
		String direction = "";
		for (int i = 0; i < players.size(); i++) {
			switch(i) {
			case 0: direction = "up";
					break;
			case 1: if (players.size() >= 3) {
						direction = "left";
					} else {
						direction = "down"; //just put the change method when you hit the third player
					}
					break;
			case 2: direction = "down";
					break;
			case 3: direction = "right";
					break;
			}
			GImage hand = new GImage("hand facing " + direction + ".png");
			hand.setSize(HAND_HEIGHT, HAND_WIDTH);
			hand.setLocation(displayCanvas.getWidth() / 2 - hand.getWidth() / 2, displayCanvas.getHeight() / 2 - hand.getHeight() / 2);
			handMap.put(players.get(i), hand);
		}
	}
	
	/** Sets up each player's deck image and deck label */
	private void setUpPlayerDecks() {
		Iterator <Player> itPlayer = players.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			displayPlayerDeckImg(player);
			displayPlayerDeckLabel(player);
		}
	}
	
	/** Displays the number declaring how many cards are in the player's deck */
	private void displayPlayerDeckLabel(Player player) {
		GLabel deckLabel = new GLabel("");
		double x = playerDeckLocations.get(player).getX() + getCardWidth() / 2 - deckLabel.getWidth() / 2;
		double y = playerDeckLocations.get(player).getY() + getCardHeight() / 2 + deckLabel.getAscent() / 2;
		deckLabel.setLocation(x, y);
		
		displayPlayerDeckLabelBGs(player, deckLabel.getAscent(), deckLabel.getHeight());
		add(deckLabel);
		
		labelMap.put(player, deckLabel);
	}
	
	/** Displays the background of the deck label to improve visibility of the number. */
	private void displayPlayerDeckLabelBGs(Player player, double labelAscent, double labelHeight) {
		GRect deckLabelBG = new GRect(playerDeckLocations.get(player).getX(), playerDeckLocations.get(player).getY() 
					+ getCardHeight() / 2 - labelAscent / 2, getCardWidth(), labelHeight);
			deckLabelBG.setFilled(true);
			deckLabelBG.setFillColor(DECK_LABEL_COLOR);
			add(deckLabelBG);
			labelBGMap.put(player, deckLabelBG);
	}
	
	/** Set up "Collect Pile" buttons for each player */
	private void setUpCollectButtons() {
		Iterator <Player> itPlayer = players.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			displayPlayerCollectButton(player);
		}
	}
	
	/** Displays "Collect Pile" button for one player */
	private void displayPlayerCollectButton(Player player) {
		double x = playerButtonLocations.get(player).getX();
		double y = playerButtonLocations.get(player).getY();
		Button button = new Button("Collect Pile", x, y, BUTTON_WIDTH, BUTTON_HEIGHT, COLLECT_BUTTON_COLOR);
		buttonMap.put(player, button);
		add(button);
		
	}
	
	/** Returns the player button associated with the given player */
	public Button getPlayerButton (Player player) {
		return buttonMap.get(player);
	}
	
	public void clickPlayerButton (Player player) {
		buttonMap.get(player).clickButton();
		add(getPlayerButton(player));
	}
	
	public void releasePlayerButton (Player player) {
		buttonMap.get(player).releaseButton();
		add(getPlayerButton(player));
	}

	
	/** Displays the hands of the players who slapped the center pile */
	public void displayHands(ArrayList <Player> handList) {
		hands = handList;
		Iterator <Player> it = hands.iterator();
		while (it.hasNext()) {
			add(handMap.get(it.next()));
		}
	}
	
	/** Removes all the images of the hands */
	public void removeHands() {
		Iterator <Player> it = hands.iterator();
		while (it.hasNext()) {
			GImage handImg = handMap.get(it.next());
			remove(handImg);
		}
	}
	
	/** Creates the Slap Alert */
	private void setUpSlapAlert() {
		slapAlert = new SlapAlert (SLAP_ALERT_X, SLAP_ALERT_Y, SLAP_ALERT_WIDTH, 
				SLAP_ALERT_HEIGHT, SLAP_ALERT_COLOR);
		add(slapAlert);
	}
	
	public void showSlapAlert() {
		slapAlert.setVisible(true);
		add(slapAlert);
	}
	
	public void noShowSlapAlert() {
		slapAlert.setVisible(false);
		add(slapAlert);
	}
	

	/** Changes the color of the slap alert to indicate that the pile can be slapped. */
	public void turnOnSlapAlert(){
		slapAlert.setBGColor(SLAP_ALERT_FLASH_COLOR);
	}
	
	/** Changes the color of the slap alert to indicate that the pile cannot be slapped. */
	public void turnOffSlapAlert() {
		slapAlert.setBGColor(SLAP_ALERT_COLOR);
	}
	
	public void highlightPlayer(Player player) {
		highlightMap.get(player).setVisible(true);
	}
	
	public void unhighlightPlayer(Player player) {
		highlightMap.get(player).setVisible(false);
	}
	
	
	/** Updates the images of the player decks, player deck labels, and the center pile */
	public void updateAllDecks(ArrayList <Card> centerPile) {
		updatePlayerDecks();
		displayCenterPile(centerPile);
	}
	
	/** Updates the images of the player decks and player deck labels. */
	public void updatePlayerDecks() {
		Iterator <Player> itPlayer = players.iterator();
		while (itPlayer.hasNext()) {
			Player player = itPlayer.next();
			displayPlayerDeckImg(player);
			updateDeckLabel(player);
		}
	}
	
	/** Displays the image of the back of the card on top of the player's pile. */
	private void displayPlayerDeckImg(Player player) {
		GObject obj = displayCanvas.getElementAt(playerDeckLocations.get(player));
		if (obj != null) remove(obj);
		if (player.getDeck().size() == 0) return;
		Card topCard = player.getDeck().get(0);
		GImage topCardImg = topCard.getCardBackImg();
		add(topCardImg, playerDeckLocations.get(player));
		topCardImg.sendToBack();
	}
	
	/** Updates the number on the deck label to indicate how many cards are in the player's pile. */
	private void updateDeckLabel(Player player) {
		double x = playerDeckLocations.get(player).getX();
		double y = playerDeckLocations.get(player).getY() + getCardHeight() / 2;
		if (player.getDeck().size() == 0) { //If the player has no more cards, does not display the label.
			labelMap.get(player).setVisible(false);
			labelBGMap.get(player).setVisible(false);
			
		} else {
			GLabel deckLabel = labelMap.get(player);
			deckLabel.setLabel("" + player.getDeckSize());
			x = playerDeckLocations.get(player).getX() + getCardWidth() / 2 - deckLabel.getWidth() / 2;
			y = playerDeckLocations.get(player).getY() + getCardHeight() / 2 + deckLabel.getAscent() / 2;
			deckLabel.setLocation(x, y);
			
			if (player.getDeckSize() != 0) {
				GRect labelBG = labelBGMap.get(player);
				labelBG.setVisible(true);
				labelBG.sendToFront();
				deckLabel.setVisible(true);	
				deckLabel.sendToFront();
			}
		}
	}

	/** Eliminates all images in the center pile and moves them over to the collect player's pile*/
	public void clearCenterPile(Player collectPlayer) {
		theta = 0;
		GObject obj = displayCanvas.getElementAt(displayCanvas.getWidth() / 2, displayCanvas.getHeight() / 2);
		GCompound pile = new GCompound();
		while (obj != null) {
			remove(obj);
			pile.add(obj);
			obj = displayCanvas.getElementAt(displayCanvas.getWidth() / 2, displayCanvas.getHeight() / 2);
		}
		animateCollectPile(collectPlayer, pile);
	}
	
	private void animateCollectPile(Player collectPlayer, GCompound pile) {
		pile.sendToBack();
		add(pile);
		double dx = 0;
		double dy = 0;
		GPoint pt = new GPoint();
		switch(getPlayerSeatLocation(collectPlayer)) {
		case 'b': 	dy = 5;
					pt = new GPoint (getPlayerDeckLocation(collectPlayer).getX(), getPlayerDeckLocation(collectPlayer).getY() + getCardHeight());
					while (! pile.contains(getPlayerDeckLocation(collectPlayer))) {
						pile.move(dx, dy);
						pause (COLLECT_ANIM_PAUSE);
					}
					break;
		case 'r':	dx = 5;
					pt = new GPoint (getPlayerDeckLocation(collectPlayer).getX() + getCardWidth(), getPlayerDeckLocation(collectPlayer).getY());
					while (! pile.contains(pt)) {
						pile.move(dx, dy);
						pause (COLLECT_ANIM_PAUSE);
					}
					break;
		case 't': 	dy = -5;
					pt = new GPoint (getPlayerDeckLocation(collectPlayer));
					while (! pile.contains(pt)) {
						pile.move(dx, dy);
						pause (COLLECT_ANIM_PAUSE);
					}
					break;
		case 'l': 	dx = -5;
					pt = new GPoint (getPlayerDeckLocation(collectPlayer));
					while (! pile.contains(pt)) {
						pile.move(dx, dy);
						pause (COLLECT_ANIM_PAUSE);
					}
					break;
		}
		remove(pile);
	}
	
	
	/** Displays the image of the top card of the center pile. Offsets each additional card so the
	 * top 4 cards are visible.
	 */
	public void displayCenterPile(ArrayList <Card> centerPile) {
		if (centerPile.size() == 0) return;
		Card card = centerPile.get(0);
		GImage cardImg = card.rotateCard(theta);
		cardImg.setLocation(displayCanvas.getWidth() / 2 - cardImg.getWidth() / 2, displayCanvas.getHeight() / 2 - cardImg.getHeight() / 2);
		add(cardImg);
		if (theta < 135) {
			theta += 45;
		} else {
			theta = 0;
		}
	}
	
	/** Makes the "Check Pile" button visible */
	public void showCheckButton() {
		checkButton.setVisible(true);
	}
	
	/** Makes the "Check Pile" button invisible */
	public void noShowCheckButton() {
		checkButton.setVisible(false);
	}
	
	/** Changes the color of the "Collect Pile" button to indicate that the button should be pressed. */
	public void highlightCollectButton(Player player) {
		buttonMap.get(player).setBGColor(COLLECT_BUTTON_FLASH_COLOR);
	}
	
	/** Changes the color of the "Collect Pile" button to indicate that the button shouldn't be pressed. */
	public void unhighlightCollectButton(Player player) {
		buttonMap.get(player).setBGColor(COLLECT_BUTTON_COLOR);
	}
	

	public GPoint getPlayerDeckLocation(Player player) {
		return playerDeckLocations.get(player);
	}
	
	public char getPlayerSeatLocation(Player player) {
		return playerSeatLocations.get(player);
	}
	
	public int getWidth() {
		return displayCanvas.getWidth();
	}
	
	public int getHeight() {
		return displayCanvas.getHeight();
	}
	
	
	/** Angle of the image of the card placed on top of the center pile. */
	private int theta;
	
	/** Random card providing card dimensions for the display. */
	private double cardTemplateWidth;
	
	/** Canvas of the game screen. */
	private GCanvas displayCanvas;
	
	/** Links each player with their associated hand image */
	private Map <Player, GImage> handMap = new HashMap<Player, GImage> ();
	
	/** Links each player with their associated deck label */
	private Map <Player, GLabel> labelMap = new HashMap<Player, GLabel> ();
	private Map <Player, GRect> labelBGMap = new HashMap <Player, GRect> ();
	
	/** List of players in the game in order of play. */
	private ArrayList <Player> players;
	
	/** List of hands that slapped the center pile in order in which the pile was slapped */
	private ArrayList <Player> hands;
	
	/** Locations of each player's deck */
	private Map <Player, GPoint> playerDeckLocations = new HashMap<Player, GPoint> ();
	
	private Map <Player, Character> playerSeatLocations = new HashMap <Player, Character> ();
	
	/** Locations of each player's "Collect Pile" buttons */
	private Map <Player, GPoint> playerButtonLocations = new HashMap <Player, GPoint> ();
	
	/** Links each player with their associated "Collect Pile" buttons */
	private Map <Player, Button> buttonMap = new HashMap <Player, Button> ();
	
	private Map <Player, GRect> highlightMap = new HashMap <Player, GRect> ();
	
	/** "Check Pile" button */
	private Button checkButton;
	
	/** Slap Alert */
	private SlapAlert slapAlert;
	
	
	private double cardHeight;
	private double cardWidth;
	
	private GPoint centerPileLeftMidpoint;
	private GPoint centerPileRightMidpoint;
	private GPoint centerPileUpperMidpoint;
	private GPoint centerPileLowerMidpoint;
}
