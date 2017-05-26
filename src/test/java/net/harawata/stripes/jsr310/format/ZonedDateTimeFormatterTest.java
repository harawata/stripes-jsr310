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

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

public class ZonedDateTimeFormatterTest {
  private ZonedDateTimeFormatter formatter;

  @Before
  public void setUp() {
    formatter = new ZonedDateTimeFormatter() {
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
    formatter.setLocale(Locale.JAPAN);
    formatter.init();
    ZonedDateTime input = ZonedDateTime.of(2017, 3, 11, 14, 34, 0, 0, ZoneId.of("Asia/Tokyo"));
    assertEquals(DateTimeFormatter.ISO_ZONED_DATE_TIME.withLocale(Locale.JAPAN)
        .format(input), formatter.format(input));
  }

  @Test
  public void testNamedStyle() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("medium");
    formatter.setFormatType("datetime");
    formatter.init();
    ZonedDateTime input = ZonedDateTime.of(2017, 3, 11, 14, 34, 0, 0, ZoneId.systemDefault());
    assertEquals(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM).withLocale(Locale.US)
        .format(input), formatter.format(input));
  }

  @Test
  public void testNamedPattern() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("ISO_DATE");
    formatter.init();
    ZonedDateTime input = ZonedDateTime.of(2017, 3, 11, 14, 34, 0, 0, ZoneId.systemDefault());
    assertEquals(DateTimeFormatter.ISO_DATE.withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void testCustomPattern() throws Exception {
    String pattern = "yyyy/MM/dd HH:mm:ss";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.init();
    ZonedDateTime input = ZonedDateTime.of(2017, 3, 11, 14, 34, 0, 0, ZoneId.systemDefault());
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
    ZonedDateTime input = ZonedDateTime.of(2017, 3, 11, 14, 34, 0, 0, ZoneId.systemDefault());
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }
}
