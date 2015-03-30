import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Panel Class that handles drawing the graph.
 * @author Varun
 *
 */
public class ChartPanel extends JPanel {
	private int[] yVals; //data set of the votes
	private String[] xLabels; // labels for each data set
	private String title; // title of the graph.
	
	public ChartPanel(int[] yv, String[] xl, String t){
		this.yVals = yv; 
		this.xLabels = xl;
		this.title = t;
	}
	
	/**
	 * Paints the graphics to the panel
	 */
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		//if the data set is null or empty then return.
		if(yVals == null || yVals.length == 0){
			return;
			
		}
		//Variables to find the min and max values of the data sets.
		double minVal = 0;
		double maxVal = 0;
		//for each value in the data set.
		for(int i = 0; i<yVals.length;i++){
			//If the current min value is less than what is looked at
			if(minVal > yVals[i]){
				// Repace the current value with the new one
				minVal = yVals[i];
			}
			
			//if the current max value is less than the one looked at
			if(maxVal < yVals[i]){
				//replace the current value with the new one.
				maxVal = yVals[i];
			}
		}
		
		//Get the size of the panel.
		Dimension d= getSize();
		//get the width and height.
		int width = d.width;
		int height = d.height;
		//Calcuate the width of each bar for the graph
		int barWidth = width/yVals.length;
		//Declare fonts for each label.
		Font tf = new Font("Serif",Font.BOLD,20);
		FontMetrics tfm = g.getFontMetrics(tf);
		Font lf = new Font("Serif",Font.PLAIN,10);
		FontMetrics lfm = g.getFontMetrics(lf);
		
		int tw = tfm.stringWidth(title);
		//X and Y coordinate of the title.
		int x = (width-tw)/2;
		int y = tfm.getAscent();
		g.setFont(tf);
		//Draw the title of the graph onto the component.
		g.drawString(title, x, y);
		//topMost and Bot most are the constraints of where to draw the bars.
		int topMost = tfm.getHeight();
		int botMost = lfm.getHeight();
		
		if(maxVal == minVal){
			return;
		}
		//calculate the scale of the graph.
		double scale = (height-topMost - botMost)/(maxVal - minVal);
		//get the y value where to draw the bars.
		y = height - lfm.getDescent();
		g.setFont(lf);
		
		//For each value given for the graph
		for(int i = 0; i<yVals.length;i++){
			//Get the x coordinate where to draw the rectangle
			int barX = i*barWidth +1;
			//Get the y coordinate where to dtaw the rectangle.
			int barY = topMost;
			//Get the height of the bar based on the scale and the value in the array.
			int h = (int)(yVals[i]*scale);
			//If the value is greater than zero
			if(yVals[i]>=0){
				//Add to the height of the bar
				barY +=(int) (maxVal-yVals[i])*scale;
			}
			else{
				//else reverse the height.
				barY += (int)(maxVal*scale);
				h = -h;
			}
			
			Color c = Color.getHSBColor((float)i/ (float)yVals.length, 0.85f, 1.0f);
			g.setColor(c); //set the color of the bars to Green
			g.fillRect(barX, barY, barWidth-2, h);
			g.setColor(Color.BLACK);
			//Draw the rectangle for the histogram
			g.drawRect(barX, barY, barWidth -2, h);
			int lw = lfm.stringWidth(xLabels[i]);
			x = i*barWidth+(barWidth - lw)/2;
			//Draw the labels in the appropriate location.
			g.drawString(xLabels[i], x, y);
		}
		
		
		
		
	}
	
	 
	
}
