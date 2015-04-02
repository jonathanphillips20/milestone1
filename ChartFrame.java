import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;

public class ChartFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private Server3 s;
	private int[] yVals; 		// array of values for the histogram
	private String[] xLabels;	// array of strings for the label.
	private String title;		//String for the title of the histogram.
	private ChartPanel panel;	//ChartPanel to attach to frame and produce the graph.
	private JPanel mainPanel;
	private JTextField messageBox;
	private Timer t;

	public ChartFrame(int[] yAxis, String [] xAxis, String t,Server3 s){
		super(t);//Super constructor of JFrame
		this.s=s;
		this.yVals = yAxis;
		this.xLabels = xAxis;
		this.title = t;
		JButton b = new JButton("Refresh Now");
		b.addActionListener(new MyListener());

		messageBox = new JTextField();
		messageBox.setFocusable(false);

		JPanel bmPanel = new JPanel(new BorderLayout());
		bmPanel.add(b,BorderLayout.WEST);
		bmPanel.add(messageBox,BorderLayout.CENTER);

		mainPanel = new JPanel(new BorderLayout());
		panel = new ChartPanel();

		mainPanel.add(panel,BorderLayout.CENTER);
		mainPanel.add(bmPanel,BorderLayout.SOUTH);
		this.add(mainPanel);//add it to the frame

		this.setSize(800,600);//set the size of the window
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setVisible(true);
	}

	public void updateChartFields(int[] values, String[] labels) {
		this.yVals = values;
		this.xLabels = labels;
		this.updateChart();
	}

	private void updateChart(){
		ChartPanel p = new ChartPanel(); //Create a new chartPanel
		this.mainPanel.remove(this.panel); //remove the old panel
		this.panel = p; // set the panel to the newly created one
		this.mainPanel.add(this.panel,BorderLayout.CENTER); // add it to the frame
		this.setVisible(true); //return;
	}

	public void display(String s) {
		this.messageBox.setText(s);
	}

	private class MyListener implements ActionListener{
		public MyListener(){
			if(t==null) {
				t=new Timer(20000, this);
				t.start();
			}
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {s.reqUpdate();}	
	}

	public class ChartPanel extends JPanel {
		private static final long serialVersionUID = 1L;

		public void paintComponent(Graphics g){
			super.paintComponent(g);
			if(yVals == null || yVals.length == 0){
				display("No Votes Yet..."); return; //if the data set is uninitialized or empty then return.
			}

			double minVal = 0, maxVal = 0;
			for(int i = 0; i<yVals.length;i++){ //for each value in the data set.
				//F ind the maxVal and minVal of the dataset.
				if(minVal > yVals[i]){ minVal = yVals[i]; }
				if(maxVal < yVals[i]){ maxVal = yVals[i]; }
			}

			Dimension d  = getSize();
			int width    = d.width;
			int height   = d.height;
			int barWidth = width/yVals.length;

			Font tf = new Font("Serif",Font.BOLD,20);
			Font lf = new Font("Serif",Font.PLAIN,10);
			FontMetrics tfm = g.getFontMetrics(tf);
			FontMetrics lfm = g.getFontMetrics(lf);
			g.setFont(tf);
			g.setFont(lf);

			int x = ( width - tfm.stringWidth(title) )/2;
			int y = tfm.getAscent();
			g.drawString(title, x, y);

			int topMost = tfm.getHeight();
			int botMost = lfm.getHeight();

			double scale = (height-topMost - botMost)/(maxVal - minVal);
			y = height - lfm.getDescent();

			for(int i = 0; i<yVals.length;i++){
				int barX = i*barWidth +1;		//Get the x coordinate where to draw the rectangle
				int barY = topMost;				//Get the y coordinate where to dtaw the rectangle.
				int h = (int)(yVals[i]*scale);	//Get the height of the bar based on the scale and the value in the array.

				if(yVals[i]>=0){ //If the value is greater than zero
					barY +=(int) (maxVal-yVals[i])*scale;	//Add to the height of the bar
				} else {
					//else reverse the height.
					barY += (int)(maxVal*scale);
					h = -h;
				}

				g.setColor(Color.getHSBColor((float)i/ (float)yVals.length, 0.85f, 1.0f)); //Random color
				g.fillRect(barX, barY, barWidth-2, h);
				g.setColor(Color.BLACK);

				g.drawRect(barX, barY, barWidth -2, h);	//Draw the rectangle for the histogram
				int lw = lfm.stringWidth(xLabels[i]);
				x = i*barWidth+(barWidth - lw)/2;

				g.drawString(xLabels[i], x, y);
				String time = new SimpleDateFormat("yyyy-MM-dd@HH:mm:ss").format(Calendar.getInstance().getTime());
				t.restart();
				display("Updated as of: "+time+ " and will update in 20s");
			}
		}
	}
}
