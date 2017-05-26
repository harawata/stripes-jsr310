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

import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class MonthFormatterTest {
  private MonthFormatter formatter;

  @Before
  public void setUp() {
    formatter = new MonthFormatter() {
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
    Month input = Month.of(5);
    assertEquals(
        DateTimeFormatter.ofPattern(MonthFormatter.DEFAULT_FORMAT_PATTERN).withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void shouldFailWithInvalidPattern() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatPattern("yyyy-MM-dd");
      formatter.init();
      Month input = Month.of(5);
      formatter.format(input);
    }).isInstanceOf(UnsupportedTemporalTypeException.class);
  }

  @Test
  public void shouldFailWithWrongNamedPattern() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatPattern("ISO_DATE_TIME");
      formatter.init();
      Month input = Month.of(5);
      formatter.format(input);
    }).isInstanceOf(UnsupportedTemporalTypeException.class);
  }

  @Test
  public void testCustomPattern() throws Exception {
    String pattern = "MM";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.init();
    Month input = Month.of(5);
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void shouldIgnoreFormatType() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatType("time");
    formatter.init();
    Month input = Month.of(5);
    assertEquals(
        DateTimeFormatter.ofPattern(MonthFormatter.DEFAULT_FORMAT_PATTERN).withLocale(Locale.US).format(input),
        formatter.format(input));
  }
}
