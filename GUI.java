import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GUI extends JFrame {
	private static final long serialVersionUID = 3244323400187410934L;
	private static final int [] DIMENSIONS = {300, 400}; // int array for dimensions
	private static String REGISTERPANEL = "Register";
	private static String VOTEPANEL 	= "Vote";
	private static String STARTPANEL 	= "Main";

	private JPanel cards;
	private JPanel[] jpanels 	= new JPanel[4];
	private JLabel[][] label	= new JLabel[3][4];
	private JTextField[][] text = new JTextField[2][4];
	private JButton[][] button  = new JButton[3][3];
	private JTextArea textArea;
	private String currPanel;

	public static void main(String[] args) {
		if(args.length<3){
			System.out.println("Incorrect use. use java Client <server> <port> <timeout> <inputFile(optional)>");return;
		}
		InetAddress address = null;
		try{address = InetAddress.getByName(args[0]);}catch(UnknownHostException e){}

		new Controller(new GUI(),address,Integer.parseInt(args[1]),Integer.parseInt(args[2]));
	}

	public GUI() {
		super("Client GUI");

		this.setSize(DIMENSIONS[0], DIMENSIONS[1]);			// frame size is set to DIMENSIONS[0] and DIMENSIONS[1]
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);		// set the default close operation to be DISPOSE_ON_CLOSE

		cards = new JPanel(new CardLayout());
		this.jpanels[0] = this.panel1(); cards.add(jpanels[0], REGISTERPANEL);
		this.jpanels[1] = this.panel2(); cards.add(jpanels[1], VOTEPANEL);
		this.jpanels[2] = this.panel3(); cards.add(jpanels[2], STARTPANEL);
		CardLayout cl = (CardLayout)cards.getLayout();
		cl.show(cards,STARTPANEL);
		currPanel = STARTPANEL;

		this.add(cards);
		this.setVisible(true);								// refresh the frame to display the contents
	}

	private JPanel panel1() {
		// panel is assigned a new JPanel with a BorderLayout
		JPanel panel = new JPanel(new GridLayout(5,2));

		label[0][0] = new JLabel("Username:");	panel.add(label[0][0]);
		text[0][0] = new JTextField();			panel.add( text[0][0]);
		label[0][1] = new JLabel("Password:");	panel.add(label[0][1]);
		text[0][1] = new JTextField();			panel.add( text[0][1]);
		label[0][2] = new JLabel("Name:");		panel.add(label[0][2]);
		text[0][2] = new JTextField();			panel.add( text[0][2]);
		label[0][3] = new JLabel("District");	panel.add(label[0][3]);
		text[0][3] = new JTextField();			panel.add( text[0][3]);

		button[0][0] =new JButton("Register");
		button[0][0].setActionCommand("Register Voter");
		panel.add( button[0][0] );

		button[0][1] = new JButton("GO BACK");
		button[0][1].setActionCommand("GO BACK");
		panel.add(button[0][1]);

		return panel;
	}

	private JPanel panel2() {
		JPanel panel = new JPanel(new GridLayout(5,2));

		label[1][0] = new JLabel("ID:");       		 	 panel.add(label[1][0]);
		text[1][0] = new JTextField();        		 	 panel.add( text[1][0]);
		label[1][1] = new JLabel("Password:");  	 	 panel.add(label[1][1]);
		text[1][1] = new JTextField();        		 	 panel.add( text[1][1]);
		label[1][2] = new JLabel("Candidate:"); 		 panel.add(label[1][2]);
		text[1][2] = new JTextField();         		 panel.add( text[1][2]);
		label[1][3] = new JLabel("Current Candidates:"); panel.add(label[1][3]);
		this.textArea= new JTextArea("");  		
		this.textArea.setEditable(false);
		JScrollPane sp = new JScrollPane(textArea);		 panel.add(sp);

		button[1][0] = new JButton("Vote");
		button[1][0].setActionCommand("Vote");
		panel.add(button[1][0]);

		button[1][1] = new JButton("GO BACK");
		button[1][1].setActionCommand("GO BACK");
		panel.add( button[1][1] );

		return panel; 
	}

	private JPanel panel3() {
		JPanel panel = new JPanel(new GridLayout(3,1));

		button[2][0] = new JButton("Register As Voter");
		button[2][0].setActionCommand("Register As Voter");
		panel.add(button[2][0]);

		button[2][1] = new JButton("Vote For Candidate");
		button[2][1].setActionCommand("Vote For Candidate");
		panel.add(button[2][1]);

		button[2][2] = new JButton("EXIT");
		button[2][2].setActionCommand("EXIT");
		panel.add(button[2][2]);

		return panel;
	}

	public JLabel getLabel(int panel, int i) {
		return this.label[panel][i];
	}

	public JTextField getText(int panel, int i) {
		return this.text[panel][i];
	}

	public JButton getButton(int panel, int i) {
		return button[panel][i];
	}

	public void addController(Controller controller){
		for(int i=0; i < 3; i++){
			for(int k=0; k < 3; k++){
				if(this.button[i][k]!=null){this.button[i][k].addActionListener(controller); this.button[i][k].addKeyListener(controller);}
			}
		}
		for(int i=0; i<2 ;i++){
			for(int k=0;k<4;k++){
				if(this.text[i][k]!=null){this.text[i][k].addKeyListener(controller);}
			}
		}
	}

	public void setVoters(String x){
		textArea.setText(x);
	}

	public void setLayout(int i){
		CardLayout cl = (CardLayout)cards.getLayout();
		if(i==0){
			cl.show(cards,REGISTERPANEL);
			currPanel = REGISTERPANEL;
		} else if(i==1) {
			cl.show(cards,VOTEPANEL);
			currPanel = VOTEPANEL;
		} else if(i==2) {
			cl.show(cards,STARTPANEL);
			currPanel = STARTPANEL;
		} else {
			System.out.println("Invalid panel");
		}
	}
	
	public String getCurrPanel(){
		return currPanel;
	}
}