package com.gentics.mesh.core.data;

import java.io.IOException;
import java.util.List;

import com.gentics.mesh.core.data.impl.SchemaContainerImpl;
import com.gentics.mesh.core.rest.schema.Schema;

public interface SchemaContainer extends GenericNode {

	public Schema getSchema() throws IOException;
		
	public void setSchema(Schema schema);

	List<? extends Project> getProjects();

	void addProject(Project project);

	SchemaContainerImpl getImpl();

	void delete();

	void setSchemaName(String name);

	String getSchemaName();

}