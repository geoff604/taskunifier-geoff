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
package com.leclercb.taskunifier.gui.actions;

import java.awt.event.ActionEvent;

import com.leclercb.taskunifier.gui.api.searchers.TaskSearcher;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherFactory;
import com.leclercb.taskunifier.gui.api.searchers.TaskSearcherType;
import com.leclercb.taskunifier.gui.components.views.ViewType;
import com.leclercb.taskunifier.gui.components.views.ViewUtils;
import com.leclercb.taskunifier.gui.constants.Constants;
import com.leclercb.taskunifier.gui.translations.Translations;
import com.leclercb.taskunifier.gui.utils.ImageUtils;

public class ActionAddTaskSearcher extends AbstractViewAction {
	
	public ActionAddTaskSearcher(int width, int height) {
		super(
				Translations.getString("action.add_task_searcher"),
				ImageUtils.getResourceImage("add.png", width, height),
				ViewType.CALENDAR,
				ViewType.TASKS);
		
		this.putValue(
				SHORT_DESCRIPTION,
				Translations.getString("action.add_task_searcher"));
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		ActionAddTaskSearcher.addTaskSearcher();
	}
	
	public static void addTaskSearcher() {
		TaskSearcher defaultTaskSearcher = Constants.getDefaultTaskSearcher();
		
		TaskSearcher searcher = TaskSearcherFactory.getInstance().create(
				TaskSearcherType.PERSONAL,
				null,
				Integer.MAX_VALUE,
				Translations.getString("searcher.default.title"),
				defaultTaskSearcher.getFilter(),
				defaultTaskSearcher.getSorter(),
				defaultTaskSearcher.getGrouper());
		
		ViewType type = ViewUtils.getCurrentViewType();
		
		if (type == null)
			return;
		
		switch (type) {
			case CALENDAR:
				ViewUtils.getCurrentCalendarView().getTaskSearcherView().selectTaskSearcher(
						searcher);
				break;
			case NOTES:
				break;
			case TASKS:
				ViewUtils.getCurrentTaskView().getTaskSearcherView().selectTaskSearcher(
						searcher);
				break;
		}
		
		ActionEditTaskSearcher.editTaskSearcher(searcher);
	}
	
}
