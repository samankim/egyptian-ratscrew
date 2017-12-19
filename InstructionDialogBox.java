import java.awt.*;
import java.util.*;
import acm.graphics.*;

public class InstructionDialogBox extends GCompound{

	
	private static final int MAX_LENGTH = 50;
	private static final Color BG_COLOR = Color.WHITE;
	private static final double DIALOG_BASE_HEIGHT = 10;
	private static final double TEXT_INSET = 5;
	
	public InstructionDialogBox (String str, double x, double y) {
		instruction = str;
		dialogX = x;
		dialogY = y;
		dialogHeight = DIALOG_BASE_HEIGHT;
		parseInstruction();
		formLines();
		createLines();
		add(createBG());
		for (int i = 0; i < instructionLabels.size(); i++) {
			add(instructionLabels.get(i));
		}
	}
	

	private void parseInstruction() {
		char ch = 0;
		String result = "";
		for (int i = 0; i < instruction.length(); i++) {
			ch = instruction.charAt(i);
			if (ch != ' ') {
				result += ch;
			} else {
				instructionWords.add(result);
				result = "";
			}
		}
		instructionWords.add(result);
	}
	
	private void formLines() {
		String result = "";
		for (int i = 0; i < instructionWords.size(); i++) {
			String line = instructionWords.get(i);
			if (result.length() + line.length() > MAX_LENGTH) {
				instructionLines.add(result);
				result = line + " ";
			} else {
				result += line + " ";
			}
		}
		instructionLines.add(result);
	}
	
	private void createLines() {
		GLabel label = null;
		double x = dialogX + TEXT_INSET;
		double y = dialogY;
		for (int i = 0; i < instructionLines.size(); i++) {
			label = new GLabel(instructionLines.get(i));
			y += label.getHeight();
			label.setLocation(x, y);
			instructionLabels.add(label);
			dialogHeight += label.getHeight();
			if (label.getWidth() + TEXT_INSET >= dialogWidth + TEXT_INSET) dialogWidth = label.getWidth() + TEXT_INSET;
		}
	}
	
	private GRect createBG() {
		GRect bg = new GRect(dialogX, dialogY, dialogWidth, dialogHeight);
		bg.setFilled(true);
		bg.setFillColor(BG_COLOR);
		return bg;
	}
	
	private double dialogX;
	private double dialogY;
	
	private String instruction;
	
	
	private ArrayList <String> instructionWords = new ArrayList <String> ();
	
	private ArrayList <String> instructionLines = new ArrayList <String> ();
	
	private ArrayList <GLabel> instructionLabels = new ArrayList <GLabel> ();
	
	private double dialogWidth;
	private double dialogHeight;
}
