/*
 * OpenXES
 * 
 * The reference implementation of the XES meta-model for event 
 * log data management.
 * 
 * Copyright (c) 2017 Christian W. Guenther (christian@deckfour.org)
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
import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.extension.XExtension;
import org.deckfour.xes.factory.XFactory;
import org.deckfour.xes.factory.XFactoryRegistry;
import org.deckfour.xes.info.XGlobalAttributeNameMap;
import org.deckfour.xes.model.XAttributable;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeBoolean;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeList;
import org.deckfour.xes.model.XAttributeLiteral;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;

/**
 * @author Eric Verbeek (h.m.w.verbeek@tue.nl)
 * 
 */
public class XSoftwareEventExtension extends XExtension {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8723771503683938737L;
	
	/**
	 * Unique URI of this extension.
	 */
	public static final URI EXTENSION_URI = URI
			.create("http://www.xes-standard.org/swevent.xesext");
	/**
	 * Prefix for this extension.
	 */
	public static final String PREFIX = "swevent";

	/**
	 * Levels of all defined attributes.
	 */
	private static enum Level {
		EVENT, LOG, META
	};

	/**
	 * Types of all defined attributes.
	 */
	private static enum Type {
		BOOLEAN, INT, LIST, STRING
	};

	/**
	 * All defined attributes.
	 */
	private static enum DefinedAttribute {
		APP_NAME				("appName",		 			Level.EVENT, 	Type.STRING, 	"User defined application tier"), 
		APP_NODE				("appNode",		 			Level.EVENT, 	Type.STRING, 	"User defined application node"),
		APP_SESSION				("appSession",	 			Level.EVENT, 	Type.STRING, 	"User defined application session"),
		APP_TIER				("appTier",		 			Level.EVENT, 	Type.STRING, 	"User defined application name"),
		CALLEE_CLASS			("callee-class", 			Level.EVENT, 	Type.STRING, 	"Callee - Class"),
		CALLEE_FILENAME			("callee-filename",			Level.EVENT, 	Type.STRING, 	"Callee - File name source code artifact"),
		CALLEE_INSTANCEID		("callee-instanceId",	 	Level.EVENT, 	Type.STRING, 	"Callee - Instance id of class instance"),
		CALLEE_ISCONSTRUCTOR	("callee-isConstructor", 	Level.EVENT, 	Type.BOOLEAN, 	"Callee - Is a class constructor"),
		CALLEE_LINENR			("callee-lineNr", 			Level.EVENT, 	Type.STRING, 	"Callee - Line number in source code artifact"),
		CALLEE_METHOD			("callee-method", 			Level.EVENT, 	Type.STRING, 	"Callee - Method"),
		CALLEE_PACKAGE			("callee-package", 			Level.EVENT, 	Type.STRING, 	"Callee - Package"),
		CALLEE_PARAMSIG			("callee-paramSig", 		Level.EVENT, 	Type.STRING, 	"Callee - Parameter signature"),
		CALLEE_RETURNSIG		("callee-returnSig",		Level.EVENT, 	Type.STRING, 	"Callee - Return signature"),
		CALLER_CLASS			("caller-class", 			Level.EVENT, 	Type.STRING, 	"Caller - Class"),
		CALLER_FILENAME			("caller-filename",			Level.EVENT, 	Type.STRING, 	"Caller - File name source code artifact"),
		CALLER_INSTANCEID		("caller-instanceId",	 	Level.EVENT, 	Type.STRING, 	"Caller - Instance id of class instance"),
		CALLER_ISCONSTRUCTOR	("caller-isConstructor", 	Level.EVENT, 	Type.BOOLEAN, 	"Caller - Is a class constructor"),
		CALLER_LINENR			("caller-lineNr", 			Level.EVENT, 	Type.STRING, 	"Caller - Line number in source code artifact"),
		CALLER_METHOD			("caller-method", 			Level.EVENT, 	Type.STRING, 	"Caller - Method"),
		CALLER_PACKAGE			("caller-package", 			Level.EVENT, 	Type.STRING, 	"Caller - Package"),
		CALLER_PARAMSIG			("caller-paramSig", 		Level.EVENT, 	Type.STRING, 	"Caller - Parameter signature"),
		CALLER_RETURNSIG		("caller-returnSig",		Level.EVENT, 	Type.STRING, 	"Caller - Return signature"),
		EX_CAUGHT				("exCaught",				Level.EVENT, 	Type.STRING, 	"Caught exception type"),
		EX_THROWN				("exThrown",				Level.EVENT, 	Type.STRING, 	"Thrown exception type"),
		HAS_DATA				("hasData", 				Level.LOG, 		Type.BOOLEAN, 	"Has method data"),
		HAS_EXCEPTION			("hasException", 			Level.LOG, 		Type.BOOLEAN, 	"Has exception data"),
		NANOTIME				("nanotime",				Level.EVENT, 	Type.INT,	 	"Elapsed nano time"),
		PARAMS					("params",					Level.EVENT, 	Type.LIST,	 	"List of parameters for the called method"),
		PARAM_VALUE				("paramValue",				Level.META, 	Type.STRING, 	"A parameter value in the list params"),
		RETURN_VALUE			("returnValue",				Level.EVENT, 	Type.STRING, 	"Return value for the returning method"),
		THREAD_ID				("threadId",				Level.EVENT, 	Type.STRING, 	"Thread id for generated event"),
		TYPE					("type", 					Level.EVENT, 	Type.STRING, 	"Event type"),
		VALUE_TYPE				("valueType", 				Level.META, 	Type.STRING, 	"A runtime value type for a return or parameter value");

