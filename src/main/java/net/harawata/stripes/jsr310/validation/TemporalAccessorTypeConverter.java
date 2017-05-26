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

import java.time.temporal.TemporalAccessor;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.validation.TypeConverter;
import net.sourceforge.stripes.validation.ValidationError;

public abstract class TemporalAccessorTypeConverter<T extends TemporalAccessor> implements TypeConverter<T> {

  public static final String KEY_PRE_PROCESS_PATTERN = "stripes.temporalAccessorTypeConverter.preProcessPattern";

  public static final Pattern PRE_PROCESS_PATTERN = Pattern.compile("(?<=[0-9])T(?=[0-9])|[,/:\\s\\.-]+");

  public static final Pattern PATTERN_NORMALIZATION_PATTERN = Pattern.compile("([-,\\.\\s/:-]|('.*?'))+");

  protected Locale locale;

  protected Pattern preProcessPattern;

  protected LinkedHashSet<String> patterns = new LinkedHashSet<>();

  @Override
  public void setLocale(Locale locale) {
    this.locale = locale;
  }

  @Override
  public T convert(String input, Class<? extends T> targetType, Collection<ValidationError> errors) {
    preparePreProcessPattern();
    prepareInputPatterns();
    return parse(input, errors);
  }

  protected String normalizePattern(String orig) {
    return PATTERN_NORMALIZATION_PATTERN.matcher(orig).replaceAll(" ").trim();
  }

  protected void preparePreProcessPattern() {
    try {
      preProcessPattern = Pattern.compile(getResourceString(KEY_PRE_PROCESS_PATTERN));
    } catch (MissingResourceException e) {
      preProcessPattern = PRE_PROCESS_PATTERN;
    }
  }

  protected String preProcessInput(String input) {
    return replaceSeparators(input).trim();
  }

  protected String replaceSeparators(String src) {
    return preProcessPattern.matcher(src).replaceAll(" ");
  }

  protected String getResourceString(String key) throws MissingResourceException {
    return getErrorMessageBundle().getString(key);
  }

  protected ResourceBundle getErrorMessageBundle() {
    return StripesFilter.getConfiguration().getLocalizationBundleFactory().getErrorMessageBundle(locale);
  }

  protected abstract void prepareInputPatterns();

  protected abstract T parse(String input, Collection<ValidationError> errors);

}
