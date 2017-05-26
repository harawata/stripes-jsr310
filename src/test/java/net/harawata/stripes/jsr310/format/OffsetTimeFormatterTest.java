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

import java.time.OffsetTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.UnsupportedTemporalTypeException;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

public class OffsetTimeFormatterTest {
  private OffsetTimeFormatter formatter;

  @Before
  public void setUp() {
    formatter = new OffsetTimeFormatter() {
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
    OffsetTime input = OffsetTime.of(14, 34, 29, 78, ZoneOffset.ofHours(-7));
    assertEquals(DateTimeFormatter.ISO_OFFSET_TIME.format(input), formatter.format(input));
  }

  @Test
  public void shouldIgnoreFormatType() throws Exception {
    OffsetTime input = OffsetTime.of(14, 34, 29, 78, ZoneOffset.ofHours(-7));
    formatter.setLocale(Locale.US);
    formatter.setFormatType("datetime");
    formatter.init();
    formatter.format(input);
    assertEquals(DateTimeFormatter.ISO_OFFSET_TIME.format(input), formatter.format(input));
  }

  @Test
  public void shouldFailWithWrongNamedPattern() throws Exception {
    Assertions.assertThatThrownBy(() -> {
      formatter.setLocale(Locale.US);
      formatter.setFormatPattern("ISO_DATE_TIME");
      formatter.init();
      OffsetTime input = OffsetTime.of(14, 34, 29, 78, ZoneOffset.ofHours(-7));
      formatter.format(input);
    }).isInstanceOf(UnsupportedTemporalTypeException.class);
  }

  @Test
  public void testNamedPattern() throws Exception {
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern("ISO_TIME");
    formatter.init();
    OffsetTime input = OffsetTime.of(14, 34, 29, 78, ZoneOffset.ofHours(-7));
    assertEquals(DateTimeFormatter.ISO_TIME.withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void testCustomPattern() throws Exception {
    String pattern = "HH-mm-ss Z";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.init();
    OffsetTime input = OffsetTime.of(14, 34, 29, 78, ZoneOffset.ofHours(-7));
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }

  @Test
  public void shouldIgnoreFormatTypeIfPatternSpecified() throws Exception {
    String pattern = "HH:mm:ss";
    formatter.setLocale(Locale.US);
    formatter.setFormatPattern(pattern);
    formatter.setFormatType("date");
    formatter.init();
    OffsetTime input = OffsetTime.of(14, 34, 29, 78, ZoneOffset.ofHours(-7));
    assertEquals(DateTimeFormatter.ofPattern(pattern).withLocale(Locale.US).format(input),
        formatter.format(input));
  }
}
