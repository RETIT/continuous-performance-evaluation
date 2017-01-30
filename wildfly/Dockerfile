FROM inspectit/jboss:wildfly10

# Add configuration
ADD configuration/ /opt/jboss/wildfly/standalone/configuration/

# Add postgresql driver & eclipselink
ADD modules/ /opt/jboss/wildfly/modules/system/layers/base/

# Add application (needs to be built and copied here first)
ADD cargo-tracker.war /opt/jboss/wildfly/standalone/deployments/

# InspectIT configuration
ENV INSPECTIT_VERSION \"1.7.6.89\"
ENV INSPECTIT_CMR_ADDR 172.18.0.3
ENV INSPECTIT_CMR_PORT 9070