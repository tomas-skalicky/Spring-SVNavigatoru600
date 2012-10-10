package com.svnavigatoru600.selenium;

/**
 * Represents a server on which the web application has been/will be deployed.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public class DeployServer {

	/**
	 * The host of this {@link DeployServer}.
	 */
	private String host;
	/**
	 * The port of this {@link DeployServer}.
	 */
	private int port;

	public String getHost() {
		return this.host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	/**
	 * Gets the URL of this {@link DeployServer}.
	 */
	String getUrl() {
		return String.format("http://%s:%d", this.host, Integer.valueOf(this.port));
	}
}
