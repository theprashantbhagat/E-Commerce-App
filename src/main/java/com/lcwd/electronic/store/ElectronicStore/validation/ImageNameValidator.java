package com.lcwd.electronic.store.ElectronicStore.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageNameValidator implements ConstraintValidator<ImageNameValid,String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        //Logic
        if(value.isBlank())
        {
            return false;
        }
        else {

            return true;
        }

    }
}
