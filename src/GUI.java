import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;


public class GUI extends JFrame {
	
	//VARIABLES DE LA CLASSE
	static File timelineFile = null;
	static File blackFile = null;
	static JFrame waitWindow = null;
	
	public GUI() {
		super();
		buildMainFrame();
		this.setContentPane(buildMainContentPane());
		this.setVisible(true);
		
	}
	
	private void buildMainFrame(){
		setTitle("JTX Timeline Maker");
		setSize(600,300);
		setLocationRelativeTo(null);
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
		
	}
	private JPanel buildMainContentPane(){
		JPanel backPanel = new JPanel();
		backPanel.setLayout(new BoxLayout(backPanel, BoxLayout.Y_AXIS));
		backPanel.setSize(600,300);
		backPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		//timelinePanel
		JPanel timelinePanel = new JPanel();
		timelinePanel.setLayout(new GridBagLayout());
		timelinePanel.setOpaque(false);
		GridBagConstraints c = new GridBagConstraints();
		
		JPanel timelineLabelPanel = new JPanel();
		timelineLabelPanel.setOpaque(false);
		timelineLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		timelineLabelPanel.setBorder(BorderFactory.createTitledBorder("Timeline (playlist VLC)"));
		final JLabel labeltimeline = new JLabel("Pas de fichier de timeline");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		c.weightx=1;
		c.insets = new Insets(0,0,30,20);
		c.gridwidth = 2;
		timelineLabelPanel.add(labeltimeline);
		timelinePanel.add(timelineLabelPanel, c);
		
		JButton boutonTimeline = new JButton("Choisir la timeline");
		c.weightx=0;
		c.gridx=2;
		c.insets = new Insets(0,0,30,0);
		c.gridwidth=1;
		timelinePanel.add(boutonTimeline, c);
		
		
		backPanel.add(timelinePanel);
		
		
		//BlackFilePanel
		
		JPanel blackLabelPanel = new JPanel();
		blackLabelPanel.setOpaque(false);
		blackLabelPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		blackLabelPanel.setBorder(BorderFactory.createTitledBorder("Noir (inter-clips)"));
		final JLabel labelblack = new JLabel("Pas de fichier de noir");
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(0,0,0,20);
		c.gridwidth = 2;
		blackLabelPanel.add(labelblack);
		timelinePanel.add(blackLabelPanel, c);
		
		JButton boutonBlack = new JButton("Choisir le fichier de noir");
		c.gridx=2;
		c.insets = new Insets(0,0,0,0);
		c.gridwidth=1;
		timelinePanel.add(boutonBlack, c);
		
		JButton applyButton = new JButton("Executer");
		c.gridx=1;
		c.gridy=2;
		c.gridwidth = 3;
		c.insets = new Insets(40, 0, 0, 0);
		timelinePanel.add(applyButton, c);
		
		//action des boutons Timeline et Black
		boutonTimeline.addActionListener(actionBoutonTimeline(labeltimeline));
		boutonBlack.addActionListener(actionBoutonBlack(labelblack));
		
		//action du bouton executer
		applyButton.addActionListener(actionBoutonApply());
		return backPanel;
	}
	
