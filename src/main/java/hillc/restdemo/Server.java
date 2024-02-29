/*******************************************************************************
 * Copyright (c) 2015, Christopher Hill <ch6574@gmail.com>
 * GNU General Public License v3.0+ (see https://www.gnu.org/licenses/gpl-3.0.txt)
 * SPDX-License-Identifier: GPL-3.0-or-later
 ******************************************************************************/
package hillc.restdemo;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.ssl.SSLContextConfigurator;
import org.glassfish.grizzly.ssl.SSLEngineConfigurator;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import javax.print.attribute.standard.ReferenceUriSchemesSupported;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Logger;

/**
 * Main server class. Starts HTTP and HTTPS Grizzly servers and registers JAX-RS resources from same package at /rest-demo
 */
public class Server {
    private static final Logger log = Logger.getLogger(Server.class.getName());

    // Base URI the Grizzly servers listen on (HTTP and HTTPS)
    public URI serverUri;
    public URI secureServerUri;

    private HttpServer server;
    private HttpServer secureServer;

    // Helper to build URIs for "/rest-demo" on localhost only.
    private URI getBaseUri(int port, ReferenceUriSchemesSupported protocol) {
        return UriBuilder.fromPath("").scheme(protocol.toString()).host("localhost").port(port).path("rest-demo").build();
    }

    // Resource config that scans for JAX-RS resources and providers in hillc.restdemo package
    private final ResourceConfig rc = new ResourceConfig().packages("hillc.restdemo");

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     *
     * @param port Port to start server running on. A value of 0 will result in a random ephemeral port being used
     */
    public void startServer(int port) {
        // Shut it down if already running
        if (server != null && server.isStarted())
            server.shutdown();

        // Create and start a new instance of grizzly http server exposing the Jersey application
        serverUri = getBaseUri(port, ReferenceUriSchemesSupported.HTTP);
        server = GrizzlyHttpServerFactory.createHttpServer(serverUri, rc);

        // If started on random port, update URI to reflect what was used
        if (port == 0) {
            serverUri = UriBuilder.fromUri(serverUri).port(server.getListener("grizzly").getPort()).build();
        }
    }

    /**
     * Starts Grizzly HTTPS server exposing JAX-RS resources defined in this application.
     *
     * @param port Port to start server running on. A value of 0 will result in a random ephemeral port being used
     */
    public void startSecureServer(int port) {
        // Shut it down if already running
        if (secureServer != null && secureServer.isStarted())
            secureServer.shutdown();

        // Security context
        SSLContextConfigurator sslContext = new SSLContextConfigurator();
        sslContext.setKeyStoreFile("./keystore_server"); // contains server keypair
        sslContext.setKeyStorePass("qwerty");
        sslContext.setTrustStoreFile("./truststore_server"); // contains client certificate
        sslContext.setTrustStorePass("qwerty");

        // Create and start a new instance of grizzly https server exposing the Jersey application
        secureServerUri = getBaseUri(port, ReferenceUriSchemesSupported.HTTPS);
        secureServer = GrizzlyHttpServerFactory.createHttpServer(secureServerUri, rc, true,
                new SSLEngineConfigurator(sslContext).setClientMode(false));

        // If started on random port, update URI to reflect what was used
        if (port == 0) {
            secureServerUri = UriBuilder.fromUri(secureServerUri).port(secureServer.getListener("grizzly").getPort()).build();
        }
    }

    /**
     * Stops all running servers
     */
    public void stopServers() {
        if (server != null && server.isStarted())
            server.shutdown();
        if (secureServer != null && secureServer.isStarted())
            secureServer.shutdown();
    }

    /**
     * Main method. Starts http server on port 8080, and starts https server on port 8081.
     */
    public static void main(String[] args) throws IOException {
        Server server = new Server();

        server.startServer(8080);
        log.info(String.format("Jersey app started with WADL available at " + "%s/application.wadl", server.serverUri));

        server.startSecureServer(8081);
        log.info(String.format("Jersey app started with WADL available at " + "%s/application.wadl", server.secureServerUri));

        System.out.println("Hit enter to shutdown...");
        System.in.read();
        server.stopServers();
    }
}
