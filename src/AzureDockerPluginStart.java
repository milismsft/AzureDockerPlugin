import com.microsoft.azure.docker.creds.AzureCredsManager;
import com.microsoft.azure.docker.resources.AzureDockerSubscription;
import com.microsoft.azure.docker.ui.AzureDockerUIManager;
import com.microsoft.azure.docker.ui.utils.PluginUtil;
import com.microsoft.azure.management.resources.Location;
import com.microsoft.azure.management.resources.Subscription;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class AzureDockerPluginStart extends JDialog {
  private JPanel mainPanel;
  private JButton buttonCancel;
  private JButton publishAsAzureDockerButton;
  private JButton viewAzureDockerHostsButton;
  private JRadioButton selectADLoginRadioButton;
  private JRadioButton selectSPLoginRadioButton;
  private JTable subscriptionTable;
  private JScrollPane subscriptionPane;
  private AzureDockerUIManager dockerUIManager;

  public AzureDockerPluginStart() {
    setContentPane(mainPanel);
    setModal(true);
    getRootPane().setDefaultButton(buttonCancel);

    buttonCancel.addActionListener(new ActionListener() {
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
    mainPanel.registerKeyboardAction(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        onCancel();
      }
    }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

    publishAsAzureDockerButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onPublishAsAzureDockerContainer();
      }
    });

    viewAzureDockerHostsButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onViewAzureDockerHosts();
      }
    });

    selectADLoginRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onSelectADLoginRadioButton();
      }
    });

    selectSPLoginRadioButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        onSelectSPLoginRadioButton();
      }
    });

    final DefaultTableModel model = new DefaultTableModel() {
      @Override
      public boolean isCellEditable(int row, int col) {
        return (col == 0);
      }

      public Class<?> getColumnClass(int colIndex) {
        return getValueAt(0, colIndex).getClass();
      }
    };

    model.addColumn("");
    model.addColumn("Name");
    model.addColumn("Id");

    subscriptionTable.setModel(model);

    TableColumn column = subscriptionTable.getColumnModel().getColumn(0);
    column.setMinWidth(23);
    column.setMaxWidth(23);
  }

  private void onSelectADLoginRadioButton() {
    selectSPLoginRadioButton.setSelected(false);

    try {
      List<Subscription> subscriptionList = AzureCredsManager.getADSubscriptions();
      List<AzureDockerSubscription> subscriptions = new ArrayList<>();

      for (Subscription subscription : subscriptionList) {
        AzureDockerSubscription dockerSubscription = createSubscriptionElement(subscription);
        subscriptions.add(dockerSubscription);
      }

      createSubscriptionTable(subscriptions);
    } catch (Exception e) {
      String msg = "An error occurred while attempting to get the subscription list.\n" + e.getMessage();
      PluginUtil.displayErrorDialogAndLog("Error", msg, e);
    }
  }

  private void onSelectSPLoginRadioButton() {
    selectADLoginRadioButton.setSelected(false);

    try {
      List<Subscription> subscriptionList = AzureCredsManager.getSPSubscriptions();
      List<AzureDockerSubscription> subscriptions = new ArrayList<>();

      for (Subscription subscription : subscriptionList) {
        AzureDockerSubscription dockerSubscription = createSubscriptionElement(subscription);
        subscriptions.add(dockerSubscription);
      }

      createSubscriptionTable(subscriptions);
    } catch (Exception e) {
      String msg = "An error occurred while attempting to get the subscription list.\n" + e.getMessage();
      PluginUtil.displayErrorDialogAndLog("Error", msg, e);
    }
  }

  private void createSubscriptionTable(List<AzureDockerSubscription> subscriptionList) {
    final DefaultTableModel model = (DefaultTableModel) subscriptionTable.getModel();

    while (model.getRowCount() > 0) {
      model.removeRow(0);
    }

    try {
      if (subscriptionList != null) {
        for (AzureDockerSubscription subs : subscriptionList) {
          Vector<Object> row = new Vector<Object>();
          row.add(subs.selected);
          row.add(subs.name);
          row.add(subs.id);
          model.addRow(row);
        }
      }
    } catch (Exception e) {
      String msg = "An error occurred while attempting to get the subscription list.\n" + e.getMessage();
      PluginUtil.displayErrorDialogAndLog("Error", msg, e);
    }
  }

  private void onPublishAsAzureDockerContainer() {
    System.out.println("Publishing as Azure Docker Container");
    setSelectedSubscriptions();
    dispose();
  }

  private void onViewAzureDockerHosts() {
    System.out.println("View Azure Docker Hosts");
    setSelectedSubscriptions();
    dispose();
  }

  private void onCancel() {
    // add your code here if necessary
    dispose();
  }

  private void setSelectedSubscriptions() {
    try {
      System.out.println("Get selected subscriptions");

      java.util.List<String> selectedList = new ArrayList<String>();
      TableModel model = subscriptionTable.getModel();

      for (int i = 0; i < model.getRowCount(); i++) {
        Object selected = model.getValueAt(i, 0);
        if (selected instanceof Boolean && (Boolean) selected) {
          selectedList.add(model.getValueAt(i, 2).toString());
        }
      }

      dockerUIManager = new AzureDockerUIManager();

      if (selectADLoginRadioButton.isSelected()) {
        dockerUIManager.azureMainClient = AzureCredsManager.createSPDefaultAzureClient();
        List<Subscription> subscriptionList = AzureCredsManager.getSPSubscriptions();
        dockerUIManager.subscriptionList = new ArrayList<>();

        for (Subscription subscription : subscriptionList) {
          if (selectedList.contains(subscription.subscriptionId())) {
            AzureDockerSubscription dockerSubscription = createSubscriptionElement(subscription);
            dockerSubscription.azureClient = AzureCredsManager.getAzureSPClient(dockerSubscription.id);
            dockerUIManager.subscriptionList.add(dockerSubscription);
          }
        }

      } else if (selectADLoginRadioButton.isSelected()) {
        dockerUIManager.azureMainClient = AzureCredsManager.createAuthLoginDefaultAzureClient();
        List<Subscription> subscriptionList = AzureCredsManager.getADSubscriptions();
        dockerUIManager.subscriptionList = new ArrayList<>();

        for (Subscription subscription : subscriptionList) {
          if (selectedList.contains(subscription.subscriptionId())) {
            AzureDockerSubscription dockerSubscription = createSubscriptionElement(subscription);
            dockerSubscription.azureClient = AzureCredsManager.getAzureADClient(dockerSubscription.id);
            dockerUIManager.subscriptionList.add(dockerSubscription);
          }
        }

      }
    } catch (Exception e) {
      String msg = "An error occurred while attempting to set the subscription list.\n" + e.getMessage();
      PluginUtil.displayErrorDialogAndLog("Error", msg, e);
    }
  }

  private AzureDockerSubscription createSubscriptionElement(Subscription subscription) {
    AzureDockerSubscription dockerSubscription = new AzureDockerSubscription();
    dockerSubscription.id = subscription.subscriptionId();
    dockerSubscription.name = subscription.displayName();
    dockerSubscription.selected = true;
    dockerSubscription.locations = new ArrayList<>();

    for (Location location : subscription.listLocations()) {
      dockerSubscription.locations.add(location.name());
    }

    return dockerSubscription;
  }

}
