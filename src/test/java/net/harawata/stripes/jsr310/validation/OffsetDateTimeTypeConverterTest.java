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

import static org.junit.Assert.*;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class OffsetDateTimeTypeConverterTest {
  private Collection<ValidationError> errors;

  private OffsetDateTimeTypeConverter converter;

  @Before
  public void setUp() {
    converter = new OffsetDateTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] {};
          }
        };
      }
    };
    errors = new HashSet<>();
  }

  @Test
  public void shouldParseDefaultFormat() throws Exception {
    converter.setLocale(Locale.JAPAN);
    assertEquals(OffsetDateTime.of(2017, 3, 28, 2, 47, 24, 789000000, ZoneOffset.ofHours(9)),
        converter.convert("2017-03-28T02:47:24.789+09:00", OffsetDateTime.class, errors));
    assertEquals(OffsetDateTime.of(2017, 3, 28, 2, 47, 24, 0, ZoneOffset.ofHours(9)),
        converter.convert("2017-03-28T02:47:24+09:00", OffsetDateTime.class, errors));
    assertEquals(OffsetDateTime.of(2017, 3, 28, 2, 47, 0, 0, ZoneOffset.ofHours(9)),
        converter.convert("2017-03-28T02:47+09:00", OffsetDateTime.class, errors));
  }

  @Test
  public void shouldCustomPatternOverwritesDefaults() throws Exception {
    converter = new OffsetDateTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { OffsetDateTimeTypeConverter.KEY_PATTERNS, "M d yyyy a h:m:s [XXX][X]" } };
          }
        };
      }
    };
    converter.setLocale(Locale.US);
    assertEquals(OffsetDateTime.of(2017, 3, 28, 14, 47, 59, 0, ZoneOffset.ofHours(-6)),
        converter.convert("3 28 2017 pm 2:47:59 -06", OffsetDateTime.class, errors));
    assertEquals(OffsetDateTime.of(2017, 3, 28, 14, 47, 59, 0, ZoneOffset.ofHours(-6)),
        converter.convert("3 28 2017 pm 2:47:59 -06:00", OffsetDateTime.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.",
        converter.convert("2017-03-28T02:47:00+09:00", OffsetDateTime.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.offsetDateTime", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }
}
