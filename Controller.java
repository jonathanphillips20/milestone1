import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.ActionEvent;
import java.awt.Point;
import java.net.InetAddress;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JFrame;

public class Controller implements ActionListener, KeyListener {
	private DialogBox messagebox;
	private Client cl;
	private GUI gui;
	
	private static String REGISTERPANEL = "Register";
	private static String VOTEPANEL 	= "Vote";

	public Controller(GUI gui, InetAddress address, int port, int timeout) {
		this.gui = gui;
		this.cl = new Client(address, port, timeout);
		String x = "" + cl.getCandidates();
		if (x.trim().equals("null")) {
			this.addDialogBox("Server is not online. Please try again later.");
			this.gui.dispose();
			return;
		}
		this.gui.setVoters(x.trim());
		this.gui.addController(this);
	}

	public void addDialogBox(String s) {
		if(messagebox!=null){messagebox.dispose();}
		Point origin = gui.getLocation();
		//double x = ; if(x<0){x==0}
		messagebox = new DialogBox(s,new Point((new Double(origin.getX()+gui.getWidth()/2)).intValue(),(new Double(origin.getY()+gui.getHeight())).intValue()));
		messagebox.addController(this);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		String action = arg0.getActionCommand();

		if (action.equals("Register Voter")) {
			String username = gui.getText(0, 0).getText();
			String password = gui.getText(0, 1).getText();
			String name = gui.getText(0, 2).getText();
			String dist = gui.getText(0, 3).getText();
			byte r;
			try {
				r = cl.register(Integer.parseInt(username),
						password.toCharArray(), name.toCharArray(),
						dist.toCharArray());
			} catch (NumberFormatException e) {
				r = (byte) -4;
			}

			if (r == (byte) -4) {
				this.addDialogBox("Invalid input: ID must be comprised of solely numbers and in the and of 0 - (2^31)-1");
			} else if (r == (byte) -3) {
				this.addDialogBox("Socket Timed out");
			} else if (r == (byte) -2) {
				this.addDialogBox("Invalid input. Password, Name and District limited 16 chars");
			} else if (r == (byte) 0) {
				this.addDialogBox("ID already registered");
			} else if (r == (byte) 1) {
				this.addDialogBox("Register Successful");
			} else {
				this.addDialogBox("Unknown Error");
			}
		} else if (action.equals("Vote")) {
			String id = gui.getText(1, 0).getText();
			String password = gui.getText(1, 1).getText();
			String candidate = gui.getText(1, 2).getText();
			byte r;
			try {
				r = cl.vote(Short.parseShort(candidate), Integer.parseInt(id),
						password.toCharArray());
			} catch (NumberFormatException e) {
				r = (byte) -4;
			}
			if (r == (byte) -4) {
				this.addDialogBox("Invalid input: ID must be comprised of solely numbers and in the and of 0 - (2^31)-1");
			} else if (r == (byte) -3) {
				this.addDialogBox("Socket Timed out");
			} else if (r == (byte) -2) {
				this.addDialogBox("Invalid password length (16 max)");
			} else if (r == (byte) 0) {
				this.addDialogBox("Vote Successful");
			} else if (r == (byte) 1) {
				this.addDialogBox("Incorrect ID/Pass combo");
			} else if (r == (byte) 2) {
				this.addDialogBox("Vote Already Registered");
			} else if (r == (byte) 3) {
				this.addDialogBox("Vote Index out of bounds");
			} else if (r == (byte) 4) {
				this.addDialogBox("User not registered");
			} else {
				this.addDialogBox("Unknown Error");
			}
		} else if (action.equals("Register As Voter")) {
			gui.setLayout(0);
		} else if (action.equals("Vote For Candidate")) {
			gui.setLayout(1);
		} else if (action.equals("GO BACK")) {
			gui.setLayout(2);
		} else if (action.equals("EXIT")) {
			gui.dispose();
		} else if (action.equals("OK")) {
			messagebox.dispose();
		} else if (action.equals("View Results")) {
			gui.setLayout(3);
		}
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode()==KeyEvent.VK_ENTER){
        	Component focus = gui.getFocusOwner();
        	if(focus instanceof JTextField){
        		if(gui.getCurrPanel().equals(REGISTERPANEL)){
        			this.actionPerformed(new ActionEvent(this,1,"Register Voter"));
        		} else if(gui.getCurrPanel().equals(VOTEPANEL)){
        			this.actionPerformed(new ActionEvent(this,1,"Vote"));
        		}
        	}else if(focus instanceof JButton){
        		this.actionPerformed(new ActionEvent(this,1,((JButton) focus).getActionCommand()));
        	}
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
    }

    @Override
    public void keyTyped(KeyEvent arg0) {
    }
}
