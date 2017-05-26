/**
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package net.harawata.stripes.jsr310.format;

import java.time.ZonedDateTime;
import java.util.MissingResourceException;

public class ZonedDateTimeFormatter extends TemporalAccessorFormatter<ZonedDateTime> {

  public static final String DEFAULT_FORMAT_PATTERN = "ISO_ZONED_DATE_TIME";

  public static final String KEY_DEFAULT_FORMAT_PATTERN = "stripes.zonedDateTimeFormatter.defaultFormatPattern";

  public static final String KEY_DEFAULT_FORMAT_TYPE = "stripes.zonedDateTimeFormatter.defaultFormatType";

  protected String getDefaultFormatPattern() {
    try {
      return getResourceString(KEY_DEFAULT_FORMAT_PATTERN);
    } catch (MissingResourceException e) {
      return DEFAULT_FORMAT_PATTERN;
    }
  }

  protected String getDefaultFormatType() {
    try {
      return getResourceString(KEY_DEFAULT_FORMAT_TYPE);
    } catch (MissingResourceException e) {
      return "datetime";
    }
  }

}
