/*
 * TaskUnifier
 * Copyright (c) 2013, Benjamin Leclerc
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 * 
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 * 
 *   - Neither the name of TaskUnifier or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.leclercb.taskunifier.gui.threads.communicator;

import java.io.IOException;
import java.net.ServerSocket;

import com.leclercb.commons.gui.logger.GuiLogger;
import com.leclercb.taskunifier.gui.main.Main;

public class CommunicatorThread extends Thread {
	
	private int port;
	private ThreadGroup group;
	
	public CommunicatorThread() {
		super("TU_CommunicatorThread");
		
		this.port = Main.getSettings().getIntegerProperty(
				"general.communicator.port");
		this.group = new ThreadGroup("CommunicatorGroup");
	}
	
	@Override
	public void interrupt() {
		try {
			this.group.interrupt();
		} catch (Throwable t) {
			
		}
		
		if (this.isAlive())
			GuiLogger.getLogger().info(
					"Communicator closed on port " + this.port);
		
		super.interrupt();
	}
	
	@Override
	public void run() {
		ServerSocket serverSocket = null;
		
		try {
			serverSocket = new ServerSocket(this.port);
			
			GuiLogger.getLogger().info(
					"Communicator initialized on port " + this.port);
			
			while (!this.isInterrupted()) {
				CommunicatorClient client = new CommunicatorClient(
						this.group,
						serverSocket.accept());
				
				client.start();
			}
		} catch (Exception e) {
			GuiLogger.getLogger().warning(
					"Cannot initialize communicator on port " + this.port);
		} finally {
			try {
				if (serverSocket != null)
					serverSocket.close();
			} catch (IOException e) {
				GuiLogger.getLogger().warning(
						"Cannot close communicator on port " + this.port);
			}
		}
	}
	
}
