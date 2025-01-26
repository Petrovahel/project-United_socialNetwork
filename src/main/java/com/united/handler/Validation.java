package com.united.handler;

public abstract class Validation {
    private Validation next;

    public static Validation link(Validation first, Validation... chain) {
        Validation head = first;
        for (Validation nextInChain : chain) {
            head.next = nextInChain;
            head = nextInChain;
        }
        return first;
    }

    public abstract boolean check(Long userId, Long friendId);

    protected boolean checkNext(Long userId, Long friendId) {
        if (next == null) {
            return true;
        }
        return next.check(userId, friendId);
    }
}
