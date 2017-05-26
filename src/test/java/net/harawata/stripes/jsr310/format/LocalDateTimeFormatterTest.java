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

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class LocalDateTimeFormatterTest {
  private LocalDateTimeFormatter formatter;

  @Before
  public void setUp() {
    formatter = new LocalDateTimeFormatter() {
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
    LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
    assertEquals(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT).withLocale(Locale.US)
        .format(input), formatter.format(input));
  }

  @Test
  public void testNamedStyle() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("medium");
    formatter.setFormatType("datetime");
    formatter.init();
    LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
    assertEquals(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US)
        .format(input), formatter.format(input));
  }

  @Test
  public void shouldLongDateTimeFail() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatPattern("Long");
      formatter.setFormatType("datetime");
      formatter.init();
      LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
      formatter.format(input);
    }).isInstanceOf(DateTimeException.class);
  }

  @Test
  public void shouldFullDateTimeFail() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatPattern("FULL");
      formatter.setFormatType("datetime");
      formatter.init();
      LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
      formatter.format(input);
    }).isInstanceOf(DateTimeException.class);
  }

  @Test
  public void testNamedPattern() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("ISO_DATE");
    formatter.init();
    LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
    assertEquals(DateTimeFormatter.ISO_DATE.withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void testNamedPattern2() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("ISO_TIME");
    formatter.init();
    LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
    assertEquals(DateTimeFormatter.ISO_TIME.withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void testCustomPattern() throws Exception {
    String pattern = "yyyy/MM/dd HH:mm:ss";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.init();
    LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void shouldIgnoreFormatTypeIfPatternSpecified() throws Exception {
    String pattern = "yyyy/MM/dd HH:mm:ss";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.setFormatType("date");
    formatter.init();
    LocalDateTime input = LocalDateTime.of(2017, 3, 11, 14, 34);
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }
}
