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

import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.MissingResourceException;
import java.util.stream.Stream;

import net.sourceforge.stripes.util.Log;
import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class MonthTypeConverter extends TemporalAccessorTypeConverter<Month> {

  private static final Log LOG = Log.getInstance(MonthTypeConverter.class);

  public static final String KEY_PATTERNS = "stripes.MonthTypeConverter.patterns";

  @Override
  protected void prepareInputPatterns() {
    try {
      Stream.of(getResourceString(KEY_PATTERNS).split(", *")).forEach(patterns::add);
    } catch (MissingResourceException e) {
      // No user defined patterns.
      Stream.of("MMMM", "MMM", "M").forEach(patterns::add);
    }
  }

  protected Month parse(String input, Collection<ValidationError> errors) {
    String parseable = preProcessInput(input);
    for (String pattern : patterns) {
      DateTimeFormatter formatter = new DateTimeFormatterBuilder().parseCaseInsensitive().appendPattern(pattern).toFormatter(locale);
      try {
        return formatter.parse(parseable, Month::from);
      } catch (DateTimeParseException e) {
        LOG.debug("Could not parse '", parseable, "' using pattern '", pattern, "'. ", e.getMessage());
      }
    }
    errors.add(new ScopedLocalizableError("converter.month", "invalidInput"));
    return null;
  }

}
