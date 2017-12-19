import java.awt.*;

import acm.graphics.*;

public class Button extends GCompound{

	
	public Button(String name, double x, double y, double width, double height, Color color) {
		buttonWidth = width;
		buttonHeight = height;
		buttonName = name;
		buttonColor = color;
		buttonX = x;
		buttonY = y;
		
		createButtonBG();
		createButtonLabel();
		add(buttonBG);
		add(buttonLabel);
	}
	
	private void createButtonLabel() {
		buttonLabel = new GLabel(buttonName);
		buttonLabel.setLocation(buttonBG.getX() + buttonWidth / 2 - buttonLabel.getWidth() / 2, 
				buttonBG.getY() + buttonHeight / 2 + buttonLabel.getAscent() / 2 );
	}
	
	private void createButtonBG() {
		buttonBG = new G3DRect(buttonWidth, buttonHeight);
		buttonBG.setRaised(true);
		buttonBG.setLocation(buttonX - buttonWidth / 2, buttonY - buttonHeight / 2);
		buttonBG.setFilled(true);
		buttonBG.setFillColor(buttonColor);
		buttonBG.sendToBack();
	}
	
	public void clickButton() {
		buttonBG.setRaised(false);
	}
	
	public void releaseButton() {
		buttonBG.setRaised(true);
	}
	
	public void setBGColor(Color color) {
		buttonBG.setFillColor(color);
		buttonColor = buttonBG.getColor();
	}
	
	public boolean isRaised() {
		return buttonBG.isRaised();
	}
	
	private double buttonX;
	private double buttonY;
	private String buttonName;
	private GLabel buttonLabel;
	private G3DRect buttonBG;
	private double buttonWidth;
	private double buttonHeight;
	private Color buttonColor;
	
}
