package ru.smirnov.musicplatform.util;

import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

@Component
public class TransactionUtil {

    public TransactionSynchronization registerAfterCommitSynchronization(Actionable actionable) {

        return new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                actionable.action();
            }
        };

    }

}
