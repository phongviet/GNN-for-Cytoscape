package your.org.myapp.internal;

import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class MyTaskFactory extends AbstractTaskFactory {
    public MyTaskFactory() {
        super();
    }

    public TaskIterator createTaskIterator() {
        return null; //Fill in here
    }

    public boolean isReady() {
        return true;
    }
}
