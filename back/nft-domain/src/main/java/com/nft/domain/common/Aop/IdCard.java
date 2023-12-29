package com.nft.domain.common.Aop;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * 身份证号码。字符串必须是格式正确的身份证号码。
 * <p>
 * {@code null} 或 空字符串，是有效的（能够通过校验）。
 * <p>
 * 支持的类型：字符串
 *
 * @author songguanxun
 * @since 1.0
 */
@Target({FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = IdCardValidator.class)
public @interface IdCard {

    /**
     * @return the error message template
     */
    String message() default "身份证号码，格式错误";

    /**
     * @return the groups the constraint belongs to
     */
    Class<?>[] groups() default {};

    /**
     * @return the payload associated to the constraint
     */
    Class<? extends Payload>[] payload() default {};

}

