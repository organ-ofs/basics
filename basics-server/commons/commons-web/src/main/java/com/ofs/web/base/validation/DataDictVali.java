package com.ofs.web.base.validation;

import com.ofs.web.base.validation.validator.DataDictValidator;
import com.ofs.web.knowledge.IDataDictEnum;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 自动码表验证,验证值是否为自动码表中的候选值<br>
 * 可以检查 String
 *
 * @author gaoly
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DataDictValidator.class})
public @interface DataDictVali {

    String message() default "data is not in range";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        DataDictVali[] value();
    }

    /**
     * 自动码表类型
     *
     * @return
     */
    Class<? extends Enum<? extends IDataDictEnum>> dataDictType();
}
