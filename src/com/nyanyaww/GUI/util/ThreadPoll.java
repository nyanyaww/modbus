package com.nyanyaww.GUI.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoll
{
	private static ExecutorService service = Executors.newCachedThreadPool();
	
	public static void execute(Runnable runnable) {
		service.execute(runnable);
	}
}
