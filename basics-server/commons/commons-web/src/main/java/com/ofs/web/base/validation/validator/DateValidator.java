package com.ofs.web.base.validation.validator;

import com.ofs.web.base.validation.DateVali;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期验证
 *
 * @author ly
 */
public class DateValidator implements ConstraintValidator<DateVali, String> {

    private String format;

    @Override
    public void initialize(DateVali constraintAnnotation) {
        format = constraintAnnotation.format();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        try {
            Date date = fmt.parse(value);
            return value.equals(fmt.format(date));
        } catch (ParseException e) {
            return false;
        }
    }

}
