package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.awt.*;
import java.util.Collection;

public class DisplayEdgeIndicesTask extends AbstractTask {
    private final CyApplicationManager applicationManager;

    public DisplayEdgeIndicesTask(CyApplicationManager applicationManager) {
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

        Collection<CyEdge> edges = activeNetwork.getEdgeList();
        if (edges.isEmpty()) {
            monitor.setStatusMessage("No edges in the network.");
            return;
        }

        StringBuilder edgeListBuilder = new StringBuilder();
        for (CyEdge edge : edges) {
            // Retrieve the source and target node names directly from the CyNode objects
            String source = activeNetwork.getRow(edge.getSource()).get("name", String.class);
            String target = activeNetwork.getRow(edge.getTarget()).get("name", String.class);

            // Append to the edge list
            edgeListBuilder.append(source).append("\t").append(target).append("\n");
        }


        // Display the edge indices in a JFrame
        JFrame frame = new JFrame("Edge Indices");
        JTextArea textArea = new JTextArea(edgeListBuilder.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(textArea);
        frame.add(scrollPane);

        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
