
import acm.program.*;
import acm.util.*;
import java.io.*;
import java.util.*;

public class DeckOfCards {

	/* Method: init */
	/** Imports the list of strings corresponding to a deck of cards and stores it in the program. */
	public DeckOfCards(double cardHeight, String file) {
		deck = new ArrayList <Card>();
		BufferedReader rd = openFileReader(file);
		importDeck(rd, cardHeight);
	}
	
	public ArrayList <Card> getDeck() {
		return deck;
	}
	
	/* Method: getShuffledDeck */
	/** Shuffles the deck of cards
	 * @return	List of shuffled cards
	 */
	public ArrayList <Card> getShuffledDeck() { //SHUFFLE METHOD Collections.shuffle(arrlist);??? to use or no??
		int shuffle = rgen.nextInt(deck.size(), deck.size() * 3);
		for (int i = 0; i < shuffle; i++) {
			int numcard = rgen.nextInt(deck.size());
	    	Card tempcard = deck.remove(numcard);
	    	deck.add(tempcard);
		}
		return deck;
	}
		
	/* Method: openFileReader */
	/** Creates a new BufferedReaader. Based on openFileReader method in The Art and Science of Java by Eric S. Roberts.
	 * @return	New BufferedReader
	 */
    private BufferedReader openFileReader (String prompt) {
		BufferedReader rd = null;
		while (rd == null) {
			try{
    			rd = new BufferedReader (new FileReader(prompt));
	    	} catch (IOException ex) {
	    		throw new ErrorException(ex);
	    	}
		}
		return rd;
	}
    
	/* Method: importDeck */
	/** Reads the file containing the deck of cards and stores it in the program.
	 * @param	rd	A BufferedReader
	 */
    private void importDeck(BufferedReader rd, double cardHeight) {
    	try {
    		while (true) {
    			String cardStr = rd.readLine();
    			if (cardStr == null) break;
    			Card card = new Card (cardStr, cardHeight);
    			deck.add(card);
    		}
    		rd.close();
    	} catch (IOException ex) {
    		throw new ErrorException(ex);
    	}
    }
    
    public int getSize() {
    	return deck.size();
    }
    

	
	private ArrayList <Card> deck;
	private RandomGenerator rgen = RandomGenerator.getInstance();
}
