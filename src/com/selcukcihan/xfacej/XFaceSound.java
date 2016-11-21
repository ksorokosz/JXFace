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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import sun.audio.*;


public class XFaceSound implements Runnable
{	
	public XFaceSound(String filename)
	{
		this.filename = filename;
	}
	public volatile boolean m_play = false;
	private String filename;

	public void run()
	{		
		try
		{
			InputStream in = new FileInputStream(this.filename);
			
			// create an audiostream from the inputstream
		    AudioStream audioStream = new AudioStream(in);

		    // play the audio clip with the audioplayer class
		    AudioPlayer.player.start(audioStream);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

}
