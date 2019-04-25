/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyp.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.sql.DataSource;
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
public class TempHumiController {
    
     private static final Logger log = LoggerFactory.getLogger(TempHumiController.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    DataSource datasource;
    
    @CrossOrigin(origins = "*")
    @PostMapping("/post-temp-humi/{temp}/{humi}")
    
    public void postLight(@PathVariable("temp") float temp, @PathVariable("humi") float humi){
        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
         
        TempHumi tempHumi = new TempHumi(temp, humi, currentTime);

        jdbcTemplate.update("INSERT INTO TempHumi (temp, humi, time) VALUES (" 
                + tempHumi.getTemp() + ", "
                + tempHumi.getHumi() +
                ",\"" + tempHumi.getTime() + "\")");   
        
//        String updateStatement = "INSERT INTO TempHumi (temp, humi, time) VALUES ( ? , ? , ? )";   
        
//        try {
//            
//            Connection conn = datasource.getConnection();
//            PreparedStatement ps = conn.prepareStatement(updateStatement);
//            ps.setFloat(1, tempHumi.getTemp());
//            ps.setFloat(2, tempHumi.getHumi());
//            ps.setString(3, tempHumi.getTime());
//            
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException ex) {
//            java.util.logging.Logger.getLogger(SoilMoistureController.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/temp-humi")
    public List<TempHumi> getTempHumi(){
        
        List<TempHumi> tempHumiList = jdbcTemplate.query(
                "SELECT * FROM TempHumi",
                (rs, rowNum) ->
                new TempHumi(rs.getFloat("temp"), rs.getFloat("humi") ,rs.getString("time"))
        );
        
        return tempHumiList;
    }
    
    
}
