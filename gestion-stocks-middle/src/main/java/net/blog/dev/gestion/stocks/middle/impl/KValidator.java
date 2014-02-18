/**
 * 
 */
package net.blog.dev.gestion.stocks.middle.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

/**
 * @author Kiva
 * 
 */
public class KValidator<T> {

	public Map<String, String> validate(T bean) {

		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		Set<ConstraintViolation<T>> validate = validator.validate(bean);
		Map<String, String> map = new HashMap<String, String>();
		if (validate != null) {
			for (ConstraintViolation<T> constraint : validate) {
				map.put(constraint.getPropertyPath().toString(),
						constraint.getMessage());
			}
		}
		return map;
	}

}
