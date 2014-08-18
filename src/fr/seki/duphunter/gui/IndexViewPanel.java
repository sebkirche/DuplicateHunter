/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.seki.duphunter.gui;

import fr.seki.duphunter.IndexModel;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import quick.dbtable.DBTable;

/**
 *
 * @author Sebastien
 */
public class IndexViewPanel extends javax.swing.JPanel implements Observer {

	IndexModel model;
	File db;
	
	/**
	 * Creates new form IndexViewer
	 */
	public IndexViewPanel() {
		initComponents();
		dbTable.createControlPanel(DBTable.READ_NAVIGATION);
	}
	
	public void setModel(IndexModel model){
		this.model = model;
		model.addObserver(this);
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
	 * this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        queryText = new javax.swing.JTextArea();
        executeBtn = new javax.swing.JButton();
        dbTable = new quick.dbtable.DBTable();
        jLabel2 = new javax.swing.JLabel();
        dbName = new javax.swing.JLabel();

        jLabel1.setText("Search");

        queryText.setColumns(20);
        queryText.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        queryText.setLineWrap(true);
        queryText.setRows(5);
        queryText.setText("select * from fileindex where hash in (select hash from FileIndex group by hash having count(*) > 1) order by hash");
        queryText.setWrapStyleWord(true);
        queryText.setEnabled(false);
        jScrollPane1.setViewportView(queryText);

        executeBtn.setText("Execute");
        executeBtn.setEnabled(false);
        executeBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                executeBtnActionPerformed(evt);
            }
        });

        jLabel2.setText("Base");

        dbName.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        dbName.setForeground(new java.awt.Color(0, 51, 255));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(dbName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jScrollPane1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(executeBtn))))
                    .addComponent(dbTable, javax.swing.GroupLayout.DEFAULT_SIZE, 481, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(dbName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(executeBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(dbTable, javax.swing.GroupLayout.DEFAULT_SIZE, 271, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void executeBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_executeBtnActionPerformed
        runQuery(queryText.getText());
    }//GEN-LAST:event_executeBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel dbName;
    private quick.dbtable.DBTable dbTable;
    private javax.swing.JButton executeBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea queryText;
    // End of variables declaration//GEN-END:variables

	@Override
	public void update(Observable o, Object arg) {
		
		db = model.getDbFile();
		dbName.setText(db.getPath());
		queryText.setEnabled(true);
		executeBtn.setEnabled(true);
		
		try {
			dbTable.connectDatabase("org.sqlite.JDBC", "jdbc:sqlite:" + db.getCanonicalPath().replace("\\", "/"), null, null);
			runQuery(queryText.getText());
		} catch (SQLException ex) {
			Logger.getLogger(IndexViewPanel.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(IndexViewPanel.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ClassNotFoundException ex) {
			Logger.getLogger(IndexViewPanel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	
//	public void runQuery(String qry) {
//		try {
//			
//			dbTable.setSelectSql(queryText.getText());
//			//dbTable.setRowCountSql("select count(*) from fileindex where hash in (select hash from FileIndex group by hash having count(*) > 1) order by hash");
//			dbTable.refresh();
//		} catch (SQLException ex) {
//			Logger.getLogger(IndexViewPanel.class.getName()).log(Level.SEVERE, null, ex);
//		}
//	}
	public void runQuery(String qry){
		try {
			dbTable.refresh(model.queryIndex(qry));
		} catch (SQLException ex) {
			Logger.getLogger(IndexViewPanel.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}