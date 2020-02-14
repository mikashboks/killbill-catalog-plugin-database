package org.killbill.billing.plugin.catalog.db;

/*
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


import org.jooby.Result;
import org.jooby.Results;
import org.jooby.Status;
import org.jooby.mvc.*;
import org.killbill.billing.osgi.libs.killbill.OSGIKillbillDataSource;
import org.killbill.billing.plugin.catalog.db.dao.CatalogDatabaseDao;
import org.killbill.billing.plugin.catalog.db.models.Plan;
import org.killbill.billing.plugin.core.resources.ExceptionResponse;
import org.killbill.billing.tenant.api.Tenant;
import org.skife.config.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.sql.SQLException;
import java.util.UUID;

// Find me on http://127.0.0.1:8080/plugins/catalog-database
@Singleton
@Path("/")
public class CatalogDatabaseServlet {

	private static final Logger logger = LoggerFactory.getLogger(CatalogDatabaseServlet.class);
	private final OSGIKillbillDataSource dataSource;
	private CatalogDatabaseDao dao;

	@Inject
	public CatalogDatabaseServlet(OSGIKillbillDataSource dataSource) {
		this.dataSource = dataSource;
		try {
			this.dao = new CatalogDatabaseDao(this.dataSource.getDataSource());
		} catch (SQLException e) {
			logger.error(e.getMessage());
		}
	}

	@GET
	@Path("/")
	public Result isListening() {
		logger.debug("Acknowledge from catalog-database plugin!!!, I am active and listening");
		return Results.ok();
	}

	@DELETE
	@Path("/plans/{id}")
	public Result inactivatePlan(@Param("id") final UUID id, @Local @Named("killbill_tenant") final Tenant tenant) {
		try {
			dao.inactivatePlan(id, tenant.getId());
			return Results.with(Status.NO_CONTENT);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return Results.with(new ExceptionResponse(e, true), Status.SERVER_ERROR);
		}
	}

	@GET
	@Path("/plans/{id}")
	public Result retrievePlan(@Param("id") final UUID id, @Local @Named("killbill_tenant") final Tenant tenant) {
		try {
			return Results.json(dao.retrievePlan(id, tenant.getId()));
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return Results.with(new ExceptionResponse(e, true), Status.SERVER_ERROR);
		}
	}

	@GET
	@Path("/plans")
	public Result listPlans(@Local @Named("killbill_tenant") final Tenant tenant) {
		try {
			return Results.json(dao.listPlans(tenant.getId()));
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			return Results.with(new ExceptionResponse(e, true), Status.SERVER_ERROR);
		}
	}

	@POST
	@Path("/plans")
	public Result addPlan(@Body final Plan payload, @Local @Named("killbill_tenant") final Tenant tenant) {
		try {
			dao.addPlan(
					payload.getId(),
					payload.getPlanId(), payload.getProductName(), payload.getProductCategory(), payload.getCurrency(),
					payload.getAmount(), payload.getBillingPeriod(), payload.getTrialLength(), payload.getTrialTimeUnit(),
					payload.getAvailableBaseProducts(), tenant.getId()
			);
			logger.info("Added new plan " + payload.getPlanId());
			return Results.with(Status.CREATED);
		} catch (SQLException e) {
			logger.error(e.getMessage());
			return Results.with(new ExceptionResponse(e, true), Status.SERVER_ERROR);
		}
	}
}

