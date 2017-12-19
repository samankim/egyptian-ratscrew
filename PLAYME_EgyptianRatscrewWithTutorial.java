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
import acm.io.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;


public class PLAYME_EgyptianRatscrewWithTutorial extends GraphicsProgram {
	
	public static final int APPLICATION_WIDTH = 900;
	public static final int APPLICATION_HEIGHT = 900;
	
	public static final int P1_CODE = KeyEvent.VK_Q;
	public static final int P2_CODE = KeyEvent.VK_R;
	public static final int P3_CODE = KeyEvent.VK_U;
	public static final int P4_CODE = KeyEvent.VK_P;
	
	private static final double FLIP_PAUSE = .5; //CHANGE
	
	private static final double GRAVITY = 1;
	private static final double CARD_ANIM_PAUSE = 10;
	private static final double BOUNCE_REDUCE = .7;
	
	private static final double COMP_PAUSE = 100; //Pause between computer player slaps
	private static final double COLLECT_PAUSE = 800;
	private static final double CHECK_PAUSE = 1000;
	private static final double CLICK_PAUSE = 300;
	
	private static final double FADE_PAUSE = 30;
	private static final double ARROW_WIDTH = 100;
	private static final double ARROW_HEIGHT = 50;
	private static final double ARROW_SPACE = 30;
	
	private static final double ARROW_ANIM_PAUSE = 20;
	
	private static final double TITLE_HAND_WIDTH = 400;
	private static final double TITLE_HAND_HEIGHT = 400;
	
	private static final double TITLE_PAUSE = 1000;
	private static final double TITLE_GROW = .5;
	private static final double TITLE_GROW_PAUSE = 10;
	
	private static final double PLAY_BUTTON_WIDTH = 200;
	private static final double PLAY_BUTTON_HEIGHT = 100;
	private static final Color PLAY_BUTTON_COLOR = Color.YELLOW;
	
	private static final double YES_BUTTON_WIDTH = 200;
	private static final double YES_BUTTON_HEIGHT = 100;
	private static final Color YES_BUTTON_COLOR = Color.GREEN;
	
	private static final double NO_BUTTON_WIDTH = 200;
	private static final double NO_BUTTON_HEIGHT = 100;
	private static final Color NO_BUTTON_COLOR = Color.RED;
	

	

	/** Initializes the game */
	public void init() {
		display = new EgyptianRatscrewDisplay(getGCanvas());
		addMenu();
		addKeyListeners();
		addMouseListeners();
		shouldPlayTutorial = true;
		tutorialPromptActive = true;
	}
	
	/** Runs the game */
	public void run() {
		displayTitleScreen();
		if (! titleScreenActive) {
			titleSlap();
			display.removeAll();
		}
		while (tutorialPromptActive) {
			promptTutorial();
		}
		display.removeAll();
		if (shouldPlayTutorial) playTutorial();
		playGame();
	}
	
	public void displayTitleScreen() {
		titleScreenActive = true;
		GImage title = new GImage ("titleScreen.png");
		add(title);
		
		play = new G3DRect (getWidth() * 2 / 3, getHeight() / 5, PLAY_BUTTON_WIDTH, PLAY_BUTTON_HEIGHT);
		play.setFilled(true);
		play.setFillColor(PLAY_BUTTON_COLOR);
		play.setRaised(true);
		add(play);
		
		GLabel playLabel = new GLabel ("PLAY");
		playLabel.setFont("SansSerif-BOLD-48");
		playLabel.setLocation(play.getX() + play.getWidth() / 2 - playLabel.getWidth() / 2, 
				play.getY() + play.getHeight() / 2 + playLabel.getAscent() / 2);
		add(playLabel);
		
		while (titleScreenActive) {
			while (titleScreenActive && title.getWidth() < 1.05 * getWidth()) {
				title.setSize(title.getWidth() + TITLE_GROW, title.getHeight() + TITLE_GROW);
				title.sendToBack();
				add(title);
				pause(TITLE_GROW_PAUSE);
			}
			while (titleScreenActive && title.getWidth() > getWidth()) {
				title.setSize(title.getWidth() - TITLE_GROW, title.getHeight() - TITLE_GROW);
				title.sendToBack();
				add(title);
				pause(TITLE_GROW_PAUSE);
			}
		}
	}
	
