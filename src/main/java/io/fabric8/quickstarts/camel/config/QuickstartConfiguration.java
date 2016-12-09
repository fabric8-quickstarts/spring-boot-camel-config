package io.fabric8.quickstarts.camel.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "quickstart")
public class QuickstartConfiguration {

    /**
     * A comma-separated list of routes to use as recipients for messages.
     */
    private String recipients;

    /**
     * The username to use when connecting to the async queue (simulation)
     */
    private String queueUsername;

    /**
     * The password to use when connecting to the async queue (simulation)
     */
    private String queuePassword;

    public String getRecipients() {
        return recipients;
    }

    public void setRecipients(String recipients) {
        this.recipients = recipients;
    }

    public String getQueueUsername() {
        return queueUsername;
    }

    public void setQueueUsername(String queueUsername) {
        this.queueUsername = queueUsername;
    }

    public String getQueuePassword() {
        return queuePassword;
    }

    public void setQueuePassword(String queuePassword) {
        this.queuePassword = queuePassword;
    }

}
