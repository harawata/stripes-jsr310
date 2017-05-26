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

import java.time.LocalTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class LocalTimeTypeConverterTest {
  private Collection<ValidationError> errors;

  private LocalTimeTypeConverter converter;

  @Before
  public void setUp() {
    converter = new LocalTimeTypeConverter() {
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
  public void shouldParseHm() throws Exception {
    LocalTime expected = LocalTime.of(2, 47, 0);
    converter.setLocale(Locale.JAPAN);
    LocalTime actual = converter.convert("2:47", LocalTime.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseNonColonSeparator() throws Exception {
    LocalTime expected = LocalTime.of(2, 47, 58);
    converter.setLocale(Locale.JAPAN);
    LocalTime actual = converter.convert("2.47-58", LocalTime.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseHms() throws Exception {
    LocalTime expected = LocalTime.of(2, 47, 58);
    converter.setLocale(Locale.JAPAN);
    LocalTime actual = converter.convert("2:47:58", LocalTime.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseHmsz() throws Exception {
    LocalTime expected = LocalTime.of(2, 47, 58);
    converter.setLocale(Locale.JAPAN);
    assertEquals(expected, converter.convert("2:47:58 ET", LocalTime.class, errors));
    assertEquals(expected, converter.convert("2:47:58 jst", LocalTime.class, errors));
  }

  @Test
  public void shouldParseHmsa() throws Exception {
    LocalTime expected = LocalTime.of(14, 47, 58);
    converter.setLocale(Locale.US);
    assertEquals(expected, converter.convert("2:47:58 pm", LocalTime.class, errors));
  }

  @Test
  public void shouldCustomPatternOverwritesDefaults() throws Exception {
    converter = new LocalTimeTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { LocalTimeTypeConverter.KEY_PATTERNS, "s m H, s H m" } };
          }
        };
      }
    };
    LocalTime expected = LocalTime.of(14, 47, 58);
    converter.setLocale(Locale.JAPAN);
    assertEquals(expected, converter.convert("58-47-14", LocalTime.class, errors));
    assertEquals(expected, converter.convert("58-14-47", LocalTime.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("14:47:58", LocalTime.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.localTime", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }
}
