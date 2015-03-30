import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.Timer;

/**
 * Frame class to display Graph.
 * @author Varun
 *
 */
public class ChartFrame extends JFrame {

	private MyListener listener;
	private TestServer3 s;
	private int[] yVals; // array of values for the histogram
	private String[] xLabels;// array of strings for the label.
	private String title;//String for the title of the histogram.
	private ChartPanel panel = null;//ChartPanel to attach to frame and produce the graph.

	public ChartFrame(int[] yAxis, String [] xAxis, String t,TestServer3 s){
		super(t);//Super constructor of JFrame
		this.s=s;
		listener = new MyListener();
		yVals = yAxis;
		xLabels = xAxis;
		title = t;

		panel = new ChartPanel(yVals, xLabels, title);//Create a new graph.
		this.getContentPane().add(panel);//add it to the frame


		this.setSize(800,600);//set the size of the window
		WindowListener wndCloser = new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		};
		this.addWindowListener(wndCloser);
		this.setVisible(true);
	}

	/**
	 * Method to update the graph.
	 * @param values Y axis values for the graph
	 * @param labels labels for each data set
	 * @param title title of the graph.
	 */
	public void updateChart(int[] values, String[] labels, String title){

		ChartPanel p = new ChartPanel(values,labels,title); //Create a new chartPanel
		this.getContentPane().remove(this.panel); //remove the old panel
		this.panel = p; // set the panel to the newly created one
		this.getContentPane().add(this.panel); // add it to the frame
		this.setVisible(true); //return;
	}

	private class MyListener implements ActionListener{
		public MyListener(){
			(new Timer(2000, this)).start();
		}
		@Override
		public void actionPerformed(ActionEvent arg0) {
			s.main();
		}	
	}
}
