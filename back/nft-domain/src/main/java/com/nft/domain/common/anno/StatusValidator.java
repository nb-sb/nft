package com.nft.domain.common.anno;



import org.assertj.core.util.Arrays;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class StatusValidator implements ConstraintValidator<Status, Integer> {
    private List<Object> typeStatus;
    @Override
    public void initialize(Status constraintAnnotation) {
        typeStatus = Arrays.asList(constraintAnnotation.statusType());
        ConstraintValidator.super.initialize(constraintAnnotation);

    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
        if (value != null) {
            if (!typeStatus.contains(String.valueOf(value))) {
                // 如果值不是有效的状态类型，则添加自定义错误信息
                constraintValidatorContext.disableDefaultConstraintViolation();
                constraintValidatorContext.buildConstraintViolationWithTemplate("参数错误，支持的参数值为：" + typeStatus.toString())
                        .addConstraintViolation();
                return false;
            }
        }
        return true;

    }
}
