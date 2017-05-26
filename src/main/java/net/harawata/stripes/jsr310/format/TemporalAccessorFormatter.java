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

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.format.Formatter;

/**
 * Subclasses implement Formatter only because Stripes cannot auto-detect them without it. ReflectUtil#getActualTypeArguments cannot resolve type variables correctly .
 * 
 * @author Iwao AVE!
 * @param <T>
 */
public abstract class TemporalAccessorFormatter<T extends TemporalAccessor> implements Formatter<T> {

  protected Locale locale;

  protected String formatType;

  protected String formatPattern;

  protected DateTimeFormatter formatter;

  public Locale getLocale() {
    return locale;
  }

  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  public String getFormatType() {
    return formatType;
  }

  public void setFormatType(String formatType) {
    this.formatType = formatType;
  }

  public String getFormatPattern() {
    return formatPattern;
  }

  public void setFormatPattern(String formatPattern) {
    this.formatPattern = formatPattern;
  }

  public void init() {
    FormatStyle style = null;
    if (formatPattern == null) {
      formatPattern = getDefaultFormatPattern();
    }

    formatter = resolveConst(formatPattern);
    if (formatter != null) {
      return;
    }

    style = resolveFormatStyle(formatPattern);
    if (style != null) {
      if (formatType == null) {
        formatType = getDefaultFormatType();
      }
      if ("date".equalsIgnoreCase(formatType)) {
        formatter = DateTimeFormatter.ofLocalizedDate(style).withLocale(locale);
      } else if ("time".equalsIgnoreCase(formatType)) {
        formatter = DateTimeFormatter.ofLocalizedTime(style).withLocale(locale);
      } else if ("datetime".equalsIgnoreCase(formatType)) {
        formatter = DateTimeFormatter.ofLocalizedDateTime(style).withLocale(locale);
      } else {
        throw new StripesRuntimeException("Invalid formatType for Date: " + formatType + ". Allowed types are 'date', 'time' and 'datetime'.");
      }
      return;
    }
    formatter = DateTimeFormatter.ofPattern(formatPattern, locale);
  }

  protected static FormatStyle resolveFormatStyle(String styleName) {
    try {
      if (styleName != null) {
        return FormatStyle.valueOf(styleName.toUpperCase(Locale.ENGLISH));
      }
    } catch (IllegalArgumentException e) {
      // expected
    }
    return null;
  }

  protected static DateTimeFormatter resolveConst(String constName) {
    if (constName == null || constName.isEmpty()) {
      return null;
    }
    try {
      return (DateTimeFormatter) DateTimeFormatter.class.getDeclaredField(constName.toUpperCase(Locale.ENGLISH)).get(null);
    } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
      // expected
    }
    return null;
  }

  protected String getResourceString(String key) throws MissingResourceException {
    return getErrorMessageBundle().getString(key);
  }

  protected ResourceBundle getErrorMessageBundle() {
    return StripesFilter.getConfiguration().getLocalizationBundleFactory().getErrorMessageBundle(locale);
  }

  public String format(T input) {
    return formatter.format(input);
  }

  protected abstract String getDefaultFormatPattern();

  protected abstract String getDefaultFormatType();

}
