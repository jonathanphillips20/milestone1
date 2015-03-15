import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GUI3 extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = 3244323400187410934L;

    // int array for dimensions
    private static final int [] DIMENSIONS = {300, 400};

		
    private JPanel panel;               // JPanel instance for the panel to be added to the frame
    private JButton[] button;


    public GUI3()
    {	
        super("CLIENT");
        button = new JButton[3];

        this.makeFrame();         // call makeFrame method
        this.makePanel();         // call makePanel method	

        this.setVisible(true);    // refresh the frame to display the contents
    }

    private void makeFrame()
    {
        this.setSize(DIMENSIONS[0], DIMENSIONS[1]);        // frame size is set to DIMENSIONS[0] and DIMENSIONS[1]
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);        // set the default close operation to be DISPOSE_ON_CLOSE
    }

    private void makePanel()
    {
        // panel is assigned a new JPanel with a BorderLayout
        this.panel = new JPanel(new GridLayout(3,1));

       

        button[0] = new JButton("Register As Voter");
        button[0].setActionCommand("Register As Voter");
        this.panel.add(button[0]);

        button[1] = new JButton("Vote For Candidate");
        button[1].setActionCommand("Vote For Candidate");
        this.panel.add(button[1]);

        button[2] = new JButton("EXIT");
        button[2].setActionCommand("EXIT");
        this.panel.add(button[2]);

        this.add(this.panel);        // adds panel to the frame
    }

    public void addController(Controller2 controller)
    {
        for(int i=0; i < button.length;i++)
        {
            this.button[i].addActionListener(controller);
        }
    }

    public JButton getButton(int i) {
        return button[i];
    }
}
