package ru.smirnov.musicplatform.entity.auxiliary.enums;

public enum DistributionStatus {

    ACTIVE(false),
    SUSPENDED_BY_DISTRIBUTOR(true),
    SUSPENDED_BY_ADMIN(true);

    private final boolean isDeactivating;

    public boolean isDeactivating() { return this.isDeactivating; }

    DistributionStatus(boolean isDeactivating) {
        this.isDeactivating = isDeactivating;
    }
}
