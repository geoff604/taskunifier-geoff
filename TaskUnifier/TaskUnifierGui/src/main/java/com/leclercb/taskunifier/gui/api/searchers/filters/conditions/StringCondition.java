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
package com.leclercb.taskunifier.gui.api.searchers.filters.conditions;

import java.text.Normalizer;

import com.leclercb.taskunifier.gui.api.accessor.PropertyAccessor;

public enum StringCondition implements Condition<String, Object> {
	
	CONTAINS,
	DOES_NOT_CONTAIN,
	DOES_NOT_END_WITH,
	DOES_NOT_START_WITH,
	ENDS_WITH,
	EQUALS,
	NOT_EQUALS,
	STARTS_WITH;
	
	private StringCondition() {
		
	}
	
	@Override
	public Class<?> getValueType() {
		return String.class;
	}
	
	@Override
	public Class<?> getModelValueType() {
		return Object.class;
	}
	
	@Override
	public boolean include(
			PropertyAccessor<?> accessor,
			Object objectValue,
			Object objectModelValue) {
		String value = (objectValue == null ? null : objectValue.toString());
		String modelValue = (objectModelValue == null ? null : objectModelValue.toString());
		
		if (value == null)
			value = "";
		
		if (modelValue == null)
			modelValue = "";
		
		String string = value.toLowerCase();
		String taskString = modelValue.toString().toLowerCase();
		
		string = Normalizer.normalize(string, Normalizer.Form.NFD);
		taskString = Normalizer.normalize(taskString, Normalizer.Form.NFD);
		
		switch (this) {
			case CONTAINS:
				return taskString.contains(string);
			case DOES_NOT_CONTAIN:
				return !taskString.contains(string);
			case DOES_NOT_END_WITH:
				return !taskString.endsWith(string);
			case DOES_NOT_START_WITH:
				return !taskString.startsWith(string);
			case ENDS_WITH:
				return taskString.endsWith(string);
			case EQUALS:
				return taskString.equals(string);
			case NOT_EQUALS:
				return !taskString.equals(string);
			case STARTS_WITH:
				return taskString.startsWith(string);
		}
		
		return false;
	}
	
}
