package com.gentics.mesh.log;

import com.gentics.mesh.Mesh;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;

/**
 * Logback converter which provides the instance codename within the log output.
 */
public class MeshLogNameConverter extends ClassicConverter {

	@Override
	public String convert(ILoggingEvent event) {
		if (Mesh.isInitalized()) {
			return Mesh.mesh().getOptions().getNodeName();
		} else {
			return "";
		}
	}
}
