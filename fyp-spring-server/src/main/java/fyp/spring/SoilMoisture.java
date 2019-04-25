/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyp.spring;

/**
 *
 * @author tamseelong
 */
public class SoilMoisture {
    
    private int moisture;
    private String time;

    public SoilMoisture(int moisture, String time) {
        this.moisture = moisture;
        this.time = time;
    }
     

    public int getMoisture() {
        return moisture;
    }

    public void setMoisture(int moisture) {
        this.moisture = moisture;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }   
    
}
