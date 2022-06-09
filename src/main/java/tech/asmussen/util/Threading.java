package tech.asmussen.util;

import java.util.ArrayList;

/**
 * A utility class for managing threads.
 *
 * @author Bastian A. W. Asmussen (BastianA)
 * @version 1.0.0
 * @see #THREAD_POOL
 * @see #startThreads()
 * @see #stopThreads()
 * @see #createThread(Runnable)
 * @see #deleteThread(Thread)
 */
public class Threading {
	
	/**
	 * The list of threads that are currently running.
	 */
	public static final ArrayList<Thread> THREAD_POOL = new ArrayList<>();
	
	/**
	 * Starts all the threads in the {@link #THREAD_POOL}.
	 */
	public static void startThreads() {
		
		for (Thread thread : THREAD_POOL)
			
			thread.start();
	}
	
	/**
	 * Stops all the threads in the {@link #THREAD_POOL}.
	 */
	public static void stopThreads() {
		
		for (Thread thread : THREAD_POOL)
			
			thread.interrupt();
	}
	
	/**
	 * Creates a thread from a runnable and adds it to the {@link #THREAD_POOL}.
	 *
	 * @param runnable The runnable to run in the thread.
	 */
	public static void createThread(Runnable runnable) {
		
		THREAD_POOL.add(new Thread(runnable));
	}
	
	/**
	 * Remove a thread from the {@link #THREAD_POOL}.
	 *
	 * @param thread The thread to remove.
	 */
	public static void deleteThread(Thread thread) {
		
		THREAD_POOL.remove(thread);
	}
	
	/**
	 * Clears the {@link #THREAD_POOL}.
	 */
	public static void clearThreads() {
		
		THREAD_POOL.clear();
	}
}
