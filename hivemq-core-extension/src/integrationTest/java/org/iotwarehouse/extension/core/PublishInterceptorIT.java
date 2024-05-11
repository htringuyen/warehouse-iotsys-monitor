package org.iotwarehouse.extension.core;

import com.hivemq.client.mqtt.mqtt5.datatypes.Mqtt5UserProperties;
import org.junit.jupiter.api.Test;

public class PublishInterceptorIT extends ExtensionBaseIT {

    @Test
    void testInterceptPublish() {
        final var WAREHOUSE_CODE = "VN-BD-001";

        final var OBS_ROLE_AUTH_NAME = "whscopeobs";
        final var OBS_ROLE_AUTH_PASSWORD = "abcd1234";
        final var OBSERVER_NAME = "warehousescopeobserver01";
        final var OBSERVER_PASSWORD = "obs1234";
        final var OBSERVER_ID = OBSERVER_NAME + "xxx";

        final var SSR_ROLE_AUTH_NAME = "tempsys";
        final var SSR_ROLE_AUTH_PASSWORD = "abcd1234";
        final var SENSOR_ID = "VN-BD-001|tempsys|001";

        var ssrClient = createClient(SENSOR_ID, SSR_ROLE_AUTH_NAME, SSR_ROLE_AUTH_PASSWORD);
        connectClient(ssrClient);

        var obsClient = createClient(OBSERVER_ID, OBS_ROLE_AUTH_NAME, OBS_ROLE_AUTH_PASSWORD);
        var userProperties = Mqtt5UserProperties.builder()
                .add("OBSERVER_NAME", OBSERVER_NAME)
                .add("OBSERVER_PASSWORD", OBSERVER_PASSWORD)
                .build();
        connectClient(obsClient, userProperties);

        // obs subscribe a topic
        var topic = WAREHOUSE_CODE + "/temperature_system/report";
        subscribeToTopic(obsClient, topic);

        // ssr publish a topic 10 times
        for (int i = 0; i < 10; i++) {
            var message = "Temperature is " + i;
            //sleepIn(10);
            publishMessage(ssrClient, topic, message);
        }

        sleepIn(1000);
    }
}
