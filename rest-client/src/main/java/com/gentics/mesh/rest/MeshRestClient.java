package com.gentics.mesh.rest;

import static io.vertx.core.http.HttpMethod.DELETE;
import static io.vertx.core.http.HttpMethod.GET;
import static io.vertx.core.http.HttpMethod.POST;
import static io.vertx.core.http.HttpMethod.PUT;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.http.HttpClientRequest;

import com.gentics.mesh.api.common.PagingInfo;
import com.gentics.mesh.core.rest.common.GenericMessageResponse;
import com.gentics.mesh.core.rest.common.Permission;
import com.gentics.mesh.core.rest.group.GroupCreateRequest;
import com.gentics.mesh.core.rest.group.GroupListResponse;
import com.gentics.mesh.core.rest.group.GroupResponse;
import com.gentics.mesh.core.rest.group.GroupUpdateRequest;
import com.gentics.mesh.core.rest.node.NodeCreateRequest;
import com.gentics.mesh.core.rest.node.NodeListResponse;
import com.gentics.mesh.core.rest.node.NodeResponse;
import com.gentics.mesh.core.rest.node.NodeUpdateRequest;
import com.gentics.mesh.core.rest.node.QueryParameterProvider;
import com.gentics.mesh.core.rest.project.ProjectCreateRequest;
import com.gentics.mesh.core.rest.project.ProjectListResponse;
import com.gentics.mesh.core.rest.project.ProjectResponse;
import com.gentics.mesh.core.rest.project.ProjectUpdateRequest;
import com.gentics.mesh.core.rest.role.RoleCreateRequest;
import com.gentics.mesh.core.rest.role.RoleListResponse;
import com.gentics.mesh.core.rest.role.RoleResponse;
import com.gentics.mesh.core.rest.role.RoleUpdateRequest;
import com.gentics.mesh.core.rest.schema.SchemaCreateRequest;
import com.gentics.mesh.core.rest.schema.SchemaListResponse;
import com.gentics.mesh.core.rest.schema.SchemaResponse;
import com.gentics.mesh.core.rest.schema.SchemaUpdateRequest;
import com.gentics.mesh.core.rest.tag.TagCreateRequest;
import com.gentics.mesh.core.rest.tag.TagFamilyCreateRequest;
import com.gentics.mesh.core.rest.tag.TagFamilyListResponse;
import com.gentics.mesh.core.rest.tag.TagFamilyResponse;
import com.gentics.mesh.core.rest.tag.TagFamilyUpdateRequest;
import com.gentics.mesh.core.rest.tag.TagListResponse;
import com.gentics.mesh.core.rest.tag.TagResponse;
import com.gentics.mesh.core.rest.tag.TagUpdateRequest;
import com.gentics.mesh.core.rest.user.UserCreateRequest;
import com.gentics.mesh.core.rest.user.UserListResponse;
import com.gentics.mesh.core.rest.user.UserResponse;
import com.gentics.mesh.core.rest.user.UserUpdateRequest;

public class MeshRestClient extends AbstractMeshRestClient {

	public MeshRestClient(String host) {
		this(host, DEFAULT_PORT);
	}

	public MeshRestClient(String host, int port) {
		HttpClientOptions options = new HttpClientOptions();
		options.setDefaultHost(host);
		options.setDefaultPort(port);
		client = Vertx.vertx().createHttpClient(options);
	}

	@Override
	public Future<NodeResponse> findNodeByUuid(String projectName, String uuid, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/nodes/" + uuid + getQuery(parameters), NodeResponse.class);
	}

	@Override
	public Future<NodeResponse> createNode(String projectName, NodeCreateRequest nodeCreateRequest, QueryParameterProvider... parameters) {
		return handleRequest(POST, "/" + projectName + "/nodes" + getQuery(parameters), NodeResponse.class, nodeCreateRequest);
	}

