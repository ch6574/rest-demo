/*******************************************************************************
 * Copyright (c) 2015, Christopher Hill <ch6574@gmail.com>
 * GNU General Public License v3.0+ (see https://www.gnu.org/licenses/gpl-3.0.txt)
 * SPDX-License-Identifier: GPL-3.0-or-later
 ******************************************************************************/
@XmlJavaTypeAdapters({@XmlJavaTypeAdapter(type = ZonedDateTime.class, value = ZonedDateTimeAdapter.class)})
package hillc.restdemo;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;
import java.time.ZonedDateTime;

