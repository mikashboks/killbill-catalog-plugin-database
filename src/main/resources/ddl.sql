/*! SET storage_engine=INNODB */;
drop table if exists catalog_db_plans;
create table catalog_db_plans (
  record_id serial,
  id char(36) not null,
  plan_name varchar(255) not null,
  product_name varchar(255) not null,
  product_category char(36) not null,
  currency char(3) not null,
  amount numeric(15,9) not null,
  billing_period char(36) not null,
  trial_length int not null,
  trial_time_unit char(36) not null,
  available_base_products varchar(255) not null,
  kb_tenant_id char(36) not null,
  created_date datetime not null,
  updated_date datetime not null,
  is_deleted smallint not null default 0,
  primary key(record_id)
) /*! CHARACTER SET utf8 COLLATE utf8_bin */;
create unique index catalog_db_plans_plan_id on catalog_db_plans(id);
create unique index catalog_db_plans_plan_name on catalog_db_plans(plan_name);
create unique index catalog_db_plans_product_name on catalog_db_plans(product_name);
