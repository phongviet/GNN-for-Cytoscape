package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.work.TaskFactory;
import org.cytoscape.work.TaskIterator;

public class predictNodeNode2VecTaskFactory implements TaskFactory {
    private final CyApplicationManager applicationManager;

    public predictNodeNode2VecTaskFactory(CyApplicationManager applicationManager) {
        this.applicationManager = applicationManager;
    }

    @Override
    public TaskIterator createTaskIterator() {
        return new TaskIterator(new predictNodeNode2VecTask(applicationManager));
    }

    @Override
    public boolean isReady() {
        return applicationManager.getCurrentNetwork() != null;
    }
}
