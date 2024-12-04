// File: PrintSelectedNodeFeaturesTask.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.model.CyColumn;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PrintSelectedNodeFeaturesTask extends AbstractTask {
    private final CyApplicationManager applicationManager;

    public PrintSelectedNodeFeaturesTask(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public void run(TaskMonitor monitor) throws Exception {
        // Get the active network from the application manager
        CyNetwork activeNetwork = applicationManager.getCurrentNetwork();

        if (activeNetwork == null) {
            monitor.setStatusMessage("No network selected.");
            return;
        }

        monitor.setStatusMessage("Printing selected node features...");

        // Get the default node table, which contains node attributes
        CyTable nodeTable = activeNetwork.getDefaultNodeTable();

        // Create a StringBuilder to collect node features
        StringBuilder output = new StringBuilder();
        List<CyNode> nodeList = activeNetwork.getNodeList();
        boolean foundSelectedNode = false;

        for (CyNode node : nodeList) {
            CyRow row = nodeTable.getRow(node.getSUID());

            // Check if the node is selected
            Boolean isSelected = row.get("selected", Boolean.class);
            if (isSelected != null && isSelected) {
                foundSelectedNode = true;
                output.append("Node ").append(row.get("name", String.class)).append(" features:\n");

                // Append each attribute (column) of the selected node
                for (CyColumn column : nodeTable.getColumns()) {
                    String columnName = column.getName();
                    Object value = row.get(columnName, Object.class);
                    output.append(" - ").append(columnName).append(": ").append(value).append("\n");
                }
                output.append("\n");
            }
        }

        if (!foundSelectedNode) {
            monitor.setStatusMessage("No nodes are selected.");
            output.append("No nodes are selected.");
        }

        // Display the output in a GUI window
        JFrame frame = new JFrame("Selected Node Features");
        JTextArea textArea = new JTextArea(output.toString());
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);

        monitor.setStatusMessage("Selected node features displayed in window.");
    }
}
