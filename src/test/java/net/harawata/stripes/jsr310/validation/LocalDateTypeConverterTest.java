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

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class LocalDateTypeConverterTest {
  private Collection<ValidationError> errors;

  private LocalDateTypeConverter converter;

  @Before
  public void setUp() {
    converter = new LocalDateTypeConverter() {
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
  public void shouldParse() throws Exception {
    LocalDate expected = LocalDate.of(2017, 3, 27);
    converter.setLocale(Locale.JAPAN);
    LocalDate actual = converter.convert("2017/3/27", LocalDate.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseBeCaseInsensitive() throws Exception {
    LocalDate expected = LocalDate.of(2017, 3, 27);
    converter.setLocale(Locale.US);
    LocalDate actual = converter.convert("MAR 27, 2017", LocalDate.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseFull() throws Exception {
    LocalDate expected = LocalDate.of(2017, 5, 4);
    converter.setLocale(Locale.UK);
    LocalDate actual = converter.convert("thursday 4 May 2017", LocalDate.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldCustomPatternsOverrideDefaults() throws Exception {
    converter = new LocalDateTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { LocalDateTypeConverter.KEY_PATTERNS, "d M y" } };
          }
        };
      }
    };
    converter.setLocale(Locale.JAPAN);
    assertEquals(LocalDate.of(2017, 8, 9), converter.convert("9 8 2017", LocalDate.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("2017 9 8", LocalDate.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.localDate", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }

  @Test
  public void demoCustomPatternWithDefaultYear() throws Exception {
    converter = new LocalDateTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { LocalDateTypeConverter.KEY_PATTERNS, "[yyyy ]M d" } };
          }
        };
      }
    };
    converter.setLocale(Locale.JAPAN);
    assertEquals(LocalDate.of(2017, 8, 9), converter.convert("8/9", LocalDate.class, errors));
    assertEquals(LocalDate.of(2014, 8, 9), converter.convert("2014/8/9", LocalDate.class, errors));
  }
}
