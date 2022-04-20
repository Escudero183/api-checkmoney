/**
 * 
 */
package com.app.checkmoney.helper;

import java.io.File;
import java.util.Random;

/**
 * @author Linygn Escudero
 *
 */
public class Helpers {
	
	public String getFileExtension(String fullName) {        
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }
	
	public String getAlphaNumeric(int len) {
		char[] ch = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L',
			'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		char[] c=new char[len];
		Random random=new Random();
		for (int i = 0; i < len; i++) {
		  c[i]=ch[random.nextInt(ch.length)];
		}
		return new String(c);
	}

}
