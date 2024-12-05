package your.org.myapp.internal;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils; // Add this import for EntityUtils
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.model.CyEdge;
import org.cytoscape.model.CyNetwork;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

import java.util.Collection;

public class SendEdgeIndicesTask extends AbstractTask {
    private static final String SERVER_URL = "http://localhost:5000/receive_edge_indices";
    private final CyApplicationManager applicationManager;
    private static final Gson gson = new Gson(); // Use Gson for more robust JSON handling

    public SendEdgeIndicesTask(CyApplicationManager applicationManager) {
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

        // Extract edges and their source-target node names
        Collection<CyEdge> edges = currentNetwork.getEdgeList();
        JsonArray edgeArray = new JsonArray();

        for (CyEdge edge : edges) {
            JsonObject edgeObject = new JsonObject();

            // Retrieve source and target node names
            String source = currentNetwork.getRow(edge.getSource()).get("name", String.class);
            String target = currentNetwork.getRow(edge.getTarget()).get("name", String.class);

            edgeObject.addProperty("source", source);
            edgeObject.addProperty("target", target);
            edgeArray.add(edgeObject);
        }

        // Prepare the JSON payload
        JsonObject requestBody = new JsonObject();
        requestBody.add("edge_index", edgeArray);

        // Convert to JSON string for logging
        String jsonPayload = gson.toJson(requestBody);
        System.out.println("Sending JSON payload: " + jsonPayload);

        // Send HTTP POST request
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(SERVER_URL);
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setEntity(new StringEntity(jsonPayload)); // Use the converted JSON string

            taskMonitor.setStatusMessage("Sending edge data to server and training...");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                // Read the response body to get more details about the error
                String responseBody = EntityUtils.toString(response.getEntity());
                taskMonitor.setStatusMessage("Server response: " + response.getStatusLine());
                System.out.println("Response body: " + responseBody);
            }
        } catch (Exception e) {
            taskMonitor.setStatusMessage("Failed to send edge data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}