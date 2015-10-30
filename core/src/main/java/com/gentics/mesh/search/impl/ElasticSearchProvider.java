package com.gentics.mesh.search.impl;

import static org.elasticsearch.client.Requests.refreshRequest;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;

import com.gentics.mesh.cli.MeshNameProvider;
import com.gentics.mesh.etc.ElasticSearchOptions;
import com.gentics.mesh.search.SearchProvider;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Elastic search provider class which implements the {@link SearchProvider} interface.
 */
public class ElasticSearchProvider implements SearchProvider {

	private static final Logger log = LoggerFactory.getLogger(ElasticSearchProvider.class);

	private Node node;

	private ElasticSearchOptions options;

	@Override
	public void start() {

		if (log.isDebugEnabled()) {
			log.debug("Creating elasticsearch node");
		}
		long start = System.currentTimeMillis();
		ImmutableSettings.Builder elasticsearchSettings = ImmutableSettings.settingsBuilder();
		elasticsearchSettings.put("http.enabled", options.isHttpEnabled());
		elasticsearchSettings.put("path.data", options.getDirectory());
		elasticsearchSettings.put("node.name", MeshNameProvider.getInstance().getName());
		NodeBuilder builder = NodeBuilder.nodeBuilder();
		//TODO configure ES cluster options
		node = builder.local(true).settings(elasticsearchSettings.build()).node();
		if (log.isDebugEnabled()) {
			log.debug("Waited for elasticsearch shard: " + (System.currentTimeMillis() - start) + "[ms]");
		}
	}

	/**
	 * Initialize and start the search provider using the given options.
	 * 
	 * @param options
	 * @return Fluent API
	 */
	public ElasticSearchProvider init(ElasticSearchOptions options) {
		this.options = options;
		start();
		return this;
	}

	@Override
	public Node getNode() {
		return node;
	}

	@Override
	public void reset() {
		if (log.isDebugEnabled()) {
			log.debug("Resetting elastic search");
		}
		stop();
		try {
			if (options.getDirectory() != null) {
				File storageDirectory = new File(options.getDirectory());
				if (storageDirectory.exists()) {
					FileUtils.deleteDirectory(storageDirectory);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		start();
	}

	@Override
	public void stop() {
		node.close();
	}

	@Override
	public void refreshIndex() {
		getNode().client().admin().indices().refresh(refreshRequest()).actionGet();
	}

	private Client getSearchClient() {
		return getNode().client();
	}

	//	@Override
	//	public void createIndex(String indexName, String type) {
	//		//TODO Add method which will be used to create an index and set a custom mapping 
	//		CreateIndexRequestBuilder createIndexRequestBuilder = getSearchClient().admin().indices().prepareCreate(indexName);
	//		Mapping mapping = new Mapping();
	//		mapping.setAnalyzer("not_analyzed");
	//		mapping.setIndex("node");
	//		mapping.setType("string");
	//		String mappingJson = JsonUtil.toJson(mapping);
	//		createIndexRequestBuilder.addMapping(type, mappingJson);
	//	}

	@Override
	public void getDocument(String index, String type, String uuid, Handler<AsyncResult<Map<String, Object>>> handler) {
		getSearchClient().prepareGet(index, type, uuid).execute().addListener(new ActionListener<GetResponse>() {

			@Override
			public void onResponse(GetResponse response) {
				if (log.isDebugEnabled()) {
					log.debug("Get object {" + uuid + ":" + type + "} from index {" + index + "}");
				}
				handler.handle(Future.succeededFuture(response.getSourceAsMap()));
			}

			@Override
			public void onFailure(Throwable e) {
				log.error("Could not get object {" + uuid + ":" + type + "} from index {" + index + "}");
				handler.handle(Future.failedFuture(e));
			}

		});
	}

	@Override
	public void deleteDocument(String index, String type, String uuid, Handler<AsyncResult<Void>> handler) {
		getSearchClient().prepareDelete(index, type, uuid).execute().addListener(new ActionListener<DeleteResponse>() {

			@Override
			public void onResponse(DeleteResponse response) {
				if (log.isDebugEnabled()) {
					log.debug("Deleted object {" + uuid + ":" + type + "} from index {" + index + "}");
				}
				handler.handle(Future.succeededFuture());
			}

			@Override
			public void onFailure(Throwable e) {
				log.error("Could not delete object {" + uuid + ":" + type + "} from index {" + index + "}");
				handler.handle(Future.failedFuture(e));
			}
		});
	}

	@Override
	public void updateDocument(String index, String type, String uuid, Map<String, Object> map, Handler<AsyncResult<Void>> handler) {
		long start = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("Updating object {" + uuid + ":" + type + "} to index.");
		}
		UpdateRequestBuilder builder = getSearchClient().prepareUpdate(index, type, uuid);
		builder.setDoc(map);
		builder.execute().addListener(new ActionListener<UpdateResponse>() {

			@Override
			public void onResponse(UpdateResponse response) {
				if (log.isDebugEnabled()) {
					log.debug("Update object {" + uuid + ":" + type + "} to index. Duration " + (System.currentTimeMillis() - start) + "[ms]");
				}
				handler.handle(Future.succeededFuture());
			}

			@Override
			public void onFailure(Throwable e) {
				log.error("Updating object {" + uuid + ":" + type + "} to index failed. Duration " + (System.currentTimeMillis() - start) + "[ms]",
						e);
				handler.handle(Future.failedFuture(e));
			}
		});

	}

	@Override
	public void storeDocument(String index, String type, String uuid, Map<String, Object> map, Handler<AsyncResult<Void>> handler) {
		long start = System.currentTimeMillis();
		if (log.isDebugEnabled()) {
			log.debug("Adding object {" + uuid + ":" + type + "} to index.");
		}
		IndexRequestBuilder builder = getSearchClient().prepareIndex(index, type, uuid);

		builder.setSource(map);
		builder.execute().addListener(new ActionListener<IndexResponse>() {

			@Override
			public void onResponse(IndexResponse response) {
				if (log.isDebugEnabled()) {
					log.debug("Added object {" + uuid + ":" + type + "} to index. Duration " + (System.currentTimeMillis() - start) + "[ms]");
				}
				handler.handle(Future.succeededFuture());
			}

			@Override
			public void onFailure(Throwable e) {
				log.error("Adding object {" + uuid + ":" + type + "} to index failed. Duration " + (System.currentTimeMillis() - start) + "[ms]", e);
				handler.handle(Future.failedFuture(e));
			}
		});

	}
}
