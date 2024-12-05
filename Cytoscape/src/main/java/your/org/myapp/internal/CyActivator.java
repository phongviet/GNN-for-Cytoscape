// File: CyActivator.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;
import org.cytoscape.work.TaskFactory;

import java.util.Properties;

public class CyActivator extends AbstractCyActivator {
    @Override
    public void start(BundleContext context) throws Exception {
        CyApplicationManager applicationManager = getService(context, CyApplicationManager.class);

        // Register CountNodesTaskFactory
        CountNodesTaskFactory countNodesFactory = new CountNodesTaskFactory(applicationManager);
        Properties countNodesProps = new Properties();
        countNodesProps.setProperty("preferredMenu", "Apps.MyApp");
        countNodesProps.setProperty("title", "Count Nodes");
        registerService(context, countNodesFactory, org.cytoscape.work.TaskFactory.class, countNodesProps);

        // Register CountEdgesTaskFactory
        CountEdgesTaskFactory countEdgesFactory = new CountEdgesTaskFactory(applicationManager);
        Properties countEdgesProps = new Properties();
        countEdgesProps.setProperty("preferredMenu", "Apps.MyApp");
        countEdgesProps.setProperty("title", "Count Edges");
        registerService(context, countEdgesFactory, org.cytoscape.work.TaskFactory.class, countEdgesProps);

        PrintSelectedNodeFeaturesTaskFactory printSelectedNodeFeaturesTaskFactory = new PrintSelectedNodeFeaturesTaskFactory(applicationManager);
        Properties printSelectedNodeFeaturesProps = new Properties();
        printSelectedNodeFeaturesProps.setProperty("preferredMenu", "Apps.MyApp");
        printSelectedNodeFeaturesProps.setProperty("title", "Print Selected Node Features");
        registerService(context, printSelectedNodeFeaturesTaskFactory, org.cytoscape.work.TaskFactory.class, printSelectedNodeFeaturesProps);

        DisplayEdgeIndicesTaskFactory displayEdgeIndicesTaskFactory = new DisplayEdgeIndicesTaskFactory(applicationManager);
        Properties displayEdgeIndicesProps = new Properties();
        displayEdgeIndicesProps.setProperty("preferredMenu", "Apps.MyApp");
        displayEdgeIndicesProps.setProperty("title", "Display Edge Indices");
        registerService(context, displayEdgeIndicesTaskFactory, org.cytoscape.work.TaskFactory.class, displayEdgeIndicesProps);

        SendEdgeIndicesTaskFactory sendEdgeIndexTaskFactory = new SendEdgeIndicesTaskFactory(applicationManager);
        Properties sendEdgeIndexProps = new Properties();
        sendEdgeIndexProps.setProperty("preferredMenu", "Apps.MyApp");
        sendEdgeIndexProps.setProperty("title", "Train on Node2Vec");
        registerService(context, sendEdgeIndexTaskFactory, org.cytoscape.work.TaskFactory.class, sendEdgeIndexProps);

        predictNodeTaskFactory predictNodeTaskFactory = new predictNodeTaskFactory(applicationManager);
        Properties predictNodeProps = new Properties();
        predictNodeProps.setProperty("preferredMenu", "Apps.MyApp");
        predictNodeProps.setProperty("title", "Predict Node");
        registerService(context, predictNodeTaskFactory, org.cytoscape.work.TaskFactory.class, predictNodeProps);
    }
}
