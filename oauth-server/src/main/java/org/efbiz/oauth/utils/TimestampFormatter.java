package org.efbiz.oauth.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.format.Formatter;
public class TimestampFormatter implements Formatter<java.util.Date> {

    @Override
    public String print(Date date, Locale locale) {
        return String.valueOf(date.getTime());
    }

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        long time = NumberUtils.toLong(text);
        if (time > 0) 
            return new Date(time);
        else 
            return DateTimeUtil.DEFAULT_DATE_MIN;
    }

}
