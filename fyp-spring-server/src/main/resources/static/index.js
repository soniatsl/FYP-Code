var coll = document.getElementsByClassName("collapsible");
var i;


    callAPI("temp-humi", "temp", "current-temp-data", "past-temp-data");
    callAPI("temp-humi", "humi", "current-humi-data", "past-humi-data");
    callAPI("light", "light", "current-light-data", "past-light-data");
    callAPI("soil-moisture", "moisture", "current-soil-data", "past-soil-data");
    callAPI("status", "status", "health-img", null);  



function callAPI(urlPath, key, divName, pastDivName){
    $.ajaxPrefilter( "json script", function( options ) {
    options.crossDomain = true;
    });
    $.ajax({
        type: 'GET',
        url: 'http://localhost:31415/' + urlPath,
        dataType: "JSON", // data type expected from server
        success: function (data) {

            
            if(urlPath !== "status" ){
                
                console.log(data);
                
                $("." + divName).text(data[data.length - 1][key]);
                if(key === "temp")
                  $("." + divName).append(" °C");
                else if(key === "humi")
                  $("." + divName).append(" %");

                var dataPoints = [];
                var timeLabel = [];
                for(var num = 0; num < data.length - 1; num++){
                    dataPoints[num] = data[num][key];
                    timeLabel[num] = data[num].time;
                }
                console.log(dataPoints);
                
                var pastDataCanvas = document.getElementsByClassName(pastDivName)[0].getContext("2d");
                var chart = new Chart(pastDataCanvas, {
                    type: "line",
                    "data": {
                        labels:timeLabel,
                        datasets: [{
                            label: pastDivName,
                            borderColor: 'rgb(255, 99, 132)',
                            data: dataPoints
                        }]
                    }
                });
            }
            else if(urlPath === "status"){
                
                console.log("status: " + data[0][key]);
                
                if(data[0][key] === "good")
                     $("." + divName).attr("src", "good.png");
                else
                    $("." + divName).attr("src", "poor.png");
            }
            
            

        },
        error: function() {
            console.log('Error: ' + data);
        }
    });
}


for (i = 0; i < coll.length; i++) {
  coll[i].addEventListener("click", function() {
    this.classList.toggle("active");
    var content = this.nextElementSibling;  
    if (content.style.display === "block") {
      content.style.display = "none";
    } else {
      content.style.display = "block";
    }
  });
}
