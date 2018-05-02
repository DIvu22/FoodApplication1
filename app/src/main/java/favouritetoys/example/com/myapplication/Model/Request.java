package favouritetoys.example.com.myapplication.Model;

import java.util.List;

/**
 * Created by Divya Gupta on 24-04-2018.
 */

public class Request {
    private String paymentState;
    private String phone;
    private String name;
    private String address;
    private String total;

    private String status;
    private List<Order> foods;//list of food orders

    public Request() {
    }

    public Request(String paymentState, String phone, String name, String address, String total, List<Order> foods) {
        this.paymentState = paymentState;
        this.phone = phone;
        this.name = name;
        this.address = address;
        this.total = total;
        this.foods = foods;
        this.status = "0";
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Order> getFoods() {
        return foods;
    }

    public void setFoods(List<Order> foods) {
        this.foods = foods;
    }
}


