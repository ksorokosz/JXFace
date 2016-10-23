package com.selcukcihan.xfacej;

public class XFaceConfiguration {
	
	public XFaceConfiguration(String p_face, String p_language) {
		m_face = p_face;
		m_language = p_language;
	}
	
	public String getFace() {
		return m_face;
	}
	public String getLanguage() {
		return m_language;
	}
	public String getFaceRoot() {
		return m_faceRoot;
	}
	public String getDictionaries() {
		return m_dictionaries;
	}

	private String m_face;
	private final String m_faceRoot = "faces/";
	private final String m_dictionaries = "dicts/";
	private String m_language;

}
