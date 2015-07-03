package com.gentics.mesh.core.rest.schema;

import com.gentics.mesh.core.rest.node.field.NodeField;

public interface NodeFieldSchema extends NodeField, MicroschemaListableFieldSchema {

	String[] getAllowedSchemas();

	void setAllowedSchemas(String[] allowedSchemas);

}