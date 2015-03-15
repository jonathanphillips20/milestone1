import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Controller2 implements ActionListener {
//    private client client;
    private GUI gui;
    private GUI2 gui2;
    private GUI3 gui3;
    private GUI4 gui4;
    private Dialog messagebox;
    private Client cl;

    public Controller2(GUI3 gui3)
    {
       this.gui3 = gui3;
       this.cl = new Client();
	   this.gui2.getLabel(3).setText(cl.getCandidate());
    }

    @Override
    public void actionPerformed(ActionEvent arg0) 
    {
        String action = arg0.getActionCommand();

        if (action.equals("Register Voter")) {
            String username = gui.getText(0).getText();
            String password = gui.getText(1).getText();
            String name = gui.getText(2).getText();
            String dist = gui.getText(3).getText();
            cl.register(Integer.parseInt(username), 
            			password.toCharArray(), 
            			name.toCharArray(), 
            			dist.toCharArray());
            
        }
        
        else if (action.equals("Vote")) {

            String id = gui2. getText(0).getText();
            String password  = gui2.getText(1).getText();
            String candidate = gui2.getText(2).getText();
           cl.vote(Short.parseShort(candidate), 
        		   Integer.parseInt(id), 
        		   password.toCharArray());
        }
        else if (action.equals("Vote For Candidate")) {
            gui3.dispose();
            gui2 = new GUI2();
            gui2.addController(this);
        }
        else if (action.equals("GO BACK1")) {
            gui.dispose();
            gui3 = new GUI3();
            gui3.addController(this);
        }
        else if (action.equals("GO BACK2")) {
            gui2.dispose();
            gui3 = new GUI3();
            gui3.addController(this);
        }
        else if (action.equals("GO BACK4")) {
            gui4.dispose();
            gui3 = new GUI3();
            gui3.addController(this);
        }
        else if (action.equals("Register As Candidate")) {
            gui3.dispose();
            gui4 = new GUI4();
            gui4.addController(this);
        }
        else if (action.equals("Register As Voter")) {
            gui3.dispose();
            gui = new GUI();
            gui.addController(this);
        }
        else if (action.equals("EXIT")) {
            gui3.dispose();
        }

        else if (action.equals("OK")) {
            messagebox.dispose();
        }
    }
}