		String key;
		String alias;
		Level level;
		Type type;
		XAttribute prototype;

		DefinedAttribute(String key, Level level, Type type, String alias) {
			this.key = PREFIX + ":" + key;
			this.level = level;
			this.type = type;
			this.alias = alias;
		}

		void setPrototype(XAttribute prototype) {
			this.prototype = prototype;
		}
	}

	/**
	 * Global key place holders. Can be initialized immediately.
	 */
	public static final String KEY_APP_NAME		 		= DefinedAttribute.APP_NAME.key;
	public static final String KEY_APP_NODE		 		= DefinedAttribute.APP_NODE.key;
	public static final String KEY_APP_SESSION	 		= DefinedAttribute.APP_SESSION.key;
	public static final String KEY_APP_TIER		 		= DefinedAttribute.APP_TIER.key;
	public static final String KEY_CALLEE_CLASS 		= DefinedAttribute.CALLEE_CLASS.key;
	public static final String KEY_CALLEE_FILENAME 		= DefinedAttribute.CALLEE_FILENAME.key;
	public static final String KEY_CALLEE_INSTANCEID	= DefinedAttribute.CALLEE_INSTANCEID.key;
	public static final String KEY_CALLEE_ISCONSTRUCTOR	= DefinedAttribute.CALLEE_ISCONSTRUCTOR.key;
	public static final String KEY_CALLEE_LINENR 		= DefinedAttribute.CALLEE_LINENR.key;
	public static final String KEY_CALLEE_METHOD 		= DefinedAttribute.CALLEE_METHOD.key;
	public static final String KEY_CALLEE_PACKAGE 		= DefinedAttribute.CALLEE_PACKAGE.key;
	public static final String KEY_CALLEE_PARAMSIG 		= DefinedAttribute.CALLEE_PARAMSIG.key;
	public static final String KEY_CALLEE_RETURNSIG 	= DefinedAttribute.CALLEE_PARAMSIG.key;
	public static final String KEY_CALLER_CLASS 		= DefinedAttribute.CALLER_CLASS.key;
	public static final String KEY_CALLER_FILENAME 		= DefinedAttribute.CALLER_FILENAME.key;
	public static final String KEY_CALLER_INSTANCEID	= DefinedAttribute.CALLER_INSTANCEID.key;
	public static final String KEY_CALLER_ISCONSTRUCTOR	= DefinedAttribute.CALLER_ISCONSTRUCTOR.key;
	public static final String KEY_CALLER_LINENR 		= DefinedAttribute.CALLER_LINENR.key;
	public static final String KEY_CALLER_METHOD 		= DefinedAttribute.CALLER_METHOD.key;
	public static final String KEY_CALLER_PACKAGE 		= DefinedAttribute.CALLER_PACKAGE.key;
	public static final String KEY_CALLER_PARAMSIG 		= DefinedAttribute.CALLER_PARAMSIG.key;
	public static final String KEY_CALLER_RETURNSIG 	= DefinedAttribute.CALLER_PARAMSIG.key;
	public static final String KEY_EX_CAUGHT		 	= DefinedAttribute.EX_CAUGHT.key;
	public static final String KEY_EX_THROWN		 	= DefinedAttribute.EX_THROWN.key;
	public static final String KEY_HAS_DATA		 		= DefinedAttribute.HAS_DATA.key;
	public static final String KEY_HAS_EXCEPTION 		= DefinedAttribute.HAS_EXCEPTION.key;
	public static final String KEY_NANOTIME				= DefinedAttribute.NANOTIME.key;
	public static final String KEY_PARAMS				= DefinedAttribute.PARAMS.key;
	public static final String KEY_PARAM_VALUE			= DefinedAttribute.PARAM_VALUE.key;
	public static final String KEY_RETURN_VALUE			= DefinedAttribute.RETURN_VALUE.key;
	public static final String KEY_THREAD_ID			= DefinedAttribute.THREAD_ID.key;
	public static final String KEY_TYPE 				= DefinedAttribute.TYPE.key;
	public static final String KEY_VALUE_TYPE 			= DefinedAttribute.VALUE_TYPE.key;
	
