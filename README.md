# Stripes JSR-310 : Formatters and TypeConverters for Java Time API

## Formatters

Formatters are used when displaying values in various Stripes tags.
Here is the list of included Formatters.

- InstantFormatter
- LocalDateFormatter
- LocalDateTimeFormatter
- LocalTimeFormatter
- MonthFormatter
- OffsetDateTimeFormatter
- OffsetTimeFormatter
- YearFormatter
- YearMonthFormatter
- ZonedDateTimeFormatter

### Configuration

For now, you need to create a custom formatter factory to use these classes.

```java
public class CustomFormatterFactory extends DefaultFormatterFactory {
  @Override
  public void init(Configuration configuration) {
    super.init(configuration);
    add(LocalDate.class, LocalDateFormatter.class);
    add(LocalTime.class, LocalTimeFormatter.class);
    add(LocalDateTime.class, LocalDateTimeFormatter.class);
    add(Instant.class, InstantFormatter.class);
    add(Month.class, MonthFormatter.class);
    add(YearMonth.class, YearMonthFormatter.class);
    add(Year.class, YearFormatter.class);
    add(OffsetDateTime.class, OffsetDateTimeFormatter.class);
    add(OffsetTime.class, OffsetTimeFormatter.class);
    add(ZonedDateTime.class, ZonedDateTimeFormatter.class);
  }
}
```

### Options

There are two options to control the format: `formatType` and `formatPattern`.

- `formatType` takes either one of `date`, `time` or `datetime`.
- `formatPattern` takes values like the following.
  - FormatStyle : `short`, `medium`, `long` or `full` which correspond to the enum [FormatStyle](https://docs.oracle.com/javase/8/docs/api/java/time/format/FormatStyle.html).
  - Predefined format : `basic_iso_date`, `iso_local_date`, `iso_zoned_date_time`, etc. See the list in the [API reference](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#predefined).
  - Custom pattern : e.g. `yyyy/MM/dd`, `HH:mm:ss`, etc. See the [API reference](https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html#patterns).

### Tip

- Each formatter has the default `formatType` and `formatPattern`. You can change them by adding an entry to the error bundle.
- You can use `<stripes:format />` tag as an alternative to JSTL's `<fmt:formatDate />` tag. 

## TypeConverters

TypeConverters are used when binding a request parameter to an action bean property.

- InstantTypeConverter
- LocalDateTimeTypeConverter
- LocalDateTypeConverter
- LocalTimeTypeConverter
- MonthTypeConverter
- OffsetDateTimeTypeConverter
- OffsetTimeTypeConverter
- YearMonthTypeConverter
- YearTypeConverter
- ZonedDateTimeTypeConverter

### Configuration

Same as the formatters, a custom type converter factory is needed to register these type converters.

```java
public class CustomTypeConverterFactory extends DefaultTypeConverterFactory {
  @Override
  public void init(Configuration configuration) {
    super.init(configuration);
    add(LocalDate.class, LocalDateTypeConverter.class);
    add(LocalTime.class, LocalTimeTypeConverter.class);
    add(LocalDateTime.class, LocalDateTimeTypeConverter.class);
    add(Instant.class, InstantTypeConverter.class);
    add(Month.class, MonthTypeConverter.class);
    add(YearMonth.class, YearMonthTypeConverter.class);
    add(Year.class, YearTypeConverter.class);
    add(OffsetDateTime.class, OffsetDateTimeTypeConverter.class);
    add(OffsetTime.class, OffsetTimeTypeConverter.class);
    add(ZonedDateTime.class, ZonedDateTimeTypeConverter.class);
  }
}
```

You may also need to add validation error messages to the error bundle.

```ini
converter.instant.invalidInput=The value ({1}) entered in field {0} must be a valid instant
converter.localDateTime.invalidInput=The value ({1}) entered in field {0} must be a valid localDateTime
converter.localDate.invalidInput=The value ({1}) entered in field {0} must be a valid localDate
converter.localTime.invalidInput=The value ({1}) entered in field {0} must be a valid localTime
converter.month.invalidInput=The value ({1}) entered in field {0} must be a valid month
converter.offsetDateTime.invalidInput=The value ({1}) entered in field {0} must be a valid offsetDateTime
converter.offsetTime.invalidInput=The value ({1}) entered in field {0} must be a valid offsetTime
converter.yearMonth.invalidInput=The value ({1}) entered in field {0} must be a valid yearMonth
converter.year.invalidInput=The value ({1}) entered in field {0} must be a valid year
converter.zonedDateTime.invalidInput=The value ({1}) entered in field {0} must be a valid zonedDateTime
```

To override the default parsing patterns, add desirable patterns in the error bundle.
If you need more than that, you may have to subclass the type converter (or create one from scratch).
For the details, please read the source code and test cases for now.

Note that these type converters are considered to be in the beta stage and the behavior might change in the future.

## Bugs, questions, suggestions, etc.

Please use the [issue tracker](https://github.com/harawata/stripes-jsr310/issues).  

## License

Apache License 2.0
