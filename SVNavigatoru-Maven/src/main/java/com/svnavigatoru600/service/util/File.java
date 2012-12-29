package com.svnavigatoru600.service.util;

import java.sql.Blob;
import java.sql.SQLException;

import javax.sql.rowset.serial.SerialBlob;

import com.svnavigatoru600.web.Configuration;

/**
 * Provides a set of static functions related to files.
 * 
 * @author Tomas Skalicky
 */
public final class File {

    private static final String[] SUPPORTED_EXTENSIONS = new String[] { "pdf", "doc", "docx", "txt" };

    private File() {
    }

    /**
     * Indicates whether the given <code>fileName</code> is valid in terms of its format.
     */
    public static boolean isValid(String fileName) {
        String[] fileNameParts = fileName.split("\\.");
        if (fileNameParts.length == 0) {
            // The filename is likely blank.
            return false;
        }

        String extension = fileNameParts[fileNameParts.length - 1];
        String lowerCased = extension.toLowerCase();
        for (String supported : File.SUPPORTED_EXTENSIONS) {
            if (supported.equals(lowerCased)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Modifies the given <code>fileName</code> in a way that it is unique in the whole application.
     */
    public static String getUniqueFileName(final String inputFileName) {
        String uniqueFileName = null;
        java.io.File destinationFile = null;

        do {
            uniqueFileName = String.format("%s_%s", Password.generateNew(), inputFileName);
            destinationFile = File.getUploadedFile(uniqueFileName);
        } while (destinationFile.exists());

        return uniqueFileName;
    }

    /**
     * Gets the path of the uploaded file with the given <code>fileName</code>.
     */
    public static java.io.File getUploadedFile(String fileName) {
        return new java.io.File(String.format("/%s%s", Configuration.FILE_STORAGE, fileName));
    }

    /**
     * Creates a new {@link Blob} with the given <code>bytes</code>.
     */
    public static Blob convertToBlob(byte[] bytes) throws SQLException {
        return new SerialBlob(bytes);
    }
}
