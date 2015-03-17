import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Controller implements ActionListener {
    private Dialog messagebox;
    private Client cl;
	private GUI gui;

    public Controller(GUI gui){
       this.gui = gui;
       this.cl = new Client(); 
	   String x = "" + cl.getCandidates();
	   this.gui.setVoters(x);
	   this.gui.addController(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        String action = arg0.getActionCommand();

        if(action.equals("Register Voter")) {
            String username = gui.getText(0,0).getText();
            String password = gui.getText(0,1).getText();
            String name = gui.getText(0,2).getText();
            String dist = gui.getText(0,3).getText();
            cl.register(Integer.parseInt(username), 
            			password.toCharArray(), 
            			name.toCharArray(), 
            			dist.toCharArray());
        } else if(action.equals("Vote")) {
            String id = gui.getText(1,0).getText();
            String password  = gui.getText(1,1).getText();
            String candidate = gui.getText(1,2).getText();
            cl.vote(Short.parseShort(candidate), 
				   Integer.parseInt(id), 
				   password.toCharArray());
        } else if(action.equals("Register As Voter")) {
			gui.setLayout(0);
		} else if(action.equals("Vote For Candidate")) {
			gui.setLayout(1);
		} else if(action.equals("GO BACK")) {
			gui.setLayout(2);
        } else if(action.equals("EXIT")) {
            gui.dispose();
        } else if(action.equals("OK")) {
            messagebox.dispose();
        }
    }
}