	private static ActionListener actionBoutonTimeline(final JLabel label){
		ActionListener action = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser;
				if (timelineFile!=null) chooser = new JFileChooser(timelineFile.getParent());
				else if (blackFile!=null) chooser = new JFileChooser(blackFile.getParent());
				else chooser = new JFileChooser();
				int option = chooser.showOpenDialog(new JFrame());
				if (option == JFileChooser.APPROVE_OPTION){
					timelineFile = chooser.getSelectedFile();
					setRightPath(label, timelineFile.getAbsolutePath());
				}
				
			}
		};
		
		return action;
	}
	
	private static ActionListener actionBoutonBlack(final JLabel label){
		ActionListener action = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser;
				if (blackFile!=null) chooser = new JFileChooser(blackFile.getParent());
				else if (timelineFile!=null) chooser = new JFileChooser(timelineFile.getParent());
				else chooser = new JFileChooser();
				int option = chooser.showOpenDialog(new JFrame());
				if (option == JFileChooser.APPROVE_OPTION){
					blackFile = chooser.getSelectedFile();
					setRightPath(label, blackFile.getAbsolutePath());
				}
				
			}
		};
		
		return action;
	}

	private static void setRightPath(JLabel label, String path){
	
		   label.setText(path);
		   int taille = label.getFontMetrics(label.getFont()).stringWidth(path);
		   boolean alreadyCut = false; //savoir si on est deja rentré dans la boucle pour savoir si on enleve de suite le ...\
		   String separator  = File.separator; //la variable separator sert à ne pas avoir une boucle propre à un système d'exploitation - Par défaut cas windows.
		   while (taille > 325){ 
			   String[] paths;
			   if(alreadyCut){ //si on a deja ajouté .../ on l'enleve.
				   path = path.split(Pattern.quote(separator),2)[1];
			   }
			   if(path.contains(separator)) paths= path.split(Pattern.quote(separator), 2); //Le Pattern.quote sert à corriger un bug du au fait qu'on peut pas spliter une string selon un backslash
			   else break;  // si il reste rien à couper, tant pis.
			   path = "..."+separator + paths[1];
			   label.setText(path);
			   taille = label.getFontMetrics(label.getFont()).stringWidth(path);            		   
			   alreadyCut=true;
			   
		   }
	}
	
	private static ActionListener actionBoutonApply(){
		ActionListener action = new ActionListener() {
			 
            public void actionPerformed(ActionEvent e)
            {
            	//On vérifie que la timeline ait bien été définie
            	if (timelineFile==null){
            		JOptionPane.showMessageDialog(null, "Vous devez choisir un fichier de Timeline!");
            		return;
            	}
            	
            	//On vérifie que le fichier de noir ait bien été définie
            	if(blackFile==null){
            		JOptionPane.showMessageDialog(null, "Vous devez choisir un fichier de noir!");
            		return;
            	}
            	
            	//On définit un nouveau Thread pour la création de la timeline.
            	//Ainsi l'interface graphique continue à répondre
                Thread t = new Thread(){
                	public void run(){
              
                		Parser.parse(timelineFile.getAbsolutePath(), blackFile.getAbsolutePath());
        		
                		
                		//On demande à Swing de fermer la fenêtre d'attente
                		SwingUtilities.invokeLater(new Runnable(){
                			public void run(){
                				if (GUI.waitWindow!=null) GUI.waitWindow.setVisible(false);
                			}
                		});
            	    }
                };
                
                //On demande à Swing d'ouvrir la fenêtre d'attente dès qu'il en aura le temps.
            	SwingUtilities.invokeLater(new Runnable() {
            	    @Override
            	    public void run() {
            	    	waitWindow = new JFrame();
                    	waitWindow.setSize(300, 120);
                    	waitWindow.setLocationRelativeTo(null);
                    	waitWindow.setResizable(true);
                    	waitWindow.setName("Travail en cours");
                 
                    	JPanel backPanelWaiting = new JPanel();
                    	backPanelWaiting.setMaximumSize(new Dimension(300,120));
                    	backPanelWaiting.setLayout(new BoxLayout(backPanelWaiting, BoxLayout.PAGE_AXIS));
                    	backPanelWaiting.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
                    	JLabel labelWaiting = new JLabel("<html><center>La timeline est en cours d'élaboration.</br>Veuillez patientez...</center></html>");
                    	labelWaiting.setOpaque(false);
                		backPanelWaiting.setVisible(true);
                    		
                    	JProgressBar progressBarWaiting = new JProgressBar(0,100);
                    	progressBarWaiting.setOpaque(false);
                    	progressBarWaiting.setIndeterminate(true);
                    	backPanelWaiting.add(labelWaiting);
                    	backPanelWaiting.add(Box.createVerticalGlue());
                    	backPanelWaiting.add(progressBarWaiting);
                    	waitWindow.setContentPane(backPanelWaiting);
                    	waitWindow.setVisible(true);
            	    }
            	});   
            	
            	//On démarre le thread contenant l'action principale de justification
            	t.start();
            }
        };
        
        return action;
	}



}


