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
package com.leclercb.taskunifier.gui.components.change_data_folder;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.logging.Level;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;

import org.apache.commons.io.FileUtils;
import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;

import com.leclercb.taskunifier.gui.actions.ActionQuit;
import com.leclercb.taskunifier.gui.actions.ActionSave;
import com.leclercb.taskunifier.gui.main.Main;
import com.leclercb.taskunifier.gui.main.frames.FrameUtils;
import com.leclercb.taskunifier.gui.swing.TUDialogPanel;
import com.leclercb.taskunifier.gui.swing.TUFileField;
import com.leclercb.taskunifier.gui.swing.buttons.TUCancelButton;
import com.leclercb.taskunifier.gui.translations.Translations;

public class ChangeDataFolderDialogPanel extends TUDialogPanel {
	
	private static ChangeDataFolderDialogPanel INSTANCE;
	
	protected static ChangeDataFolderDialogPanel getInstance() {
		if (INSTANCE == null)
			INSTANCE = new ChangeDataFolderDialogPanel();
		
		return INSTANCE;
	}
	
	private TUFileField fileField;
	private JCheckBox copyData;
	
	private ChangeDataFolderDialogPanel() {
		this.initialize();
	}
	
	private void initialize() {
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		// Data folder
		this.fileField = new TUFileField(
				Translations.getString("general.folder"),
				true,
				null,
				JFileChooser.DIRECTORIES_ONLY,
				null,
				null);
		
		this.add(this.fileField, BorderLayout.NORTH);
		
		// Copy data
		this.copyData = new JCheckBox(
				Translations.getString("change_data_folder_location.copy_data"));
		
		this.add(this.copyData, BorderLayout.SOUTH);
		
		this.initializeButtonsPanel();
	}
	
	private void initializeButtonsPanel() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent event) {
				boolean quit = false;
				
				if (event.getActionCommand().equals("RESET")) {
					quit = true;
					
					Main.getInitSettings().remove(
							"com.leclercb.taskunifier.data_folder");
				}
				
				if (event.getActionCommand().equals("CHANGE")) {
					quit = true;
					
					if (!ChangeDataFolderDialogPanel.this.changeDataFolder())
						return;
				}
				
				ChangeDataFolderDialogPanel.this.fileField.setFile(null);
				ChangeDataFolderDialogPanel.this.getDialog().setVisible(false);
				
				if (quit) {
					ActionQuit.quitAndApply();
				}
			}
			
		};
		
		JButton resetButton = new JButton(
				Translations.getString("general.reset_default"));
		resetButton.setActionCommand("RESET");
		resetButton.addActionListener(listener);
		
		JButton importButton = new JButton(
				Translations.getString("action.change_data_folder_location"));
		importButton.setActionCommand("CHANGE");
		importButton.addActionListener(listener);
		
		JButton cancelButton = new TUCancelButton(listener);
		
		this.setButtons(importButton, resetButton, importButton, cancelButton);
	}
	
	private boolean changeDataFolder() {
		try {
			String path = this.fileField.getFile();
			
			if (path == null || path.length() == 0)
				throw new Exception();
			
			File file = new File(path);
			
			if (!file.exists() || !file.isDirectory())
				throw new Exception();
			
			Main.getInitSettings().setStringProperty(
					"com.leclercb.taskunifier.data_folder",
					file.getAbsolutePath());
			
			ActionSave.save();
			
			if (this.copyData.isSelected())
				FileUtils.copyDirectory(new File(Main.getDataFolder()), file);
			
			return true;
		} catch (Exception e) {
			ErrorInfo info = new ErrorInfo(
					Translations.getString("general.error"),
					Translations.getString("error.not_valid_directory"),
					null,
					"GUI",
					null,
					Level.INFO,
					null);
			
			JXErrorPane.showDialog(FrameUtils.getCurrentWindow(), info);
			
			return false;
		}
	}
	
	@Override
	protected void dialogLoaded() {
		this.getDialog().addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				File file = new File(Main.getDataFolder());
				ChangeDataFolderDialogPanel.this.fileField.setFile(file.getAbsolutePath());
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				ChangeDataFolderDialogPanel.this.fileField.setFile(null);
				ChangeDataFolderDialogPanel.this.getDialog().setVisible(false);
			}
			
		});
	}
	
}
