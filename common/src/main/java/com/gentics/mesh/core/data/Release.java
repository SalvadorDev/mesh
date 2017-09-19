package com.gentics.mesh.core.data;

import static com.gentics.mesh.Events.EVENT_RELEASE_CREATED;
import static com.gentics.mesh.Events.EVENT_RELEASE_DELETED;
import static com.gentics.mesh.Events.EVENT_RELEASE_UPDATED;

import java.util.List;

import com.gentics.mesh.core.TypeInfo;
import com.gentics.mesh.core.data.release.ReleaseMicroschemaEdge;
import com.gentics.mesh.core.data.release.ReleaseSchemaEdge;
import com.gentics.mesh.core.data.root.ReleaseRoot;
import com.gentics.mesh.core.data.schema.MicroschemaContainer;
import com.gentics.mesh.core.data.schema.MicroschemaContainerVersion;
import com.gentics.mesh.core.data.schema.SchemaContainer;
import com.gentics.mesh.core.data.schema.SchemaContainerVersion;
import com.gentics.mesh.core.rest.release.ReleaseReference;
import com.gentics.mesh.core.rest.release.ReleaseResponse;
import com.gentics.mesh.error.InvalidArgumentException;

/**
 * The Release domain model interface.
 *
 * A release is a bundle of specific schema versions which are used within a project. Releases can be used to create multiple tree structures within a single
 * project.
 */
public interface Release extends MeshCoreVertex<ReleaseResponse, Release>, NamedElement, ReferenceableElement<ReleaseReference>, UserTrackingVertex {

	/**
	 * Type Value: {@value #TYPE}
	 */
	static final String TYPE = "release";

	static TypeInfo TYPE_INFO = new TypeInfo(TYPE, EVENT_RELEASE_CREATED, EVENT_RELEASE_UPDATED, EVENT_RELEASE_DELETED);

	@Override
	default TypeInfo getTypeInfo() {
		return TYPE_INFO;
	}

	/**
	 * Get whether the release is active.
	 * 
	 * @return true for active release
	 */
	boolean isActive();

	/**
	 * Set whether the release is active.
	 * 
	 * @param active
	 *            true for active
	 * @return Fluent API
	 */
	Release setActive(boolean active);

	/**
	 * Get whether all nodes of the previous release have been migrated.
	 * 
	 * @return true iff all nodes have been migrated
	 */
	boolean isMigrated();

	/**
	 * Set whether all nodes have been migrated.
	 * 
	 * @param migrated
	 *            true iff all nodes have been migrated
	 * @return Fluent API
	 */
	Release setMigrated(boolean migrated);

	/**
	 * Get the next Release.
	 * 
	 * @return next Release
	 */
	Release getNextRelease();

	/**
	 * Set the next Release.
	 * 
	 * @param release
	 *            next Release
	 * @return Fluent API
	 */
	Release setNextRelease(Release release);

	/**
	 * Get the previous Release.
	 * 
	 * @return previous Release
	 */
	Release getPreviousRelease();

	/**
	 * Get the root vertex.
	 * 
	 * @return
	 */
	ReleaseRoot getRoot();

	/**
	 * Assign the given schema version to the release. This will effectively unassign all other schema versions of the schema.
	 * 
	 * @param schemaContainerVersion
	 * @return Edge between release and schema version
	 */
	ReleaseSchemaEdge assignSchemaVersion(SchemaContainerVersion schemaContainerVersion);

	/**
	 * Unassign all schema versions of the given schema from this release.
	 * 
	 * @param schemaContainer
	 * @return Fluent API
	 */
	Release unassignSchema(SchemaContainer schemaContainer);

	/**
	 * Check whether a version of this schema container is assigned to this release.
	 *
	 * @param schema
	 *            schema
	 * @return true iff assigned
	 */
	boolean contains(SchemaContainer schema);

	/**
	 * Check whether the given schema container version is assigned to this release.
	 *
	 * @param schemaContainerVersion
	 *            schema container version
	 * @return true iff assigned
	 */
	boolean contains(SchemaContainerVersion schemaContainerVersion);

	/**
	 * Get the schema container version of the given schema container, that is assigned to this release or null if not assigned at all.
	 * 
	 * @param schemaContainer
	 *            schema container
	 * @return assigned version or null
	 */
	SchemaContainerVersion getVersion(SchemaContainer schemaContainer);

	/**
	 * Get list of all schema container versions.
	 * 
	 * @return list
	 */
	List<? extends SchemaContainerVersion> findAllSchemaVersions();

	/**
	 * Assign the given microschema version to the release Unassign all other versions of the microschema.
	 * 
	 * @param microschemaContainerVersion
	 * @return Edge between release and microschema
	 */
	ReleaseMicroschemaEdge assignMicroschemaVersion(MicroschemaContainerVersion microschemaContainerVersion);

	/**
	 * Unassigns all versions of the given microschema from this release.
	 * 
	 * @param microschemaContainer
	 * @return Fluent API
	 */
	Release unassignMicroschema(MicroschemaContainer microschemaContainer);

	/**
	 * Check whether a version of this microschema container is assigned to this release.
	 *
	 * @param microschema
	 *            microschema
	 * @return true iff assigned
	 */
	boolean contains(MicroschemaContainer microschema);

	/**
	 * Check whether the given microschema container version is assigned to this release.
	 *
	 * @param microschemaContainerVersion
	 *            microschema container version
	 * @return true iff assigned
	 */
	boolean contains(MicroschemaContainerVersion microschemaContainerVersion);

	/**
	 * Get the microschema container version of the given microschema container, that is assigned to this release or null if not assigned at all.
	 * 
	 * @param microschemaContainer
	 *            schema container
	 * @return assigned version or null
	 */
	MicroschemaContainerVersion getVersion(MicroschemaContainer microschemaContainer);

	/**
	 * Get list of all microschema container versions.
	 * 
	 * @return list
	 * @throws InvalidArgumentException
	 */
	List<? extends MicroschemaContainerVersion> findAllMicroschemaVersions() throws InvalidArgumentException;

	/**
	 * Project to which the release belongs.
	 * 
	 * @return
	 */
	Project getProject();

	/**
	 * Assign the release to a specific project.
	 * 
	 * @param project
	 * @return Fluent API
	 */
	Release setProject(Project project);

	/**
	 * Return all schema versions which are linked to the release.
	 * 
	 * @return
	 */
	Iterable<? extends ReleaseSchemaEdge> findAllSchemaVersionEdges();

	/**
	 * Return all microschema versions which are linked to the release.
	 * 
	 * @return
	 */
	Iterable<? extends ReleaseMicroschemaEdge> findAllMicroschemaVersionEdges();

	/**
	 * Find the release schema edge for the given version.
	 * 
	 * @param schemaContainerVersion
	 * @return
	 */
	ReleaseSchemaEdge findReleaseSchemaEdge(SchemaContainerVersion schemaContainerVersion);

	/**
	 * Find the release microschema edge for the given version.
	 * 
	 * @param microschemaContainerVersion
	 * @return
	 */
	ReleaseMicroschemaEdge findReleaseMicroschemaEdge(MicroschemaContainerVersion microschemaContainerVersion);

}
