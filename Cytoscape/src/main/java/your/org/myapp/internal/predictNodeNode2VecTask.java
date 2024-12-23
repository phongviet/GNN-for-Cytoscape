package your.org.myapp.internal;

import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

public class predictNodeNode2VecTask extends AbstractTask {
    private static final String SERVER_URL = "http://localhost:5000/predict_node_label_Node2Vec";
    private final CyApplicationManager applicationManager;

    public predictNodeNode2VecTask(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public void run(TaskMonitor monitor) throws Exception {
        // Get the active network
        CyNetwork activeNetwork = applicationManager.getCurrentNetwork();

        if (activeNetwork == null) {
            monitor.setStatusMessage("No network selected.");
            return;
        }

        // Access the default node table
        CyTable nodeTable = activeNetwork.getDefaultNodeTable();
        List<CyNode> nodeList = activeNetwork.getNodeList();

        StringBuilder output = new StringBuilder();
        boolean foundSelectedNode = false;

        for (CyNode node : nodeList) {
            CyRow row = nodeTable.getRow(node.getSUID());

            // Check if the node is selected
            Boolean isSelected = row.get("selected", Boolean.class);
            if (isSelected != null && isSelected) {
                foundSelectedNode = true;

                String nodeName = row.get("name", String.class);
                if (nodeName == null) {
                    output.append("Node name is missing for a selected node.\n");
                    continue;
                }

                monitor.setStatusMessage("Predicting label for node: " + nodeName);

                // Send node name to the server for prediction
                try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
                    // Create request body
                    JsonObject requestBody = new JsonObject();
                    requestBody.addProperty("node_name", nodeName);

                    // Set up the HTTP POST request
                    HttpPost httpPost = new HttpPost(SERVER_URL);
                    httpPost.setHeader("Content-Type", "application/json");
                    httpPost.setEntity(new StringEntity(requestBody.toString()));

                    try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                        // Parse response
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(response.getEntity().getContent()));
                        StringBuilder responseBuilder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            responseBuilder.append(line);
                        }

                        output.append("Node: ").append(nodeName).append("\n");
                        output.append("Prediction: ").append(responseBuilder.toString()).append("\n\n");
                    }
                } catch (Exception e) {
                    output.append("Error predicting label for node: ").append(nodeName).append("\n");
                    output.append("Error: ").append(e.getMessage()).append("\n\n");
                }
            }
        }

        if (!foundSelectedNode) {
            monitor.setStatusMessage("No nodes are selected.");
            output.append("No nodes are selected.");
        }

        // Display the result in a GUI window
        JFrame frame = new JFrame("Node Prediction Results");
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

        monitor.setStatusMessage("Prediction results displayed in window.");
    }
}
