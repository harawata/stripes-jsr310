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

import java.time.OffsetTime;
import java.util.MissingResourceException;

public class OffsetTimeFormatter extends TemporalAccessorFormatter<OffsetTime> {

  public static final String DEFAULT_FORMAT_PATTERN = "ISO_OFFSET_TIME";

  public static final String KEY_DEFAULT_FORMAT_PATTERN = "stripes.offsetTimeFormatter.defaultFormatPattern";

  protected String getDefaultFormatPattern() {
    try {
      return getResourceString(KEY_DEFAULT_FORMAT_PATTERN);
    } catch (MissingResourceException e) {
      return DEFAULT_FORMAT_PATTERN;
    }
  }

  protected String getDefaultFormatType() {
    return "time";
  }

}
