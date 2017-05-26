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

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.ListResourceBundle;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.Before;
import org.junit.Test;

import net.sourceforge.stripes.validation.ScopedLocalizableError;
import net.sourceforge.stripes.validation.ValidationError;

public class InstantTypeConverterTest {
  private Collection<ValidationError> errors;

  private InstantTypeConverter converter;

  @Before
  public void setUp() {
    converter = new InstantTypeConverter() {
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
    converter.setLocale(Locale.US);
    assertEquals(Instant.ofEpochMilli(1483228800000L), converter.convert("2017-01-01T00:00:00.000Z", Instant.class, errors));
    assertEquals(Instant.ofEpochMilli(1483228800000L), converter.convert("2017-01-01T00:00:00Z", Instant.class, errors));
  }

  @Test
  public void shouldNotNormalizeInput() throws Exception {
    converter.setLocale(Locale.JAPAN);
    assertNull(converter.convert("2017/01/01T00:00:00Z", Instant.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.instant", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }

  @Test
  public void shouldCustomPatternOverwritesDefaults() throws Exception {
    converter = new InstantTypeConverter() {
      @Override
      protected ResourceBundle getErrorMessageBundle() {
        return new ListResourceBundle() {
          @Override
          protected Object[][] getContents() {
            return new Object[][] { { InstantTypeConverter.KEY_PATTERNS, "yyyy-MM-dd HH:mm X,yyyy-MM-dd HH:mm VV" } };
          }
        };
      }
    };
    converter.setLocale(Locale.US);
    assertEquals(Instant.parse("2014-05-22T02:39:00Z"), converter.convert("2014-05-22 11:39 +0900", Instant.class, errors));
    assertEquals(Instant.parse("2014-05-22T02:39:00Z"), converter.convert("2014-05-22 11:39 Asia/Tokyo", Instant.class, errors));
    errors.clear();
    assertNull("Default patterns should not be used.", converter.convert("2014-05-22T11:39:00Z", Instant.class, errors));
    assertEquals(1, errors.size());
    assertEquals("converter.instant", ((ScopedLocalizableError) errors.iterator().next()).getDefaultScope());
    assertEquals("invalidInput", ((ScopedLocalizableError) errors.iterator().next()).getKey());
  }
}
