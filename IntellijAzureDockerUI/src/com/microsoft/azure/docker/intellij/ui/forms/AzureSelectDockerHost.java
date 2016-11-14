package com.microsoft.azure.docker.intellij.ui.forms;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.AnActionButtonRunnable;
import com.intellij.ui.AnActionButtonUpdater;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.TableView;
import com.intellij.ui.wizard.WizardNavigationState;
import com.intellij.ui.wizard.WizardStep;
import com.microsoft.azure.docker.intellij.ui.AzureSelectDockerWizardModel;
import com.microsoft.azure.docker.intellij.ui.AzureSelectDockerWizardStep;
import com.microsoft.azure.docker.resources.DockerHost;
import com.microsoft.azure.docker.ui.utils.PluginUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AzureSelectDockerHost extends AzureSelectDockerWizardStep {
  private JPanel rootSelectDockerPanel;
  private JTextField dockerImageName;
  private JButton createDockerHostButton;
  private JButton editDockerHostButton;
  private JButton removeDockerHostButton;
  private JCheckBox createRunConfigurationCheckBox;
  private JCheckBox createDebugConfigurationCheckBox;
  private JTable dockerHostsTable;
  private TextFieldWithBrowseButton artifactPath;
  private JPanel hostsPanel;
  private JPanel hostsViewPanel;
  private AzureSelectDockerWizardModel model;
  private TableView<DockerHost> hostsViewResources;

  private List<DockerHost> dockerHostsList;
  private DockerHost newDockerHost;

  public AzureSelectDockerHost(String title, AzureSelectDockerWizardModel model) {
    // TODO: The message should go into the plugin property file that handles the various dialog titles
    super(title, "Configure you Docker Container");
    this.model = model;
    newDockerHost = null;


    final DefaultTableModel dockerListTableModel = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int col) {
        return (col == 0);
      }

      public Class<?> getColumnClass(int colIndex) {
        return getValueAt(0, colIndex).getClass();
      }
    };

    dockerListTableModel.addColumn("");
    dockerListTableModel.addColumn("Name");
    dockerListTableModel.addColumn("State");
    dockerListTableModel.addColumn("OS");
    dockerListTableModel.addColumn("API URL");
    dockerHostsTable.setModel(dockerListTableModel);

    TableColumn column = dockerHostsTable.getColumnModel().getColumn(0);
    column.setMinWidth(23);
    column.setMaxWidth(23);
    column = dockerHostsTable.getColumnModel().getColumn(1);
    column.setPreferredWidth(150);
    column = dockerHostsTable.getColumnModel().getColumn(2);
    column.setPreferredWidth(30);
    column = dockerHostsTable.getColumnModel().getColumn(3);
    column.setPreferredWidth(110);

    forceRefreshDockerHostsTable();

    createDockerHostButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onNewDockerHostAction();
      }
    });
  }

  void onNewDockerHostAction() {
    newDockerHost = newDockerHostDescription();

    refreshDockerHostsTable();
    if (newDockerHost != null) {
      addNewDockerHostEntry();
    }
  }

  @Override
  public JComponent prepare(final WizardNavigationState state) {
    rootSelectDockerPanel.revalidate();
    return rootSelectDockerPanel;
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

  /* adds a new raw in the docker hosts table
   *
   */
  void addNewDockerHostEntry() {
    final DefaultTableModel model = (DefaultTableModel) dockerHostsTable.getModel();

    Vector<Object> row = new Vector<Object>();
    row.add(false);
    row.add(newDockerHost.name);
    row.add(newDockerHost.state.toString());
    row.add(newDockerHost.hostOSType.toString());
    row.add(newDockerHost.apiUrl);
    model.addRow(row);
  }

  /* Refresh the docker hosts entries in the select host table
   *
   */
  void refreshDockerHostsTable() {
    final DefaultTableModel model = (DefaultTableModel) dockerHostsTable.getModel();

    while (model.getRowCount() > 0) {
      model.removeRow(0);
    }

    try {
      if (dockerHostsList != null) {
        for (DockerHost host : dockerHostsList) {
          Vector<Object> row = new Vector<Object>();
          row.add(false);
          row.add(host.name);
          row.add(host.state.toString());
          row.add(host.hostOSType.toString());
          row.add(host.apiUrl);
          model.addRow(row);
        }
      }
    } catch (Exception e) {
      String msg = "An error occurred while attempting to get the list of recognizable Docker hosts.\n" + e.getMessage();
      PluginUtil.displayErrorDialogAndLog("Error", msg, e);
    }
  }

  /* Force a refresh of the docker hosts entries in the select host table
   *   This call will retrieve the latest list of VMs form Azure suitable to be a Docker Host
   */
  void forceRefreshDockerHostsTable() {
    // call into Ops to retrieve the latest list of Docker VMs

    // Fake call to create some dummy entries
    dockerHostsList = createNewFakeDockerHostList();

    refreshDockerHostsTable();
  }


  DockerHost newDockerHostDescription() {
    // TODO: create new docker dialog window and retrieve the description of the docker host to be created
    // i.e.:
    //    newDockerHost = myNewDialog.getDockerHostDescription();

    // temporary hack to add a fake docker host entry
    return createNewFakeDockerHost("myNewHost1");
  }

  private DockerHost createNewFakeDockerHost(String name) {
    DockerHost host = new DockerHost();
    host.name = name;
    host.apiUrl = "http://" + name + ".centralus.cloudapp.azure.com";
    host.port = "2375";
    host.isTLSSecured = false;
    host.state = DockerHost.DockerHostVMState.ACTIVE;
    host.hostOSType = DockerHost.DockerHostOSType.UBUNTU_SERVER_16;

    return host;
  }

  private List<DockerHost> createNewFakeDockerHostList() {
    List<DockerHost> hosts = new ArrayList<>();
    hosts.add(createNewFakeDockerHost("someDockerHost112"));
    hosts.add(createNewFakeDockerHost("otherDockerHost212"));
    hosts.add(createNewFakeDockerHost("qnyDockerHost132"));
    hosts.add(createNewFakeDockerHost("anyDockerHost612"));

    return hosts;
  }

  private void createUIComponents() {
    // TODO: place custom component creation code here
    hostsViewResources = new TableView<DockerHost>();
    hostsViewPanel = ToolbarDecorator.createDecorator(hostsViewResources, null)
        .setAddAction(new AnActionButtonRunnable() {
          @Override
          public void run(AnActionButton button) {
            onNewDockerHostAction();
          }
        }).setEditAction(new AnActionButtonRunnable() {
          @Override
          public void run(AnActionButton anActionButton) {
            // onEditDockerHostAction
            onNewDockerHostAction();
          }
        }).setRemoveAction(new AnActionButtonRunnable() {
          @Override
          public void run(AnActionButton button) {
            // onEditDockerHostAction
            onNewDockerHostAction();
          }
        }).setEditActionUpdater(new AnActionButtonUpdater() {
          @Override
          public boolean isEnabled(AnActionEvent e) {
            return true; // dockerHostsTable.getSelectedObject() != null;
          }
        }).setRemoveActionUpdater(new AnActionButtonUpdater() {
          @Override
          public boolean isEnabled(AnActionEvent e) {
            return true; //dockerHostsTable.getSelectedObject() != null;
          }
        }).disableUpDownActions().createPanel();
  }

  private class DockerHostTableSelection {
    int row;
    int column;
    DockerHost host;
  }
}
