
package fr.seki.tinytests;

import com.twmacinta.util.MD5;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author Sebastien
 */
public class TestMD5 {

	public static void main(String[] args){
		try {
			MD5.initNativeLibrary();
			
			File f = new File("test.db");
			
			System.out.println("MD5 for file " + f.getAbsolutePath() + " = " + MD5.asHex(MD5.getHash(f)));
		
		} catch (IOException ex) {
//			Logger.getLogger(TestMD5.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
			System.exit(1);
		}
	}
}
