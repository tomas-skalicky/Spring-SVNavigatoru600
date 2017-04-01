package com.svnavigatoru600.common.constants;

import java.nio.charset.Charset;

import org.apache.commons.io.Charsets;

public final class CommonConstants {

    @SuppressWarnings("deprecation")
    public static final Charset DEFAULT_CHARSET = Charsets.UTF_8;
    public static final String NEW_LINE = "\n";

    public static final String CSS_FILE_EXCEPTION_WITH_DOT = ".css";
    public static final String JAVASCRIPT_FILE_EXCEPTION_WITH_DOT = ".js";
    public static final String PNG_FILE_EXCEPTION_WITH_DOT = ".png";
    public static final String JPG_FILE_EXCEPTION_WITH_DOT = ".jpg";
    public static final String JPEG_FILE_EXCEPTION_WITH_DOT = ".jpeg";

    private CommonConstants() {
    }
}
