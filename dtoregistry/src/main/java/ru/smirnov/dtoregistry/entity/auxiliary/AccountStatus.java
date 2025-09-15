package ru.smirnov.dtoregistry.entity.auxiliary;

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