package com.svnavigatoru600.service.util;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

/**
 * Provides a set of static functions related to {@link HttpServletResponse HttpServletResponses}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class HttpResponseUtils {

    private HttpResponseUtils() {
    }

    /**
     * Assigns the specified file to the given {@link HttpServletResponse HTTP response}.
     * 
     * @param response
     *            The target HTTP response
     * @param fileBytes
     *            The contents of the file
     * @param fileName
     *            The name of the file
     * @param fileExtension
     *            The extension of the file (e.g. txt, doc, pdf)
     */
    public static void sendFile(HttpServletResponse response, byte[] fileBytes, String fileName,
            String fileExtension) throws IOException {
        if ("txt".equalsIgnoreCase(fileExtension)) {
            response.setContentType("text/plain");
        } else if ("doc".equalsIgnoreCase(fileExtension) || "docx".equalsIgnoreCase(fileExtension)) {
            response.setContentType("application/msword");
        } else if ("pdf".equalsIgnoreCase(fileExtension)) {
            response.setContentType("application/pdf");
        } else {
            response.setContentType("application/octet-stream");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("cache-control", "no-cache");

        // Outputs the file.
        OutputStream output = response.getOutputStream();
        output.write(fileBytes);
        output.flush();
        output.close();
    }
}
