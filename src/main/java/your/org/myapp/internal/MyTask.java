package your.org.myapp.internal;

import org.cytoscape.model.*;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class MyTask extends AbstractTask {
    //this class will be used in the taskIterator of MyTaskFactory class
    private final CyNetworkFactory networkFactory;
    private final CyNetworkManager networkManager;
    private final CyNetworkViewFactory networkViewFactory;
    private final CyNetworkViewManager networkViewManager;

    public MyTask(CyNetworkFactory networkFactory,
                  CyNetworkManager networkManager,
                  CyNetworkViewFactory networkViewFactory,
                  CyNetworkViewManager networkViewManager) {
        this.networkFactory = networkFactory;
        this.networkManager = networkManager;
        this.networkViewFactory = networkViewFactory;
        this.networkViewManager = networkViewManager;
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

        CyNetworkView networkView = networkViewFactory.createNetworkView(network);
        //todo: change the default position of the 2 nodes so it is shown on the screen
        networkViewManager.addNetworkView(networkView);
    }
}
