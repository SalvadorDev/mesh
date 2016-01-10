package com.gentics.mesh.core.data.node.field;

import com.gentics.mesh.core.data.node.field.nesting.ListableGraphField;
import com.gentics.mesh.core.rest.node.field.DateField;

/**
 * The DateField Domain Model interface.
 * 
 * A date graph field is a basic node field which can be used to store date values.
 */
public interface DateGraphField extends ListableGraphField, BasicGraphField<DateField> {

	/**
	 * Set the date within the field.
	 * 
	 * @param date
	 */
	void setDate(Long date);

	/**
	 * Return the date which is stored in the field.
	 * 
	 * @return
	 */
	Long getDate();

}