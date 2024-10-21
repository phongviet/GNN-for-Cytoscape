package your.org.myapp.internal;

import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class MyTaskFactory extends AbstractTaskFactory {
    // vi du dung private final, whatever
    private CyNetworkFactory networkFactory;
    private CyNetworkManager networkManager;
    public MyTaskFactory(CyNetworkFactory networkFactory, CyNetworkManager networkManager) {
        //CyActivator se dung class nay de tao 1 cai taskfactory de roi register no voi OSGi? slide trang 31
        //super(); //ko biet tai sao slide goi constructor cua AbstractTaskFactory, tim hieu sau
        this.networkFactory = networkFactory;
        this.networkManager = networkManager;
    }

    public TaskIterator createTaskIterator() {
        // Create a list or array of tasks (easier to scale in the future)
        return createNetworkTasks();
    }

    // This method encapsulates the task creation logic, making it easier to extend in the future
    private TaskIterator createNetworkTasks() {
        // The MyTask class file
        MyTask networkTask = new MyTask(networkFactory, networkManager);
        // You could add more tasks to this iterator in the future, making it more scalable
        return new TaskIterator(networkTask);
    }

    public boolean isReady() {
        return true;
    }
}
