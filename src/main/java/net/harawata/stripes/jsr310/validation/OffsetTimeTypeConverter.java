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
package net.harawata.stripes.jsr310.validation;

import java.time.OffsetTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.stream.Stream;

import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class OffsetTimeTypeConverter extends TemporalAccessorTypeConverter<OffsetTime> {

  private static final Log LOG = Log.getInstance(OffsetTimeTypeConverter.class);

  public static final String KEY_PATTERNS = "stripes.offsetTimeTypeConverter.patterns";

  @Override
  public OffsetTime convert(String input, Class<? extends OffsetTime> targetType, Collection<ValidationError> errors) {
    prepareInputPatterns();
    return parse(input.trim(), errors);
  }

  @Override
  protected void prepareInputPatterns() {
    try {
      Stream.of(getResourceString(KEY_PATTERNS).split(", *")).forEach(patterns::add);
    } catch (MissingResourceException e) {
      // No user defined patterns.
    }
  }

  @Override
  protected OffsetTime parse(String input, Collection<ValidationError> errors) {
    if (patterns.isEmpty()) {
      try {
        return OffsetTime.parse(input);
      } catch (DateTimeParseException e) {
        LOG.debug("Could not parse '", input, "'. ", e.getMessage());
      }
    } else {
      for (String pattern : patterns) {
        try {
          DateTimeFormatter formatter = new DateTimeFormatterBuilder()
              .parseCaseInsensitive()
              .appendPattern(pattern)
              .toFormatter(locale);
          return formatter.parse(input, OffsetTime::from);
        } catch (DateTimeParseException e) {
          LOG.debug("Could not parse '", input, "' using pattern '", pattern, "'. ", e.getMessage());
        }
      }
    }
    errors.add(new ScopedLocalizableError("converter.offsetTime", "invalidInput"));
    return null;
  }

}
