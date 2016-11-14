package com.microsoft.azure.docker.intellij.actions;

import com.intellij.CommonBundle;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogEarthquakeShaker;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.wm.IdeFocusManager;
import com.intellij.util.Alarm;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.event.*;

public class TestDialogWrapper extends DialogWrapper {
  private JPanel contentPane;
  private Project project;
  private JButton finishButton;


  public TestDialogWrapper(Project project) {

    super(project, true);
    setModal(true);
    setTitle("View Azure Docker Hosts");

    finishButton = new JButton("Finish");

    finishButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onOK();
      }
    });

    init();

//    setContentPane(contentPane);
//    setModal(true);
//    getRootPane().setDefaultButton(buttonOK);
//
//    buttonOK.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        onOK();
//      }
//    });
//
//    buttonCancel.addActionListener(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        onCancel();
//      }
//    });
//
//    // call onCancel() when cross is clicked
//    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
//    addWindowListener(new WindowAdapter() {
//      public void windowClosing(WindowEvent e) {
//        onCancel();
//      }
//    });
//
//    // call onCancel() on ESCAPE
//    contentPane.registerKeyboardAction(new ActionListener() {
//      public void actionPerformed(ActionEvent e) {
//        onCancel();
//      }
//    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
  }

  private void onOK() {
    // add your code here
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return contentPane;
  }

  @Nullable
  @Override
  protected String getHelpId() {
    return null;
  }

  private Action myClickNextAction;
  private Action myClickBackAction;

  Action getMyClickNextAction() {return myClickNextAction;}
  Action getMyClickBackAction() {return myClickBackAction;}

  @Nullable
  @Override
  protected Action[] createActions() {
    Action action = getOKAction();
    action.setEnabled(false);
    action.putValue(Action.NAME, "Finish");
    myClickBackAction = new ClickBackAction();
    myClickBackAction.setEnabled(false);
    myClickNextAction = new ClickNextAction();
    myClickNextAction.setEnabled(true);

    return new Action[] {getOKAction(), getCancelAction(), myClickBackAction, myClickNextAction};
  }

  @Nullable
  @Override
  protected JComponent createSouthPanel() {
    JComponent southPanel =  super.createSouthPanel();

    return southPanel;
  }

  protected class ClickNextAction extends DialogWrapper.DialogWrapperAction {
    protected ClickNextAction() {
      super("Next");
    }

    protected void doAction(ActionEvent e) {
      ValidationInfo info = doClickNextValidate();
      if(info != null) {
        if(info.component != null && info.component.isVisible()) {
          IdeFocusManager.getInstance((Project)null).requestFocus(info.component, true);
        }

        DialogEarthquakeShaker.shake((JDialog) getPeer().getWindow());
      } else {
        doClickNextAction();
      }
    }
  }

  private ValidationInfo doClickNextValidate() {
    //this.getButton(getOKAction());
    return new ValidationInfo("Next is not implemented", this.getButton(getOKAction()));
  }

  private void doClickNextAction() {

  }

  protected class ClickBackAction extends DialogWrapper.DialogWrapperAction {
    protected ClickBackAction() {
      super("Back");
    }

    protected void doAction(ActionEvent e) {
      ValidationInfo info = doClickBackValidate();
      if(info != null) {
        if(info.component != null && info.component.isVisible()) {
          IdeFocusManager.getInstance((Project)null).requestFocus(info.component, true);
        }

        DialogEarthquakeShaker.shake((JDialog) getPeer().getWindow());
      } else {
        doClickBackAction();
      }
    }
  }

  private ValidationInfo doClickBackValidate() {
    return new ValidationInfo("Next is not implemented", getButton(getMyClickBackAction()));
  }

  private void doClickBackAction() {

  }

  //  public static void main(String[] args) {
//    TestDialogWrapper dialog = new TestDialogWrapper();
//    dialog.pack();
//    dialog.setVisible(true);
//    System.exit(0);
//  }
}
