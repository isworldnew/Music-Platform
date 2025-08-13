package ru.smirnov.musicplatform.entity.auxiliary.enums;

public enum AccountStatus {

    ENABLED {
        @Override
        public boolean isEnabled() {
            return true;
        }
    },

    DISABLED {
        @Override
        public boolean isEnabled() { return false; }
    };

    public abstract boolean isEnabled();

}
