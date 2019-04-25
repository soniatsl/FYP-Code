/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyp.spring;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author tamseelong
 */
@RestController
public class HealthStatusController {
    
    private static final Logger log = LoggerFactory.getLogger(HealthStatusController.class);

    @Autowired
    JdbcTemplate jdbcTemplate;
    
    
    @CrossOrigin(origins = "*")
    @GetMapping("/status")
    public List<HealthStatus> getHealthStatus(){
        
        List<Light> LightList = jdbcTemplate.query(
            "SELECT * FROM Light ORDER BY time DESC LIMIT 1",
            (rs, rowNum) ->
            new Light(rs.getInt("light"), rs.getString("time"))
        );
        
        List<TempHumi> tempHumiList = jdbcTemplate.query(
            "SELECT * FROM TempHumi ORDER BY time DESC LIMIT 1",
            (rs, rowNum) ->
            new TempHumi(rs.getFloat("temp"), rs.getFloat("humi") ,rs.getString("time"))
        );
        
        List<SoilMoisture> soilMoistureList = jdbcTemplate.query(
            "SELECT * FROM soilMoisture ORDER BY time DESC LIMIT 1",
            (rs, rowNum) ->
            new SoilMoisture(rs.getInt("moisture"), rs.getString("time"))
        );
        
        int light = LightList.get(0).getLight();
        float temp = tempHumiList.get(0).getTemp();
        float humi = tempHumiList.get(0).getHumi();
        int soilMoisture = soilMoistureList.get(0).getMoisture();

        List<HealthStatus> healthStatusList = new ArrayList<>();
        healthStatusList.add(new HealthStatus(KNN(temp, humi, light, soilMoisture)));
        
        return healthStatusList;
    }
    
    
public String KNN(float temp, float humi, int light, int soilMoisture){
        String[] featuresArray = new String[5];
        String[] dataArray = new String[5];
        String[] classArray = new String[201];
        float[] tempArray = new float[201];
        float[] humiArray = new float[201];
        int[] lightArray = new int[201];
        int[] soilArray = new int[201];
        HashMap<Double, String> KNNDistance = new HashMap<>();
        String HealthStatus = "";
        
        
        try {
            
            String fileName = "datas.txt";
            ClassLoader classLoader = new HealthStatusController().getClass().getClassLoader();
//            File f = new File("src/fyp.spring/datas.txt");
            File f = new File(classLoader.getResource(fileName).getFile());
            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            int i = 0;
            while(br.readLine() != null){
                
                line = br.readLine();
//                System.out.println(line);
                if(i == 0){
                    featuresArray = line.split(" ");
                    
//                    System.out.println("First round " + featuresArray[4]);
                }
                else{
//                    System.out.println("Other round");
                    dataArray = line.split(" ");
                    classArray[i] = dataArray[4];
                    tempArray[i] = Float.parseFloat(dataArray[0]);
                    humiArray[i] = Float.parseFloat(dataArray[1]);
                    lightArray[i] = Integer.parseInt(dataArray[2]);
                    soilArray[i] = Integer.parseInt(dataArray[3]);
//                    System.out.println(classArray[i]);
                }
                i++;
            }
            
            double k_double = Math.sqrt(classArray.length)/2;
//            System.out.println(classArray.length);
            int k_int = Math.round((float)k_double);
//            System.out.println(k_int);
            if (k_int % 2 == 0){
                k_int--;
            }          
//            System.out.println(k_int);
            
            

            for(int n = 0; n < classArray.length; n++){
                double distance = Math.sqrt(
                                Math.pow(tempArray[n] - temp, 2) 
                            + Math.pow(humiArray[n] - humi, 2) 
                            + Math.pow(lightArray[n] - light, 2) 
                            + Math.pow(soilArray[n] - soilMoisture, 2) 
                            );
//                System.out.println(classArray[n] + " " + distance);
                KNNDistance.put(distance, classArray[n]);
                
                log.debug("Calculating distances" + distance);

                
            }
            
//            System.out.println(KNNDistance);
            
            List sortedKeys = new ArrayList(KNNDistance.keySet());
            Collections.sort(sortedKeys);
            
//            System.out.println(sortedKeys);
            
            int good = 0;
            int bad = 0;
            for(int k = 0; k < k_int; k ++){
//                System.out.println(KNNDistance.get(sortedKeys.get(k)));
                String status = KNNDistance.get(sortedKeys.get(k));
                if(status.equals("Good"))
                    good++;
                else
                    bad++;
            }
            
            if(good > bad){
//                System.out.println("Status: good");
                HealthStatus = "good";
                
                log.debug("Health status: " + HealthStatus);
                
                return HealthStatus;
            }
            else{
//                System.out.println("Status: bad");
                HealthStatus = "bad";
                
                log.debug("Health status: " + HealthStatus);
                
                return HealthStatus;
            }
            
            
            
        } catch (IOException ex) {
            log.error("IOException!");
        }
        
        log.debug("Finish function " + HealthStatus);
        
        return "";
        
    }
    
    
        
}
