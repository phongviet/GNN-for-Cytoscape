package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class predictNodeTaskFactory implements TaskFactory {
    private final CyApplicationManager applicationManager;

    public predictNodeTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new predictNodeTask(applicationManager));
    }

    @Override
    public boolean isReady() {
        return applicationManager.getCurrentNetwork() != null;
    }
}