	@Override
	public Future<NodeResponse> updateNode(String projectName, String uuid, NodeUpdateRequest nodeUpdateRequest, QueryParameterProvider... parameters) {
		return handleRequest(PUT, "/" + projectName + "/nodes/" + uuid + getQuery(parameters), NodeResponse.class, nodeUpdateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteNode(String projectName, String uuid) {
		return handleRequest(DELETE, "/" + projectName + "/nodes/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<NodeListResponse> findNodes(String projectName, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/nodes" + getQuery(parameters), NodeListResponse.class);
	}

	@Override
	public Future<TagListResponse> findTagsForNode(String projectName, String nodeUuid, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/nodes/" + nodeUuid + "/tags" + getQuery(parameters), TagListResponse.class);
	}

	@Override
	public Future<NodeListResponse> findNodeChildren(String projectName, String parentNodeUuid, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/nodes/" + parentNodeUuid + "/chilren" + getQuery(parameters), NodeListResponse.class);
	}

	@Override
	public Future<TagResponse> createTag(String projectName, TagCreateRequest tagCreateRequest) {
		return handleRequest(POST, "/" + projectName + "/tags", TagResponse.class, tagCreateRequest);
	}

	@Override
	public Future<TagResponse> findTagByUuid(String projectName, String uuid) {
		return handleRequest(GET, "/" + projectName + "/tags/" + uuid, TagResponse.class);
	}

	@Override
	public Future<TagResponse> updateTag(String projectName, String uuid, TagUpdateRequest tagUpdateRequest) {
		return handleRequest(PUT, "/" + projectName + "/tags/" + uuid, TagResponse.class, tagUpdateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteTag(String projectName, String uuid) {
		return handleRequest(DELETE, "/" + projectName + "/tags/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<TagListResponse> findTags(String projectName, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/tags" + getQuery(parameters), TagListResponse.class);
	}

	// TODO can we actually do this?
	@Override
	public Future<TagResponse> findTagByName(String projectName, String name) {
		return handleRequest(GET, "/" + projectName + "/tags/" + name, TagResponse.class);
	}

	@Override
	public Future<NodeListResponse> findNodesForTag(String projectName, String tagUuid, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/tags/" + tagUuid + "/nodes" + getQuery(parameters), NodeListResponse.class);
	}

	@Override
	public Future<ProjectResponse> findProjectByUuid(String uuid) {
		return handleRequest(GET, "/projects/" + uuid, ProjectResponse.class);
	}

	@Override
	public Future<ProjectListResponse> findProjects(QueryParameterProvider... parameters) {
		return handleRequest(GET, "/projects" + getQuery(parameters), ProjectListResponse.class);
	}

	@Override
	public Future<ProjectResponse> assignLanguageToProject(String projectUuid, String languageUuid) {
		return handleRequest(POST, "/projects/" + projectUuid + "/languages/" + languageUuid, ProjectResponse.class);
	}

	@Override
	public Future<ProjectResponse> unassignLanguageFromProject(String projectUuid, String languageUuid) {
		return handleRequest(DELETE, "/projects/" + projectUuid + "/languages/" + languageUuid, ProjectResponse.class);
	}

	@Override
	public Future<ProjectResponse> createProject(ProjectCreateRequest projectCreateRequest) {
		return handleRequest(POST, "/projects", ProjectResponse.class, projectCreateRequest);
	}

	@Override
	public Future<ProjectResponse> updateProject(String uuid, ProjectUpdateRequest projectUpdateRequest) {
		return handleRequest(PUT, "/projects/" + uuid, ProjectResponse.class, projectUpdateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteProject(String uuid) {
		return handleRequest(DELETE, "/projects/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<TagFamilyResponse> findTagFamilyByUuid(String projectName, String uuid) {
		return handleRequest(GET, "/" + projectName + "/tagFamilies/" + uuid, TagFamilyResponse.class);
	}

	@Override
	public Future<TagFamilyListResponse> findTagFamilies(String projectName, PagingInfo pagingInfo) {
		return handleRequest(GET, "/" + projectName + "/tagFamilies", TagFamilyListResponse.class);
	}

	@Override
	public Future<TagFamilyResponse> createTagFamily(String projectName, TagFamilyCreateRequest tagFamilyCreateRequest) {
		return handleRequest(POST, "/" + projectName + "/tagFamilies", TagFamilyResponse.class, tagFamilyCreateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteTagFamily(String projectName, String uuid) {
		return handleRequest(DELETE, "/" + projectName + "/tagFamilies/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<TagFamilyResponse> updateTagFamily(String projectName, String tagFamilyUuid, TagFamilyUpdateRequest tagFamilyUpdateRequest) {
		return handleRequest(PUT, "/" + projectName + "/tagFamilies/" + tagFamilyUuid, TagFamilyResponse.class, tagFamilyUpdateRequest);
	}

	@Override
	public Future<GroupResponse> findGroupByUuid(String uuid) {
		return handleRequest(GET, "/groups/" + uuid, GroupResponse.class);
	}

	@Override
	public Future<GroupListResponse> findGroups(PagingInfo pagingInfo) {
		return handleRequest(GET, "/groups", GroupListResponse.class);
	}

	@Override
	public Future<GroupResponse> createGroup(GroupCreateRequest groupCreateRequest) {
		return handleRequest(POST, "/groups", GroupResponse.class, groupCreateRequest);
	}

	@Override
	public Future<GroupResponse> updateGroup(String uuid, GroupUpdateRequest groupUpdateRequest) {
		return handleRequest(PUT, "/groups/" + uuid, GroupResponse.class, groupUpdateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteGroup(String uuid) {
		return handleRequest(DELETE, "/groups/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<UserResponse> findUserByUuid(String uuid) {
		return handleRequest(GET, "/users/" + uuid, UserResponse.class);
	}

	@Override
	public Future<UserResponse> findUserByUsername(String username) {
		return handleRequest(GET, "/users/" + username, UserResponse.class);
	}

	@Override
	public Future<UserListResponse> findUsers(QueryParameterProvider... parameters) {
		return handleRequest(GET, "/users" + getQuery(parameters), UserListResponse.class);
	}

	@Override
	public Future<UserResponse> createUser(UserCreateRequest userCreateRequest) {
		return handleRequest(POST, "/users", UserResponse.class, userCreateRequest);
	}

	@Override
	public Future<UserResponse> updateUser(String uuid, UserUpdateRequest userUpdateRequest) {
		return handleRequest(PUT, "/users/" + uuid, UserResponse.class, userUpdateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteUser(String uuid) {
		return handleRequest(DELETE, "/users/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<RoleResponse> findRoleByUuid(String uuid) {
		return handleRequest(GET, "/roles/" + uuid, RoleResponse.class);
	}

	@Override
	public Future<RoleListResponse> findRoles(QueryParameterProvider... parameters) {
		return handleRequest(GET, "/roles" + getQuery(parameters), RoleListResponse.class);
	}

	@Override
	public Future<RoleResponse> createRole(RoleCreateRequest roleCreateRequest) {
		return handleRequest(POST, "/roles", RoleResponse.class, roleCreateRequest);
	}

	@Override
	public Future<GenericMessageResponse> deleteRole(String uuid) {
		return handleRequest(DELETE, "/roles/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<UserResponse> me() {
		return handleRequest(GET, "/auth/me", UserResponse.class);
	}

	@Override
	public Future<NodeResponse> addTagToNode(String projectName, String nodeUuid, String tagUuid, QueryParameterProvider... parameters) {
		return handleRequest(POST, "/" + projectName + "/nodes/" + nodeUuid + "/tags/" + tagUuid + getQuery(parameters), NodeResponse.class);
	}

	@Override
	public Future<NodeResponse> removeTagFromNode(String projectName, String nodeUuid, String tagUuid, QueryParameterProvider... parameters) {
		return handleRequest(DELETE, "/" + projectName + "/nodes/" + nodeUuid + "/tags/" + tagUuid + getQuery(parameters), NodeResponse.class);
	}

	@Override
	public Future<UserListResponse> findUsersOfGroup(String groupUuid, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/groups/" + groupUuid + "/users" + getQuery(parameters), UserListResponse.class);
	}

	@Override
	public Future<GroupResponse> addUserToGroup(String groupUuid, String userUuid) {
		return handleRequest(POST, "/groups/" + groupUuid + "/users/" + userUuid, GroupResponse.class);
	}

	@Override
	public Future<GroupResponse> removeUserFromGroup(String groupUuid, String userUuid) {
		return handleRequest(DELETE, "/groups/" + groupUuid + "/users/" + userUuid, GroupResponse.class);
	}

	@Override
	public Future<RoleListResponse> findRolesForGroup(String groupUuid) {
		return handleRequest(GET, "/groups/" + groupUuid + "/roles", RoleListResponse.class);
	}

	@Override
	public Future<GroupResponse> addRoleToGroup(String groupUuid, String roleUuid) {
		return handleRequest(POST, "/groups/" + groupUuid + "/roles/" + roleUuid, GroupResponse.class);
	}

	@Override
	public Future<GroupResponse> removeRoleFromGroup(String groupUuid, String roleUuid) {
		return handleRequest(DELETE, "/groups/" + groupUuid + "/roles/" + roleUuid, GroupResponse.class);
	}

	@Override
	public Future<SchemaResponse> createSchema(SchemaCreateRequest request) {
		return handleRequest(POST, "/schemas", SchemaResponse.class, request);
	}

	public Future<RoleResponse> updateRole(String uuid, RoleUpdateRequest restRole) {
		return handleRequest(PUT, "/roles/" + uuid, RoleResponse.class, restRole);
	}

	@Override
	public Future<SchemaResponse> findSchemaByUuid(String uuid) {
		return handleRequest(GET, "/schemas/" + uuid, SchemaResponse.class);
	}

	@Override
	public Future<SchemaResponse> updateSchema(String uuid, SchemaUpdateRequest request) {
		return handleRequest(PUT, "/schemas/" + uuid, SchemaResponse.class, request);
	}

	@Override
	public Future<NodeResponse> webroot(String projectName, String path, QueryParameterProvider... parameters) {
		return handleRequest(GET, "/" + projectName + "/webroot/" + path + getQuery(parameters), NodeResponse.class);
	}

	@Override
	public Future<GenericMessageResponse> deleteSchema(String uuid) {
		return handleRequest(DELETE, "/schemas/" + uuid, GenericMessageResponse.class);
	}

	@Override
	public Future<SchemaResponse> addSchemaToProject(String schemaUuid, String projectUuid) {
		return handleRequest(POST, "/schemas/" + schemaUuid + "/projects/" + projectUuid, SchemaResponse.class);
	}

	@Override
	public Future<SchemaListResponse> findSchemas(PagingInfo pagingInfo) {
		return handleRequest(GET, "/schemas", SchemaListResponse.class);
	}

	@Override
	public Future<SchemaResponse> removeSchemaFromProject(String schemaUuid, String projectUuid) {
		return handleRequest(DELETE, "/schemas/" + schemaUuid + "/projects/" + projectUuid, SchemaResponse.class);
	}

	@Override
	public Future<UserResponse> login() {

		MeshResponseHandler<UserResponse> meshHandler = new MeshResponseHandler<>(UserResponse.class, this);
		meshHandler.handle(rh -> {
			if (rh.statusCode() == 200) {
				setCookie(rh.headers().get("Set-Cookie"));
			}
		});
		HttpClientRequest request = client.get(BASEURI + "/auth/me", meshHandler);
		request.headers().add("Authorization", "Basic " + authEnc);
		request.headers().add("Accept", "application/json");
		request.end();
		return meshHandler.getFuture();

	}

	@Override
	public Future<GenericMessageResponse> permissions(String roleUuid, String objectUuid, Permission permission, boolean recursive) {
		// TODO Auto-generated method stub
		return null;
	}

	//
	// @Override
	// public Future<TagResponse> createTag(TagCreateRequest tagCreateRequest) {
	// Future<TagResponse> future = Future.future();
	//
	// Map<String, String> extraHeaders = new HashMap<>();
	// Buffer buffer = Buffer.buffer();
	// buffer.appendString(JsonUtil.toJson(tagCreateRequest));
	// extraHeaders.put("content-length", String.valueOf(buffer.length()));
	// extraHeaders.put("content-type", "application/json");
	//
	// HttpClientRequest request = client.post(BASEURI + "/project/tags", rh -> {
	// rh.bodyHandler(bh -> {
	// if (rh.statusCode() == 200) {
	// String json = bh.toString();
	// try {
	// TagResponse tagResponse = JsonUtil.readValue(json, TagResponse.class);
	// future.complete(tagResponse);
	// } catch (Exception e) {
	// future.fail(e);
	// }
	// } else {
	// future.fail("Could not fetch tag:" + rh.statusCode());
	// }
	// });
	// });
	//
	// return future;
	// }
	//
	// @Override
	// public Future<TagResponse> findTag(String uuid) {
	// Future<TagResponse> future = Future.future();
	// HttpClientRequest request = client.get(BASEURI + "/tags", rh -> {
	// rh.bodyHandler(bh -> {
	//
	// });
	// System.out.println("Received response with status code " + rh.statusCode());
	// });
	//
	// request.exceptionHandler(e -> {
	// System.out.println("Received exception: " + e.getMessage());
	// e.printStackTrace();
	// });
	// return future;
	// }

	private String getQuery(QueryParameterProvider... parameters) {
		StringBuilder builder = new StringBuilder();
		for (QueryParameterProvider provider : parameters) {
			builder.append(provider.getQueryParameters());
		}
		if (builder.length() > 0) {
			return "?" + builder.toString();
		} else {
			return "";
		}
	}
}