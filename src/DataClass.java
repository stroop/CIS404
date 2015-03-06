/*
  DataClass.java
  Jacob Stroop
  Bellevue University
  CIS 404 - Assignment 1.2
  February 20, 2015
  A simple inner class to represent the data
*/

class DataClass{
    String name;
    String address;
    String city;
    String state;
    String zipCode;

    /* replace default constructor */
    DataClass() {
        super();
    }
    DataClass(String name, String address, String city, String state, String zipCode) {
        this();
        setName(name);
        setAddress(address);
        setCity(city);
        setState(state);
        setZip(zipCode);
    }
    // Accessors and Mutators:
    void setName(String nm) {
        this.name = nm;
    }
    String getName() {
        return this.name;
    }
    void setAddress(String addr) {
        this.address = addr;
    }
    String getAddress() {
        return this.address;
    }
    void setCity(String cty) {
        this.city = cty;
    }
    String getCity() {
        return this.city;
    }
    void setState(String st) {
        this.state = st;
    }
    String getState() {
        return this.state;
    }
    void setZip(String zp) {
        this.zipCode = zp;
    }
    String getZip() {
        return this.zipCode;
    }
}