	/**
	 * Global prototype place holders. Need to be initialized by constructor.
	 */
	public static XAttributeLiteral 	ATTR_APP_NAME;
	public static XAttributeLiteral 	ATTR_APP_NODE;
	public static XAttributeLiteral 	ATTR_APP_SESSION;
	public static XAttributeLiteral 	ATTR_APP_TIER;
	public static XAttributeLiteral 	ATTR_CALLEE_CLASS;
	public static XAttributeLiteral 	ATTR_CALLEE_FILENAME;
	public static XAttributeLiteral 	ATTR_CALLEE_INSTANCEID;
	public static XAttributeBoolean 	ATTR_CALLEE_ISCONSTRUCTOR;
	public static XAttributeLiteral 	ATTR_CALLEE_LINENR;
	public static XAttributeLiteral 	ATTR_CALLEE_METHOD;
	public static XAttributeLiteral 	ATTR_CALLEE_PACKAGE;
	public static XAttributeLiteral 	ATTR_CALLEE_PARAMSIG;
	public static XAttributeLiteral 	ATTR_CALLEE_RETURNSIG;
	public static XAttributeLiteral 	ATTR_CALLER_CLASS;
	public static XAttributeLiteral 	ATTR_CALLER_FILENAME;
	public static XAttributeLiteral 	ATTR_CALLER_INSTANCEID;
	public static XAttributeBoolean 	ATTR_CALLER_ISCONSTRUCTOR;
	public static XAttributeLiteral 	ATTR_CALLER_LINENR;
	public static XAttributeLiteral 	ATTR_CALLER_METHOD;
	public static XAttributeLiteral 	ATTR_CALLER_PACKAGE;
	public static XAttributeLiteral 	ATTR_CALLER_PARAMSIG;
	public static XAttributeLiteral 	ATTR_CALLER_RETURNSIG;
	public static XAttributeLiteral 	ATTR_EX_CAUGHT;
	public static XAttributeLiteral 	ATTR_EX_THROWN;
	public static XAttributeBoolean 	ATTR_HAS_DATA;
	public static XAttributeBoolean 	ATTR_HAS_EXCEPTION;
	public static XAttributeDiscrete	ATTR_NANOTIME;
	public static XAttributeList		ATTR_PARAMS;
	public static XAttributeLiteral 	ATTR_PARAM_VALUE;
	public static XAttributeLiteral 	ATTR_RETURN_VALUE;
	public static XAttributeLiteral 	ATTR_THREAD_ID;
	public static XAttributeLiteral 	ATTR_TYPE;
	public static XAttributeLiteral 	ATTR_VALUE_TYPE;

