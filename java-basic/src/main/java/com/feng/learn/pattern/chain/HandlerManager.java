package com.feng.learn.pattern.chain;

import java.util.LinkedList;

/**
 * @author zhanfeng.zhang
 * @date 2021/01/14
 */
public class HandlerManager extends AbstractHandler {

    private final LinkedList<AbstractHandler> chain = new LinkedList<>();

    @Override
    public Object handle(Object in) {
        return handleBySuccessor(in);
    }

    public void addLast(AbstractHandler handler) {
        add(handler, chain.size());
    }

    public void addFirst(AbstractHandler handler) {
        add(handler, 0);
    }

    public void add(AbstractHandler handler, int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        if (chain.isEmpty()) {
            chain.add(handler);
            return;
        }
        if (index > chain.size()) {
            index = chain.size();
        }
        if (index < chain.size()) {
            handler.setSuccessor(chain.get(index));
        }
        int prev = index - 1;
        if (prev >= 0) {
            chain.get(prev).setSuccessor(handler);
        }
        chain.add(index, handler);
        setSuccessor(chain.get(0));
    }


}
