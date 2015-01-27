package com.github.cecchisandrone.raspio.input;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JoypadController implements Runnable, UncaughtExceptionHandler {

  private static final Logger LOGGER = LoggerFactory.getLogger(JoypadController.class);

  private static JoypadController joypadInterface;

  public enum Button {
    A, B, X, Y, LB, RB, BACK, START, GUIDE, TL, TR
  }

  public enum Analog {
    X1, Y1, X2, Y2, LT, RT, X3, Y3
  }

  private static Map<Button, Integer> oldButtonValues = new HashMap<Button, Integer>();

  private static Map<Analog, Integer> oldAnalogValues = new HashMap<Analog, Integer>();

  private static String device;

  private boolean connected = false;

  private static FileInputStream deviceStream;

  private List<JoypadEventListener> listeners = new ArrayList<JoypadEventListener>();

  private JoypadController(String device) {
    JoypadController.device = device;

    // Init old values to 0 for all buttons
    for (Button button : Button.values()) {
      oldButtonValues.put(button, 0);
    }

    // Init old values to 0 for all keys
    for (Analog analog : Analog.values()) {
      oldAnalogValues.put(analog, 0);
    }
  }

  public static JoypadController getInstance(String device) {

    if (joypadInterface == null) {
      joypadInterface = new JoypadController(device);

    }
    return joypadInterface;
  }

  public synchronized boolean connect() {
    if (!connected) {
      connected = !connected;
      Thread t = new Thread(this);
      t.setUncaughtExceptionHandler(this);
      t.start();
      return true;
    }
    return false;
  }

  public synchronized void disconnect() {

    LOGGER.debug("Disconnetting...");
    if (connected) {
      connected = !connected;
    }
  }

  public void addEventListener(JoypadEventListener eventListener) {
    listeners.add(eventListener);
  }

  public void removeEventListener(JoypadEventListener eventListener) {
    listeners.remove(eventListener);
  }

  @Override
  public void run() {
    try {
      deviceStream = new FileInputStream(device);
      connected = true;
      byte[] buf = new byte[8];
      while (connected && deviceStream.read(buf) >= 0) {

        int newValue = -1;
        int oldValue = -1;
        Button changedButton = null;
        Analog changedAnalog = null;

        LOGGER.debug(String.format("%02X%02X %02X%02X %02X%02X %02X%02X", buf[0], buf[1], buf[2], buf[3],
            buf[4], buf[5], buf[6], buf[7]));

        if ((buf[6] & 0x80) == 0x80) {
          LOGGER.debug("Skipping configuration data");
        }
        else {
          // Check BYTE[6] to verify if it's a button (0x01) or analog
          // (0x02)
          if (buf[6] == 0x01) {
            // If it's a button check BYTE[4] if it's pressed or
            // released
            newValue = buf[4];
            changedButton = Button.values()[buf[7]];
            oldValue = oldButtonValues.get(changedButton);

            // Update old value for button
            oldButtonValues.put(changedButton, newValue);

          }
          else if (buf[6] == 0x02) {

            // buf[4] LSB buf[5] MSB
            newValue = (buf[5]) << 8 | (0xFF & buf[4]);
            changedAnalog = Analog.values()[buf[7]];
            oldValue = oldAnalogValues.get(changedAnalog);

            // Update old value for button
            oldAnalogValues.put(changedAnalog, newValue);
          }

          for (JoypadEventListener listener : listeners) {

            // Check if listener is interested in this button or
            // analog
            if (listener.getButtonsToNotify().contains(changedButton)) {
              listener.joypadEventTriggered(new JoypadEvent(newValue, oldValue, changedButton));
            }
            else if (listener.getAnalogsToNotify().contains(changedAnalog)) {
              listener.joypadEventTriggered(new JoypadEvent(newValue, oldValue, changedAnalog));
            }
          }
        }
      }
    }
    catch (FileNotFoundException e) {
      throw new JoypadException(e.toString(), e);
    }
    catch (IOException e) {
      throw new JoypadException(e.toString(), e);
    }
    finally {
      try {
        LOGGER.debug("Closing stream...");
        deviceStream.close();
      }
      catch (IOException e) {
        throw new JoypadException(e.toString(), e);
      }
    }
  }

  @Override
  public void uncaughtException(Thread t, Throwable e) {
    LOGGER.error("Fatal error in JoypadController. Reason: " + e.toString(), e);
  }
}
