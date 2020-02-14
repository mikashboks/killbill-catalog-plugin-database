/*
 * Copyright 2014-2020 The Billing Project, LLC
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
package org.killbill.billing.plugin.catalog.db.dao;

import org.joda.time.DateTime;
import org.jooq.impl.DSL;
import org.jooq.types.ULong;
import org.killbill.billing.catalog.api.BillingPeriod;
import org.killbill.billing.catalog.api.Currency;
import org.killbill.billing.catalog.api.ProductCategory;
import org.killbill.billing.catalog.api.TimeUnit;
import org.killbill.billing.plugin.catalog.db.dao.gen.tables.CatalogDbPlans;
import org.killbill.billing.plugin.catalog.db.dao.gen.tables.records.CatalogDbPlansRecord;
import org.killbill.billing.plugin.catalog.db.models.Plan;
import org.killbill.billing.plugin.dao.PluginDao;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CatalogDatabaseDao extends PluginDao {

	public CatalogDatabaseDao(final DataSource dataSource) throws SQLException {
		super(dataSource);
	}

	public Plan retrievePlan(final UUID id, final UUID kbTenantId) throws SQLException {
		return execute(this.dataSource.getConnection(), conn -> {
			CatalogDbPlansRecord record = DSL.using(conn, CatalogDatabaseDao.this.dialect, CatalogDatabaseDao.this.settings)
					.selectFrom(CatalogDbPlans.CATALOG_DB_PLANS)
					.where(CatalogDbPlans.CATALOG_DB_PLANS.ID.equal(id.toString()))
					.and(CatalogDbPlans.CATALOG_DB_PLANS.KB_TENANT_ID.equal(kbTenantId.toString()))
					.limit(1)
					.fetchOne();
			return convert(record);
		});
	}

	/**
	 * @param kbTenantId
	 * @return
	 * @throws SQLException
	 * @TODO limit and paging
	 */
	public List<Plan> listPlans(final UUID kbTenantId) throws SQLException {
		return execute(this.dataSource.getConnection(), conn -> {
			return DSL.using(conn, CatalogDatabaseDao.this.dialect, CatalogDatabaseDao.this.settings)
					.selectFrom(CatalogDbPlans.CATALOG_DB_PLANS)
					.where(CatalogDbPlans.CATALOG_DB_PLANS.KB_TENANT_ID.equal(String.valueOf(kbTenantId)))
					.and(CatalogDbPlans.CATALOG_DB_PLANS.IS_DELETED.isFalse())
					.fetch()
					.stream()
					.map(record -> convert(record))
					.collect(Collectors.toList());
		});

	}

	public Void activatePlan(final UUID id, final UUID kbTenantId) throws SQLException {
		return execute(this.dataSource.getConnection(), conn -> {
			DSL.using(conn, CatalogDatabaseDao.this.dialect, CatalogDatabaseDao.this.settings)
					.update(CatalogDbPlans.CATALOG_DB_PLANS)
					.set(CatalogDbPlans.CATALOG_DB_PLANS.IS_DELETED, (short) 0)
					.where(CatalogDbPlans.CATALOG_DB_PLANS.ID.equal(id.toString()))
					.execute();
			return null;
		});
	}


	public Void inactivatePlan(final UUID id, final UUID kbTenantId) throws SQLException {
		return execute(this.dataSource.getConnection(), conn -> {
			DSL.using(conn, CatalogDatabaseDao.this.dialect, CatalogDatabaseDao.this.settings)
					.update(CatalogDbPlans.CATALOG_DB_PLANS)
					.set(CatalogDbPlans.CATALOG_DB_PLANS.IS_DELETED, (short) 1)
					.where(CatalogDbPlans.CATALOG_DB_PLANS.ID.equal(id.toString()))
					.execute();
			return null;
		});
	}

	public Void addPlan(
			final UUID id,
			final String planName,
			final String productName,
			final ProductCategory productCategory,
			final Currency currency,
			final BigDecimal amount,
			final BillingPeriod billingPeriod,
			final Integer trialLength,
			final TimeUnit trialTimeUnit,
			final List<String> availableBaseProducts,
			final UUID kbTenantId
	) throws SQLException {
		DateTime now = DateTime.now();
		return execute(this.dataSource.getConnection(), conn -> {

			DSL.using(conn, CatalogDatabaseDao.this.dialect, CatalogDatabaseDao.this.settings)
					.insertInto(
							CatalogDbPlans.CATALOG_DB_PLANS,
							CatalogDbPlans.CATALOG_DB_PLANS.ID,
							CatalogDbPlans.CATALOG_DB_PLANS.PLAN_NAME,
							CatalogDbPlans.CATALOG_DB_PLANS.PRODUCT_NAME,
							CatalogDbPlans.CATALOG_DB_PLANS.PRODUCT_CATEGORY,
							CatalogDbPlans.CATALOG_DB_PLANS.CURRENCY,
							CatalogDbPlans.CATALOG_DB_PLANS.AMOUNT,
							CatalogDbPlans.CATALOG_DB_PLANS.BILLING_PERIOD,
							CatalogDbPlans.CATALOG_DB_PLANS.TRIAL_LENGTH,
							CatalogDbPlans.CATALOG_DB_PLANS.TRIAL_TIME_UNIT,
							CatalogDbPlans.CATALOG_DB_PLANS.AVAILABLE_BASE_PRODUCTS,
							CatalogDbPlans.CATALOG_DB_PLANS.KB_TENANT_ID,
							CatalogDbPlans.CATALOG_DB_PLANS.CREATED_DATE,
							CatalogDbPlans.CATALOG_DB_PLANS.UPDATED_DATE)
					.values(
						id.toString(),
						planName,
						productName,
						productCategory.name(),
						currency.name(),
						amount,
						billingPeriod.name(),
						trialLength,
						trialTimeUnit.name(),
						availableBaseProducts.stream().collect(Collectors.joining(",")),
						kbTenantId.toString(),
						toTimestamp(now),
						toTimestamp(now)
					)
					.execute();
			return null;
		});
	}

	private Plan convert(CatalogDbPlansRecord record) {
		return new Plan(
				UUID.fromString(record.getId()),
				record.getPlanName(),
				record.getProductName(),
				Enum.valueOf(ProductCategory.class, record.getProductCategory()),
				Enum.valueOf(Currency.class, record.getCurrency()),
				record.getAmount(),
				Enum.valueOf(BillingPeriod.class, record.getBillingPeriod()),
				record.getTrialLength(),
				Enum.valueOf(TimeUnit.class, record.getTrialTimeUnit()),
				Arrays.asList(record.getAvailableBaseProducts().split(","))
		);
	}
}
