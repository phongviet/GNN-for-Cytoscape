package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;

public class SendEdgeIndicesTaskFactory extends AbstractTaskFactory {
    private final CyApplicationManager applicationManager;

    public SendEdgeIndicesTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new SendEdgeIndicesTask(applicationManager));
    }
}
