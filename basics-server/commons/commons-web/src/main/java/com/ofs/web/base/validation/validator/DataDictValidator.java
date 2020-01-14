package com.ofs.web.base.validation.validator;


import com.ofs.web.base.validation.DataDictVali;
import com.ofs.web.knowledge.IDataDictEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ly
 */
@Slf4j
public class DataDictValidator implements ConstraintValidator<DataDictVali, String> {

    private Set<String> codeSet;

    @Override
    public void initialize(DataDictVali constraintAnnotation) {
        Class<? extends Enum<? extends IDataDictEnum>> c = constraintAnnotation.dataDictType();
        this.codeSet = new HashSet<>();
        try {
            Method m = c.getMethod("values");
            IDataDictEnum[] objArray = (IDataDictEnum[]) m.invoke(null);
            for (IDataDictEnum obj : objArray) {
                codeSet.add(obj.getCode());
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
                | InvocationTargetException e) {
            log.error(e.toString(), e);
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isEmpty(value)) {
            return true;
        }
        return codeSet.contains(value);
    }

}
