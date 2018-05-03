package com.android.development.thread;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import android.content.Context;

/**�̳߳ص�����*/
public class ThreadTaskTool {
	private static final String TAG = ThreadTaskTool.class.getSimpleName();

	private volatile static ThreadTaskTool instance = new ThreadTaskTool();
	
	//����ִ��
	private ExecutorService executorService;
	
	private ThreadTaskTool(){
		initThreadPool();
	}
	
	/** 
	 * 
	 *  */
	public static ThreadTaskTool getInstance(){
		if (instance == null) {
			synchronized (ThreadTaskTool.class) {
				if (instance == null) {
					instance = new ThreadTaskTool();
				}
			}
		}
		return instance;
	}
	
	
	private void initThreadPool(){
		if(executorService == null || executorService.isShutdown()){
			int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
			int KEEP_ALIVE_TIME = 1;
			TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
			BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<Runnable>();
			executorService = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES * 2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue);
			/*executorService = new ThreadPoolExecutor(NUMBER_OF_CORES, NUMBER_OF_CORES*2, KEEP_ALIVE_TIME, KEEP_ALIVE_TIME_UNIT, taskQueue, 
					new BackgroundThreadFactory(), new DefaultRejectedExecutionHandler());*/
		}
	}
	
	/**�ύ����*/
	/*public void executeTask(Runnable runnable) throws RejectedExecutionException{
		initThreadPool();
		if(runnable != null){
			executorService.execute(runnable);
		}
	}*/
	
	/**�ύ����*/
	public void executeTask(Runnable runnable){
		initThreadPool();
		if(runnable != null){
			executorService.execute(runnable);
		}
	}
	
	/*public void executeTask(ThreadTaskInfo taskInfo){
		initThreadPool();
		if(taskInfo != null && taskInfo.runnable != null){
			executorService.execute(taskInfo.runnable);
		}
	}*/
	
	private void initExecutorsIfNeed() {
		if (executorService == null || executorService.isShutdown()) {
			initThreadPool();
		}
	}
	
	/**ֹͣ��������*/
	public static void stop(){
		if(instance != null){
			instance.stopAllTask();
		}
	}
	
	private void stopAllTask(){
		if(executorService != null){
			executorService.shutdownNow();
		}
	}
	
	/**�ͷ���Դ*/
	public static void release(){
		if(instance != null){
			instance.releaseThreadPool();
		}
	}
	
	private void releaseThreadPool(){
		if(executorService != null){
			executorService.shutdownNow();
		}
		executorService = null;
	}
}
