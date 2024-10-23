package your.org.myapp.internal;

import static org.cytoscape.work.ServiceProperties.*; //the ALL_CAPS symbols

import org.cytoscape.model.CyNetwork;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.view.model.CyNetworkView;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.work.TaskFactory; //register service
import org.cytoscape.service.util.AbstractCyActivator; //inherits from
import org.osgi.framework.BundleContext;

import java.util.Properties;

/**
 * {@code CyActivator} is a class that is a starting point for OSGi bundles.
 * 
 * A quick overview of OSGi: The common currency of OSGi is the <i>service</i>.
 * A service is merely a Java interface, along with objects that implement the
 * interface. OSGi establishes a system of <i>bundles</i>. Most bundles import
 * services. Some bundles export services. Some do both. When a bundle exports a
 * service, it provides an implementation to the service's interface. Bundles
 * import a service by asking OSGi for an implementation. The implementation is
 * provided by some other bundle.
 * 
 * When OSGi starts your bundle, it will invoke {@CyActivator}'s
 * {@code start} method. So, the {@code start} method is where
 * you put in all your code that sets up your app. This is where you import and
 * export services.
 * 
 * Your bundle's {@code Bundle-Activator} manifest entry has a fully-qualified
 * path to this class. It's not necessary to inherit from
 * {@code AbstractCyActivator}. However, we provide this class as a convenience
 * to make it easier to work with OSGi.
 *
 * Note: AbstractCyActivator already provides its own {@code stop} method, which
 * {@code unget}s any services we fetch using getService().
 */
public class CyActivator extends AbstractCyActivator {
	/**
	 * This is the {@code start} method, which sets up your app. The
	 * {@code BundleContext} object allows you to communicate with the OSGi
	 * environment. You use {@code BundleContext} to import services or ask OSGi
	 * about the status of some service.
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		Properties props = new Properties();
		props.setProperty(PREFERRED_MENU, "Apps.Submenu"); //The menu of the cytoscape app
		props.setProperty(TITLE, "A random task"); //Title of the menu item
		// Usually means the second menu item
		//props.setProperty(MENU_GRAVITY, "2.0");

		//slide, getService() la cach dung API cua OSGi
		CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
		CyNetworkManager networkManager = getService(context, CyNetworkManager.class);
		CyNetworkViewManager networkViewManager = getService(context, CyNetworkViewManager.class);
		CyNetworkViewFactory networkViewFactory = getService(context, CyNetworkViewFactory.class);

		MyTaskFactory myTaskFactory = new MyTaskFactory(networkFactory, networkManager, networkViewFactory, networkViewManager);
		//register cai myTaskFactory
		registerService(context, myTaskFactory, TaskFactory.class, props);
	}
}
//ご無沙汰しております