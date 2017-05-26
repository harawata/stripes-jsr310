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

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class LocalDateFormatterTest {
  private LocalDateFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LocalDateFormatter() {
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
  }

  @Test
  public void testDefaultFormat() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.init();
    LocalDate input = LocalDate.of(2017, 4, 27);
    assertEquals(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(Locale.US)
        .format(input), formatter.format(input));
  }

  @Test
  public void shouldFailIfTypeIsDatetime() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatType("datetime");
      formatter.init();
      LocalDate input = LocalDate.of(2017, 4, 27);
      formatter.format(input);
    }).isInstanceOf(UnsupportedTemporalTypeException.class);
  }

  @Test
  public void shouldFailWithIncompatibleNamedPattern() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatPattern("ISO_DATE_TIME");
      formatter.init();
      LocalDate input = LocalDate.of(2017, 4, 27);
      formatter.format(input);
    }).isInstanceOf(UnsupportedTemporalTypeException.class);
  }

  @Test
  public void testNamedPattern() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("iso_date");
    formatter.init();
    LocalDate input = LocalDate.of(2017, 4, 27);
    assertEquals(DateTimeFormatter.ISO_DATE.withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void testCustomPattern() throws Exception {
    String pattern = "yyyy/MM/dd";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.init();
    LocalDate input = LocalDate.of(2017, 4, 27);
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void shouldIgnoreFormatTypeIfPatternSpecified() throws Exception {
    String pattern = "y/M/d";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.setFormatType("datetime");
    formatter.init();
    LocalDate input = LocalDate.of(2017, 4, 27);
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }
}
