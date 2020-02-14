JOOQ_VERSION=3.9.1
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -Dartifact=org.jooq:jooq:$JOOQ_VERSION -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -Dartifact=org.jooq:jooq-meta:$JOOQ_VERSION -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:2.1:get -Dartifact=org.jooq:jooq-codegen:$JOOQ_VERSION -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.1:get -Dartifact=jakarta.xml.bind:jakarta.xml.bind-api:2.3.2 -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.1:get -Dartifact=org.glassfish.jaxb:jaxb-runtime:2.3.2 -DrepoUrl=http://sonatype.org
mvn org.apache.maven.plugins:maven-dependency-plugin:3.1.1:get -Dartifact=javax.xml.bind:jaxb-api:2.3.1 -DrepoUrl=http://sonatype.org

M2_REPOS=~/.m2/repository;
JOOQ="$M2_REPOS/org/jooq";
MYSQL_VERSION=5.1.33
MYSQL="$M2_REPOS/mysql/mysql-connector-java/$MYSQL_VERSION/mysql-connector-java-$MYSQL_VERSION.jar";
JAVAX="$M2_REPOS/javax/xml/bind/jaxb-api/2.3.1/jaxb-api-2.3.1.jar:$M2_REPOS/org/glassfish/jaxb/jaxb-runtime/2.3.2:$M2_REPOS/javax/activation/javax.activation-api/1.2.0/javax.activation-api-1.2.0.jar";
JARS="$JAVAX:$JOOQ/jooq/$JOOQ_VERSION/jooq-$JOOQ_VERSION.jar:$JOOQ/jooq-meta/$JOOQ_VERSION/jooq-meta-$JOOQ_VERSION.jar:$JOOQ/jooq-codegen/$JOOQ_VERSION/jooq-codegen-$JOOQ_VERSION.jar:$MYSQL:.";

java -cp $JARS org.jooq.util.GenerationTool src/main/resources/gen.xml
