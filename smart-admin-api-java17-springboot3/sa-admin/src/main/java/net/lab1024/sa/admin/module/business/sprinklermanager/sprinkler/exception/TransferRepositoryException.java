package net.lab1024.sa.admin.module.business.sprinklermanager.sprinkler.exception;

import net.lab1024.sa.base.common.code.ErrorCode;

public class TransferRepositoryException extends RuntimeException {

    public TransferRepositoryException() {
    }

    public TransferRepositoryException(ErrorCode errorCode) {
        super(errorCode.getMsg());
    }

    public TransferRepositoryException(String message) {
        super(message);
    }

    public TransferRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransferRepositoryException(Throwable cause) {
        super(cause);
    }

    public TransferRepositoryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
