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


//    @JsonCreator
//    public static AccountStatus fromString(String value) {
//        return value == null ? null : AccountStatus.valueOf(value.toUpperCase());
//    }
//
//    @JsonValue
//    public String toJson() {
//        return this.name().toLowerCase();
//    }
}