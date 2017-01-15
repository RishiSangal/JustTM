/**
 * Copyright (c) 2013, Redsolution LTD. All rights reserved.
 * 
 * This file is part of Xabber project; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License, Version 3.
 * 
 * Xabber is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License,
 * along with this program. If not, see http://www.gnu.org/licenses/.
 */
package com.android.encypher.justtrackme.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.WeakHashMap;

/**
 * Activity stack manager.
 * 
 * @author alexander.ivanov
 * 
 */
public class ActivityManager implements OnUnloadListener {

	private static final String EXTRA_TASK_INDEX = "com.xabber.android.data.ActivityManager.EXTRA_TASK_INDEX";

	private static final boolean LOG = true;


	/**
	 * List of launched activities.
	 */
	private final ArrayList<Activity> activities;

	/**
	 * Next index of task.
	 */
	private int nextTaskIndex;

	/**
	 * Activity with index of it task.
	 */
	private final WeakHashMap<Activity, Integer> taskIndexes;

	/**
	 * Listener for errors.
	 */

	private final static ActivityManager instance;

	static {
		instance = new ActivityManager();
	}

	public static ActivityManager getInstance() {
		return instance;
	}

	private ActivityManager() {
		activities = new ArrayList<Activity>();
		nextTaskIndex = 0;
		taskIndexes = new WeakHashMap<Activity, Integer>();
	}

	/**
	 * Removes finished activities from stask.
	 */
	private void rebuildStack() {
		Iterator<Activity> iterator = activities.iterator();
		while (iterator.hasNext())
			if (iterator.next().isFinishing())
				iterator.remove();
	}

	/**
	 * Finish all activities in stack till the root contactmale list.
	 * 
	 *            also finish root contactmale list.
	 */
	public void clearStack() {
		rebuildStack();
		for (Activity activity : activities) 
			activity.finish();
		rebuildStack();
	}



	/**
	 * Push activity to stack.
	 * 
	 * Must be called from {@link Activity#onCreate(Bundle)}.
	 * 
	 * @param activity
	 */
	public void onCreate(Activity activity) {
		activities.add(activity);
		rebuildStack();
		fetchTaskIndex(activity, activity.getIntent());
	}

	/**
	 * Pop activity from stack.
	 * 
	 * Must be called from {@link Activity#onDestroy()}.
	 * 
	 * @param activity
	 */
	public void onDestroy(Activity activity) {
		activities.remove(activity);
	}

	/**
	 * Pause activity.
	 * 
	 * Must be called from {@link Activity#onPause()}.
	 * 
	 * @param activity
	 */
	public void onPause(Activity activity) {
	}

	/**
	 * Resume activity.
	 * 
	 * Must be called from {@link Activity#onResume()}.
	 * 
	 * @param activity
	 */
	public void onResume(final Activity activity) {

	}

	/**
	 * New intent received.
	 * 
	 * Must be called from {@link Activity#onNewIntent(Intent)}.
	 * 
	 * @param activity
	 * @param intent
	 */
	public void onNewIntent(Activity activity, Intent intent) {
	}

	/**
	 * Result has been received.
	 * 
	 * Must be called from {@link Activity#onActivityResult(int, int, Intent)}.
	 * 
	 * @param activity
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(Activity activity, int requestCode,
			int resultCode, Intent data) {
	}

	/**
	 * Adds task index to the intent if specified for the source activity.
	 * 
	 * Must be used when source activity starts new own activity from
	 * {@link Activity#startActivity(Intent)} and
	 * {@link Activity#startActivityForResult(Intent, int)}.
	 * 
	 * @param source
	 * @param intent
	 */
	public void updateIntent(Activity source, Intent intent) {
		Integer index = taskIndexes.get(source);
		if (index == null)
			return;
		intent.putExtra(EXTRA_TASK_INDEX, index);
	}

	/**
	 * Mark activity to be in separate activity stack.
	 * 
	 * @param activity
	 */
	public void startNewTask(Activity activity) {
		taskIndexes.put(activity, nextTaskIndex);
		nextTaskIndex += 1;
	}

	/**
	 * Either move main task to back, either close all activities in subtask.
	 * 
	 * @param activity
	 */
	public void cancelTask(Activity activity) {
		Integer index = taskIndexes.get(activity);
		if (index == null) {
			activity.moveTaskToBack(true);
		} else {
			for (Entry<Activity, Integer> entry : taskIndexes.entrySet())
				if (entry.getValue() == index)
					entry.getKey().finish();
		}
	}

	/**
	 * Fetch task index from the intent and mark specified activity.
	 * 
	 * @param activity
	 * @param intent
	 */
	private void fetchTaskIndex(Activity activity, Intent intent) {
		int index = intent.getIntExtra(EXTRA_TASK_INDEX, -1);
		if (index == -1)
			return;
		taskIndexes.put(activity, index);
	}

	@Override
	public void onUnload() {
//		clearStack(true);
	}

}
