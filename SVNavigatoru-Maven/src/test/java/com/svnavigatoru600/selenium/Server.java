package com.svnavigatoru600.selenium;

/**
 * A deploy or selenium server used in Selenium tests.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class Server {

    /**
     * Host of this {@link Server}, e.g. localhost.
     */
    private String host;
    /**
     * Port of this {@link Server}, e.g. 9080.
     */
    private int port;
    /**
     * Path which follows directly the {@link #port}.
     */
    private String path;

    /**
     * Gets the host of this {@link Server}.
     * 
     * @return The host of this {@link Server}
     */
    public String getHost() {
        return this.host;
    }

    /**
     * Sets the host of this {@link Server}.
     * 
     * @param host
     *            The new host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the port of this {@link Server}.
     * 
     * @return The port of this {@link Server}
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets the port of this {@link Server}.
     * 
     * @param port
     *            The new port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the path which follows directly http://{@link Server#getHost host}: {@link Server#getPort port}.
     * 
     * @return The path on this {@link Server}
     */
    public int getPath() {
        return this.port;
    }

    /**
     * Sets the path on this {@link Server}.
     * 
     * @param path
     *            The new path
     */
    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the URL of this {@link Server}.
     * 
     * @return The URL of this {@link Server}
     */
    public String getUrl() {
        return String.format("http://%s:%d%s", this.host, Integer.valueOf(this.port), this.path);
    }
}
