/*
 * File: EgyptianRatscrew.java
 * ---------------------
 * This class is a blank one that you can change at will. Remember, if you change
 * the class name, you'll need to change the filename so that it matches.
 * Then you can extend GraphicsProgram, ConsoleProgram, or DialogProgram as you like.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;


public class EgyptianRatscrew extends GraphicsProgram {
	
	public static final int APPLICATION_WIDTH = 900;
	public static final int APPLICATION_HEIGHT = 900;
	
	public static final int P1_CODE = KeyEvent.VK_Q;
	public static final int P2_CODE = KeyEvent.VK_R;
	public static final int P3_CODE = KeyEvent.VK_U;
	public static final int P4_CODE = KeyEvent.VK_P;
	
	private static final double FLIP_PAUSE = 0; //CHANGE
	
	private static final double GRAVITY = 1;
	private static final double CARD_ANIM_PAUSE = 10;
	private static final double BOUNCE_REDUCE = .7;
	
	private static final double COMP_PAUSE = 00; //Pause between computer player slaps
	private static final double COLLECT_PAUSE = 00;
	private static final double CHECK_PAUSE = 000;
	private static final double CLICK_PAUSE = 0;
	
	

	/** Initializes the game */
	public void init() {
		display = new EgyptianRatscrewDisplay(getGCanvas());
		addKeyListeners();
		addMouseListeners();
		addActionListeners();
	}
	
	/** Runs the game */
	public void run() {
		playGame();
	}
	
	
	/** Plays one game of Egyptian Ratscrew */
	private void playGame() {
		initGame();
		
		while (true) {
			
			if(isPlayerWinning()) {
				animateCards();
				break;
			}
			
			if (slapAlertCheckBox.isSelected()) {
				display.showSlapAlert();
			} else {
				display.noShowSlapAlert();
			}
			
			while (currentPlayer.getDeckSize() == 0) incrementPlayer();
			
			if (isInSlap && display.isCheckButtonRaised()) {
				display.turnOnSlapAlert();
			} else if (!isInSlap){
				display.turnOffSlapAlert(); 
			}
			
			if (isInSlap && isRoundActive && display.isCheckButtonRaised()) {
				computerSlap();
				if (currentPlayer.getDeckSize() != 0) {
					Card card = currentPlayer.getDeck().get(0);
					if (obj != null && (obj == card.getCardBackImg() || obj == card.getCardFrontImg())) resetCard(card);
					display.updatePlayerDecks();
				}
			}
			
			
			if (hands.size() == 0 && isRoundActive && currentPlayer.isComputerPlayer()) {
				Card card = currentPlayer.getDeck().get(0);
				resetCard(card);
				pause(((ComputerPlayer) currentPlayer).getCardTime());
				computerPlayTurn();
			}

			if (obj != null && checkCardLocation()) {
				while (currentPlayer.getDeckSize() == 0) incrementPlayer();
				Card card = currentPlayer.getDeck().get(0);
				resetCard(card);
				playTurn();
			}
			
			
			if (! hasCheckButtonBeenPressed && isInSlap && hands.size() != 0 && hands.get(0).isComputerPlayer()) {
				pause(CHECK_PAUSE);
				display.clickCheckButton();
				pause(CLICK_PAUSE);
				display.releaseCheckButton();
				hasCheckButtonBeenPressed = true;
			}
			
			if (hasCheckButtonBeenPressed && hands.size() != 0) {
				if (hands.get(0).isComputerPlayer()) pause(COLLECT_PAUSE); //only releases after pile is checked??? the loop is acting funny
				checkPile();
				hasCheckButtonBeenPressed = false;
			}
			
			
			if (collectPlayer != null && collectPlayer.isComputerPlayer() && hands.size() == 0 && ! hasCollectButtonBeenPressed) {
				pause(COLLECT_PAUSE);
				display.clickPlayerButton(collectPlayer);
				pause(CLICK_PAUSE);
				display.releasePlayerButton(collectPlayer);
				hasCollectButtonBeenPressed = true;
			}	
			
			if (hasCollectButtonBeenPressed) {
				if (collectPile()) {
					Iterator <Player> it = players.iterator();
					while(it.hasNext()) {
						display.getPlayerButton(it.next()).releaseButton();
					}
					hasCollectButtonBeenPressed = false;
				}
			}
			
			
			if (hasPenaltyPlayers) {
				for (int i = 0; i < hands.size(); i++) {
					Player penaltyPlayer = hands.get(i);
					if (penaltyPlayer.getDeckSize() != 0) placePenaltyCard(penaltyPlayer);
					hands.remove(i);
				}
				display.removeHands();
				hands.clear();
				hasPenaltyPlayers = false;
			}
				
			
		}

	}
	
	private boolean checkCardLocation() {
		return (display.getPlayerSeatLocation(currentPlayer) == 'b' && obj.contains(display.getMidpoint("upper"))
				|| display.getPlayerSeatLocation(currentPlayer) == 'r' && obj.contains(display.getMidpoint("left"))
				|| display.getPlayerSeatLocation(currentPlayer) == 't' && obj.contains(display.getMidpoint("lower"))
				|| display.getPlayerSeatLocation(currentPlayer) == 'l' && obj.contains(display.getMidpoint("right")));
	}
	
	/** Resets the images of the card after it has been flipped. */
	private void resetCard(Card card) {
		card.resetCardImgDimensions();
		if (obj == null || obj == display.getCheckButton()) return;
		Iterator <Player> it = players.iterator();
			while (it.hasNext()) {
				Player player = it.next();
				if (obj == display.getPlayerButton(player)) return;
			}
		remove(obj);
		obj = null;
	}
	
	/** Makes computer players slap the pile if there is a valid slap */
	private void computerSlap() {
		if (isInSlap) {
			ComputerPlayer player = (ComputerPlayer) compPlayers.get(rgen.nextInt(0, compPlayers.size() -1));
			pause(player.getSlapTime());
			slapPile(player); //get the max difficulty player
			Iterator <Player> it = compPlayers.iterator();
			while (it.hasNext()) {
				player = (ComputerPlayer) it.next();
				pause(COMP_PAUSE);
				if (! waitForPlayerToCollect && isInSlap) {
					slapPile(player);
				} else {
					return;
				}
			}
		}
	}

	/** Produces list of all the computer players in the game */
	private void getComputerPlayers() {
		Iterator <Player> playerIt = players.iterator();
		compPlayers = new ArrayList <Player>();
		while (playerIt.hasNext()) {
			Player player = playerIt.next();
			if (player.isComputerPlayer()) {
				compPlayers.add(player);
			}
		}
	}

	/** Automatically places a card down for the computer player. */
	private void computerPlayTurn() {
		isComputerPlayerTurn = true;
		pause(((ComputerPlayer) currentPlayer).getCardTime());
		if (hands.size() == 0) {
			Card card = currentPlayer.getDeck().get(0);
			animateCardFlip(currentPlayer, card);
			resetCard(card);
			if (hands.size() == 0) {
				playTurn();
			} else {
				return;
			}
			isComputerPlayerTurn = false;
		}
	}
	
	/** Automatically flips card and places it on the pile */
	private void animateCardFlip(Player player, Card card) {
		double dy = 0;
		double dx = 0;
		obj = card.getCardBackImg();
		switch(display.getPlayerSeatLocation(player)) {
		case 'b':	dy = -1;
					while (! obj.contains(display.getMidpoint("upper"))) {
						flipCardUpOverXAxis(card, dx, dy);
						dy -= .001;
						if (hasPenaltyPlayers && obj.contains(display.getMidpoint("lower"))) obj.setVisible(false);
						pause(FLIP_PAUSE);
					}
					break;
		case 'r':	dx = -1;
					while (! obj.contains(display.getMidpoint("left"))) {
						flipCardLeftOverYAxis(card, dx, dy);
						dx -= .001;
						if (hasPenaltyPlayers && obj.contains(display.getMidpoint("right"))) obj.setVisible(false);
						pause(FLIP_PAUSE);
					}
					break;
		case 't':	dy = 1;
					while (! obj.contains(display.getMidpoint("lower"))) {
						flipCardDownOverXAxis(card, dx, dy);
						dy += .001;
						if (hasPenaltyPlayers && obj.contains(display.getMidpoint("upper"))) obj.setVisible(false);
						pause(FLIP_PAUSE);
					}
					break;
		case 'l':	dx = 1;
					while (! obj.contains(display.getMidpoint("right"))) {
						flipCardRightOverYAxis(card, dx, dy);
						dx += .001;
						if (hasPenaltyPlayers && obj.contains(display.getMidpoint("left"))) obj.setVisible(false);
						pause(FLIP_PAUSE);
					}
					break;
			
		}
		resetCard(card);
	}
	
	
	/** Splits the deck evenly among the players. Sets up the necessary starting values. 
	 * Displays the player profile pictures, player names, and deck images.
	 */
	private void initGame() {
		addMenu();
		setUpPlayers();
		getComputerPlayers();
		setUpCards();
		hands = new ArrayList <Player> ();
		display.init(players, players.get(0).getCard(0));
		isRoundActive = true;
		isRoyalActive = false;
		currentPlayer = players.get(0);
		display.highlightPlayer(currentPlayer);
		collectPlayer = null;
		isInSlap = false;
		numTries = 0;
		maxTries = 0;
		hasCollectButtonBeenPressed = false;
		hasCheckButtonBeenPressed = false;
	}
	
	private void addMenu() {
		slapAlertCheckBox = new JCheckBox("Slap Alert On");
		slapAlertCheckBox.setSelected(true);
		add(slapAlertCheckBox, NORTH);
	}

