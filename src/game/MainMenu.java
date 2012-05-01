package game;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainMenu extends JPanel {
	/**
	 * For serialization (warning removal)
	 */
	private static final long serialVersionUID = -4119838071156563905L;
	
	private static boolean run = true;
	private JButton btnResumeGame;

	/**
	 * Create the panel.
	 */
	public MainMenu(GameStart startObject) {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{444, 0};
		gridBagLayout.rowHeights = new int[]{132, 30, 132, 0, 0};
		gridBagLayout.columnWeights = new double[]{1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		setLayout(gridBagLayout);
		
		JButton btnStartNewGame = new JButton("Start New Game");
		btnStartNewGame.addActionListener(startObject);

		GridBagConstraints gbc_btnStartNewGame = new GridBagConstraints();
		gbc_btnStartNewGame.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnStartNewGame.insets = new Insets(5, 20, 5, 20);
		gbc_btnStartNewGame.gridx = 0;
		gbc_btnStartNewGame.gridy = 0;
		add(btnStartNewGame, gbc_btnStartNewGame);
		
		JButton btnQuitGame = new JButton("Quit Game");
		btnQuitGame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.exit(0);
			}
		});
		
		if(!run)
			super.setEnabled(false);
		
		btnResumeGame = new JButton("Resume Game");
		btnResumeGame.setVisible(false);
		btnResumeGame.addActionListener(startObject);
		
		GridBagConstraints gbc_btnResumeGame = new GridBagConstraints();
		gbc_btnResumeGame.insets = new Insets(0, 20, 0, 20);
		gbc_btnResumeGame.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnResumeGame.gridx = 0;
		gbc_btnResumeGame.gridy = 1;
		add(btnResumeGame, gbc_btnResumeGame);
		
		GridBagConstraints gbc_btnQuitGame = new GridBagConstraints();
		gbc_btnQuitGame.insets = new Insets(5, 20, 5, 20);
		gbc_btnQuitGame.fill = GridBagConstraints.HORIZONTAL;
		gbc_btnQuitGame.gridx = 0;
		gbc_btnQuitGame.gridy = 2;
		add(btnQuitGame, gbc_btnQuitGame);

	}
	
	
	public void resumeGameVisibility(boolean flag){
		btnResumeGame.setVisible(flag);
	}

}
