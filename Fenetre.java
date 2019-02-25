package devoirPC2R;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Fenetre extends JFrame{

	
	
	public Fenetre() {
		
		setTitle("Arene");
		setSize(500, 500);		
		setLocationRelativeTo(null);			
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		JPanel pan = new JPanel();
		
		
		setContentPane(new Arene());
		setVisible(true);
		
	}
	
	public static void main(String[] args) {
		
		
		JFrame fenetre = new Fenetre();
		
		
	}
}
