/*
 * This file is generated by jOOQ.
*/
package org.killbill.billing.plugin.catalog.db.dao.gen.tables;


import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Identity;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;
import org.jooq.types.ULong;
import org.killbill.billing.plugin.catalog.db.dao.gen.Keys;
import org.killbill.billing.plugin.catalog.db.dao.gen.Killbill;
import org.killbill.billing.plugin.catalog.db.dao.gen.tables.records.CatalogDbPlansRecord;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.9.1"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class CatalogDbPlans extends TableImpl<CatalogDbPlansRecord> {

    private static final long serialVersionUID = 1132219315;

    /**
     * The reference instance of <code>killbill.catalog_db_plans</code>
     */
    public static final CatalogDbPlans CATALOG_DB_PLANS = new CatalogDbPlans();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<CatalogDbPlansRecord> getRecordType() {
        return CatalogDbPlansRecord.class;
    }

    /**
     * The column <code>killbill.catalog_db_plans.record_id</code>.
     */
    public final TableField<CatalogDbPlansRecord, ULong> RECORD_ID = createField("record_id", org.jooq.impl.SQLDataType.BIGINTUNSIGNED.nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.id</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> ID = createField("id", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.plan_name</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> PLAN_NAME = createField("plan_name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.product_name</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> PRODUCT_NAME = createField("product_name", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.product_category</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> PRODUCT_CATEGORY = createField("product_category", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.currency</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> CURRENCY = createField("currency", org.jooq.impl.SQLDataType.CHAR.length(3).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.amount</code>.
     */
    public final TableField<CatalogDbPlansRecord, BigDecimal> AMOUNT = createField("amount", org.jooq.impl.SQLDataType.DECIMAL.precision(15, 9).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.billing_period</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> BILLING_PERIOD = createField("billing_period", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.trial_length</code>.
     */
    public final TableField<CatalogDbPlansRecord, Integer> TRIAL_LENGTH = createField("trial_length", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.trial_time_unit</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> TRIAL_TIME_UNIT = createField("trial_time_unit", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.available_base_products</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> AVAILABLE_BASE_PRODUCTS = createField("available_base_products", org.jooq.impl.SQLDataType.VARCHAR.length(255).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.kb_tenant_id</code>.
     */
    public final TableField<CatalogDbPlansRecord, String> KB_TENANT_ID = createField("kb_tenant_id", org.jooq.impl.SQLDataType.CHAR.length(36).nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.created_date</code>.
     */
    public final TableField<CatalogDbPlansRecord, Timestamp> CREATED_DATE = createField("created_date", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.updated_date</code>.
     */
    public final TableField<CatalogDbPlansRecord, Timestamp> UPDATED_DATE = createField("updated_date", org.jooq.impl.SQLDataType.TIMESTAMP.nullable(false), this, "");

    /**
     * The column <code>killbill.catalog_db_plans.is_deleted</code>.
     */
    public final TableField<CatalogDbPlansRecord, Short> IS_DELETED = createField("is_deleted", org.jooq.impl.SQLDataType.SMALLINT.nullable(false).defaultValue(org.jooq.impl.DSL.inline("0", org.jooq.impl.SQLDataType.SMALLINT)), this, "");

    /**
     * Create a <code>killbill.catalog_db_plans</code> table reference
     */
    public CatalogDbPlans() {
        this("catalog_db_plans", null);
    }

    /**
     * Create an aliased <code>killbill.catalog_db_plans</code> table reference
     */
    public CatalogDbPlans(String alias) {
        this(alias, CATALOG_DB_PLANS);
    }

    private CatalogDbPlans(String alias, Table<CatalogDbPlansRecord> aliased) {
        this(alias, aliased, null);
    }

    private CatalogDbPlans(String alias, Table<CatalogDbPlansRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Killbill.KILLBILL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Identity<CatalogDbPlansRecord, ULong> getIdentity() {
        return Keys.IDENTITY_CATALOG_DB_PLANS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<CatalogDbPlansRecord> getPrimaryKey() {
        return Keys.KEY_CATALOG_DB_PLANS_PRIMARY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<CatalogDbPlansRecord>> getKeys() {
        return Arrays.<UniqueKey<CatalogDbPlansRecord>>asList(Keys.KEY_CATALOG_DB_PLANS_PRIMARY, Keys.KEY_CATALOG_DB_PLANS_RECORD_ID, Keys.KEY_CATALOG_DB_PLANS_CATALOG_DB_PLANS_PLAN_ID, Keys.KEY_CATALOG_DB_PLANS_CATALOG_DB_PLANS_PLAN_NAME, Keys.KEY_CATALOG_DB_PLANS_CATALOG_DB_PLANS_PRODUCT_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CatalogDbPlans as(String alias) {
        return new CatalogDbPlans(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public CatalogDbPlans rename(String name) {
        return new CatalogDbPlans(name, null);
    }
}
