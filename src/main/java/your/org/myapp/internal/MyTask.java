package your.org.myapp.internal;

import org.cytoscape.model.*;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class MyTask extends AbstractTask {
    //this class will be used in the taskIterator of MyTaskFactory class
    private final CyNetworkFactory networkFactory;
    private final CyNetworkManager networkManager;

    public MyTask(CyNetworkFactory networkFactory, CyNetworkManager networkManager) {
        this.networkFactory = networkFactory;
        this.networkManager = networkManager;
    }

    @Override
    public void run(TaskMonitor monitor) throws Exception {
        // method run hoi giong ham main() cua 1 task
        CyNetwork network = networkFactory.createNetwork();

        CyNode node1 = network.addNode();
        CyNode node2 = network.addNode();

        CyEdge edge = network.addEdge(node1, node2, false); //false = undirected
        networkManager.addNetwork(network);

        monitor.setStatusMessage("Network initialized");
    }
}
