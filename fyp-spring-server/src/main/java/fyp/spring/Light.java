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
public class Light {
    private int light;
    private String time;

    public Light(int light, String time) {
        this.light = light;
        this.time = time;
    }

    public int getLight() {
        return light;
    }

    public String getTime() {
        return time;
    }

    public void setLight(int light) {
        this.light = light;
    }

    public void setTime(String time) {
        this.time = time;
    }


    
}
