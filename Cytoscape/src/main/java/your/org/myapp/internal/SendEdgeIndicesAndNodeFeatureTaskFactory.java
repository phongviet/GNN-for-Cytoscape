package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.AbstractTaskFactory;
import org.cytoscape.work.TaskIterator;
import org.cytoscape.work.TaskFactory;

public class SendEdgeIndicesAndNodeFeatureTaskFactory extends AbstractTaskFactory {
    private final CyApplicationManager applicationManager;

    public SendEdgeIndicesAndNodeFeatureTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new SendEdgeIndicesAndNodeFeatureTask(applicationManager));
    }
}
