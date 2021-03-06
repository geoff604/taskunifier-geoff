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
package com.leclercb.taskunifier.gui.commons.values;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jdesktop.swingx.renderer.StringValue;

import com.leclercb.taskunifier.gui.utils.BackupUtils;

public class StringValueBackup implements StringValue {
	
	public static final StringValueBackup INSTANCE = new StringValueBackup();
	
	private SimpleDateFormat format;
	
	private StringValueBackup() {
		this.format = new SimpleDateFormat("yyyyMMdd_HHmmss");
	}
	
	@Override
	public String getString(Object value) {
		if (value == null || !(value instanceof String))
			return " ";
		
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(this.format.parse((String) value));
			
			String backupName = BackupUtils.getInstance().getBackupName(
					(String) value);
			
			if (backupName == null || backupName.length() == 0)
				return StringValueCalendar.INSTANCE_DATE_TIME.getString(calendar);
			
			return StringValueCalendar.INSTANCE_DATE_TIME.getString(calendar)
					+ " - "
					+ backupName;
		} catch (Exception exc) {
			return " ";
		}
	}
	
}
