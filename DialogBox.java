import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class DialogBox extends JFrame{
	private static final long serialVersionUID = 3244323400187410935L;
	private static final int [] DIMENSIONS = {500, 150}; // int array for dimensions
	private String Message; 
	private JPanel panel;               // JPanel instance for the panel to be added to the frame
	private JButton[] button;
	private JLabel[] label;

	public DialogBox(String Message) {	
		super("MessageBox");
		this.Message = "      " + Message;

		label = new JLabel[1];
		button = new JButton[1];

		this.makeFrame();         // call makeFrame method
		this.makePanel();         // call makePanel method	

		this.setVisible(true);    // refresh the frame to display the contents
	}

	private void makeFrame() {	
		this.setSize(DIMENSIONS[0], DIMENSIONS[1]);        // frame size is set to DIMENSIONS[0] and DIMENSIONS[1]
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);        // set the default close operation to be DISPOSE_ON_CLOSE
	}

	private void makePanel() {
		// panel is assigned a new JPanel with a BorderLayout
		this.panel = new JPanel(new GridLayout(2,1));

		label[0] = new JLabel(Message);
		this.panel.add( label[0]);

		button[0] =new JButton("OK");
		button[0].setActionCommand("OK");
		this.panel.add( button[0] );

		this.add(this.panel);        // adds panel to the frame
	}

	public void addController(Controller controller) {
		for(int i=0; i < button.length;i++) {
			this.button[i].addActionListener(controller);
		}
	}

	public JLabel getLabel(int i) {
		return label[i];
	}

	public JButton getButton(int i) {
		return button[i];
	}
}
