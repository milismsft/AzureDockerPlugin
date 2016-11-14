package com.microsoft.azure.docker.intellij.ui;

import com.intellij.openapi.ui.ValidationInfo;

public interface AzureDockerValidatable {
  ValidationInfo doValidate();
}
