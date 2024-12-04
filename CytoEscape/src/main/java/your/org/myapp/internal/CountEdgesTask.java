// File: CountEdgesTask.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class CountEdgesTask extends AbstractTask {
    private final CyApplicationManager applicationManager;

    public CountEdgesTask(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public void run(TaskMonitor monitor) throws Exception {
        // Access the current network from the application manager
        CyNetwork activeNetwork = applicationManager.getCurrentNetwork();

        if (activeNetwork == null) {
            monitor.setStatusMessage("No network selected.");
            return;
        }

        int edgeCount = activeNetwork.getEdgeCount();
        monitor.setStatusMessage("Edge count: " + edgeCount);

        // Display the edge count in a JFrame
        JFrame frame = new JFrame("Edge Count");
        JLabel label = new JLabel("Edge count: " + edgeCount, JLabel.CENTER);
        frame.add(label);
        frame.setSize(300, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
