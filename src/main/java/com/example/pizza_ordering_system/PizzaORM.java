package com.example.pizza_ordering_system;

public class PizzaORM {
    private int pidORM;
    private String nameORM;
    private int phoneORM;


    private String pizzaSizeORM;
    private int toppingsORM;
    private double totalORM;

    public PizzaORM(int pidORM, String nameORM, int phoneORM, String pSizeORM, int toppingsORM) {
        this.pidORM = pidORM;
        this.nameORM = nameORM;
        this.phoneORM = phoneORM;
        this.pizzaSizeORM = pSizeORM;
        this.toppingsORM = toppingsORM;

    }

    public int getPidORM() {
        return pidORM;
    }

    public void setPidORM(int pidORM) {
        this.pidORM = pidORM;
    }

    public String getNameORM() {
        return nameORM;
    }

    public void setNameORM(String nameORM) {
        this.nameORM = nameORM;
    }

    public int getPhoneORM() {
        return phoneORM;
    }

    public String getPizzaSizeORM() {
        return pizzaSizeORM;
    }

    public void setPizzaSizeORM(String pizzaSizeORM) {
        this.pizzaSizeORM = pizzaSizeORM;
    }
    public void setPhoneORM(int phoneORM) {
        this.phoneORM = phoneORM;
    }


    public int getToppingsORM() {
        return toppingsORM;
    }

    public void setToppingsORM(int toppingsORM) {
        this.toppingsORM = toppingsORM;
    }

    public double getTotalORM() {
        return totalORM;
    }

    public void setTotalORM(double totalORM) {
        this.totalORM = totalORM;
    }

}
