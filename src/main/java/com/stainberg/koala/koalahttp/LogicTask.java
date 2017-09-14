package com.stainberg.koala.koalahttp;

/**
 * Created by Stainberg on 12/9/15.
 */
abstract class LogicTask implements Runnable {

    String taskMsgId;

    LogicTask(String taskMsgId) {
        this.taskMsgId = taskMsgId;
    }

    protected abstract void execute();

    @Override
    public void run() {
        execute();
        ExecuteHelper.getLogicHelper().finish(taskMsgId);
    }
}
