package com.microsoft.azure.docker.ui.utils;

import javax.swing.*;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class PluginUtil {
  private static Logger logger = LogManager.getLogManager().getLogger("global");

  public static void displayErrorDialogAndLog(String title, String message, Exception e) {
    Logger mainLogger = LogManager.getLogManager().getLogger("");
    if (mainLogger != null) {
      mainLogger.setLevel(Level.ALL);
      logger.log(Level.SEVERE, message, e);
      mainLogger.setLevel(Level.OFF);
    }
    displayErrorDialog(title, message);
  }

  /**
   * This method will display the error message box when any error occurs.It takes two parameters
   *
   * @param title   the text or title of the window.
   * @param message the message which is to be displayed
   */
  public static void displayErrorDialog(String title, String message) {
    JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
  }

  public static void resetLoggers() {
    LogManager log = LogManager.getLogManager();

    Enumeration<String> enum_ = LogManager.getLogManager().getLoggerNames();
    while (enum_.hasMoreElements()) {
      String name = enum_.nextElement();
      if (!name.equals("global") /*&& !name.equals("") */) {
        Logger logger = LogManager.getLogManager().getLogger(name);
        logger.setLevel(Level.OFF);
      }
    }
  }
}
