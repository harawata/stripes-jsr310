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

import java.time.YearMonth;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class YearMonthTypeConverterTest {
  private Collection<ValidationError> errors;

  private YearMonthTypeConverter converter;

  @Before
  public void setUp() {
    converter = new YearMonthTypeConverter() {
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
  public void shouldParseJapan() throws Exception {
    YearMonth expected = YearMonth.of(2014, 7);
    converter.setLocale(Locale.JAPAN);
    YearMonth actual = converter.convert("2014/7", YearMonth.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseUs() throws Exception {
    YearMonth expected = YearMonth.of(2014, 7);
    converter.setLocale(Locale.US);
    YearMonth actual = converter.convert("July 2014", YearMonth.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldParseUk() throws Exception {
    YearMonth expected = YearMonth.of(2014, 7);
    converter.setLocale(Locale.US);
    YearMonth actual = converter.convert("7 14", YearMonth.class, errors);
    assertEquals(expected, actual);
  }

  @Test
  public void shouldCustomPatternOverwritesDefaults() throws Exception {
    converter = new YearMonthTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { YearMonthTypeConverter.KEY_PATTERNS, "yyyy M" } };
          }
        };
      }
    };
    YearMonth expected = YearMonth.of(2014, 7);
    converter.setLocale(Locale.US);
    assertEquals(expected, converter.convert("2014 7", YearMonth.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("July 2014", YearMonth.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.yearMonth", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }

  @Test
  public void demoCustomPatternWithDefaultYear() throws Exception {
    converter = new YearMonthTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { YearMonthTypeConverter.KEY_PATTERNS, "[yyyy ]MMM,[yyyy ]M" } };
          }
        };
      }
    };
    converter.setLocale(Locale.US);
    assertEquals(YearMonth.of(2017, 7), converter.convert("7", YearMonth.class, errors));
    assertEquals(YearMonth.of(2017, 7), converter.convert("jul", YearMonth.class, errors));
  }
}
