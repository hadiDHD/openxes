/*
 * OpenXES
 * 
 * The reference implementation of the XES meta-model for event 
 * log data management.
 * 
 * Copyright (c) 2014 Christian W. Guenther (christian@deckfour.org)
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
package org.deckfour.xes.extension.std;

import java.net.URI;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.model.XAttributable;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeLiteral;

/**
 * @author Eric Verbeek (h.m.w.verbeek@tue.nl)
 *
 */
public class XClassExtension extends XExtension {

	private static final long serialVersionUID = -3947332482179571465L;

	public static final URI EXTENSION_URI = URI
			.create("http://www.xes-standard.org/class.xesext");

	public static final String KEY_NAME = "class:name";

	public static XAttributeLiteral ATTR_NAME;

	private transient static XClassExtension singleton = new XClassExtension();

	public static XClassExtension instance() {
		return singleton;
	}

	private Object readResolve() {
		return singleton;
	}

	private XClassExtension() {
		super("Class", "class", EXTENSION_URI);
		XFactory factory = XFactoryRegistry.instance().currentDefault();
		ATTR_NAME = factory.createAttributeLiteral(KEY_NAME, "__INVALID__",
				this);
		this.eventAttributes.add((XAttribute) ATTR_NAME.clone());
	}

	public String extractName(XAttributable element) {
		XAttribute attribute = element.getAttributes().get(KEY_NAME);
		if (attribute == null) {
			return null;
		} else {
			return ((XAttributeLiteral) attribute).getValue();
		}
	}

	public void assignName(XAttributable element, String id) {
		if (id != null && id.trim().length() > 0) {
			XAttributeLiteral attr = (XAttributeLiteral) ATTR_NAME.clone();
			attr.setValue(id);
			element.getAttributes().put(KEY_NAME, attr);
		}
	}
}
