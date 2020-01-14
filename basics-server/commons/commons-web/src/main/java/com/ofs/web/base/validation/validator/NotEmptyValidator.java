package com.ofs.web.base.validation.validator;

import com.ofs.web.base.validation.NotEmptyVali;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

/**
 * 非空检查
 *
 * @author ly
 */
public class NotEmptyValidator implements ConstraintValidator<NotEmptyVali, Object> {

    @Override
    public void initialize(NotEmptyVali constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        if (value.getClass().isArray()) {
            Object[] array = (Object[]) value;
            return array.length > 0;
        }
        if (value instanceof CharSequence) {
            CharSequence obj = (CharSequence) value;
            return StringUtils.isNotEmpty(obj);
        }
        if (value instanceof Collection) {
            Collection<?> clct = (Collection<?>) value;
            return !clct.isEmpty();
        }
        if (value instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) value;
            return !map.isEmpty();
        }
        if (value instanceof Enumeration) {
            Enumeration<?> e = (Enumeration<?>) value;
            return e.hasMoreElements();
        }
        if (value instanceof Iterable) {
            Iterable<?> e = (Iterable<?>) value;
            for (@SuppressWarnings("unused") Object obj : e) {
                return true;
            }
            return false;
        }
        if (value instanceof Iterator) {
            Iterator<?> e = (Iterator<?>) value;
            return e.hasNext();
        }
        return true;
    }

}
