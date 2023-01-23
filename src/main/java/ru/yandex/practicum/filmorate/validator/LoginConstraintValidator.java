package ru.yandex.practicum.filmorate.validator;

import ru.yandex.practicum.filmorate.annotation.NotBlankSpace;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

public class LoginConstraintValidator implements ConstraintValidator<NotBlankSpace, String > {
	@Override
	public void initialize(NotBlankSpace constraintAnnotation) {
//		ConstraintValidator.super.initialize(constraintAnnotation);
	}

	@Override
	public boolean isValid(String loginField, ConstraintValidatorContext constraintValidatorContext) {
		return !loginField.contains(" ");
	}
}
