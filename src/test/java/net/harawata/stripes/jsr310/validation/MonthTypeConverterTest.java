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

import java.time.Month;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class MonthTypeConverterTest {
  private Collection<ValidationError> errors;

  private MonthTypeConverter converter;

  @Before
  public void setUp() {
    converter = new MonthTypeConverter() {
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
  public void shouldParseFourDigitsMonth() throws Exception {
    converter.setLocale(Locale.US);
    assertEquals(Month.of(7), converter.convert("7", Month.class, errors));
  }

  @Test
  public void shouldParseTwoDigitsMonth() throws Exception {
    converter.setLocale(Locale.UK);
    assertEquals(Month.of(2), converter.convert("02", Month.class, errors));
  }

  @Test
  public void shouldParseShortName() throws Exception {
    converter.setLocale(Locale.US);
    assertEquals(Month.of(2), converter.convert("feb", Month.class, errors));
  }

  @Test
  public void shouldParseFullName() throws Exception {
    converter.setLocale(Locale.US);
    assertEquals(Month.of(2), converter.convert("february", Month.class, errors));
  }

  @Test
  public void shouldCustomPatternOverwritesDefaults() throws Exception {
    converter = new MonthTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { MonthTypeConverter.KEY_PATTERNS, "(M)" } };
          }
        };
      }
    };
    Month expected = Month.of(7);
    converter.setLocale(Locale.US);
    assertEquals(expected, converter.convert("(7)", Month.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("7", Month.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.month", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }
}
