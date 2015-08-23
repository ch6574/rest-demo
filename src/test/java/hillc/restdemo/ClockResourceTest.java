/*******************************************************************************
 * Copyright (C) 2015, Christopher Hill <ch6574@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package hillc.restdemo;

import static org.junit.Assert.assertNotNull;
import hillc.restdemo.ClockResource.Payload;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.SslConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class ClockResourceTest {

    private static WebTarget target;
    private static WebTarget secureTarget;
    private static Server server;

    @BeforeClass
    public static void setUp() throws Exception {

        // Start servers on ephemeral ports for unit testing
        server = new Server();
        server.startServer(0);
        server.startSecureServer(0);

        ClientBuilder clientBuilder = ClientBuilder.newBuilder();

        // HTTP
        target = clientBuilder.build().target(server.serverUri);

        // HTTPS
        SslConfigurator sslConfig = SslConfigurator.newInstance().trustStoreFile("./truststore_client").trustStorePassword("qwerty")
                .keyStoreFile("./keystore_client").keyPassword("qwerty");
        clientBuilder.sslContext(sslConfig.createSSLContext());
        secureTarget = clientBuilder.build().target(server.secureServerUri);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        server.stopServers();
    }

    /**
     * Test the {@link Payload} sent in response. Test both JSON and XML transports. HTTP.
     */
    @Test
    public void testGet() {
        // HTTP
        Payload clockJson = target.path("clock").request(MediaType.APPLICATION_JSON_TYPE).get(Payload.class);
        Payload clockXml = target.path("clock").request(MediaType.APPLICATION_XML_TYPE).get(Payload.class);

        assertNotNull("JSON Timestamps are populated", clockJson.dateTime);
        assertNotNull("JSON UTC Timestamps are populated", clockJson.utcDateTime);
        assertNotNull("XML Timestamps are populated", clockXml.dateTime);
        assertNotNull("XML UTC Timestamps are populated", clockXml.utcDateTime);
    }

    /**
     * Test the {@link Payload} sent in response. Test both JSON and XML transports. HTTPS.
     */
    @Test
    public void testGetSecure() {
        // HTTPS
        Payload clockJson = secureTarget.path("clock").request(MediaType.APPLICATION_JSON_TYPE).get(Payload.class);
        Payload clockXml = secureTarget.path("clock").request(MediaType.APPLICATION_XML_TYPE).get(Payload.class);

        assertNotNull("JSON Timestamps are populated", clockJson.dateTime);
        assertNotNull("JSON UTC Timestamps are populated", clockJson.utcDateTime);
        assertNotNull("XML Timestamps are populated", clockXml.dateTime);
        assertNotNull("XML UTC Timestamps are populated", clockXml.utcDateTime);
    }
}
