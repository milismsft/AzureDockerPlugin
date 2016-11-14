package com.microsoft.azure.docker.intellij.ui;

import com.intellij.ui.wizard.WizardStep;

public abstract class AzureSelectDockerWizardStep extends WizardStep<AzureSelectDockerWizardModel> implements AzureDockerValidatable {

    protected AzureSelectDockerWizardStep(String title, String explanation) {
      super(title, explanation, null);
    }

  public String getHelpId() {
    return "azure_docker_project";
  }
}
