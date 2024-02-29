/*******************************************************************************
 * Copyright (c) 2015, Christopher Hill <ch6574@gmail.com>
 * GNU General Public License v3.0+ (see https://www.gnu.org/licenses/gpl-3.0.txt)
 * SPDX-License-Identifier: GPL-3.0-or-later
 ******************************************************************************/
package hillc.restdemo;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

/**
 * Root resource (exposed at "clock")
 */
@Path("clock")
public class ClockResource {

    /**
     * This is the REST payload that will be returned to the caller
     */
    @XmlRootElement
    static class Payload {
        @XmlElement
        final String provider = "clock demo";

        @XmlElement
        ZonedDateTime dateTime;

        @XmlElement
        ZonedDateTime utcDateTime;

        @XmlElement
        UUID uuid;

        Payload() {
            Instant timestamp = Instant.now();
            dateTime = timestamp.atZone(ZoneId.systemDefault());
            utcDateTime = timestamp.atZone(ZoneOffset.UTC);
            uuid = UUID.randomUUID();
        }
    }

    /**
     * Method handling HTTP GET requests. The returned object can be sent to the client as JSON or XML.
     *
     * @return encoded {@link Payload}
     */
    @GET
    @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
    public Response get() {
        return Response.ok(new Payload()).build();
    }

}
