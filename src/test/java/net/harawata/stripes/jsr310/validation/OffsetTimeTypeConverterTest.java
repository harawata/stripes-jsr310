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

import java.time.OffsetTime;
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

public class OffsetTimeTypeConverterTest {
  private Collection<ValidationError> errors;

  private OffsetTimeTypeConverter converter;

  @Before
  public void setUp() {
    converter = new OffsetTimeTypeConverter() {
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
    assertEquals(OffsetTime.of(2, 47, 24, 789000000, ZoneOffset.ofHours(9)), converter.convert("02:47:24.789+09:00", OffsetTime.class, errors));
    assertEquals(OffsetTime.of(2, 47, 24, 0, ZoneOffset.ofHours(9)), converter.convert("02:47:24+09:00", OffsetTime.class, errors));
    assertEquals(OffsetTime.of(2, 47, 0, 0, ZoneOffset.ofHours(9)), converter.convert("02:47+09:00", OffsetTime.class, errors));
  }

  @Test
  public void shouldCustomPatternOverwritesDefaults() throws Exception {
    converter = new OffsetTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { OffsetTimeTypeConverter.KEY_PATTERNS, "s-m-h a X" } };
          }
        };
      }
    };
    OffsetTime expected = OffsetTime.of(14, 47, 58, 0, ZoneOffset.ofHours(-3));
    converter.setLocale(Locale.US);
    assertEquals(expected, converter.convert("58-47-2 pm -03", OffsetTime.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("14:47:58-03:00", OffsetTime.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.offsetTime", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }

  @Test
  public void demoCustomPattern() throws Exception {
    converter = new OffsetTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { OffsetTimeTypeConverter.KEY_PATTERNS, "H:m:s [XXX][X]" } };
          }
        };
      }
    };
    OffsetTime expected = OffsetTime.of(14, 47, 58, 0, ZoneOffset.ofHours(-3));
    converter.setLocale(Locale.US);
    assertEquals(expected, converter.convert("14:47:58 -03", OffsetTime.class, errors));
    assertEquals(expected, converter.convert("14:47:58 -03:00", OffsetTime.class, errors));
    assertEquals(expected, converter.convert("14:47:58 -0300", OffsetTime.class, errors));
  }
}
