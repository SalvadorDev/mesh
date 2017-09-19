package com.gentics.mesh.core.eventbus;

import static com.gentics.mesh.Events.MESH_MIGRATION;
import static com.gentics.mesh.test.TestSize.FULL;
import static com.gentics.mesh.test.util.MeshAssert.failingLatch;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;

import org.junit.Test;

import com.gentics.mesh.Mesh;
import com.gentics.mesh.test.context.AbstractMeshTest;
import com.gentics.mesh.test.context.MeshTestSetting;

import io.vertx.core.json.JsonObject;

@MeshTestSetting(useElasticsearch = false, testSize = FULL, startServer = true)
public class EventbusEndpointTest extends AbstractMeshTest {

	@Test
	public void testExternalEventbusMessage() throws Exception {

		String allowedAddress = MESH_MIGRATION;
		CountDownLatch latch = new CountDownLatch(1);
		client().eventbus(ws -> {
			// Register
			JsonObject msg = new JsonObject().put("type", "register").put("address", allowedAddress);
			ws.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));

			// Handle msgs
			ws.handler(buff -> {
				String str = buff.toString();
				System.out.println(str);
				JsonObject received = new JsonObject(str);
				JsonObject rec = received.getJsonObject("body");
				String value = rec.getString("test");
				assertEquals("someValue", value);
				latch.countDown();
			});

		});

		Thread.sleep(1000);
		JsonObject msg = new JsonObject();
		msg.put("test", "someValue");
		Mesh.vertx().eventBus().send(allowedAddress, msg);

		failingLatch(latch);
	}

	@Test
	public void testRegisterToEventbus() throws Exception {
		CountDownLatch latch = new CountDownLatch(1);
		client().eventbus(ws -> {
			// Register
			JsonObject msg = new JsonObject().put("type", "register").put("address", "some-address");
			ws.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));

			// Handle msgs
			ws.handler(buff -> {
				String str = buff.toString();
				JsonObject received = new JsonObject(str);
				Object rec = received.getValue("body");
				System.out.println("Handler:" + rec.toString());
				latch.countDown();
			});

			// Send msg
			msg = new JsonObject().put("type", "publish").put("address", "some-address").put("body", "someText");
			ws.writeFrame(io.vertx.core.http.WebSocketFrame.textFrame(msg.encode(), true));

		});
		failingLatch(latch);
	}

}
