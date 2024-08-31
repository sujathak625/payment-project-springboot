package com.cgi.fsdc.advice;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.security.GeneralSecurityException;

@Aspect
@Component
public class ServiceExceptionAspect {

    private static final Logger logger = LoggerFactory.getLogger(ServiceExceptionAspect.class);

    @Pointcut("within(com.cgi.fsdc.service..*)")
    public void serviceLayerPointcut() {
    }

    @AfterThrowing(pointcut = "serviceLayerPointcut()", throwing = "ex")
    public void logServiceExceptions(Exception ex) {
        if (ex instanceof GeneralSecurityException) {
            logger.error("Encryption/Decryption error occurred: {}", ex.getMessage(), ex);
        } else if (ex instanceof DataAccessException) {
            logger.error("Database error occurred: {}", ex.getMessage(), ex);
        } else {
            logger.error("Exception occurred in service layer: {}", ex.getMessage(), ex);
        }
    }
}
