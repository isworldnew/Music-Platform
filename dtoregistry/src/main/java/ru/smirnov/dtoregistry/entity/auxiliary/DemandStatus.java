package ru.smirnov.dtoregistry.entity.auxiliary;

// из:
// package ru.smirnov.demandservice.entity.auxiliary;

public enum DemandStatus {

    RECEIVED(false, false),
    IN_PROGRESS(false, false),
    COMPLETED(true, true),
    DENIED(true, false);

    private final boolean isModifying;
    private final boolean ableToRegisterDistributor;

    public boolean isModifying() {
        return this.isModifying;
    }

    public boolean isAbleToRegisterDistributor() { return this.ableToRegisterDistributor; }

    DemandStatus(boolean isModifying, boolean ableToRegisterDistributor) {
        this.isModifying = isModifying;
        this.ableToRegisterDistributor = ableToRegisterDistributor;
    }
}
