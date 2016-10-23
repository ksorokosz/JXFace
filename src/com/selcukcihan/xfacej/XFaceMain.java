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

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.media.opengl.*;
import com.sun.opengl.util.Animator;

public class XFaceMain extends Frame
{
	protected GLCanvas canvas;
	protected Animator animator;
	
	private int m_Width;
	private int m_Height;
	public boolean load(String filename)
	{
		try
		{
			File file = new File("filename");
			BufferedImage bufferedImage = ImageIO.read(file);
			m_Width = bufferedImage.getWidth();
			m_Height = bufferedImage.getHeight();
			
			int [] m_rgbArray = new int[m_Width*m_Height];
			

			bufferedImage.getRGB(0, 0, m_Width, m_Height, m_rgbArray, 0, m_Width);
			
			//System.arraycopy(arg0, arg1, arg2, arg3, arg4)
			//m_pData = bufferedImage
		}
		catch(IOException ioe)
		{
			ioe.printStackTrace();
		}
		
		return false;
	}

	public static void main(String[] args)
	{
		int argc = 0;
		XFaceConfiguration config = new XFaceConfiguration(args[argc++], args[argc++]);
		String pho = args[argc++];
		String sound = args[argc++];
		
		Frame frame = new Frame("xface-j");
		frame.setSize(800, 600);
		frame.setLayout(new BorderLayout());
	    GLCanvas canvas = new GLCanvas();

	    canvas.addGLEventListener((GLEventListener) new XFaceDriver(canvas, config, pho, sound));
	    canvas.setAutoSwapBufferMode(true);
	    frame.add(canvas, BorderLayout.NORTH);
	    
	    final Animator animator = new Animator(canvas);
	    frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                System.exit(0);
	              }
	            }).start();
	        }
	      });
	    frame.setVisible(true);
	    frame.setExtendedState(Frame.NORMAL);
	    canvas.setSize(frame.getSize());
	    canvas.validate();
	    animator.setRunAsFastAsPossible(true);
	    animator.start();
	}

}
