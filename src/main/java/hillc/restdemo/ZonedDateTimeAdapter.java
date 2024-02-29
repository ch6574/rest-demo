/*******************************************************************************
 * Copyright (c) 2015, Christopher Hill <ch6574@gmail.com>
 * GNU General Public License v3.0+ (see https://www.gnu.org/licenses/gpl-3.0.txt)
 * SPDX-License-Identifier: GPL-3.0-or-later
 ******************************************************************************/
package hillc.restdemo;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

/**
 * JAXB xml adapter for new {@link ZonedDateTime}. Hopefully not needed for long...
 */
public class ZonedDateTimeAdapter extends XmlAdapter<String, ZonedDateTime> {

    public ZonedDateTime unmarshal(String v) {
        return ZonedDateTime.parse(v);
    }

    public String marshal(ZonedDateTime v) {
        return v.toString();
    }

}
