package lk.sayuru.jungleapp;

/*
 *
 * Project Name : ${PROJECT}
 * Created by Janiru on 5/5/2019 12:32 PM.
 *
 */public class SensorStations {

     private double Humidity;
     private double Pressure;
     private double RainFall;
     private double Temperature;

     public SensorStations() {
     }

     public SensorStations(double humidity, double pressure, double rainFall, double temperature) {
          Humidity = humidity;
          Pressure = pressure;
          RainFall = rainFall;
          Temperature = temperature;
     }

     @Override
     public String toString() {
          return "SensorStations{" +
                  "Humidity=" + Humidity +
                  ", Pressure=" + Pressure +
                  ", RainFall=" + RainFall +
                  ", Temperature=" + Temperature +
                  '}';
     }

     public double getHumidity() {
          return Humidity;
     }

     public SensorStations setHumidity(double humidity) {
          Humidity = humidity;
          return this;
     }

     public double getPressure() {
          return Pressure;
     }

     public SensorStations setPressure(double pressure) {
          Pressure = pressure;
          return this;
     }

     public double getRainFall() {
          return RainFall;
     }

     public SensorStations setRainFall(double rainFall) {
          RainFall = rainFall;
          return this;
     }

     public double getTemperature() {
          return Temperature;
     }

     public SensorStations setTemperature(double temperature) {
          Temperature = temperature;
          return this;
     }
}
