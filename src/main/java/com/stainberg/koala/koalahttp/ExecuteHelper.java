package com.stainberg.koala.koalahttp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.internal.Util;

/**
 * Created by Stainberg on 12/9/15.
 */
public class ExecuteHelper {

    private static ExecuteHelper instance = null;
    private ExecutorService executorService;
    private ExecutorService bgExecutorService;
    private Map<String, LogicTask> tasks;

    private ExecuteHelper() {
        executorService = Executors.newCachedThreadPool(Util.threadFactory("Logic Dispatcher", false));
        bgExecutorService = Executors.newCachedThreadPool(Util.threadFactory("Background Dispatcher", false));
        tasks = new ConcurrentHashMap<>();
    }

    public static ExecuteHelper getLogicHelper() {
        if(instance == null) {
            synchronized (ExecuteHelper.class) {
                if(instance == null) {
                    instance = new ExecuteHelper();
                }
            }
        }
        return instance;
    }

    void execute(LogicTask task) {
        if(!tasks.containsKey(task.taskMsgId)) {
            tasks.put(task.taskMsgId, task);
            executorService.execute(task);
        }
    }

    public void removeAllTask(Object o) {
        for (LogicTask task : tasks.values()) {
            String parent = task.logic.listener.getClass().getName().substring(0, task.logic.listener.getClass().getName().indexOf("$"));
            if(o.getClass().getName().equals(parent)) {
                task.logic.listener = null;
                task.logic.interrupt = null;
                task.logic.request = null;
                task.logic = null;
                tasks.remove(task.taskMsgId);
                Logger.getLogger().Println("removeAllTask", "remove task = " + task.taskMsgId);
            }
        }
    }

    public void removeTask(String id) {
        for (LogicTask task : tasks.values()) {
            if(task.taskMsgId.equals(id)) {
                task.logic.listener = null;
                task.logic.interrupt = null;
                task.logic.request = null;
                task.logic = null;
                tasks.remove(task.taskMsgId);
                Logger.getLogger().Println("removeTask", "remove task = " + task.taskMsgId);
                break;
            }
        }
    }

    void finish(String taskMsgId) {
        tasks.remove(taskMsgId);
    }

    void executeOnBackground(Runnable r) {
        bgExecutorService.execute(r);
    }

}
