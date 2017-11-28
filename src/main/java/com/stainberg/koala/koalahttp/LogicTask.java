package com.stainberg.koala.koalahttp;

/**
 * Created by Stainberg on 12/9/15.
 */
abstract class LogicTask implements Runnable {

    String taskMsgId;
    RequestLogic logic;

    LogicTask(String taskMsgId, RequestLogic logic) {
        this.taskMsgId = taskMsgId;
        this.logic = logic;
    }

    protected abstract void execute();

    @Override
    public void run() {
        execute();
        ExecuteHelper.getLogicHelper().finish(taskMsgId);
    }
}
