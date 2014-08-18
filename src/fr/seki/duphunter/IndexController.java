/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.seki.duphunter;

import java.io.File;

/**
 *
 * @author Sebastien
 */
public class IndexController {
	
	IndexModel model;

	public IndexController(IndexModel model) {
		this.model = model;
	}

	public void setDB(File selectedFile) {
		model.setDBFile(selectedFile);
	}
	
	
}
