
package com.selcukcihan.xfacej;

import java.applet.Applet;
import java.awt.BorderLayout;
import javax.media.opengl.*;
import com.sun.opengl.util.Animator;

public class XFaceApplet extends Applet
{
	protected GLCanvas canvas;
	protected Animator animator;
	
	public void init()
	{
		XFaceConfiguration config = new XFaceConfiguration("alice", "english");
		String pho = "faces/alice/alice.pho";
		String sound = "faces/alice/alice.wav";
		
		//Frame frame = new Frame("xface-j");
		//XFaceApplet xfaceApplet = new XFaceApplet();
		//frame.add(xfaceApplet);
		setSize(640,480);
		setLayout(new BorderLayout());
		
	    GLCanvas canvas = new GLCanvas();

	    //canvas.addGLEventListener((GLEventListener) new GLTest());
	    canvas.addGLEventListener((GLEventListener) new XFaceDriver(canvas, config, pho, sound));
	    canvas.setAutoSwapBufferMode(true);
	    add(canvas, BorderLayout.NORTH);
	    //frame.add(new Label("FPS: "), BorderLayout.SOUTH);
	    
	    animator = new Animator(canvas);
	    /*frame.addWindowListener(new WindowAdapter() {
	        public void windowClosing(WindowEvent e) {
	          new Thread(new Runnable() {
	              public void run() {
	                animator.stop();
	                System.exit(0);
	              }
	            }).start();
	        }
	      });*/
	    setVisible(true);
	    //setExtendedState(Frame.MAXIMIZED_BOTH);
	    canvas.setSize(getSize());
	    canvas.validate();
	    animator.setRunAsFastAsPossible(true);
	}
	
	public void start()
	{
		animator.start();
	}
	
	public void stop()
	{
		animator.stop();
	}
	
	public void destroy()
	{
		
	}
}
