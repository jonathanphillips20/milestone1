import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogBox extends JFrame{
	private static final long serialVersionUID = 3244323400187410935L;
	private static final int [] DIMENSIONS = {500, 150}; // int array for dimensions
	private String Message; 
	private JPanel panel;               // JPanel instance for the panel to be added to the frame
	private JButton button;
	private JLabel label;

	public DialogBox(String Message,Point origin) {	
		super("MessageBox");
		this.Message = "      " + Message;

		this.setSize(DIMENSIONS[0], DIMENSIONS[1]);       // frame size is set to DIMENSIONS[0] and DIMENSIONS[1]
		if(origin!=null){
			int x = (new Double(origin.getX()-250)).intValue(); if(x<0){x=0;}
			int y = (new Double(origin.getY()-150)).intValue(); if(y<0){y=0;}
			this.setLocation(new Point(x,y));
		}
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);        // set the default close operation to be DISPOSE_ON_CLOSE
		this.makePanel();         // call makePanel method	
		this.getRootPane().setDefaultButton(button);

		this.setVisible(true);    // refresh the frame to display the contents
	}
	
	public DialogBox(String Message){
		this(Message,null);
	}

	private void makePanel() {
		// panel is assigned a new JPanel with a BorderLayout
		this.panel = new JPanel(new GridLayout(2,1));

		label = new JLabel(Message);
		this.panel.add( label);

		button =new JButton("OK");
		button.setActionCommand("OK");
		this.panel.add( button );

		this.add(this.panel);        // adds panel to the frame
	}

	public void addController(Controller controller) {
		this.button.addActionListener(controller);
	}
}
