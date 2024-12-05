package your.org.myapp.internal;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class MyTaskFactory extends AbstractTaskFactory {
    private final CyNetworkFactory networkFactory;
    private final CyNetworkManager networkManager;
    private final CyNetworkViewFactory networkViewFactory;
    private final CyNetworkViewManager networkViewManager;
    public MyTaskFactory(CyNetworkFactory networkFactory,
                         CyNetworkManager networkManager,
                         CyNetworkViewFactory networkViewFactory,
                         CyNetworkViewManager networkViewManager) {
        //CyActivator se dung class nay de tao 1 cai taskfactory de roi register no voi OSGi? slide trang 31
        //super(); //ko biet tai sao slide goi constructor cua AbstractTaskFactory, tim hieu sau
        this.networkFactory = networkFactory;
        this.networkManager = networkManager;
        this.networkViewFactory = networkViewFactory;
        this.networkViewManager = networkViewManager;
    }

    public TaskIterator createTaskIterator() {
        // Create a list or array of tasks (easier to scale in the future)
        return createNetworkTasks();
    }

    // This method encapsulates the task creation logic, making it easier to extend in the future
    private TaskIterator createNetworkTasks() {
        // The MyTask class file comes into play
        MyTask networkTask = new MyTask(networkFactory, networkManager, networkViewFactory, networkViewManager);
        // You could add more tasks to this iterator in the future, making it more scalable
        return new TaskIterator(networkTask);
    }

    public boolean isReady() {
        return true;
    }
}
