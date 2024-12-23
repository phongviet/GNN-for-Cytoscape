package your.org.myapp.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNode;
import org.cytoscape.model.CyRow;
import org.cytoscape.model.CyColumn;
import org.cytoscape.model.CyTable;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.util.Collection;

public class SendEdgeIndicesAndNodeFeatureTask extends AbstractTask {
    private static final String SERVER_URL = "http://localhost:5000/receive_edge_indices_and_features";
    private final CyApplicationManager applicationManager;
    private static final Gson gson = new Gson();

    public SendEdgeIndicesAndNodeFeatureTask(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public void run(TaskMonitor taskMonitor) throws Exception {
        // Access the current network
        CyNetwork currentNetwork = applicationManager.getCurrentNetwork();
        if (currentNetwork == null) {
            taskMonitor.setStatusMessage("No network is currently selected!");
            return;
        }

        taskMonitor.setStatusMessage("Collecting edges and node features...");

        // Extract edge indices
        Collection<CyEdge> edges = currentNetwork.getEdgeList();
        JsonArray edgeArray = new JsonArray();
        for (CyEdge edge : edges) {
            JsonObject edgeObject = new JsonObject();
            String source = currentNetwork.getRow(edge.getSource()).get("name", String.class);
            String target = currentNetwork.getRow(edge.getTarget()).get("name", String.class);
            edgeObject.addProperty("source", source);
            edgeObject.addProperty("target", target);
            edgeArray.add(edgeObject);
        }

        // Extract node features
        CyTable nodeTable = currentNetwork.getDefaultNodeTable();
        Collection<CyNode> nodes = currentNetwork.getNodeList();
        JsonArray nodeFeatureArray = new JsonArray();

        for (CyNode node : nodes) {
            JsonObject nodeObject = new JsonObject();
            CyRow row = nodeTable.getRow(node.getSUID());

            // Retrieve node name
            String nodeName = row.get("name", String.class);
            nodeObject.addProperty("name", nodeName);

            // Collect features for the node, excluding 'selected'
            JsonObject featuresObject = new JsonObject();
            for (CyColumn column : nodeTable.getColumns()) {
                String columnName = column.getName();
                if (!"selected".equalsIgnoreCase(columnName) && !"SUID".equalsIgnoreCase(columnName) && !"shared name".equalsIgnoreCase(columnName)) { // Exclude 'selected'
                    Object value = row.get(columnName, Object.class);
                    if (value != null) {
                        featuresObject.addProperty(columnName, value.toString());
                    }
                }
            }

            nodeObject.add("features", featuresObject);
            nodeFeatureArray.add(nodeObject);
        }

        // Create the JSON payload
        JsonObject requestBody = new JsonObject();
        requestBody.add("edge_index", edgeArray);
        requestBody.add("node_features", nodeFeatureArray);

        String jsonPayload = gson.toJson(requestBody);
        System.out.println("Sending JSON payload: " + jsonPayload);

        // Send HTTP POST request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SERVER_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload));

            taskMonitor.setStatusMessage("Sending data to the server...");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                String responseBody = EntityUtils.toString(response.getEntity());
                taskMonitor.setStatusMessage("Server response: " + response.getStatusLine());
                System.out.println("Response body: " + responseBody);
            }
        } catch (Exception e) {
            taskMonitor.setStatusMessage("Failed to send data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
