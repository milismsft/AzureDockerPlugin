package com.microsoft.azure.docker.intellij.forms;

import java.awt.event.*;
import javax.swing.*;

public class AzureSelectDockerHost {
  private JPanel mainPanel;
  private JTabbedPane mainTabbedPane;
  private JButton cancelButton;
  private JButton nextButton;
  private JButton finishButton;
  private JList dockerHosts;
  private JButton refreshDockerHostsButton;
  private JButton newDockerHostButton;
  private JButton editDockerHostButton;
  private JButton deleteDockerHostButton;

  public AzureSelectDockerHost() {
    refreshDockerHostsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    newDockerHostButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    editDockerHostButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    deleteDockerHostButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    cancelButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    });
    // call onCancel() on ESCAPE
    mainPanel.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    nextButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
    finishButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

      }
    });
  }

  private void onCancel() {
    // add your code here if necessary
//    dispose();

  }


}
