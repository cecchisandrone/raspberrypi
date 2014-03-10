package it.cecchi.raspsonar.gpio;

 
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalInput;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinPullResistance;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
 
public class PiBot {
 
 /**
  * @param args
  */
 public static void main(String[] args) {
 
  // Setup GPIO Pins 
  GpioController gpio = GpioFactory.getInstance();
 
  //range finder pins
 
  GpioPinDigitalOutput rangefindertrigger = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "Range Finder Trigger", PinState.LOW);
 
  GpioPinDigitalInput rangefinderresult = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "Range Pulse Result", PinPullResistance.PULL_DOWN);
 
  // Create the range finder
  RangeFinder rangefinder = new RangeFinder(rangefindertrigger,rangefinderresult);
 
  do {
  // Get the range
  double distance=rangefinder.getRange();
 
  System.out.println("RangeFinder result ="+distance +"mm");
  } while (false!=true);
   
   
 
 }
}