/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fyp.spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
public class LightController {
    
    private static final Logger log = LoggerFactory.getLogger(LightController.class);
    
    @Autowired
    JdbcTemplate jdbcTemplate;
    
    @Autowired
    DataSource datasource;
    
    
    @CrossOrigin(origins = "*")
    @PostMapping("/post-light/{light}")
    
    public void postLight(@PathVariable("light") int light){
        Date dt = new Date();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String currentTime = sdf.format(dt);
         
        Light lightObject = new Light(light, currentTime);

        jdbcTemplate.update("INSERT INTO Light (light, time) VALUES (" + lightObject.getLight() + ",\"" + lightObject.getTime() + "\")");  
        
//        String updateStatement = "INSERT INTO Light (light, time) VALUES ( ? , ? )";   
//        
//        try {
//            
//            Connection conn = datasource.getConnection();
//            PreparedStatement ps = conn.prepareStatement(updateStatement);
//            ps.setInt(1, lightObject.getLight());
//            ps.setString(2, lightObject.getTime());
//            
//            ps.executeUpdate();
//            ps.close();
//        } catch (SQLException ex) {
//            java.util.logging.Logger.getLogger(SoilMoistureController.class.getName()).log(Level.SEVERE, null, ex);
//        }
    }
    
    @CrossOrigin(origins = "*")
    @GetMapping("/light")
    public List<Light> getLight(){
        
        List<Light> LightList = jdbcTemplate.query(
                "SELECT * FROM Light",
                (rs, rowNum) ->
                new Light(rs.getInt("light"), rs.getString("time"))
        );
        
        return LightList;
    }
    
}
