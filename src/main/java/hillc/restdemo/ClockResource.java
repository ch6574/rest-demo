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

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response get() {
        return Response.ok(new Payload()).build();
    }

}
