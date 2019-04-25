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
public class SoilMoistureController {
    private static final Logger log = LoggerFactory.getLogger(SoilMoistureController.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    DataSource datasource;
    
    @CrossOrigin(origins = "*")
    @PostMapping("/post-soil-moisture/{moisture}")
    
    public void postSoilMoisture(@PathVariable int moisture){
        
        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
        
        
        SoilMoisture soilMoisture = new SoilMoisture(moisture, currentTime);
        jdbcTemplate.update("INSERT INTO soilMoisture (moisture, time) VALUES (" + soilMoisture.getMoisture() + ",\"" + soilMoisture.getTime() + "\")");  
//        String updateStatement = "INSERT INTO soilMoisture (moisture, time) VALUES ( ? , ? )";   
//        
//        try {
//            
//            Connection conn = datasource.getConnection();
//            PreparedStatement ps = conn.prepareStatement(updateStatement);
//            ps.setInt(1, soilMoisture.getMoisture());
//            ps.setString(2, soilMoisture.getTime());
//            
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException ex) {
//            java.util.logging.Logger.getLogger(SoilMoistureController.class.getName()).log(Level.SEVERE, null, ex);
//        }

    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/soil-moisture")
    public List<SoilMoisture> getSoilMoiisture(){
        
        List<SoilMoisture> soilMoisture = jdbcTemplate.query(
                "SELECT * FROM soilMoisture",
                (rs, rowNum) ->
                new SoilMoisture(rs.getInt("moisture"), rs.getString("time"))
        );
        
        return soilMoisture;
    }
}
