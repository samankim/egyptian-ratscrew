
public class ComputerPlayer extends Player{

	public static final int EASY = 1;
	public static final int MED = 2;
	public static final int HARD = 3;
	
	public static final double EASY_SLAP_TIME = 2000;
	public static final double MED_SLAP_TIME = 1000;
	public static final double HARD_SLAP_TIME = 800;
	
	public static final double EASY_CARD_TIME = 500;
	public static final double MED_CARD_TIME = 300;
	public static final double HARD_CARD_TIME = 100;
	
	
	
	public ComputerPlayer(String name) {
		super(name);
	}
	
	public void setDifficulty(int level) {
		difficulty = level;
		switch (difficulty) {
		case EASY:	slapTime = EASY_SLAP_TIME;
					cardTime = EASY_CARD_TIME;
					break;
		case MED:	slapTime = MED_SLAP_TIME;
					cardTime = MED_CARD_TIME;
					break;
		case HARD:	slapTime = HARD_SLAP_TIME;
					cardTime = HARD_CARD_TIME;
					break;
		}
		
	}
	
	public int getDifficulty() {
		return difficulty;
	}
	
	public boolean isComputerPlayer() {
		return true;
	}
	
	public double getSlapTime() {
		return slapTime;
	}
	
	public double getCardTime() {
		return cardTime;
	}

	private int difficulty;
	private double slapTime;
	private double cardTime;
	
}
