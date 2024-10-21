package your.org.myapp.internal;

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.work.AbstractTask;
import org.cytoscape.work.TaskMonitor;

public class MyTask extends AbstractTask {
    //this class will be used in the taskIterator of MyTaskFactory
    private CyNetworkFactory networkFactory;
    private CyNetworkManager networkManager;

    public MyTask(CyNetworkFactory networkFactory, CyNetworkManager networkManager) {
        this.networkFactory = networkFactory;
        this.networkManager = networkManager;
    }

    @Override
    public void run(TaskMonitor monitor) throws Exception {
        // method run hoi giong ham main() cua 1 task
        CyNetwork network = networkFactory.createNetwork();
        networkManager.addNetwork(network);

        monitor.setStatusMessage("Network created and added");
    }
}
