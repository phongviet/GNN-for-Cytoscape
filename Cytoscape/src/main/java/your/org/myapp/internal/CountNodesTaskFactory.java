// File: CountNodesTaskFactory.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class CountNodesTaskFactory extends AbstractTaskFactory {
    private final CyApplicationManager applicationManager;

    public CountNodesTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new CountNodesTask(applicationManager));
    }
}
