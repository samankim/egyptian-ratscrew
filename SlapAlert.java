import java.awt.*;
import acm.graphics.*;


public class SlapAlert extends GCompound{

	private static final String slapAlertStr = "SLAP ALERT";
	
	public SlapAlert(double x, double y, double width, double height, Color color) {
		slapAlertLocation = new GPoint (x, y);
		slapAlertWidth = width;
		slapAlertHeight = height;
		slapAlertColor = color;
		
		createSlapAlertBG();
		createSlapAlertLabel();
		add(slapAlertBG);
		add(slapAlertLabel);
		
	}
	
	private void createSlapAlertBG() {
		slapAlertBG = new GRect(slapAlertWidth, slapAlertHeight);
		slapAlertBG.setLocation(slapAlertLocation);
		slapAlertBG.setFilled(true);
		slapAlertBG.setFillColor(slapAlertColor);
	}
	
	private void createSlapAlertLabel() {
		slapAlertLabel = new GLabel (slapAlertStr);
		slapAlertLabel.setLocation(slapAlertLocation.getX() + slapAlertBG.getWidth() / 2 
				- slapAlertLabel.getWidth() / 2, slapAlertLocation.getY() 
				+ slapAlertBG.getHeight() / 2 + slapAlertLabel.getAscent() / 2);
	}
	
	public void setBGColor(Color color) {
		slapAlertBG.setFillColor(color);
	}
	
	
	private GPoint slapAlertLocation;
	private Color slapAlertColor;
	private GRect slapAlertBG;
	private GLabel slapAlertLabel;
	private double slapAlertWidth;
	private double slapAlertHeight;
}
