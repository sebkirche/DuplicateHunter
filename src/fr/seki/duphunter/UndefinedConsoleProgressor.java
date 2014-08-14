package fr.seki.duphunter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sebastien
 */
public class UndefinedConsoleProgressor {

	private String anim = "|/-\\";
	private int cur = 0;

	public void animate() {
		String data = "\r" + anim.charAt(cur++ % anim.length());
		try {
			System.out.write(data.getBytes());
		} catch (IOException ex) {
			Logger.getLogger(UndefinedConsoleProgressor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
