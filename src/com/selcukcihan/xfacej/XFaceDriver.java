/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is XfaceApp Application Library.
 *
 * The Initial Developer of the Original Code is
 * ITC-irst, TCC Division (http://tcc.fbk.eu) Trento / ITALY.
 * For info, contact: xface-info@fbk.eu or http://xface.fbk.eu
 * Portions created by the Initial Developer are Copyright (C) 2004 - 2008
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * - Selcuk Cihan (selcukcihan@gmail.com)
 * ***** END LICENSE BLOCK ***** */

package com.selcukcihan.xfacej;

import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.media.opengl.DebugGL;
import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.glu.GLU;

import com.sun.opengl.util.j2d.TextRenderer;

import com.selcukcihan.xfacej.xfaceapp.ModelCamera;
import com.selcukcihan.xfacej.xfaceapp.ModelCamera.kMODE;


public class XFaceDriver implements GLEventListener
{
	XFaceApplication m_pApp;
	ModelCamera m_pCamera;
	
	Thread m_animThread;
	Thread m_soundThread;
	
	public GL m_gl;
	public XFaceSound m_sound;
	private long m_count = 0;
	private long m_time = 0;
	private int m_fps = 0;
	
	private String m_soundfile;
	private String m_face;
	private String m_faceRoot;
	private String m_dictionaries;
	private String m_language;
	private String m_phoFile;
	private String m_animationFile;
	private GLCanvas m_canvas;
	private TextRenderer m_textRenderer;
	private Process record_process;
	private StreamGobbler errorGobbler;
	private float red;
	private float green;
	private float blue;
	
	private class StreamGobbler extends Thread {
	    InputStream is;
	    String type;

	    private StreamGobbler(InputStream is, String type) {
	        this.is = is;
	        this.type = type;
	    }

	    @Override
	    public void run() {
	        try {
	            InputStreamReader isr = new InputStreamReader(is);
	            BufferedReader br = new BufferedReader(isr);
	            String line = null;
	            while ((line = br.readLine()) != null)
	                System.out.println(type + "> " + line);
	            System.exit(0); // this is hack. In case stderr stream closes it 
	                            // means that recording script finishes his work ;) and we may close the program.
	        }
	        catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	    }
	}
	
	public XFaceDriver(GLCanvas p_canvas, XFaceConfiguration p_config, String p_phoFile, String p_soundfile, String p_animationFile)
	{
		m_canvas = p_canvas;
		m_face = p_config.getFace();
		m_faceRoot = p_config.getFaceRoot() + m_face + "/";
		m_dictionaries = p_config.getDictionaries();
		m_language = p_config.getLanguage();
		m_phoFile = p_phoFile;
		m_soundfile = p_soundfile;
		m_animationFile = p_animationFile;
		red = p_config.getRed();
		green = p_config.getGreen();
		blue = p_config.getBlue();
	}
	
	public void display(GLAutoDrawable p_autoDrawable)
	{
		if(m_pApp == null)
			return;
		while(!m_pApp.acquireLock(false));
		long renderTime = System.currentTimeMillis();
		
		m_count++;
		
		m_gl = p_autoDrawable.getGL();

		if(m_pApp == null)
			return;
		if(m_pCamera == null)
		{
			int h = p_autoDrawable.getHeight();
			int w = p_autoDrawable.getWidth();
			m_gl.glViewport(0, 0, w, h);
			m_gl.glMatrixMode(GL.GL_PROJECTION);
			m_gl.glLoadIdentity();
			
			final GLU glu = new GLU();
			float ratio = (float)w / (float)h;
			glu.gluPerspective(30, ratio, 10.0, 1000.0);
			
			m_gl.glMatrixMode(GL.GL_MODELVIEW);
			
			m_pCamera = new ModelCamera();
			m_pCamera.setScreenSize(w, h);
			m_pCamera.setDistance(-700);
			m_pCamera.setMode(kMODE.ZOOM);
		}
		m_pCamera.apply(m_gl);
		
		m_pApp.onRenderFrame(m_gl);
		
		renderTime = System.currentTimeMillis() - renderTime;
		m_textRenderer.beginRendering(p_autoDrawable.getWidth(), p_autoDrawable.getHeight());
		m_textRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
		long now = System.currentTimeMillis();
		if(now - m_time > 1000)
		{
			m_fps = (int)(m_count * 1000.0 / (now - m_time));
			m_count = 0;
			m_time = now;
		}		
		m_textRenderer.endRendering();

		m_pApp.releaseLock();
	}

	public void displayChanged(GLAutoDrawable p_autoDrawable, boolean p_modeChanged, boolean p_deviceChanged)
	{
		
	}

	public void init(GLAutoDrawable p_autoDrawable)
	{
		//testFile();
		m_textRenderer = new TextRenderer(new Font("Segoe Print", Font.BOLD, 36));
		
		p_autoDrawable.setGL(new DebugGL(p_autoDrawable.getGL()));
		m_gl = p_autoDrawable.getGL();
		
		m_gl.glShadeModel(GL.GL_SMOOTH);
		m_gl.glCullFace(GL.GL_BACK);
		m_gl.glFrontFace(GL.GL_CCW);
		m_gl.glEnable(GL.GL_CULL_FACE);
		m_gl.glClearColor(red, green, blue, 1.0f);
		m_gl.glEnable(GL.GL_DEPTH_TEST);
		m_gl.glEnable(GL.GL_LIGHTING);
		m_gl.glEnable(GL.GL_LIGHT0);
		m_gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		m_gl.glMatrixMode(GL.GL_MODELVIEW);
		
		m_textRenderer.beginRendering(p_autoDrawable.getWidth(), p_autoDrawable.getHeight());
		m_textRenderer.setColor(1.0f, 0.2f, 0.2f, 0.8f);
		m_textRenderer.draw("xface-j Loading...", 300, 200);
		m_textRenderer.endRendering();

		m_pApp = new XFaceApplication(m_canvas, m_dictionaries);
		m_pCamera = new ModelCamera();
		
		m_pApp.onLoadFDP(m_face + ".fdp", m_faceRoot, m_gl);
		m_pCamera.setAxisAngle(m_pApp.m_pFace.getFDP().getGlobalAxisAngle(), m_gl);
		m_pCamera.setTranslation(m_pApp.m_pFace.getFDP().getGlobalTranslation());
		
		m_pApp.onLoadPHO(m_phoFile, m_language);
		
		m_gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		m_sound = new XFaceSound(m_soundfile);
		m_soundThread = new Thread(m_sound, "sound");
		
		m_animThread = new Thread("playback")
		{
			public void run()
			{
				{
					m_pApp.onResumePlayback(m_gl, m_sound);
					m_pApp.onStopPlayback(m_gl);
					try {
						Runtime.getRuntime().exec("pkill record-screen*");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
		m_animThread.start();
		m_soundThread.start();
		// execute screen recording
		executeRecording(m_animationFile);
				
	}

	public void executeRecording(String filename) {
	    try {
	        record_process = new ProcessBuilder("./record-screen.sh", filename).start();
	        errorGobbler = new StreamGobbler(record_process.getErrorStream(), "ERROR");
	        errorGobbler.start();
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public void reshape(GLAutoDrawable p_autoDrawable, int p_x, int p_y, int p_width, int p_height)
	{
		/*
		 * event
		 */
		//final GL gl = p_autoDrawable.getGL();
		p_autoDrawable.setGL(new DebugGL(p_autoDrawable.getGL()));
		m_gl = p_autoDrawable.getGL();
		
		final GLU glu = new GLU();
		
		m_gl.glViewport(0, 0, p_width, p_height);
		m_gl.glMatrixMode(GL.GL_PROJECTION);
		m_gl.glLoadIdentity();
	    
		float ratio = (float)p_width / (float)p_height;
		glu.gluPerspective(30.0, ratio, 10.0, 1000.0);
		m_gl.glMatrixMode(GL.GL_MODELVIEW);

		// Camera creation takes place here
		if(m_pCamera == null)
		{
			m_pCamera = new ModelCamera();
			m_pCamera.setScreenSize(p_width, p_height);
			m_pCamera.setDistance(-700);
			m_pCamera.setMode(ModelCamera.kMODE.ZOOM);
		}
		else
		{
			m_pCamera.setScreenSize(p_width, p_height);
			m_pCamera.apply(m_gl);
		}
    }

}
