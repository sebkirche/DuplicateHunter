package fr.seki.duphunter.gui;

import fr.seki.duphunter.IndexController;
import fr.seki.duphunter.IndexModel;
import fr.seki.duphunter.SimpleExtFileFilter;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javax.swing.AbstractAction;
import javax.swing.Action;
import static javax.swing.Action.ACCELERATOR_KEY;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

/**
 *
 * @author Sebastien
 */
public class MainFrame extends javax.swing.JFrame implements Observer {

	private File selectedFile;
	private IndexModel model;
	private IndexController control;
	public final String DH_VERSION = "0.2";

	/**
	 * Creates new form MainFrame
	 */
	public MainFrame() {
		model = new IndexModel();
		control = new IndexController(model);
		initComponents();
		model.addObserver(this);
		indexViewerPanel.setModel(model);
		optionsPanel.setModel(model);
		optionsPanel.setModelController(control);
		optionsPanel.setParentFrame(this);
		optionsPanel.retrieveConfig();
		fileOpenMenuItem.setAction(new ChooseDBAction());
		fileQuitMenuItem.setAction(new QuitAction());
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
	 * this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        fileChooser = new javax.swing.JFileChooser();
        frameContent = new javax.swing.JPanel();
        tabbedPane = new javax.swing.JTabbedPane();
        indexViewerPanel = new fr.seki.duphunter.gui.IndexViewPanel();
        optionsPanel = new fr.seki.duphunter.gui.OptionsPanel();
        jLabel1 = new javax.swing.JLabel();
        dbLabel = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        menuFile = new javax.swing.JMenu();
        fileNewMenuItem = new javax.swing.JMenuItem();
        fileOpenMenuItem = new javax.swing.JMenuItem();
        fileQuitMenuItem = new javax.swing.JMenuItem();
        menuEdit = new javax.swing.JMenu();
        menuHelp = new javax.swing.JMenu();
        helpAboutMenuItem = new javax.swing.JMenuItem();

        fileChooser.setDialogTitle("Selection d'une base");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Duplicate Hunter");
        setLocationByPlatform(true);
        setName("MainFrame"); // NOI18N

        tabbedPane.addTab("View Data", indexViewerPanel);
        tabbedPane.addTab("Options", optionsPanel);

        jLabel1.setText("Database");

        dbLabel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        dbLabel.setText("Not connected");

        javax.swing.GroupLayout frameContentLayout = new javax.swing.GroupLayout(frameContent);
        frameContent.setLayout(frameContentLayout);
        frameContentLayout.setHorizontalGroup(
            frameContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(frameContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(tabbedPane)
                    .addGroup(frameContentLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(dbLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 536, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        frameContentLayout.setVerticalGroup(
            frameContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, frameContentLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(frameContentLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dbLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(tabbedPane, javax.swing.GroupLayout.DEFAULT_SIZE, 456, Short.MAX_VALUE)
                .addContainerGap())
        );

        menuFile.setText("File");

        fileNewMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        fileNewMenuItem.setText("New...");
        fileNewMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileNewMenuItemActionPerformed(evt);
            }
        });
        menuFile.add(fileNewMenuItem);

        fileOpenMenuItem.setText("Open...");
        menuFile.add(fileOpenMenuItem);

        fileQuitMenuItem.setText("Quit");
        menuFile.add(fileQuitMenuItem);

        menuBar.add(menuFile);

        menuEdit.setText("Edit");
        menuBar.add(menuEdit);

        menuHelp.setText("Help");

        helpAboutMenuItem.setText("About");
        helpAboutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                displayAbout(evt);
            }
        });
        menuHelp.add(helpAboutMenuItem);

        menuBar.add(menuHelp);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(frameContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(frameContent, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void displayAbout(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_displayAbout
		ImageIcon icn = new ImageIcon(MainFrame.class.getResource("duplicate_search.png"));
		JOptionPane.showMessageDialog(this, "DuplicateHunter\nVersion " + DH_VERSION, "About this tool", JOptionPane.OK_OPTION, icn);
    }//GEN-LAST:event_displayAbout

    private void fileNewMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_fileNewMenuItemActionPerformed
		String ext = model.getDbExtension();
		String cwd = System.getProperty("user.dir");
		fileChooser.setCurrentDirectory(new File(cwd));
		fileChooser.setFileFilter(new SimpleExtFileFilter(ext));

		int returnVal = fileChooser.showOpenDialog(MainFrame.this);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = fileChooser.getSelectedFile();
			String path = f.getPath();
			if (!path.toLowerCase().endsWith(ext)) {
				f = new File(path + "." + ext);
			}
			control.newDB(f);
		}
    }//GEN-LAST:event_fileNewMenuItemActionPerformed

	class ChooseDBAction extends AbstractAction {
		public ChooseDBAction(){
			putValue(NAME, "Open...");//same as super("Open...");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			String cwd = System.getProperty("user.dir");
			fileChooser.setCurrentDirectory(new File(cwd));
			fileChooser.setFileFilter(new SimpleExtFileFilter(model.getDbExtension()));

			int returnVal = fileChooser.showSaveDialog(MainFrame.this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				selectedFile = fileChooser.getSelectedFile();
				dbSelectedAction.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
			}
		}
	};
	
	class QuitAction extends AbstractAction {

		public QuitAction() {
			putValue(NAME, "Quit");
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	};
	
	private Action dbSelectedAction = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
			control.openDB(selectedFile);

		}
	};

	/**
	 * @param args the command line arguments
	 */
	public static void main(String args[]) {
		/* Set the Nimbus look and feel */
		//<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
		 * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
		 */
		try {
			/*for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
			 if ("Nimbus".equals(info.getName())) {
			 javax.swing.UIManager.setLookAndFeel(info.getClassName());
			 break;
			 }
			 }
			 */
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(MainFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
		}
		//</editor-fold>

		/* Create and display the form */
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new MainFrame().setVisible(true);
			}
		});
	}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    javax.swing.JLabel dbLabel;
    javax.swing.JFileChooser fileChooser;
    javax.swing.JMenuItem fileNewMenuItem;
    javax.swing.JMenuItem fileOpenMenuItem;
    javax.swing.JMenuItem fileQuitMenuItem;
    javax.swing.JPanel frameContent;
    javax.swing.JMenuItem helpAboutMenuItem;
    fr.seki.duphunter.gui.IndexViewPanel indexViewerPanel;
    javax.swing.JLabel jLabel1;
    javax.swing.JMenuBar menuBar;
    javax.swing.JMenu menuEdit;
    javax.swing.JMenu menuFile;
    javax.swing.JMenu menuHelp;
    fr.seki.duphunter.gui.OptionsPanel optionsPanel;
    javax.swing.JTabbedPane tabbedPane;
    // End of variables declaration//GEN-END:variables

	@Override
	public void update(Observable o, Object arg) {

		dbLabel.setText(model.getDbFile().getPath());

	}
}
