package com.fyxridd.lib.input.api;

public interface InputHandler {
    /**
     * 玩家输入时调用
     * @param s 输入的内容
     * @return 返回true会删除注册,false不会
     */
    boolean onInput(String s);
}