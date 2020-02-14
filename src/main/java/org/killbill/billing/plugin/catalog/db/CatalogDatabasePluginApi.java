/*
 * Copyright 2014-2018 The Billing Project, LLC
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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import org.joda.time.DateTime;
import org.killbill.billing.ErrorCode;
import org.killbill.billing.catalog.CatalogUpdater;
import org.killbill.billing.catalog.StandaloneCatalog;
import org.killbill.billing.catalog.api.*;
import org.killbill.billing.catalog.api.user.DefaultSimplePlanDescriptor;
import org.killbill.billing.catalog.plugin.api.CatalogPluginApi;
import org.killbill.billing.catalog.plugin.api.StandalonePluginCatalog;
import org.killbill.billing.catalog.plugin.api.VersionedPluginCatalog;
import org.killbill.billing.payment.api.PluginProperty;
import org.killbill.billing.plugin.catalog.db.dao.CatalogDatabaseDao;
import org.killbill.billing.plugin.catalog.db.models.StandalonePluginCatalogModel;
import org.killbill.billing.plugin.catalog.db.models.VersionedPluginCatalogModel;
import org.killbill.billing.util.callcontext.TenantContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CatalogDatabasePluginApi implements CatalogPluginApi {

	private static final Logger logger = LoggerFactory.getLogger(CatalogDatabasePluginApi.class);

	private CatalogDatabaseDao dao;

	private String catalogName = "Product Catalog";
	private DateTime catalogVersion = new DateTime(2011, 10, 8, 0, 0);

	public CatalogDatabasePluginApi(CatalogDatabaseDao dao) throws Exception {
		this.dao = dao;
	}

	private StandaloneCatalog buildDatabaseCatalog(UUID kbTenantId) throws CatalogApiException {
		try {
			final CatalogUpdater catalogUpdater = new CatalogUpdater(
					catalogVersion, Currency.SLL, Currency.INR, Currency.USD);
			for (org.killbill.billing.plugin.catalog.db.models.Plan record : dao.listPlans(kbTenantId)) {
				logger.info("Building catalog - Adding plan " + record.getPlanId());
				catalogUpdater.addSimplePlanDescriptor(
						new DefaultSimplePlanDescriptor(
								record.getPlanId(),
								record.getProductName(),
								record.getProductCategory(),
								record.getCurrency(),
								record.getAmount(),
								record.getBillingPeriod(),
								record.getTrialLength(),
								record.getTrialTimeUnit(),
								record.getAvailableBaseProducts()
						)
				);
			}
			return catalogUpdater.getCatalog();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new CatalogApiException(ErrorCode.UNEXPECTED_ERROR, "Error generating catalog");
		}

	}

	@Override
	public DateTime getLatestCatalogVersion(Iterable<PluginProperty> properties, TenantContext context) {
		return null; // we do not want the framework to cahce
	}

	@Override
	public VersionedPluginCatalog getVersionedPluginCatalog(Iterable<PluginProperty> properties,
															TenantContext tenantContext) {

		logger.info("++++++++++++  FOUND TENANT " + tenantContext.getTenantId() + ", accountId = " + tenantContext
				.getAccountId());
		final VersionedPluginCatalog result;
		try {
			return new VersionedPluginCatalogModel(catalogName,
					toStandalonePluginCatalogs(Arrays.asList(buildDatabaseCatalog(tenantContext.getTenantId()))));
		} catch (CatalogApiException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			throw new RuntimeException(e);
		}
	}

	private Iterable<StandalonePluginCatalog> toStandalonePluginCatalogs(final List<StandaloneCatalog> input) {
		return Iterables.transform(input, input1 ->
				new StandalonePluginCatalogModel(
						new DateTime(input1.getEffectiveDate()),
						ImmutableList.copyOf(input1.getSupportedCurrencies()),
						ImmutableList.<Product>copyOf(input1.getProducts()),
						ImmutableList.<Plan>copyOf(input1.getPlans()),
						input1.getPriceLists().getDefaultPricelist(),
						ImmutableList.<PriceList>copyOf(input1.getPriceLists().getChildPriceLists()),
						input1.getPlanRules(),
						null /* ImmutableList.<Unit>copyOf(input.getCurrentUnits()) */
				));
	}
}
