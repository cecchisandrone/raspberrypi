package com.github.cecchisandrone.raspio.input;


public class JoypadControllerLauncher implements Runnable {

  private JoypadController controller;

  public static void main(String[] args) {

    JoypadControllerLauncher launcher = new JoypadControllerLauncher();
    Runtime.getRuntime().addShutdownHook(new Thread(launcher));
    launcher.launch();
  }

  private void launch() {
    controller = JoypadController.getInstance("/dev/input/js0");
    controller.addEventListener(new JoypadEventLogger());
    controller.connect();
  }

  @Override
  public void run() {
    controller.disconnect();
  }
}
