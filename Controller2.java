import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;

public class Controller2 implements ActionListener {
//    private client client;
    private GUI gui;
    private GUI2 gui2;
    private GUI3 gui3;
    private GUI4 gui4;
    private DialogBox messagebox;

    public Controller2(GUI3 gui3)
    {
        this.gui3 = gui3;
       // this.client = new Client();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) 
    {
        String action = arg0.getActionCommand();

        if (action.equals("Register Voter")) {
            String Username = gui.getText(0).getText();
            String Password = gui.getText(1).getText();
            System.out.println(Username);
            System.out.println(Password);
        }
        else if (action.equals("Register Party")) {
            String Username = gui4.getText(0).getText();
            String Password = gui4.getText(1).getText();
            String ID = gui4.getText(2).getText();
            System.out.println(Username);
            System.out.println(Password);
            System.out.println(ID);
        }
        else if (action.equals("Vote")) {

            String ID = gui2. getText(0).getText();
            String Password  = gui2.getText(1).getText();
            String Candidate = gui2.getText(2).getText();
            System.out.println(ID);
            System.out.println(Password);
            System.out.println(Candidate);
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
