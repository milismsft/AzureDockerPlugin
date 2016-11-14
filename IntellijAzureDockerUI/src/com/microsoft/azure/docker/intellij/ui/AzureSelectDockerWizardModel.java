package com.microsoft.azure.docker.intellij.ui;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.wizard.WizardModel;
import com.microsoft.azure.docker.intellij.ui.forms.AzureConfigureDockerContainer;
import com.microsoft.azure.docker.intellij.ui.forms.AzureSelectDockerHost;

public class AzureSelectDockerWizardModel extends WizardModel {
  private Project project;
  private AzureSelectDockerHost selectDockerHostForm;
  private AzureConfigureDockerContainer configureDockerContainerForm;


  public AzureSelectDockerWizardModel(final Project project) {
    super("Azure Docker Deployment");
    this.project = project;

    selectDockerHostForm = new AzureSelectDockerHost(this.getTitle(), this);
    configureDockerContainerForm = new AzureConfigureDockerContainer(this.getTitle(), this);
    add(selectDockerHostForm);
    add(configureDockerContainerForm);
  }

  public Project getProject() {
    return project;
  }

  public ValidationInfo doValidate() {
    return ((AzureSelectDockerWizardStep) getCurrentStep()).doValidate();
  }

}
