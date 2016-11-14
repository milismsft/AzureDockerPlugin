package com.microsoft.azure.docker.intellij.ui;

import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.wizard.WizardDialog;
import com.microsoft.azure.docker.ui.utils.PluginUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class AzureSelectDockerWizardDialog extends WizardDialog<AzureSelectDockerWizardModel> {
  private AzureSelectDockerWizardModel model;

  public AzureSelectDockerWizardDialog(AzureSelectDockerWizardModel model) {
    super(model.getProject(), true, model);
    this.model = model;
  }

  @Override
  public void onWizardGoalAchieved() {
    super.onWizardGoalAchieved();
  }

  @Override
  protected Dimension getWindowPreferredSize() {
    return new Dimension(600, 400);
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    return myModel.doValidate();
  }

  @Override
  protected void doOKAction() {
//        validateInput();
    if (isOKActionEnabled() && performFinish()) {
      super.doOKAction();
    }
  }

  /**
   * This method gets called when wizard's finish button is clicked.
   *
   * @return True, if project gets created successfully; else false.
   */
  private boolean performFinish() {
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        // Do the heavy workload in a background thread
        doFinish();
      }
    };

    ProgressManager progressManager = null;
    try {
      progressManager = ProgressManager.getInstance();
    } catch (Exception e) {
      progressManager = null;
    }

    if (progressManager != null) {
      progressManager.runProcessWithProgressSynchronously(runnable, "Deploying...", true, model.getProject());
    } else {
      doFinish();
    }

    return true;
  }

  private void doFinish() {
    try {
      // do something interesting here
    } catch (Exception e) {
      // TODO: These strings should be retrieved from AzureBundle
      PluginUtil.displayErrorDialogAndLog("Error", "Error deploying to Docker", e);
    }
  }
}