//Eliminate when game suite set up
	private void setUpPlayers() {
		ComputerPlayer player1 = new ComputerPlayer("You");
		player1.setDifficulty(3);
		GImage img1 = new GImage("blank player.png");
		player1.setProfilePic(img1);
		
		ComputerPlayer player2 = new ComputerPlayer("Comp2");
		player2.setDifficulty(3);
		GImage img2 = new GImage("blank player.png");
		player2.setProfilePic(img2);
		
		ComputerPlayer player3 = new ComputerPlayer("Comp3");
		player3.setDifficulty(3);
		GImage img3 = new GImage("blank player.png");
		player3.setProfilePic(img3);
		
		ComputerPlayer player4 = new ComputerPlayer("Comp4");
		player4.setDifficulty(3);
		GImage img4 = new GImage("blank player.png");
		player4.setProfilePic(img4);
		
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		
		codes.add(P1_CODE);
		codes.add(P2_CODE);
		codes.add(P3_CODE);
		codes.add(P4_CODE);
		
		
		for (int i = 0; i < players.size(); i++) {
			codeMap.put(codes.get(i), players.get(i));
		}
	}
	
	/** Imports a new shuffled deck of cards and splits it among the players */
	private void setUpCards() {
		DeckOfCards deck = new DeckOfCards(display.getCardHeight(), "DeckOfCards.txt");
		deckSize = deck.getSize();
		splitDeck(deck.getShuffledDeck(), players.size()); 
		centerPile = new ArrayList <Card>();
	}
	
	/** Splits the deck evenly among the players */
	private void splitDeck(ArrayList <Card> gameDeck, int nPlayers) {
		int splitIndex = gameDeck.size() / nPlayers; //Finds index to divide the sections evenly
		for (int i = 0; i < nPlayers; i++) {
			currentPlayer = players.get(i);
			currentPlayer.setDeck(getDeckSection(splitIndex, gameDeck)); 
		}
	}
	
	/** Takes out a section of the shuffled deck
	 * 
	 * @param splitIndex	Index at which the deck is divided
	 * @param gameDeck	Shuffled deck of cards
	 * @return Divided section of the deck
	 */
	private ArrayList <Card> getDeckSection(int splitIndex, ArrayList <Card> gameDeck) {
		ArrayList <Card> playerDeck = new ArrayList <Card> ();
		for (int i = 0; i < splitIndex; i++) {
			playerDeck.add(gameDeck.remove(0));
		}
		return playerDeck;
	}
	

	
	/** Determines if there is only one player that has cards.
	 * 
	 * @return True if only one player has cards, otherwise false.
	 */
	public boolean isPlayerWinning() {
		Iterator <Player> it = players.iterator();
		int numLosingPlayers = 0;
		while(it.hasNext()) {
			Player currentPlayer = it.next();
			if (currentPlayer.getDeckSize() == 0) {
				display.unhighlightPlayer(currentPlayer);
				numLosingPlayers ++;
			} else if (currentPlayer.getDeckSize() == deckSize) {
				winningPlayer = currentPlayer;
				return true;
			}
		}
		if (numLosingPlayers == (players.size() - 1)) {
			it = players.iterator();
			while(it.hasNext()) {
				Player currentPlayer = it.next();
				if (currentPlayer.getDeckSize() != 0) {
					winningPlayer = currentPlayer;
				}
			}
			return true;
		} else {
			return false;
		}
	}	

	
	/** Slap button pressed */
	public void keyPressed (KeyEvent e) {
		int code = e.getKeyCode();
		Player slapPlayer = codeMap.get(code);
		slapPile(slapPlayer);
	}
	
	
	/** Gives the center pile to the player who deserves it */
	private boolean collectPile() {
		if(! isCollectPlayerFromSlap && isValidSlap()) {
			isInSlap = true;
			if (compPlayers.size() != 0) computerSlap();
			return false;
		}
		isInSlap = false;
		display.noShowCheckButton();
		display.unhighlightCollectButton(collectPlayer);
		display.unhighlightPlayer(currentPlayer);
		currentPlayer = collectPlayer;
		display.highlightPlayer(currentPlayer);
		resetCenterPile(collectPlayer);
		display.updateAllDecks(centerPile);
		collectPlayer = null;
		isRoundActive = true;
		waitForPlayerToCollect = false;
		return true;
	}
	
	/** Allows user to flip over and drag the cards to the center pile. Partially based on DragObjects program in Handout #22 */
	public void mousePressed (MouseEvent e) {
		last = new GPoint(e.getPoint());
		
		if (! isComputerPlayerTurn) {
			obj = getElementAt(last);
		}
		
		/** Check Pile button pressed */
		if (obj == display.getCheckButton() && isRoundActive && hands.size() > 1 && ! hands.get(0).isComputerPlayer()) {
			display.clickCheckButton();
			hasCheckButtonBeenPressed = true;
			return;
		}
		
		/** Collect Pile button pressed */
		
		if (collectPlayer != null && ! collectPlayer.isComputerPlayer() && obj == display.getPlayerButton(collectPlayer) 
				&& ! isRoundActive && hands.size() == 0 && ! isComputerPlayerTurn) {
			display.clickPlayerButton(collectPlayer);
			hasCollectButtonBeenPressed = true;
			return;
		}
	}
	
	
	public void mouseReleased (MouseEvent e) {
		if (collectPlayer != null && obj == display.getPlayerButton(collectPlayer) && ! display.getPlayerButton(collectPlayer).isRaised()) {
			display.getPlayerButton(collectPlayer).releaseButton();
			return;
		}
		
		if (obj == display.getCheckButton() && ! display.isCheckButtonRaised()) {
			display.releaseCheckButton();
			return;
		}
	}
	
	public void mouseDragged (MouseEvent e) {
		if ((obj == currentPlayer.getDeck().get(0).getCardBackImg() || obj == currentPlayer.getDeck().get(0).getCardFrontImg()) && isRoundActive && hands.size() == 0 && ! isComputerPlayerTurn) {
			double dy = e.getY() - last.getY();
			double dx = e.getX() - last.getX();
			Card card = currentPlayer.getDeck().get(0);
			switch(display.getPlayerSeatLocation(currentPlayer)) {
			case 'b':	flipCardUpOverXAxis(card, dx, dy);
						break;
			case 'r':	flipCardLeftOverYAxis(card, dx, dy);
						break;
			case 't':	flipCardDownOverXAxis(card, dx, dy);
						break;
			case 'l':	flipCardRightOverYAxis(card, dx, dy);
						break;
			}
			last = new GPoint(e.getPoint());
		}

	}
	

	private void flipCardUpOverXAxis (Card card, double dx, double dy) {
		if (obj == card.getCardBackImg()) {
			if (obj.getHeight() > 0) {
				((GImage) obj).setSize(obj.getWidth(), obj.getHeight() + dy);
				if (Math.abs(obj.getHeight()) > card.getOrigHeight()) ((GImage) obj).setSize(obj.getWidth(), card.getOrigHeight());
			} else {
				double x = obj.getX();
				double y = obj.getY();
				card.setCardFrontHeight(1);
				remove(obj);
				obj = card.getCardFrontImg();
				add(obj, x, y);
			}
		}

		if (obj == card.getCardFrontImg()) {
			if (obj.getHeight() < card.getOrigHeight()) {
				((GImage) obj).setSize(obj.getWidth(), obj.getHeight() - dy);
				obj.move(0, dy);
			} else {
				obj.move(dx, dy);
			}
		}
	}
	
	private void flipCardDownOverXAxis (Card card, double dx, double dy) {
		if (obj == card.getCardBackImg()) {
			if (obj.getHeight() > 0) {
				((GImage) obj).setSize(obj.getWidth(), obj.getHeight() - dy);
				obj.move(0, dy);
			} else {
				double x = obj.getX();
				double y = obj.getY();
				card.setCardFrontHeight(1);
				remove(obj);
				obj = card.getCardFrontImg();
				add(obj, x, y);
			}
		}

		if (obj == card.getCardFrontImg()) {
			if (obj.getHeight() < card.getOrigHeight()) {
				((GImage) obj).setSize(obj.getWidth(), obj.getHeight() + dy);
			} else {
				obj.move(dx, dy);
			}
		}
	}
	
	private void flipCardLeftOverYAxis (Card card, double dx, double dy) {
		if (obj == card.getCardBackImg()) {
			if (obj.getWidth() > 0) {
				((GImage) obj).setSize(obj.getWidth() + dx, obj.getHeight());
			} else {
				double x = obj.getX();
				double y = obj.getY();
				card.setCardFrontWidth(1);
				remove(obj);
				obj = card.getCardFrontImg();
				add(obj, x, y);
			}
		}

		if (obj == card.getCardFrontImg()) {
			if (obj.getWidth() < card.getOrigWidth()) {
				((GImage) obj).setSize(obj.getWidth() - dx, obj.getHeight());
				obj.move(dx, 0);
			} else {
				obj.move(dx, dy);
			}
		}
	}
	
	private void flipCardRightOverYAxis (Card card, double dx, double dy) {
		if (obj == card.getCardBackImg()) {
			if (obj.getWidth() > 0) {
				((GImage) obj).setSize(obj.getWidth() - dx, obj.getHeight());
				obj.move(dx, 0);
			} else {
				double x = obj.getX();
				double y = obj.getY();
				card.setCardFrontWidth(1);
				remove(obj);
				obj = card.getCardFrontImg();
				add(obj, x, y);
			}
		}

		if (obj == card.getCardFrontImg()) {
			if (obj.getWidth() < card.getOrigWidth()) {
				((GImage) obj).setSize(obj.getWidth() + dx, obj.getHeight());
			} else {
				obj.move(dx, dy);
			}
		}
	}
	

	
	/** Bounces the cards on the screen once a player has won in a style similar to Solitaire. Based partially on Bouncing Ball program
	 * from Handout 21.
	 */
	private void animateCards() {
		while (winningPlayer.getDeckSize() != 0) {
			Card card = winningPlayer.removeCard(0);
			resetCard(card);
			bounceCard(card);
		}
		
	}
	

	/** Animates one card such that it bounces off the ground and prints images of its path as it travels. */
	private void bounceCard(Card card) {
		GImage img = card.getCardFrontImg();
		img.setLocation(display.getPlayerDeckLocation(winningPlayer));
		add(img);
		setVelocity();
		while (img.getX() + img.getWidth() >= 0 && img.getX() <= getWidth() && img.getY() >= 0) {
			img.move(vx, vy);
			if (img.getY() + img.getHeight() >= getHeight()) {
				vy = -vy * BOUNCE_REDUCE;
				double diff = img.getY() - (getHeight() - img.getHeight());
				img.move(0, -2 * diff);
			}
			GImage copy = new GImage(img.getImage());
			copy.setSize(img.getWidth(), img.getHeight());
			add(copy, img.getX(), img.getY());
			vy += GRAVITY;
			pause(CARD_ANIM_PAUSE);
		}
		remove(img);

	}
	
	/** Sets the starting velocity of the bouncing cards */
	private void setVelocity() {
		vy = 0;
		vx = 0;
		vx = rgen.nextDouble(1.0, 5.0);
		if (rgen.nextBoolean(0.5)) vx = -vx;
	}
	

	

	/** Places card and checks its properties */
	private void playTurn() {
		placeCard();
		if (isRoyalCard(getTopCard())) {
			setRoyal();
			setNextPlayer();
		} else if (! isRoyalActive) {
			setNextPlayer();
		} else {
			playRoyal();
		}
	}
	
	/** Finds the next player to place a card */
	private void setNextPlayer() {
		display.unhighlightPlayer(currentPlayer);
		incrementPlayer();
		while (currentPlayer.getDeckSize() == 0) { //Keeps picking a new player until finds a player with cards
			incrementPlayer();	
		}
		display.highlightPlayer(currentPlayer);
	}
	

	/** Sets the current player to be the next player in line the game
	 * 
	 * @param initcount Index of the current player
	 * @return Next player
	 */
	private void incrementPlayer() {
		Player nextPlayer = null;
		if (players.indexOf(currentPlayer) + 1 < players.size()) { 
			nextPlayer = players.get(players.indexOf(currentPlayer) + 1);
		} else { //If the current player is the last player, set the next player to be Player 1
			nextPlayer = players.get(0);
		}
		currentPlayer = nextPlayer;
	}
	
	/** Gets the card at the top of the center pile
	 * 
	 * @return Card at the top of the center pile
	 */
	private Card getTopCard() {
		return centerPile.get(0);
	}
	
	/** Determines if the top card is royal
	 * 
	 * @param card Card at the top of the pile
	 * @return True if the card is a royal, otherwise, false.
	 */
	private boolean isRoyalCard(Card card) {
		return card.getKind().equals("J") || card.getKind().equals("Q") || card.getKind().equals("K") || card.getKind().equals("A"); 
	}
	
	/** Sets the criteria for other players to continue the round. If the number of cards placed by 
	 * the other players reaches the maximum number of tries for the type of royal, the center pile
	 * goes to the player who put down the royal.
	 */
	private void setRoyal() {
		numTries = 0;
		maxTries = 0;
		isRoyalActive = true;
		currentRoyalCard = getTopCard();
		currentRoyalCardPlayer = currentPlayer;
		String royalType = currentRoyalCard.getKind();
		switch(royalType) {
			case "J": maxTries = 1; 
					break;
			case "Q": maxTries = 2;
					break;
			case "K": maxTries = 3;
					break;
			case "A": maxTries = 4;
					break;
		}
		
	}
	
	/** Method called only if the card placed by the player is not a royal. If the number of cards
	 * placed is less than the maximum number of tries, increment the number of tries. If the
	 * maximum number of tries is reached, end the round, give the center pile to the player
	 * who placed the royal and set the current player to be the player who placed the royal.
	 */
	private void playRoyal() {
		numTries ++;
		if (numTries < maxTries) {
			return;
		} else {
			if (isValidSlap() && hands.size() != 0) return;
			isRoundActive = false;
			isRoyalActive = false;
			numTries = 0;
			maxTries = 0;
			collectPlayer = currentRoyalCardPlayer;
			isCollectPlayerFromSlap = false;
			display.highlightCollectButton(collectPlayer);
			display.unhighlightPlayer(currentPlayer);
			currentPlayer = currentRoyalCardPlayer;
			currentRoyalCardPlayer = null;
			display.unhighlightPlayer(currentPlayer);
		}
	}
	
	/** Puts a card on the center pile */
	private void placeCard() {
		if (isPlayerWinning()) return;
		if (centerPile.size() == 0) {
			centerPile.add(currentPlayer.removeCard(0));
			display.updateAllDecks(centerPile);
		} else {
			centerPile.add(0, currentPlayer.removeCard(0));
			display.updateAllDecks(centerPile);
		}
		
		if (centerPile.size() > 1) {
			isInSlap = isValidSlap();
		} else {
			isInSlap = false;
		}
	}
	
	/** Adds the hand of the player who slapped to the top of the pile */
	public void slapPile(Player slapPlayer) {
		if (isInSlap) {
			if (collectPlayer != null) display.unhighlightCollectButton(collectPlayer);
			isRoundActive = true;
		}
		if (isInSlap && hands.indexOf(slapPlayer) == -1) {
			Iterator <Player> it = players.iterator();
			while (it.hasNext()) {
				display.unhighlightPlayer(it.next());
			}
			hands.add(slapPlayer);
			display.displayHands(hands);
			display.showCheckButton();
		} else if (!isValidSlap() && centerPile.size() > 0){
			if (slapPlayer.getDeckSize() != 0) {
				hands.add(slapPlayer);
				display.displayHands(hands);
				hasPenaltyPlayers = true;
			}
		}
	}
	
	/** Triggers placement of a penalty card whenever the pile is incorrectly slapped */
	private void placePenaltyCard(Player player) {
		Card card = player.getCard(0);
		card.getCardBackImg().sendToBack();
		animateCardFlip(player, card);
		resetCard(card);
		centerPile.add(player.removeCard(0));
		display.updatePlayerDecks();
	}
	
	/** Determines who among the players who slapped the pile should get the center pile */
	public void checkPile() {
		display.noShowCheckButton();
		display.unhighlightPlayer(currentPlayer);
		Player slapPlayer = hands.get(0);
		display.removeHands();
		isRoundActive = false;
		isRoyalActive = false;
		if (! slapPlayer.isComputerPlayer()) waitForPlayerToCollect = true;
		collectPlayer = slapPlayer;
		isCollectPlayerFromSlap = true;
		currentPlayer = slapPlayer;
		display.highlightCollectButton(collectPlayer);
		hands.clear();
		display.highlightPlayer(currentPlayer);
		isInSlap = false;
	}
	
	/** Clears the center pile and gives the pile to the player who deserves it.
	 * 
	 * @param player Player who deserves the center pile
	 */
	private void resetCenterPile(Player player) {
		Iterator <Card> it = centerPile.iterator();
		while (it.hasNext()) {
			Card card = it.next();
			card.resetCardImgDimensions();
		}
		player.addPile(centerPile);
		centerPile.clear();
		display.clearCenterPile(player);
		display.updatePlayerDecks();
	}
	
	/** Determines if the center pile can be slapped.
	 * 
	 * @return True if the center pile can be slapped, otherwise false.
	 */
	private boolean isValidSlap() {
		if (centerPile.size() > 1) {
			Card topCard = getTopCard();
			Card card2 = centerPile.get(1);
			
			/* Double */
			if (topCard.getKind().equals(card2.getKind())) return isInSlap = true;
			
			/* Marriage */
			if (topCard.getKind().equals("K") && card2.getKind().equals("Q") || topCard.getKind().equals("Q") && card2.getKind().equals("K")) return isInSlap = true;
			
			if (centerPile.size() > 2) {
			Card card3 = centerPile.get(2);
			
			/* Sandwich */
			if (topCard.getKind().equals(card3.getKind())) return isInSlap = true;
			}
			return false;
		} else {
		return false;
		}
	}
	
	
	
	private EgyptianRatscrewDisplay display;
	
	/** Gameplay conditionals */
	private boolean isRoyalActive;
	public boolean isRoundActive;
	private boolean isInSlap;
	private boolean isComputerPlayerTurn;
	private boolean isCollectPlayerFromSlap;
	private boolean hasPenaltyPlayers;
	private boolean waitForPlayerToCollect;
	
	/** Center pile of cards */
	private ArrayList <Card> centerPile;
	
	/** Hands that have slapped the pile */
	private ArrayList <Player> hands; 
	
	/** Players in the game */
	private ArrayList <Player> players = new ArrayList <Player> ();
	
	/** Computer players in the game */
	private ArrayList <Player> compPlayers;
	
	/** List of key codes for slapping*/
	private ArrayList <Integer> codes = new ArrayList <Integer> ();	
	
	/** Links players with their key codes for slapping */
	private Map <Integer, Player> codeMap = new HashMap <Integer, Player> ();
	
	/** Current royal card in play */
	private Card currentRoyalCard;
	
	/** Number of cards placed after a royal */
	int numTries;
	
	/** Maximum number of cards that can be placed for the royal placed */
	int maxTries;
	
	
	private Player currentPlayer;
	
	/** Player taking the center pile */
	private Player collectPlayer;
	
	/** Player who placed the active royal card */
	private Player currentRoyalCardPlayer;
	
	private Player winningPlayer;
	
	/** Size of the starting deck */
	private int deckSize;
	
	/** Last place the mouse was located when the mouse is pressed/dragged */
	private GPoint last;
	
	/** Object being clicked/dragged */
	private GObject obj;
	
	
	/** Velocity of the bouncing cards */
	private double vx;
	private double vy;
	
	private boolean hasCollectButtonBeenPressed;
	private boolean hasCheckButtonBeenPressed;
	
	private JCheckBox slapAlertCheckBox;
	
	
	/** Generates a random number */
	private RandomGenerator rgen = RandomGenerator.getInstance();
}

