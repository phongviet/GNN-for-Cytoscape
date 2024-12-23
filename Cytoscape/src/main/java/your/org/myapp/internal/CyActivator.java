// File: CyActivator.java
package your.org.myapp.internal;

import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.service.util.AbstractCyActivator;
import org.osgi.framework.BundleContext;

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

        predictNodeNode2VecTaskFactory predictNodeNode2VecTaskFactory = new predictNodeNode2VecTaskFactory(applicationManager);
        Properties predictNodeN2VProps = new Properties();
        predictNodeN2VProps.setProperty("preferredMenu", "Apps.MyApp");
        predictNodeN2VProps.setProperty("title", "Predict class for Node2Vec");
        registerService(context, predictNodeNode2VecTaskFactory, org.cytoscape.work.TaskFactory.class, predictNodeN2VProps);

        SendEdgeIndicesAndNodeFeatureTaskFactory sendEdgeIndicesAndNodeFeatureTaskFactory = new SendEdgeIndicesAndNodeFeatureTaskFactory(applicationManager);
        Properties sendEdgeIndicesAndNodeFeatureProps = new Properties();
        sendEdgeIndicesAndNodeFeatureProps.setProperty("preferredMenu", "Apps.MyApp");
        sendEdgeIndicesAndNodeFeatureProps.setProperty("title", "Train on GCN");
        registerService(context, sendEdgeIndicesAndNodeFeatureTaskFactory, org.cytoscape.work.TaskFactory.class, sendEdgeIndicesAndNodeFeatureProps);

        predictNodeGCNTaskFactory predictNodeGCNTaskFactory = new predictNodeGCNTaskFactory(applicationManager);
        Properties predictNodeGCNProps = new Properties();
        predictNodeGCNProps.setProperty("preferredMenu", "Apps.MyApp");
        predictNodeGCNProps.setProperty("title", "Predict class for GCN");
        registerService(context, predictNodeGCNTaskFactory, org.cytoscape.work.TaskFactory.class, predictNodeGCNProps);
    }
}
