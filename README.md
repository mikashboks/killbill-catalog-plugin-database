# killbill-catalog-plugin-database

Plugin that allows plans and products to be saved to the database and catalog built dynamically from the database.
This is meant to enable use in applications that require constant changes to the catalog without havivng to constantly edit xml files

This plugin allows plans and products to be managed via a rest call
The plugin will load the catalog from the database.



## Requirements

The plugin needs a database. The latest version of the schema can be found [here](src/main/resources/ddl.sql).


## Usage

### Creating a new Plan

```
curl -v \
     -X POST \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: admin' \
     -H 'Content-Type: application/json' \
     -d '{ "id": "fc07bb1f-a3db-496a-90bf-409cfb6c5d66", "planName": "Test Plan", "productName": "Test Product", "productCategory": "BASE", "currency": "SLL", "amount": 1000, "billingPeriod": "DAILY", "trialLength": 30, "trialTimeUnit": "DAYS", "availableBaseProducts": [] }' \
     http://127.0.0.1:8080/plugins/catalog-database/plans
```

### List all Plans

```
curl -v \
     -X GET \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: admin' \
     -H 'Content-Type: application/json' \
     http://127.0.0.1:8080/plugins/catalog-database/plans
```


### Get A Plan

```
curl -v \
     -X GET \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: admin' \
     -H 'Content-Type: application/json' \
     http://127.0.0.1:8080/plugins/catalog-database/plans/:id
```

### Inactivate Plan

```
curl -v \
     -X DELETE \
     -u admin:password \
     -H 'X-Killbill-ApiKey: bob' \
     -H 'X-Killbill-ApiSecret: lazar' \
     -H 'X-Killbill-CreatedBy: admin' \
     -H 'Content-Type: application/json' \
     http://127.0.0.1:8080/plugins/catalog-database/plans/:id
```

## TODO
- Ability to edit plans
- Ability to enable per tenant and if not enabled on a tenant we use the normal catalog
- Caching on a per-client basis to improve performance



