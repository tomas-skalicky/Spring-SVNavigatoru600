package com.svnavigatoru600.selenium;

/**
 * A deploy or selenium server used in Selenium tests.
 * 
 * @author <a href="mailto:tomas.skalicky@gfk.com">Tomas Skalicky</a>
 */
public class Server {

    private String host;
    private int port;
    private String path;

    /**
     * Gets the host of this {@link Server}.
     */
    public String getHost() {
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets the port of this {@link Server}.
     */
    public int getPort() {
        return this.port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Gets the path which follows after http://{@link Server#getHost host}: {@link Server#getPort port}.
     */
    public int getPath() {
        return this.port;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * Gets the URL of this {@link Server}.
     */
    public String getUrl() {
        return String.format("http://%s:%d%s", this.host, Integer.valueOf(this.port), this.path);
    }
}