	/**
	 * Singleton instance of this extension.
	 */
	private transient static XSoftwareEventExtension singleton = new XSoftwareEventExtension();

	/**
	 * Provides access to the singleton instance.
	 * 
	 * @return Singleton extension.
	 */
	public static XSoftwareEventExtension instance() {
		return singleton;
	}

	private Object readResolve() {
		return singleton;
	}

	/**
	 * Private constructor
	 */
	private XSoftwareEventExtension() {
		super("Software Event", PREFIX, EXTENSION_URI);
		XFactory factory = XFactoryRegistry.instance().currentDefault();

		/*
		 * Initialize all defined attributes.
		 */
		for (DefinedAttribute attribute : DefinedAttribute.values()) {
			/*
			 * Initialize the prototype of the attribute. Depends on its type.
			 */
			switch (attribute.type) {
			case BOOLEAN: {
				attribute.setPrototype(factory.createAttributeBoolean(
						attribute.key, false, this));
				break;
			}
			case INT: {
				attribute.setPrototype(factory.createAttributeDiscrete(
						attribute.key, -1, this));
				break;
			}
			case LIST: {
				attribute.setPrototype(factory.createAttributeList(
						attribute.key, this));
				break;
			}
			case STRING: {
				attribute.setPrototype(factory.createAttributeLiteral(
						attribute.key, "__INVALID__", this));
				break;
			}
			}
			/*
			 * Add the attribute to the proper list. Depends on the level.
			 */
			switch (attribute.level){
			case EVENT: {
				this.eventAttributes.add((XAttribute) attribute.prototype.clone());
				break;
			}
			case LOG: {
				this.logAttributes.add((XAttribute) attribute.prototype.clone());
				break;
			}
			case META: {
				this.metaAttributes.add((XAttribute) attribute.prototype.clone());
				break;
			}
			}
			/*
			 * Initialize the proper global prototype place holder.
			 */
			switch (attribute){
			case APP_NAME: {
				ATTR_APP_NAME = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case APP_NODE: {
				ATTR_APP_NODE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case APP_SESSION: {
				ATTR_APP_SESSION = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case APP_TIER: {
				ATTR_APP_TIER = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_CLASS: {
				ATTR_CALLEE_CLASS = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_FILENAME: {
				ATTR_CALLEE_FILENAME = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_ISCONSTRUCTOR: {
				ATTR_CALLEE_ISCONSTRUCTOR = (XAttributeBoolean) attribute.prototype;
				break;
			}
			case CALLEE_INSTANCEID: {
				ATTR_CALLEE_INSTANCEID = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_LINENR: {
				ATTR_CALLEE_LINENR = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_METHOD: {
				ATTR_CALLEE_METHOD = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_PACKAGE: {
				ATTR_CALLEE_PACKAGE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_PARAMSIG: {
				ATTR_CALLEE_PARAMSIG = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLEE_RETURNSIG: {
				ATTR_CALLEE_RETURNSIG = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_CLASS: {
				ATTR_CALLER_CLASS = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_FILENAME: {
				ATTR_CALLER_FILENAME = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_ISCONSTRUCTOR: {
				ATTR_CALLER_ISCONSTRUCTOR = (XAttributeBoolean) attribute.prototype;
				break;
			}
			case CALLER_INSTANCEID: {
				ATTR_CALLER_INSTANCEID = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_LINENR: {
				ATTR_CALLER_LINENR = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_METHOD: {
				ATTR_CALLER_METHOD = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_PACKAGE: {
				ATTR_CALLER_PACKAGE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_PARAMSIG: {
				ATTR_CALLER_PARAMSIG = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case CALLER_RETURNSIG: {
				ATTR_CALLER_RETURNSIG = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case EX_CAUGHT: {
				ATTR_EX_CAUGHT = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case EX_THROWN: {
				ATTR_EX_THROWN = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case HAS_DATA: {
				ATTR_HAS_DATA = (XAttributeBoolean) attribute.prototype;
				break;
			}
			case HAS_EXCEPTION: {
				ATTR_HAS_EXCEPTION = (XAttributeBoolean) attribute.prototype;
				break;
			}
			case NANOTIME: {
				ATTR_NANOTIME = (XAttributeDiscrete) attribute.prototype;
				break;
			}
			case PARAMS: {
				ATTR_PARAMS = (XAttributeList) attribute.prototype;
				break;
			}
			case PARAM_VALUE: {
				ATTR_PARAM_VALUE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case RETURN_VALUE: {
				ATTR_RETURN_VALUE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case THREAD_ID: {
				ATTR_THREAD_ID = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case TYPE: {
				ATTR_TYPE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			case VALUE_TYPE: {
				ATTR_VALUE_TYPE = (XAttributeLiteral) attribute.prototype;
				break;
			}
			}
			/*
			 * Initialize the key alias.
			 */
			XGlobalAttributeNameMap.instance().registerMapping(
					XGlobalAttributeNameMap.MAPPING_ENGLISH, attribute.key,
					attribute.alias);
		}
	}

	public String extractAppName(XEvent event) {
		return extract(event, DefinedAttribute.APP_NAME, (String) null);
	}
	
	public void assignAppName(XEvent event, String appName) {
		assign(event, DefinedAttribute.APP_NAME, appName);
	}
	
	public void removeAppName(XEvent event) {
		remove(event, DefinedAttribute.APP_NAME);
	}
	
	public String extractAppNode(XEvent event) {
		return extract(event, DefinedAttribute.APP_NODE, (String) null);
	}
	
	public void assignAppNode(XEvent event, String appNode) {
		assign(event, DefinedAttribute.APP_NODE, appNode);
	}
	
	public void removeAppNode(XEvent event) {
		remove(event, DefinedAttribute.APP_NODE);
	}
	
	public String extractAppSession(XEvent event) {
		return extract(event, DefinedAttribute.APP_SESSION, (String) null);
	}
	
	public void assignAppSession(XEvent event, String appSession) {
		assign(event, DefinedAttribute.APP_SESSION, appSession);
	}
	
	public void removeAppSession(XEvent event) {
		remove(event, DefinedAttribute.APP_SESSION);
	}
	
	public String extractAppTier(XEvent event) {
		return extract(event, DefinedAttribute.APP_TIER, (String) null);
	}
	
	public void assignAppTier(XEvent event, String appTier) {
		assign(event, DefinedAttribute.APP_TIER, appTier);
	}
	
	public void removeAppTier(XEvent event) {
		remove(event, DefinedAttribute.APP_TIER);
	}
	
	public String extractCalleeClass(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_CLASS, (String) null);
	}
	
	public void assignCalleeClass(XEvent event, String calleeClass) {
		assign(event, DefinedAttribute.CALLEE_CLASS, calleeClass);
	}
	
	public void removeCalleeClass(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_CLASS);
	}
	
	public String extractCalleeFilename(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_FILENAME, (String) null);
	}
	
	public void assignCalleeFilename(XEvent event, String calleeFilename) {
		assign(event, DefinedAttribute.CALLEE_FILENAME, calleeFilename);
	}
	
	public void removeCalleeFilename(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_FILENAME);
	}
	
	public String extractCalleeInstanceId(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_INSTANCEID, (String) null);
	}
	
	public void assignCalleeInstanceId(XEvent event, String calleeInstanceId) {
		assign(event, DefinedAttribute.CALLEE_INSTANCEID, calleeInstanceId);
	}
	
	public void removeCalleeInstanceId(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_INSTANCEID);
	}
	
	public boolean extractCalleeIsConstructor(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_ISCONSTRUCTOR, false);
	}
	
	public void assignCalleeIsConstructor(XEvent event, boolean calleeInstanceId) {
		assign(event, DefinedAttribute.CALLEE_ISCONSTRUCTOR, calleeInstanceId);
	}
	
	public void removeCalleeIsConstructor(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_ISCONSTRUCTOR);
	}
	
	public String extractCalleeLineNr(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_LINENR, (String) null);
	}
	
	public void assignCalleeLineNr(XEvent event, String calleeLineNr) {
		assign(event, DefinedAttribute.CALLEE_LINENR, calleeLineNr);
	}
	
	public void removeCalleeLineNr(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_LINENR);
	}
	
	public String extractCalleeMethod(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_METHOD, (String) null);
	}
	
	public void assignCalleeMethod(XEvent event, String calleeMethod) {
		assign(event, DefinedAttribute.CALLEE_METHOD, calleeMethod);
	}
	
	public void removeCalleeMethod(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_METHOD);
	}
	
	public String extractCalleePackage(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_PACKAGE, (String) null);
	}
	
	public void assignCalleePackage(XEvent event, String calleePackage) {
		assign(event, DefinedAttribute.CALLEE_PACKAGE, calleePackage);
	}
	
	public void removeCalleePackage(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_PACKAGE);
	}
	
	public String extractCalleeParamSig(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_PARAMSIG, (String) null);
	}
	
	public void assignCalleeParamSig(XEvent event, String calleeParamSig) {
		assign(event, DefinedAttribute.CALLEE_PARAMSIG, calleeParamSig);
	}
	
	public void removeCalleeParamSig(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_PARAMSIG);
	}
	
	public String extractCalleeReturnSig(XEvent event) {
		return extract(event, DefinedAttribute.CALLEE_RETURNSIG, (String) null);
	}
	
	public void assignCalleeReturnSig(XEvent event, String calleeReturnSig) {
		assign(event, DefinedAttribute.CALLEE_RETURNSIG, calleeReturnSig);
	}
	
	public void removeCalleeReturnSig(XEvent event) {
		remove(event, DefinedAttribute.CALLEE_RETURNSIG);
	}
	
	public String extractCallerClass(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_CLASS, (String) null);
	}
	
	public void assignCallerClass(XEvent event, String callerClass) {
		assign(event, DefinedAttribute.CALLER_CLASS, callerClass);
	}
	
	public void removeCallerClass(XEvent event) {
		remove(event, DefinedAttribute.CALLER_CLASS);
	}
	
	public String extractCallerFilename(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_FILENAME, (String) null);
	}
	
	public void assignCallerFilename(XEvent event, String callerFilename) {
		assign(event, DefinedAttribute.CALLER_FILENAME, callerFilename);
	}
	
	public void removeCallerFilename(XEvent event) {
		remove(event, DefinedAttribute.CALLER_FILENAME);
	}
	
	public String extractCallerInstanceId(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_INSTANCEID, (String) null);
	}
	
	public void assignCallerInstanceId(XEvent event, String callerInstanceId) {
		assign(event, DefinedAttribute.CALLER_INSTANCEID, callerInstanceId);
	}
	
	public void removeCallerInstanceId(XEvent event) {
		remove(event, DefinedAttribute.CALLER_INSTANCEID);
	}
	
	public boolean extractCallerIsConstructor(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_ISCONSTRUCTOR, false);
	}
	
	public void assignCallerIsConstructor(XEvent event, boolean callerInstanceId) {
		assign(event, DefinedAttribute.CALLER_ISCONSTRUCTOR, callerInstanceId);
	}
	
	public void removeCallerIsConstructor(XEvent event) {
		remove(event, DefinedAttribute.CALLER_ISCONSTRUCTOR);
	}
	
	public String extractCallerLineNr(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_LINENR, (String) null);
	}
	
	public void assignCallerLineNr(XEvent event, String callerLineNr) {
		assign(event, DefinedAttribute.CALLER_LINENR, callerLineNr);
	}
	
	public void removeCallerLineNr(XEvent event) {
		remove(event, DefinedAttribute.CALLER_LINENR);
	}
	
	public String extractCallerMethod(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_METHOD, (String) null);
	}
	
	public void assignCallerMethod(XEvent event, String callerMethod) {
		assign(event, DefinedAttribute.CALLER_METHOD, callerMethod);
	}
	
	public void removeCallerMethod(XEvent event) {
		remove(event, DefinedAttribute.CALLER_METHOD);
	}
	
	public String extractCallerPackage(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_PACKAGE, (String) null);
	}
	
	public void assignCallerPackage(XEvent event, String callerPackage) {
		assign(event, DefinedAttribute.CALLER_PACKAGE, callerPackage);
	}
	
	public void removeCallerPackage(XEvent event) {
		remove(event, DefinedAttribute.CALLER_PACKAGE);
	}
	
	public String extractCallerParamSig(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_PARAMSIG, (String) null);
	}
	
	public void assignCallerParamSig(XEvent event, String callerParamSig) {
		assign(event, DefinedAttribute.CALLER_PARAMSIG, callerParamSig);
	}
	
	public void removeCallerParamSig(XEvent event) {
		remove(event, DefinedAttribute.CALLER_PARAMSIG);
	}
	
	public String extractCallerReturnSig(XEvent event) {
		return extract(event, DefinedAttribute.CALLER_RETURNSIG, (String) null);
	}
	
	public void assignCallerReturnSig(XEvent event, String callerReturnSig) {
		assign(event, DefinedAttribute.CALLER_RETURNSIG, callerReturnSig);
	}
	
	public void removeCallerReturnSig(XEvent event) {
		remove(event, DefinedAttribute.CALLER_RETURNSIG);
	}
	
	public String extractExCaught(XEvent event) {
		return extract(event, DefinedAttribute.EX_CAUGHT, (String) null);
	}
	
	public void assignExCaught(XEvent event, String exCaught) {
		assign(event, DefinedAttribute.EX_CAUGHT, exCaught);
	}
	
	public void removeExCaught(XEvent event) {
		remove(event, DefinedAttribute.EX_CAUGHT);
	}
	
	public String extractExThrown(XEvent event) {
		return extract(event, DefinedAttribute.EX_THROWN, (String) null);
	}
	
	public void assignExThrown(XEvent event, String exThrown) {
		assign(event, DefinedAttribute.EX_THROWN, exThrown);
	}
	
	public void removeExThrown(XEvent event) {
		remove(event, DefinedAttribute.EX_THROWN);
	}
	
	public boolean extractHasData(XLog log) {
		return extract(log, DefinedAttribute.HAS_DATA, false);
	}
	
	public void assignHasData(XLog log, boolean hasData) {
		assign(log, DefinedAttribute.HAS_DATA, hasData);
	}
	
	public void removeHasData(XLog log) {
		remove(log, DefinedAttribute.HAS_DATA);
	}
	
	public boolean extractHasException(XLog log) {
		return extract(log, DefinedAttribute.HAS_EXCEPTION, false);
	}
	
	public void assignHasException(XLog log, boolean hasException) {
		assign(log, DefinedAttribute.HAS_EXCEPTION, hasException);
	}
	
	public void removeHasException(XLog log) {
		remove(log, DefinedAttribute.HAS_EXCEPTION);
	}
	
	public long extractNanotime(XEvent event) {
		return extract(event, DefinedAttribute.NANOTIME, -1);
	}
	
	public void assignNanotime(XEvent event, long nanotime) {
		assign(event, DefinedAttribute.NANOTIME, nanotime);
	}
	
	public void removeNanotime(XEvent event) {
		remove(event, DefinedAttribute.NANOTIME);
	}
	
	public List<XAttribute> extractParams(XEvent event) {
		return extract(event, DefinedAttribute.PARAMS, (List<XAttribute>) null);
	}
	
	public void assignParams(XEvent event, List<XAttribute> params) {
		assign(event, DefinedAttribute.PARAMS, params);
	}
	
	public void removeParams(XEvent event) {
		remove(event, DefinedAttribute.PARAMS);
	}
	
	public String extractParamValue(XAttribute attribute) {
		return extract(attribute, DefinedAttribute.PARAM_VALUE, (String) null);
	}
	
	public void assignParamValue(XAttribute attribute, String paramValue) {
		assign(attribute, DefinedAttribute.PARAM_VALUE, paramValue);
	}
	
	public void removeParamValue(XAttribute attribute) {
		remove(attribute, DefinedAttribute.PARAM_VALUE);
	}
	
	public String extractReturnValue(XEvent event) {
		return extract(event, DefinedAttribute.RETURN_VALUE, (String) null);
	}
	
	public void assignReturnValue(XEvent event, String returnValue) {
		assign(event, DefinedAttribute.RETURN_VALUE, returnValue);
	}
	
	public void removeReturnValue(XEvent event) {
		remove(event, DefinedAttribute.RETURN_VALUE);
	}
	
	public String extractThreadId(XEvent event) {
		return extract(event, DefinedAttribute.THREAD_ID, (String) null);
	}
	
	public void assignThreadId(XEvent event, String threadId) {
		assign(event, DefinedAttribute.THREAD_ID, threadId);
	}
	
	public void removeThreadId(XEvent event) {
		remove(event, DefinedAttribute.THREAD_ID);
	}
	
	public String extractType(XEvent event) {
		return extract(event, DefinedAttribute.TYPE, (String) null);
	}
	
	public void assignType(XEvent event, String type) {
		assign(event, DefinedAttribute.TYPE, type);
	}
	
	public void removeType(XEvent event) {
		remove(event, DefinedAttribute.TYPE);
	}
	
	public String extractValueType(XAttribute attribute) {
		return extract(attribute, DefinedAttribute.VALUE_TYPE, (String) null);
	}
	
	public void assignValueType(XAttribute attribute, String valueTYpe) {
		assign(attribute, DefinedAttribute.VALUE_TYPE, valueTYpe);
	}
	
	public void removeValueType(XAttribute attribute) {
		remove(attribute, DefinedAttribute.VALUE_TYPE);
	}
	
	/*
	 * Helper functions
	 */
	private boolean extract(XAttributable element, DefinedAttribute definedAttribute, boolean defaultValue) {
		XAttribute attribute = element.getAttributes().get(definedAttribute.key);
		if (attribute == null) {
			return defaultValue;
		} else {
			return ((XAttributeBoolean) attribute).getValue();
		}
	}
	
	private void assign(XAttributable element, DefinedAttribute definedAttribute, boolean value) {
		XAttributeBoolean attr = (XAttributeBoolean) definedAttribute.prototype.clone();
		attr.setValue(value);
		element.getAttributes().put(definedAttribute.key, attr);
	}

	private List<XAttribute> extract(XAttributable element, DefinedAttribute definedAttribute, List<XAttribute> defaultValue) {
		XAttribute attribute = element.getAttributes().get(definedAttribute.key);
		if (attribute == null) {
			return defaultValue;
		} else {
			return new ArrayList<XAttribute>(((XAttributeList) attribute).getCollection());
		}
	}
	
	private void assign(XAttributable element, DefinedAttribute definedAttribute, List<XAttribute> attributes) {
		XAttributeList attr = (XAttributeList) definedAttribute.prototype.clone();
		for (XAttribute attribute : attributes) {
			attr.addToCollection(attribute);
		}
		element.getAttributes().put(definedAttribute.key, attr);
	}

	private long extract(XAttributable element, DefinedAttribute definedAttribute, long defaultValue) {
		XAttribute attribute = element.getAttributes().get(definedAttribute.key);
		if (attribute == null) {
			return defaultValue;
		} else {
			return ((XAttributeDiscrete) attribute).getValue();
		}
	}
	
	private void assign(XAttributable element, DefinedAttribute definedAttribute, long value) {
		XAttributeDiscrete attr = (XAttributeDiscrete) definedAttribute.prototype.clone();
		attr.setValue(value);
		element.getAttributes().put(definedAttribute.key, attr);
	}

	private String extract(XAttributable element, DefinedAttribute definedAttribute, String defaultValue) {
		XAttribute attribute = element.getAttributes().get(definedAttribute.key);
		if (attribute == null) {
			return defaultValue;
		} else {
			return ((XAttributeLiteral) attribute).getValue();
		}
	}
	
	private void assign(XAttributable element, DefinedAttribute definedAttribute, String value) {
		if (value != null) {
			XAttributeLiteral attr = (XAttributeLiteral) definedAttribute.prototype.clone();
			attr.setValue(value);
			element.getAttributes().put(definedAttribute.key, attr);
		}
	}
	
	private void remove(XAttributable element, DefinedAttribute definedAttribute) {
		element.getAttributes().remove(definedAttribute.key);
	}


}
