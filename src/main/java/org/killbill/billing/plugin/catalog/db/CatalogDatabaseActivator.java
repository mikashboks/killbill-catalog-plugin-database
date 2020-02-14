/*
 * Copyright 2010-2014 Ning, Inc.
 * Copyright 2014-2015 Groupon, Inc
 * Copyright 2014-2015 The Billing Project, LLC
 *
 * The Billing Project licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.plugin.catalog.db;

import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.osgi.api.Healthcheck;
import org.killbill.billing.osgi.api.OSGIPluginProperties;
import org.killbill.billing.osgi.libs.killbill.KillbillActivatorBase;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillEventDispatcher;
import org.killbill.billing.plugin.api.notification.PluginConfigurationEventHandler;
import org.killbill.billing.plugin.catalog.db.dao.CatalogDatabaseDao;
import org.killbill.billing.plugin.core.resources.jooby.PluginApp;
import org.killbill.billing.plugin.core.resources.jooby.PluginAppBuilder;
import org.osgi.framework.BundleContext;
import org.osgi.service.log.LogService;

import javax.servlet.Servlet;
import javax.servlet.http.HttpServlet;
import java.util.Hashtable;

public class CatalogDatabaseActivator extends KillbillActivatorBase {

	public static final String PLUGIN_NAME = "catalog-database";
	private CatalogDatabaseConfigurationHandler helloWorldConfigurationHandler;
	private OSGIKillbillEventDispatcher.OSGIKillbillEventHandler killbillEventHandler;

	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		logService.log(LogService.LOG_INFO, "Starting " + PLUGIN_NAME);

		final CatalogPluginApi catalogPluginApi = new CatalogDatabasePluginApi(
				new CatalogDatabaseDao(dataSource.getDataSource()));
		registerCatalogPluginApi(context, catalogPluginApi);

		// Expose a healthcheck (optional), so other plugins can check on the plugin status
		final Healthcheck healthcheck = new CatalogDatabaseHealthcheck();
		registerHealthcheck(context, healthcheck);

		// Register an event listener (optional)
		killbillEventHandler = new CatalogDatabaseListener(killbillAPI);

		final PluginApp pluginApp = new PluginAppBuilder(
				PLUGIN_NAME,
				killbillAPI,
				logService,
				dataSource,
				super.clock,
				configProperties).withRouteClass(CatalogDatabaseServlet.class)
				.withRouteClass(CatalogDatabaseHealthcheckServlet.class)
				.withService(healthcheck)
				.build();

		registerServlet(context, pluginApp.createServlet(pluginApp));

		registerHandlers();
	}

	private void registerHandlers() {
		final PluginConfigurationEventHandler configHandler = new PluginConfigurationEventHandler(helloWorldConfigurationHandler);

		dispatcher.registerEventHandlers(configHandler,
				(OSGIKillbillEventDispatcher.OSGIFrameworkEventHandler) () -> dispatcher.registerEventHandlers(killbillEventHandler));
	}

	private void registerCatalogPluginApi(final BundleContext context, final CatalogPluginApi api) {
		final Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
		registrar.registerService(context, CatalogPluginApi.class, api, props);
	}

	private void registerServlet(final BundleContext context, final HttpServlet servlet) {
		final Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
		registrar.registerService(context, Servlet.class, servlet, props);
	}

	private void registerHealthcheck(final BundleContext context, final Healthcheck healthcheck) {
		final Hashtable<String, String> props = new Hashtable<String, String>();
		props.put(OSGIPluginProperties.PLUGIN_NAME_PROP, PLUGIN_NAME);
		registrar.registerService(context, Healthcheck.class, healthcheck, props);
	}
}
