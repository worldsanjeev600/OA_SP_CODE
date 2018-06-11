/**
 * 
 */
package com.oneassist.serviceplatform.externalcontracts.ecom;

/**
 * @author Savita.kodli
 *
 */
public class PincodeAWBRequest {

    private String userName;
    private String passWrod;
    private String pinCode;
    private int count;
    private String type;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWrod() {
        return passWrod;
    }

    public void setPassWrod(String passWrod) {
        this.passWrod = passWrod;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "PincodeTest [userName=" + userName + ", passWrod=" + passWrod + ", pinCode=" + pinCode + "]";
    }

}
