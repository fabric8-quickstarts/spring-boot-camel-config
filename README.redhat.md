# Spring Boot and Camel using ConfigMaps and Secrets 

This quickstart demonstrates how to configure a Spring-Boot application using Openshift ConfigMaps and Secrets.

A route generates sample messages that are delivered to a list of recipient endpoints configured through a property named `quickstart.recipients` in the `src/main/resources/application.properties` file.
The property can be overridden using a Openshift ConfigMap object.
As soon as a ConfigMap named `camel-config` (containing a property named `application.properties`) is created or changed in the project, an application-context refresh event will be triggered and the logs will reflect the new configuration. 
A sample `ConfigMap` (`sample-configmap.yml`) is contained in this repository (it changes the configuration to use all available endpoints in the `recipientList`). 

The quickstart will run on Openshift using a `ServiceAccount` named `qs-camel-config`, with the `view` role granted.
This way, the application is allowed to read the `ConfigMap` and to listen for changes in the current Openshift project.

Secrets can also be used to configure the application (a sample username/password combination is configured using secrets in this quickstart).
Unlike the `ConfigMap` objects, secrets require higher permissions in order to be read using the Openshift APIs.
To overcome this security limitation, the approach used in this quickstart is to mount the secret as a volume in the Pod and 
configure its location in the spring-cloud config file (`src/main/resources/bootstrap.yml`).

A sample secret (`sample-secret.yml`) is contained in this repository (it just replaces the username `wrong-username` with `myuser`). 

**Note: a secret named `camel-config` must be present in the namespace before the application is deployed**
(otherwise the container remains in a pending status, waiting for it).

### Building

The example can be built with

    mvn clean install


### Running the example in OpenShift

It is assumed that:
- OpenShift platform is already running, if not you can find details how to [Install OpenShift at your site](https://docs.openshift.com/container-platform/3.3/install_config/index.html).
- Your system is configured for Fabric8 Maven Workflow, if not you can find a [Get Started Guide](https://access.redhat.com/documentation/en/red-hat-jboss-middleware-for-openshift/3/single/red-hat-jboss-fuse-integration-services-20-for-openshift/)

The following command will create the (**required**) secret:

    oc create -f sample-secret.yml

The following command can be used to create the ConfigMap (the ConfigMap can be also created after the application has been deployed, to see the live-reload feature in action):

    oc create -f sample-configmap.yml

Then the following command will package your app and run it on Openshift:

    mvn fabric8:deploy

To list all the running pods:

    oc get pods

Then find the name of the pod that runs this quickstart, and output the logs from the running pods with:

    oc logs <name of pod>

You can also use the openshift [web console](https://docs.openshift.com/container-platform/3.3/getting_started/developers_console.html#developers-console-video) to manage the
running pods, and view logs and much more.

### Running via an S2I Application Template

Applicaiton templates allow you deploy applications to OpenShift by filling out a form in the OpenShift console that allows you to adjust deployment parameters.  This template uses an S2I source build so that it handle building and deploying the application for you.

First, import the Fuse image streams:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/fis-image-streams.json

Then create the quickstart template:

    oc create -f https://raw.githubusercontent.com/jboss-fuse/application-templates/GA/quickstarts/spring-boot-camel-config-template.json

Now when you use "Add to Project" button in the OpenShift console, you should see a template for this quickstart. 

### Integration Testing

The example includes a [fabric8 arquillian](https://github.com/fabric8io/fabric8/tree/v2.2.170.redhat/components/fabric8-arquillian) OpenShift Integration Test. 
Once the container image has been built and deployed in OpenShift, the integration test can be run with:

    mvn test -Dtest=*KT

The test is disabled by default and has to be enabled using `-Dtest`. Open Source Community documentation at [Integration Testing](https://fabric8.io/guide/testing.html) and [Fabric8 Arquillian Extension](https://fabric8.io/guide/arquillian.html) provide more information on writing full fledged black box integration tests for OpenShift. 
