package com.microsoft.azure.docker.intellij.forms;

import javax.swing.*;
import java.awt.event.*;

// TODO: This class needs to extend DialogWrapper
public class AzureSelectDockerHostDlg extends JDialog {
  private JPanel mainPane;
  private JButton finishButton;
  private JButton cancelButton;
  private JButton nextButton;
  private JTabbedPane tabbedPane1;

  public AzureSelectDockerHostDlg() {
    setContentPane(mainPane);
    setModal(true);
    getRootPane().setDefaultButton(finishButton);

    cancelButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    });

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        onCancel();
      }
    });

    // call onCancel() on ESCAPE
    mainPane.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  public static void main(String[] args) {
    AzureSelectDockerHostDlg dialog = new AzureSelectDockerHostDlg();
    dialog.pack();
    dialog.setVisible(true);
    System.exit(0);
  }
}
