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

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class LocalDateTimeTypeConverterTest {
  private Collection<ValidationError> errors;

  private LocalDateTimeTypeConverter converter;

  @Before
  public void setUp() {
    converter = new LocalDateTimeTypeConverter() {
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
  public void shouldParseUs() throws Exception {
    converter.setLocale(Locale.US);
    assertEquals(LocalDateTime.of(2017, 1, 30, 13, 47, 0),
        converter.convert("1 30 17 1 47 pm", LocalDateTime.class, errors));
    assertEquals(LocalDateTime.of(2017, 1, 30, 13, 47, 39),
        converter.convert("1 30 17 1 47 39 pm", LocalDateTime.class, errors));
    assertEquals(LocalDateTime.of(2017, 1, 3, 8, 47, 39),
        converter.convert("Jan 3 2017 8 47 39 am", LocalDateTime.class, errors));
  }

  @Test
  public void shouldParseJapan() throws Exception {
    converter.setLocale(Locale.JAPAN);
    assertEquals(LocalDateTime.of(2017, 1, 30, 13, 47, 0),
        converter.convert("2017 1 30 13 47", LocalDateTime.class, errors));
    assertEquals(LocalDateTime.of(2017, 1, 30, 13, 47, 29),
        converter.convert("2017 1 30 13 47 29", LocalDateTime.class, errors));
  }

  @Test
  public void shouldParseUk() throws Exception {
    converter.setLocale(Locale.UK);
    assertEquals(LocalDateTime.of(2017, 1, 30, 13, 47, 0),
        converter.convert("30 01 17 13 47", LocalDateTime.class, errors));
    assertEquals(LocalDateTime.of(2017, 1, 30, 13, 47, 29),
        converter.convert("30 Jan 2017 13 47 29", LocalDateTime.class, errors));
  }

  @Test
  public void shouldCustomPatternsOverrideDefaults() throws Exception {
    converter = new LocalDateTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { LocalDateTimeTypeConverter.KEY_PATTERNS, "d M y H m s" } };
          }
        };
      }
    };
    converter.setLocale(Locale.JAPAN);
    assertEquals(LocalDateTime.of(2017, 8, 9, 18, 29, 17), converter.convert("9 8 2017 18 29 17", LocalDateTime.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("2017 9 8 18 29 17", LocalDateTime.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.localDateTime", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }

  @Test
  public void demoCustomPatternWithDefaultYear() throws Exception {
    converter = new LocalDateTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { LocalDateTimeTypeConverter.KEY_PATTERNS, "[yyyy ]M d H" } };
          }
        };
      }
    };
    converter.setLocale(Locale.JAPAN);
    assertEquals(LocalDateTime.of(2017, 8, 9, 19, 0, 0), converter.convert("8/9 19", LocalDateTime.class, errors));
    assertEquals(LocalDateTime.of(2014, 8, 9, 19, 0, 0), converter.convert("2014/8/9 19", LocalDateTime.class, errors));
  }
}
