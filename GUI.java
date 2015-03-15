import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GUI extends JFrame
{
    private static final long serialVersionUID = 3244323400187410934L;
    private static final int [] DIMENSIONS = {300, 400}; // int array for dimensions

    private JPanel panel;               // JPanel instance for the panel to be added to the frame
    private JButton[] button;
    private JLabel[] label;
    private JTextField[] Text;

    public GUI()
    {   
        super("Registrations For Voter");
        button = new JButton[2];
        label = new JLabel[2];
        Text = new JTextField[2];

        this.makeFrame();         // call makeFrame method
        this.makePanel();         // call makePanel method  
        // refresh the frame to display the contents
        this.setVisible(true);   
    }

    private void makeFrame()
    {
        this.setSize(DIMENSIONS[0], DIMENSIONS[1]);        // frame size is set to DIMENSIONS[0] and DIMENSIONS[1]
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);        // set the default close operation to be DISPOSE_ON_CLOSE
    }

    private void makePanel()
    {
        // panel is assigned a new JPanel with a BorderLayout
        this.panel = new JPanel(new GridLayout(4,2));

        label[0] = new JLabel("Username:");  this.panel.add(label[0]);
         Text[0] = new JTextField();         this.panel.add( Text[0]);
        label[1] = new JLabel("Password:");  this.panel.add(label[1]);
         Text[1] = new JTextField();         this.panel.add( Text[1]);

        button[0] =new JButton("Register");
        button[0].setActionCommand("Register Voter");
        this.panel.add( button[0] );

        button[1] =new JButton("GO BACK");
        button[1].setActionCommand("GO BACK1");
        this.panel.add( button[1] );

        this.add(this.panel); // adds panel to the frame
    }

    public void addController(Controller2 controller)
    {
        for(int i=0; i < button.length;i++)
        {
            this.button[i].addActionListener(controller);
        }
    }

    public JLabel getLabel(int i) {
        return label[i];
    }

    public JTextField getText(int i) {
        return Text[i];
    }

    public JButton getButton(int i) {
        return button[i];
    }
}