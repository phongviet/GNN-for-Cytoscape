package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class predictNodeGCNTaskFactory implements TaskFactory {
    private final CyApplicationManager applicationManager;

    public predictNodeGCNTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new predictNodeGCNTask(applicationManager));
    }

    @Override
    public boolean isReady() {
        return applicationManager.getCurrentNetwork() != null;
    }
}
