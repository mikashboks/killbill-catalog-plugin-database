#!/usr/bin/env bash

CONTAINER=billing-mikashboksapis-com_killbill_1
VERSION=$(grep -E '<version>([0-9]+\.[0-9]+(\.[0-9]+)?)-SNAPSHOT</version>' pom.xml | sed 's/[\t \n]*<version>\(.*\)<\/version>[\t \n]*/\1/')

echo "---- Building VERSION $VERSION ----------------"

source build.sh

echo "---- Copying VERSION $VERSION  to docker ----------------"

docker cp target/catalog-database-$VERSION.jar $CONTAINER:/tmp

docker exec -it billing-mikashboksapis-com_killbill_1 script /dev/null -c "kpm install_java_plugin 'catalog-database' --from-source-file=/tmp/catalog-database-$VERSION.jar  --destination=/var/lib/killbill/bundles"

echo "---- Done deploying VERSION $VERSION  to docker.. RESTARTING CONTAINER ----------------"

docker restart $CONTAINER

echo "---- RESTARTED DOCKER CONTAINER ----------------"
