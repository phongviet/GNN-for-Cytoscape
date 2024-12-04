// File: CountEdgesTaskFactory.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class CountEdgesTaskFactory extends AbstractTaskFactory {
    private final CyApplicationManager applicationManager;

    public CountEdgesTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new CountEdgesTask(applicationManager));
    }
}
