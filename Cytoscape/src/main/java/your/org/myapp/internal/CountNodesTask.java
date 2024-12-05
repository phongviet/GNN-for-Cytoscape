// File: CountNodesTask.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class CountNodesTask extends AbstractTask {
    private final CyApplicationManager applicationManager;

    public CountNodesTask(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public void run(TaskMonitor monitor) throws Exception {
        // Get the current network from the application manager
        CyNetwork activeNetwork = applicationManager.getCurrentNetwork();

        if (activeNetwork == null) {
            monitor.setStatusMessage("No network selected.");
            return;
        }

        int nodeCount = activeNetwork.getNodeCount();
        monitor.setStatusMessage("Node count: " + nodeCount);

        // Display the node count in a JFrame
        JFrame frame = new JFrame("Node Count");
        JLabel label = new JLabel("Node count: " + nodeCount, JLabel.CENTER);
        frame.add(label);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
