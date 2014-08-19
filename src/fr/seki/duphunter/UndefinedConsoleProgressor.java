package fr.seki.duphunter;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A console progressor when the total amount of steps is unknown.
 * Useful to show that the application is not blocked
 * @author Sebastien
 */
public class UndefinedConsoleProgressor implements Progressor{

	private String anim = "|/-\\";
	private int cur = 0;

	public void progress() {
		String data = "\r" + anim.charAt(cur++ % anim.length());
		try {
			System.out.write(data.getBytes());
		} catch (IOException ex) {
			Logger.getLogger(UndefinedConsoleProgressor.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
