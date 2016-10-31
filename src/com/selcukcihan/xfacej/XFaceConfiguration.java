package com.selcukcihan.xfacej;

public class XFaceConfiguration {
	
	public XFaceConfiguration(String p_face, String p_language, int red, int green, int blue) {
		m_face = p_face;
		m_language = p_language;
		this.red = (float)(red / 256.0);
		this.green = (float)(green / 256.0);
		this.blue = (float)(blue / 256.0);
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
	
	public float getRed() {
		return red;
	}

	public void setRed(float red) {
		this.red = red;
	}

	public float getGreen() {
		return green;
	}

	public void setGreen(float green) {
		this.green = green;
	}

	public float getBlue() {
		return blue;
	}

	public void setBlue(float blue) {
		this.blue = blue;
	}

	private String m_face;
	private final String m_faceRoot = "faces/";
	private final String m_dictionaries = "dicts/";
	private String m_language;
	private float red;
	private float green;
	private float blue;

}
