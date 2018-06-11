package com.oneassist.serviceplatform.commons.exception;

public class InputValidationException extends Exception {


    /**
	 * 
	 */
	private static final long serialVersionUID = -1159103547126470280L;
	
	/**
     * the message of the PincodeServiceFulfilmentMasterException.
     */
    private String exceptionMessage;

    /**
     * A public constructor for PincodeServiceFulfilmentMasterException containing no arguments.
     *  
     */
    public InputValidationException() {
    }

    /**
     * A public constructor for PincodeServiceFulfilmentMasterException specifying exception message.
     * <p>
     * 
     * @param msg
     *            exception message.
     *  
     */
    public InputValidationException(String msg) {
        this.exceptionMessage = msg;
    }

    /**
     * A public constructor of <code>PincodeServiceFulfilmentMasterException</code> containing
     * message and root cause (as <code>Throwable</code>) of the exception.
     * 
     * @param msg
     *            exception message.
     * @param e
     *            Throwable object.
     *  
     */
    public InputValidationException(String msg, Throwable e) {
        this.exceptionMessage = msg;
        this.initCause(e);
    }

    /**
     * sets the root cause of the exception. Used for setting Java built in
     * exception in <code>PincodeServiceFulfilmentMasterException</code>.
     * 
     * @param e
     *            Throwable object.
     *  
     */
    public void setCause(Throwable e) {
        this.initCause(e);
    }

       
    /*
     * Gets the class name and exception message.
     * 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String s = getClass().getName();
        return s + ": " + exceptionMessage;
    }

    /*
     * Gets the message of the exception. equivalent to Exception.getMessage().
     * 
     * @see java.lang.Throwable#getMessage()
     */
    public String getMessage() {
        return exceptionMessage;
    }
    
    /*
     * Gets the message of the exception. equivalent to Exception.getMessage().
     * 
     * @see java.lang.Throwable#getMessage()
     */
    public String getFaultCode() {
        return exceptionMessage;
    }


}
