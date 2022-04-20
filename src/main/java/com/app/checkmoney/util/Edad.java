package com.app.checkmoney.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class Edad {
	

	public static String getEdad(Date fechaNacimiento) {
	    if (fechaNacimiento != null) {
	        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
	        StringBuilder result = new StringBuilder();
	        if (fechaNacimiento != null) {
	            Calendar c = new GregorianCalendar();
	            c.setTime(fechaNacimiento);
	            result.append(calcularEdad(c));
	        }
	        return result.toString();
	    }
	    return "";
	}
	
	private static int calcularEdad(Calendar fechaNac) {
	    Calendar today = Calendar.getInstance();
	    int diffYear = today.get(Calendar.YEAR) - fechaNac.get(Calendar.YEAR);
	    int diffMonth = today.get(Calendar.MONTH) - fechaNac.get(Calendar.MONTH);
	    int diffDay = today.get(Calendar.DAY_OF_MONTH) - fechaNac.get(Calendar.DAY_OF_MONTH);
	    // Si está en ese año pero todavía no los ha cumplido
	    if (diffMonth < 0 || (diffMonth == 0 && diffDay < 0)) {
	        diffYear = diffYear - 1;
	    }
	    return diffYear;
	}
}
