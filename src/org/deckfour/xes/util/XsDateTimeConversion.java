/*
 * OpenXES
 * 
 * The reference implementation of the XES meta-model for event 
 * log data management.
 * 
 * Copyright (c) 2008 Christian W. Guenther (christian@deckfour.org)
 * 
 * 
 * LICENSE:
 * 
 * This code is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 * 
 * EXEMPTION:
 * 
 * The use of this software can also be conditionally licensed for
 * other programs, which do not satisfy the specified conditions. This
 * requires an exemption from the general license, which may be
 * granted on a per-case basis.
 * 
 * If you want to license the use of this software with a program
 * incompatible with the LGPL, please contact the author for an
 * exemption at the following email address: 
 * christian@deckfour.org
 * 
 */
package org.deckfour.xes.util;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.DatatypeConverter;

/**
 * This class serves as a provider for static xs:dateTime-related manipulation
 * and parsing methods. 
 * 
 * @author Christian W. Guenther (christian at deckfour dot org)
 */
public class XsDateTimeConversion {

	/**
	 * Expects an XML xs:dateTime lexical format string, as in
	 * <code>2005-10-24T11:57:31.000+01:00</code>. Some bad MXML files miss
	 * timezone or milliseconds information, thus a certain amount of tolerance
	 * is applied towards malformed timestamp string representations. If
	 * unparseable, this method will return <code>null</code>.
	 * 
	 * @param xsDateTime
	 *            Timestamp string in the XML xs:dateTime format.
	 * @return Parsed Date object.
	 */
	public Date parseXsDateTime(String xsDateTime) {
		try {
			Calendar cal = DatatypeConverter.parseDateTime(xsDateTime);
			return cal.getTime();
		} catch (IllegalArgumentException e) {
			return null;
		}
	}
	
	/**
	 * Formats a given date to the xs:dateTime format of XML.
	 * 
	 * @param date
	 *            Date to be formatted.
	 * @return String formatting the given date.
	 */
	public String format(Date date) {
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		return DatatypeConverter.printDateTime(cal);
	}
}