	private void titleSlap() {
		GImage titleHand = new GImage ("hand facing up.png");
		titleHand.setLocation(getWidth() / 5, getHeight() / 3);
		titleHand.setSize(TITLE_HAND_WIDTH, TITLE_HAND_HEIGHT);
		display.add(titleHand);
		pause(TITLE_PAUSE);
		display.remove(titleHand);
	}
	
	private void promptTutorial() {
		InstructionDialogBox dialog = new InstructionDialogBox ("Would you like to see a tutorial? "
				+ "(Click anywhere on screen if button does not immediately register.)", 0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		yes = new G3DRect (YES_BUTTON_WIDTH, YES_BUTTON_HEIGHT);
		yes.setLocation(getWidth() / 4 - yes.getWidth() / 2, getHeight() / 2 + yes.getHeight() / 2);
		yes.setFilled(true);
		yes.setFillColor(YES_BUTTON_COLOR);
		yes.setRaised(true);
		add(yes);
		
		GLabel yesLabel = new GLabel ("YES");
		yesLabel.setFont("SansSerif-BOLD-48");
		yesLabel.setLocation(yes.getX() + yes.getWidth() / 2 - yesLabel.getWidth() / 2, 
				yes.getY() + yes.getHeight() / 2 + yesLabel.getAscent() / 2);
		add(yesLabel);
		
		no = new G3DRect (NO_BUTTON_WIDTH, NO_BUTTON_HEIGHT);
		no.setLocation(getWidth() * 3 / 4 - no.getWidth() / 2, getHeight() /2 + no.getHeight() / 2);
		no.setFilled(true);
		no.setFillColor(NO_BUTTON_COLOR);
		no.setRaised(true);
		add(no);
		
		GLabel noLabel = new GLabel ("NO");
		noLabel.setFont("SansSerif-BOLD-48");
		noLabel.setLocation(no.getX() + no.getWidth() / 2 - noLabel.getWidth() / 2, 
				no.getY() + no.getHeight() / 2 + noLabel.getAscent() / 2);
		add(noLabel);
		
		waitForClick();
	}
	
	
	private void playTutorial() {
		initTutorial();
		explainRules();
	}
	
	private void initTutorial() {
		setUpTutorialPlayers();
		getComputerPlayers();
		setUpTutorial1Cards();
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
	
	private void initTutorialPart2() {
		setUpTutorialPlayers();
		getComputerPlayers();
		setUpTutorial2Cards();
		ArrayList <Card> player4deck = new ArrayList <Card> ();
		Card card = new Card("?NB", display.getCardHeight());
		player4deck.add(card);
		players.get(3).setDeck(player4deck);
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
	
	
	private void setUpTutorialPlayers() {
		ComputerPlayer player1 = new ComputerPlayer("Karel");
		player1.setDifficulty(2);
		GImage img = new GImage("karel.png");
		player1.setProfilePic(img);
		
		ComputerPlayer player2 = new ComputerPlayer("Nick");
		player2.setDifficulty(2);
		GImage img2 = new GImage("nick.png");
		player2.setProfilePic(img2);
		
		ComputerPlayer player3 = new ComputerPlayer("Mehran");
		player3.setDifficulty(2);
		GImage img3 = new GImage("mehran.png");
		player3.setProfilePic(img3);
		
		ComputerPlayer player4 = new ComputerPlayer("Marc");
		player4.setDifficulty(2);
		GImage img4 = new GImage("marc.png");
		player4.setProfilePic(img4);
		
		players.add(player1);
		players.add(player2);
		players.add(player3);
		players.add(player4);
		
	}
	
	
	private void setUpTutorial1Cards() {
		DeckOfCards deck = new DeckOfCards(display.getCardHeight(), "TutorialDeck1.txt");
		deckSize = deck.getSize();
		splitDeck(deck.getDeck(), players.size()); 
		centerPile = new ArrayList <Card>();
	}
	
	private void setUpTutorial2Cards() {
		DeckOfCards deck = new DeckOfCards(display.getCardHeight(), "TutorialDeck2.txt");
		deckSize = deck.getSize();
		splitDeck(deck.getDeck(), 3); 
		centerPile = new ArrayList <Card>();
	}
	
	
	private void explainRules() {
		InstructionDialogBox dialog = null;
		
		
		dialog = new InstructionDialogBox ("The object of the game is to take possession of the "
				+ "entire deck of cards. This can be done through placing royal cards and slapping. (Keep clicking "
				+ "to continue through tutorial.)", 0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("Each player starts with the same number of cards. "
				+ "A round begins with a player placing a card on the center. ", 0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		
		waitForClick();
		
		//player places a 10
		currentPlayer = players.get(0);
		computerPlayTurn();
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("This continues on, with each player placing a card "
				+ "until a royal card is placed, or the pile can be slapped.", 0, 0);
		dialog.setLocation(display.getWidth() * 3 / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		
		waitForClick();
		
		for (int i = 0; i < 10; i ++) {
			computerPlayTurn();
		}
		
		
		waitForClick();
		display.remove(dialog);

		dialog = new InstructionDialogBox ("If a player places a royal card (Jack, Queen, King, "
				+ "or Ace), the next player now has a certain number of tries to place another royal card (1, 2, 3, "
				+ "and 4 cards, respectively).", 0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		//Player places 8, 6, 7, 5. Orig player takes pile, places 3
		
		waitForClick();
		
		for (int i = 0; i < 4; i++) {
			computerPlayTurn();
		}
		
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("If the next player does not place down another royal card within the "
				+ "allotted number of tries, the player who placed the royal card takes the entire pile. That player "
				+ "then places the first card on the center, and another round begins.", 
				0, 0);
		dialog.setLocation(display.getWidth() * 3 / 4 - dialog.getWidth() / 2, display.getHeight() / 4 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		
		collectPlayer = players.get(2);
		pause(COLLECT_PAUSE);
		display.clickPlayerButton(collectPlayer);
		pause(CLICK_PAUSE);
		display.releasePlayerButton(collectPlayer);
		collectPile();
		computerPlayTurn();
		
		for (int i = 0; i < 12; i++) {
			computerPlayTurn();
		}
		
//		Player places king
		computerPlayTurn();
		
		
		waitForClick();
		display.remove(dialog);
		
		
		dialog = new InstructionDialogBox ("If the next player does place down another royal card "
				+ "within the allotted number of tries, then the previous royal card is now null, and the round continues"
				+ " in the same way with the new royal card.", 
				0, 0);
		dialog.setLocation(display.getWidth() * 3 / 4 - dialog.getWidth() / 2, display.getHeight() * 3 / 4 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		

		// player places 2, 4, then queen
		//player places 6, 3, collect player takes pile
		waitForClick();
		for (int i = 0; i < 5; i ++) {
			computerPlayTurn();
		}
		
		collectPlayer = players.get(0);
		pause(COLLECT_PAUSE);
		display.clickPlayerButton(collectPlayer);
		pause(CLICK_PAUSE);
		display.releasePlayerButton(collectPlayer);
		collectPile();
		
		//collect player places card, round continues
		for (int i = 0; i < 7; i++) {
			computerPlayTurn();
		}
		
		waitForClick();
		display.remove(dialog);
		dialog = new InstructionDialogBox ("If the player runs out of cards before the allotted "
				+ "number of tries, the royal play passes on to the next player, who inherits the remaining number of tries.", 
				0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		
		//player places 2, 3, runs out
		//next player places 5, 10, collect player collects pile
		for (int i = 0; i < 2; i++) {
			computerPlayTurn();
		}
		incrementPlayer();
		for (int i = 0; i < 2; i++) {
			computerPlayTurn();
		}
		collectPlayer = players.get(2);
		pause(COLLECT_PAUSE);
		display.clickPlayerButton(collectPlayer);
		pause(CLICK_PAUSE);
		display.releasePlayerButton(collectPlayer);
		collectPile();
		
		
		waitForClick();
		resetScreen();
		init();
		initTutorialPart2();
		
		waitForClick();
		dialog = new InstructionDialogBox ("Slapping can be done at any time. While many slapping "
				+ "rules exist, the rules in this particular version of Egyptian Ratscrew include:", 
				0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("Double: The top two cards are the same kind.", 0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		//Player places a 5, next player places a 5 - slap
		
		waitForClick();
		computerPlayTurn();
		computerPlayTurn();
		Player player = players.get(0);
		pause(((ComputerPlayer) player).getSlapTime());
		slapPile(player);
		Iterator <Player> it = compPlayers.iterator();
		while (it.hasNext()) {
			player = (ComputerPlayer) it.next();
			pause(COMP_PAUSE);
			slapPile(player);
		}
		
		waitForClick();
		checkPile();
		collectPile();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("Marriage: The top two cards are a King and Queen, in either order.", 0, 0);
		dialog.setLocation(display.getWidth() * 3 / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		//player places a queen, next player places a king - slap
		
		waitForClick();
		computerPlayTurn();
		computerPlayTurn();
		player = players.get(0);
		pause(((ComputerPlayer) player).getSlapTime());
		slapPile(player);
		it = compPlayers.iterator();
		while (it.hasNext()) {
			player = (ComputerPlayer) it.next();
			pause(COMP_PAUSE);
			slapPile(player);
		}
		
		waitForClick();
		checkPile();
		collectPile();

		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("Sandwich: The top card and third card are the same kind.", 0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		//player places a 5, next player places a 7, next player places a 5 - slap
		
		waitForClick();
		waitForClick();
		computerPlayTurn();
		computerPlayTurn();
		computerPlayTurn();
		player = players.get(0);
		pause(((ComputerPlayer) player).getSlapTime());
		slapPile(player);
		it = compPlayers.iterator();
		while (it.hasNext()) {
			player = (ComputerPlayer) it.next();
			pause(COMP_PAUSE);
			slapPile(player);
		}
		
		waitForClick();
		display.remove(dialog);
			
		
		dialog = new InstructionDialogBox ("When any of these cases are true, the pile can be slapped, "
				+ "and the first player to slap wins the pile.", 0, 0);
		dialog.setLocation(display.getWidth() * 3 / 4 - dialog.getWidth() / 2, display.getHeight() * 3 / 4 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		display.clickCheckButton();
		pause(CLICK_PAUSE);
		display.releaseCheckButton();
		checkPile();
		pause(COLLECT_PAUSE);
		display.clickPlayerButton(collectPlayer);
		pause(CLICK_PAUSE);
		display.releasePlayerButton(collectPlayer);
		collectPile();
		
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("These rules apply during royal plays as well.", 0, 0);
		dialog.setLocation(display.getWidth() * 3 / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		//player places a queen, next player places a 5, then a queen --> slap		
		
		waitForClick();
		computerPlayTurn();
		computerPlayTurn();
		computerPlayTurn();
		player = players.get(0);
		pause(((ComputerPlayer) player).getSlapTime());
		slapPile(player);
		it = compPlayers.iterator();
		while (it.hasNext()) {
			player = (ComputerPlayer) it.next();
			pause(COMP_PAUSE);
			slapPile(player);
		}
		
		waitForClick();
		checkPile();
		collectPile();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("If a player slaps the pile when it is not slappable, that player "
				+ "must place a card at the bottom of the pile.", 0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() / 3 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);


		waitForClick();
		computerPlayTurn();
		computerPlayTurn();
		player = players.get(3);
		pause(((ComputerPlayer) player).getSlapTime());
		slapPile(player);
		placePenaltyCard(player);
		display.removeHands();
		hands.clear();
		
		waitForClick();
		collectPlayer = players.get(0);
		collectPile();
		
		
		display.remove(dialog);
		dialog = new InstructionDialogBox ("If a player has no more cards, they can still \"slap back in\" by making a valid slap.", 0, 0);
		dialog.setLocation(display.getWidth() / 4 - dialog.getWidth() / 2, display.getHeight() * 3 / 4 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		//5, 5, player 4 slaps
		
		waitForClick();
		computerPlayTurn();
		computerPlayTurn();
		player = players.get(3);
		pause(((ComputerPlayer) player).getSlapTime());
		slapPile(player);
		
		pause(CHECK_PAUSE);
		display.clickCheckButton();
		pause(CLICK_PAUSE);
		display.releaseCheckButton();
		checkPile();
		
		pause(COLLECT_PAUSE);
		display.clickPlayerButton(collectPlayer);
		pause(CLICK_PAUSE);
		display.releasePlayerButton(collectPlayer);
		collectPile();
		
		waitForClick();
		display.remove(dialog);
		
		double x = display.getSlapAlertX() + display.getSlapAlertWidth();
		double y = display.getSlapAlertY() - display.getSlapAlertHeight() / 2;
		
		dialog = new InstructionDialogBox ("The Slap Alert indicates when the pile can be slapped. It can be turned on or off at "
				+ "any point during the game.", 
				x + ARROW_WIDTH + ARROW_SPACE, y);
		dialog.sendToFront();
		display.add(dialog);
		
		GPoint pt = new GPoint(x, y);
		getBlinkingArrow(pt, ARROW_WIDTH, ARROW_HEIGHT, "left", 4);
		
		waitForClick();
		resetScreen();

	}
	
	private void resetScreen() {
		display.removeAll();
		hands.clear();
		players.clear();
		centerPile.clear();
		compPlayers.clear();
		codes.clear();
	}
	
	private void explainGameMechanics() {
		
		double x = players.get(0).getPlayerX() + ARROW_WIDTH;
		double y = players.get(0).getPlayerY();
		
		
		InstructionDialogBox dialog = null;
		dialog = new InstructionDialogBox ("The highlight indicates it's your turn to place a card.", 0, 0);
		y -= dialog.getHeight() ;
		dialog.setLocation(x, y);
		dialog.sendToFront();
		display.add(dialog);
		
		y += dialog.getHeight();
		GPoint pt = new GPoint (x, y);
		GImage arrowImg = getBlinkingArrow(pt, ARROW_WIDTH, ARROW_HEIGHT, "left", 2);
		
		
		waitForClick();
		display.remove(dialog);
		display.remove(arrowImg);
		
		x = display.getPlayerDeckLocation(players.get(0)).getX() + display.getCardWidth() / 2 - ARROW_HEIGHT / 2;
		y = display.getPlayerDeckLocation(players.get(0)).getY() + display.getCardHeight() / 2 - ARROW_WIDTH / 2;
		
		
		dialog = new InstructionDialogBox ("Click and drag the card toward the pile to flip the card.", 0, 0);
		y -= dialog.getHeight() ;
		dialog.setLocation(x, y);
		dialog.sendToFront();
		display.add(dialog);
		
		y += dialog.getHeight();
		pt = new GPoint (x, y);
		arrowImg = getBlinkingArrow(pt, ARROW_HEIGHT, ARROW_WIDTH, "up", 2);
		
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("Drag the card to the center to place the card.", 0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		double dy = -3;
		while (arrowImg.getY() > display.getHeight() / 2) {
			display.remove(arrowImg);
			arrowImg.move(0, dy);
			display.add(arrowImg);
			pause(ARROW_ANIM_PAUSE);
		}
		
		waitForClick();
		display.remove(dialog);
		display.remove(arrowImg);
		
		dialog = new InstructionDialogBox ("Press your slap key to slap the pile.", 0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("When it appears, press the Check Pile button after players have slapped to determine which"
				+ " player should get the pile.", 0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		display.remove(dialog);
		
		dialog = new InstructionDialogBox ("When highlighted, press the Collect Pile button to take the pile.", 0, 0);
		dialog.setLocation(display.getWidth() / 2 - dialog.getWidth() / 2, display.getHeight() / 2 - dialog.getHeight() / 2);
		dialog.sendToFront();
		display.add(dialog);
		
		waitForClick();
		display.remove(dialog);
		
	}
	
	
	private GImage getBlinkingArrow(GPoint pt, double width, double height, String direction, int nBlinks) {
		int opacity = 100;
		GImage arrowImg = new GImage ("arrow/arrow facing " + direction + " opacity " + opacity + ".png");
		display.add(arrowImg);
		for (int i = 0; i < nBlinks; i++) {
			while (opacity > 0) {
				display.remove(arrowImg);
				arrowImg = new GImage ("arrow/arrow facing " + direction + " opacity " + opacity + ".png");
				arrowImg.setSize(width, height);
				arrowImg.setLocation(pt);
				display.add(arrowImg);
				pause(FADE_PAUSE);
				opacity -= 5;
			}
			while (opacity < 100) {
				display.remove(arrowImg);
				arrowImg = new GImage ("arrow/arrow facing " + direction + " opacity " + opacity + ".png");
				arrowImg.setSize(width, height);
				arrowImg.setLocation(pt);
				display.add(arrowImg);
				pause(FADE_PAUSE);
				opacity += 5;
			}
		}
		return arrowImg;
	}
	
	
	
	/** Plays one game of Egyptian Ratscrew */
	private void playGame() {
 		initGame();
		if (shouldPlayTutorial) explainGameMechanics();
		
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
			
			if (isInSlap && isRoundActive && display.isCheckButtonRaised() && compPlayers.size() > 0) { //changed after submission time
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
		hasPenaltyPlayers = false;
	}
	
	private void addMenu() {
		slapAlertCheckBox = new JCheckBox("Slap Alert On");
		slapAlertCheckBox.setSelected(true);
		add(slapAlertCheckBox, NORTH);
	}

	
	
	private void setUpPlayers() {
		IODialog dialog = getDialog();
		int nPlayers = dialog.readInt("Enter number of players, including computer players:", 2, 4);
		
		boolean isComputerPlayer = dialog.readBoolean("Is Player 1 a computer player?");
		String name = dialog.readLine("Enter Player 1 name:");
		Player player1 = new Player(name);
		if (isComputerPlayer) {
			int difficulty = dialog.readInt("Set difficulty level: (1 = Easy, 2 = Medium, 3 = Hard)");
			player1 = new ComputerPlayer(name);
			((ComputerPlayer) player1).setDifficulty(difficulty);
		}
		GImage img1 = new GImage(dialog.readLine("Enter file for Player 1 profile picture. (Presets: mehran.png, nick.png, karel.png, marc.png, blank player.png)"));
		player1.setProfilePic(img1);
		players.add(player1);
		codes.add(dialog.readLine("Set slap key: (Recommended: Q)").charAt(0));
		
		isComputerPlayer = dialog.readBoolean("Is Player 2 a computer player?");
		name = dialog.readLine("Enter Player 2 name:");
		Player player2 = new Player(name);
		if (isComputerPlayer) {
			int difficulty = dialog.readInt("Set difficulty level: (1 = Easy, 2 = Medium, 3 = Hard)");
			player2 = new ComputerPlayer(name);
			((ComputerPlayer) player2).setDifficulty(difficulty);
		}
		GImage img2 = new GImage(dialog.readLine("Enter file for Player 2 profile picture. (Presets: mehran.png, nick.png, karel.png, marc.png, blank player.png)"));
		player2.setProfilePic(img2);
		players.add(player2);
		char letter = 0;
		if (nPlayers == 2) {
			letter = 'P';
		} else if (nPlayers == 3) {
			letter = 'R';
		} else if (nPlayers == 4) {
			letter = 'R';
		}
		codes.add(dialog.readLine("Set slap key: (Recommended: " + letter + ")").charAt(0));
		
		if (nPlayers > 2) {
			isComputerPlayer = dialog.readBoolean("Is Player 3 a computer player?");
			name = dialog.readLine("Enter Player 3 name:");
			Player player3 = new Player(name);
			if (isComputerPlayer) {
				int difficulty = dialog.readInt("Set difficulty level: (1 = Easy, 2 = Medium, 3 = Hard)");
				player3 = new ComputerPlayer(name);
				((ComputerPlayer) player3).setDifficulty(difficulty);
			}
			GImage img3 = new GImage(dialog.readLine("Enter file for Player 3 profile picture. (Presets: mehran.png, nick.png, karel.png, marc.png, blank player.png)"));
			player3.setProfilePic(img3);
			players.add(player3);
			if (nPlayers == 3) {
				letter = 'P';
			} else if (nPlayers == 4) {
				letter = 'U';
			}
			codes.add(dialog.readLine("Set slap key: (Recommended: " + letter + ")").charAt(0));
		}
		
		if (nPlayers > 3) {
			isComputerPlayer = dialog.readBoolean("Is Player 4 a computer player?");
			name = dialog.readLine("Enter Player 4 name:");
			Player player4 = new Player(name);
			if (isComputerPlayer) {
				int difficulty = dialog.readInt("Set difficulty level: (1 = Easy, 2 = Medium, 3 = Hard)");
				player4 = new ComputerPlayer(name);
				((ComputerPlayer) player4).setDifficulty(difficulty);
			}
			GImage img4 = new GImage(dialog.readLine("Enter file for Player 4 profile picture. (Presets: mehran.png, nick.png, karel.png, marc.png, blank.png)"));
			player4.setProfilePic(img4);
			players.add(player4);
			codes.add(dialog.readLine("Set slap key: (Recommended: P)").charAt(0));
		}
		
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
	public void keyTyped(KeyEvent e) {
		char code = e.getKeyChar();
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
		if (titleScreenActive && play.contains(new GPoint(e.getPoint()))) {
			play.setRaised(false);
			return;
		}
		
		if (tutorialPromptActive) {
			if (yes.contains(new GPoint(e.getPoint()))) {
				yes.setRaised(false);
				display.add(yes);
				return;
			} else if (no.contains(new GPoint(e.getPoint()))) {
				no.setRaised(false);
				display.add(no);
				return;
			}
		}
		
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
		
		if (titleScreenActive && play.contains(new GPoint(e.getPoint()))) {
			play.setRaised(true);
			titleScreenActive = false;
			return;
		}
		
		if (tutorialPromptActive) {
			if (yes.contains(new GPoint(e.getPoint()))) {
				yes.setRaised(true);
				display.add(yes);
				tutorialPromptActive = false;
				shouldPlayTutorial = true;
				return;
			} else if (no.contains(new GPoint(e.getPoint()))) {
				no.setRaised(true);
				display.add(no);
				tutorialPromptActive = false;
				shouldPlayTutorial = false;
				return;
			}
		}
		
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
	private ArrayList <Character> codes = new ArrayList <Character> ();	
	
	/** Links players with their key codes for slapping */
	private Map <Character, Player> codeMap = new HashMap <Character, Player> ();
	
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
	
	private G3DRect play;
	private G3DRect yes;
	private G3DRect no;
	
	private boolean titleScreenActive;
	private boolean tutorialPromptActive;
	
	private boolean shouldPlayTutorial;
	
	/** Generates a random number */
	private RandomGenerator rgen = RandomGenerator.getInstance();
}

