package com.gentics.mesh.core.rest.project;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.gentics.mesh.core.rest.common.RestModel;

public class ProjectUpdateRequest implements RestModel {

	@JsonProperty(required = true)
	@JsonPropertyDescription("New name of the project")
	private String name;

	public ProjectUpdateRequest() {
	}

	/**
	 * Return the project name.
	 * 
	 * @return New project name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the project name.
	 * 
	 * @param name
	 *            New name of the project
	 * @return Fluent API
	 */
	public ProjectUpdateRequest setName(String name) {
		this.name = name;
		return this;
	}

}
