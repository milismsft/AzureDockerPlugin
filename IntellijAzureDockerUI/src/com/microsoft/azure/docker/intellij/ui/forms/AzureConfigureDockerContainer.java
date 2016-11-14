package com.microsoft.azure.docker.intellij.ui.forms;

import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.microsoft.azure.docker.intellij.ui.AzureSelectDockerWizardModel;
import com.microsoft.azure.docker.intellij.ui.AzureSelectDockerWizardStep;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class AzureConfigureDockerContainer extends AzureSelectDockerWizardStep {
  private JPanel rootConfigDockerContainerPanel;
  private JTable table1;
  private AzureSelectDockerWizardModel model;

  public AzureConfigureDockerContainer(String title, AzureSelectDockerWizardModel model) {
    // TODO: The message should go into the plugin property file that handles the various dialog titles
    super(title, "Select a Docker host from the list");
    this.model = model;
    table1.addPropertyChangeListener(new PropertyChangeListener() {
      @Override
      public void propertyChange(PropertyChangeEvent evt) {

      }
    });
  }

  @Override
  public JComponent prepare(final WizardNavigationState state) {
    rootConfigDockerContainerPanel.revalidate();
    return rootConfigDockerContainerPanel;
  }

  @Override
  public WizardStep onNext(final AzureSelectDockerWizardModel model) {
    if (doValidate() == null) {
      return super.onNext(model);
    } else {
      return this;
    }
  }

  @Override
  public ValidationInfo doValidate() {
    return null;
  }
}
