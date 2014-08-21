package fr.seki.duphunter.gui;

import fr.seki.duphunter.IndexController;
import fr.seki.duphunter.IndexModel;
import fr.seki.duphunter.SVNWorkingCopyHelper;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import static javax.swing.event.TableModelEvent.DELETE;
import static javax.swing.event.TableModelEvent.INSERT;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Sebastien
 */
public class OptionsPanel extends javax.swing.JPanel implements Observer {
	
	private IndexModel model = null;
	private MainFrame parent = null;
	private SourcesTableModel srcTableModel = new SourcesTableModel();
	private boolean isConnected = false;
	boolean ignoreEmpty;
	private IndexController control;

	/**
	 * Creates new form OptionsPanel
	 */
	public OptionsPanel() {
		initComponents();

		//react to selections
		sourcesTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				updateGui();
			}
		});

		//react to drop files / directories
		sourcesScroll.setDropTarget(new DropTarget() {
			@Override
			public synchronized void drop(DropTargetDropEvent dtde) {
				dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Transferable t = dtde.getTransferable();
				try {
					List<File> fileList = (List<File>) t.getTransferData(DataFlavor.javaFileListFlavor);
					SVNWorkingCopyHelper svnhelper = new SVNWorkingCopyHelper();

					for (File f : fileList) {
						if(f.isDirectory()){
							String repoUrl = svnhelper.getRepoUrl(f);
							if(repoUrl != null)
								addPathToList(repoUrl);
							else
								addPathToList(f.getCanonicalPath());
						}
					}
				} catch (UnsupportedFlavorException ex) {
					Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
				
			}
		});
	}

	/**
	 * This method is called from within the constructor to initialize the form. WARNING: Do NOT modify this code. The content of
	 * this method is always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sourcesScroll = new javax.swing.JScrollPane();
        sourcesTable = new javax.swing.JTable();
        removeSrcBtn = new javax.swing.JButton();
        saveBtn = new javax.swing.JButton();
        addDirBtn = new javax.swing.JButton();
        refreshSingleBtn = new javax.swing.JButton();
        refreshAllBtn = new javax.swing.JButton();
        addRepoBtn = new javax.swing.JButton();

        sourcesTable.setModel(srcTableModel);
        sourcesTable.setColumnSelectionAllowed(true);
        sourcesScroll.setViewportView(sourcesTable);
        sourcesTable.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        removeSrcBtn.setText("Remove selected");
        removeSrcBtn.setEnabled(false);
        removeSrcBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeSrcBtnActionPerformed(evt);
            }
        });

        saveBtn.setText("Save");
        saveBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveBtnActionPerformed(evt);
            }
        });

        addDirBtn.setText("Add directory");
        addDirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDirBtnActionPerformed(evt);
            }
        });

        refreshSingleBtn.setText("Refresh selected");
        refreshSingleBtn.setEnabled(false);
        refreshSingleBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshSingleBtnActionPerformed(evt);
            }
        });

        refreshAllBtn.setText("Refresh all");
        refreshAllBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshAllBtnActionPerformed(evt);
            }
        });

        addRepoBtn.setText("Add repositiory");
        addRepoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addRepoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(sourcesScroll, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(removeSrcBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addDirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(addRepoBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(refreshSingleBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(refreshAllBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 147, Short.MAX_VALUE)
                        .addComponent(saveBtn)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sourcesScroll, javax.swing.GroupLayout.DEFAULT_SIZE, 138, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(removeSrcBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addDirBtn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(addRepoBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(refreshSingleBtn)
                    .addComponent(refreshAllBtn)
                    .addComponent(saveBtn))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void saveBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveBtnActionPerformed
		
		model.setSources(srcTableModel.getData());

    }//GEN-LAST:event_saveBtnActionPerformed

    private void removeSrcBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeSrcBtnActionPerformed
		int i = sourcesTable.getSelectedRow();
		while (i > -1) {
			srcTableModel.removeRow(i);
			sourcesTable.tableChanged(new TableModelEvent(srcTableModel, i, i, TableModelEvent.ALL_COLUMNS, DELETE));
			i = sourcesTable.getSelectedRow();
		}
		sourcesTable.repaint();
    }//GEN-LAST:event_removeSrcBtnActionPerformed

    private void addDirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDirBtnActionPerformed
		JFileChooser jfs = new JFileChooser();
		String cwd = System.getProperty("user.dir");
		jfs.setCurrentDirectory(new File(cwd));
		jfs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		jfs.setMultiSelectionEnabled(true);
		jfs.setAcceptAllFileFilterUsed(false);
		if (jfs.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			for (File f : jfs.getSelectedFiles()) {
				try {
					addPathToList(f.getCanonicalPath());
				} catch (IOException ex) {
					Logger.getLogger(OptionsPanel.class.getName()).log(Level.SEVERE, null, ex);
				}
			}
			sourcesTable.repaint();
		}
    }//GEN-LAST:event_addDirBtnActionPerformed
	
	private void addPathToList(String path) {
		int r = srcTableModel.addRow(new SourceData(path, true));
		sourcesTable.tableChanged(new TableModelEvent(srcTableModel, r, r, TableModelEvent.ALL_COLUMNS, INSERT));
	}

    private void refreshSingleBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshSingleBtnActionPerformed
		int i;
		int[] sel = sourcesTable.getSelectedRows();
		for (i = 0; i < sel.length; i++) {
			String s = (String) srcTableModel.getValueAt(sel[i], 0);
			control.index(s);
		}
    }//GEN-LAST:event_refreshSingleBtnActionPerformed

    private void addRepoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addRepoBtnActionPerformed
		String repo = JOptionPane.showInputDialog(this, "Enter the repositiory path", "svn://");

		//TODO sanity check for repository validity
		int r = srcTableModel.addRow(new SourceData(repo, true));
		sourcesTable.tableChanged(new TableModelEvent(srcTableModel, r, r, TableModelEvent.ALL_COLUMNS, INSERT));
		sourcesTable.repaint();
    }//GEN-LAST:event_addRepoBtnActionPerformed

    private void refreshAllBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshAllBtnActionPerformed
		for (int i = 0; i < srcTableModel.getRowCount(); i++) {
			SourceData sd = srcTableModel.getValueAt(i);
			if(sd.active)
				control.index(sd.path);
		}
    }//GEN-LAST:event_refreshAllBtnActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDirBtn;
    private javax.swing.JButton addRepoBtn;
    private javax.swing.JButton refreshAllBtn;
    private javax.swing.JButton refreshSingleBtn;
    private javax.swing.JButton removeSrcBtn;
    private javax.swing.JButton saveBtn;
    private javax.swing.JScrollPane sourcesScroll;
    private javax.swing.JTable sourcesTable;
    // End of variables declaration//GEN-END:variables

	void setModel(IndexModel model) {
		this.model = model;
		model.addObserver(this);
	}
	
	void setParentFrame(MainFrame parent) {
		this.parent = parent;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		isConnected = true;
		
		srcTableModel.retrieveData();
		sourcesTable.tableChanged(new TableModelEvent(srcTableModel));
		sourcesTable.repaint();
		
		updateGui();
	}
	
	private void updateGui() {
		boolean sourceSelected;
		
		sourceSelected = (sourcesTable.getSelectedRow() > -1);
		
		removeSrcBtn.setEnabled(sourceSelected);
		refreshSingleBtn.setEnabled(sourceSelected && isConnected);
		refreshAllBtn.setEnabled(isConnected);
	}
	
	void setModelController(IndexController control) {
		this.control = control;
	}
	
	private class ColumnData {
		
		String name;
		int alignment;
		
		public ColumnData(String name, int alignment) {
			this.name = name;
			this.alignment = alignment;
		}
	}
	
	private class SourcesTableModel extends AbstractTableModel {
		
		protected ColumnData[] colNames = {new ColumnData("Source", JLabel.LEFT), new ColumnData("Active?", JLabel.CENTER)};
		protected Vector<SourceData> data;
		
		public SourcesTableModel() {
		}
		
		@Override
		public int getRowCount() {
			return data == null ? 0 : data.size();
		}
		
		@Override
		public int getColumnCount() {
			return colNames.length;
		}
		
		@Override
		public String getColumnName(int column) {
			return colNames[column].name;
		}
		
		@Override
		public Class getColumnClass(int c) {
			return getValueAt(0, c).getClass();
		}
		
		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (rowIndex < 0 || rowIndex > getRowCount()) {
				return "";
			}
			SourceData src = (SourceData) data.elementAt(rowIndex);
			switch (columnIndex) {
				case 0:
					return src.path;
				case 1:
					return src.active;
				default:
					return "";
			}
		}
		
		public SourceData getValueAt(int row){
			return data.elementAt(row);
		}
		
		@Override
		public void setValueAt(Object value, int rowIndex, int columnIndex) {
			SourceData src = data.elementAt(rowIndex);
			switch (columnIndex) {
				case 0:
					src.path = (String) value;
					break;
				case 1:
					src.active = (boolean) value;
					break;
			}
		}
		
		public void removeRow(int row) {
			data.remove(row);
		}
		
		public int addRow(SourceData sd) {
			data.add(sd);
			return data.size() - 1;
		}
		
		@Override
		public boolean isCellEditable(int row, int col) {
			return true;
		}
		
		public void retrieveData() {
			data = model.getSources();
		}
		
		Vector<SourceData> getData() {
			return data;
		}
		
	}
}
