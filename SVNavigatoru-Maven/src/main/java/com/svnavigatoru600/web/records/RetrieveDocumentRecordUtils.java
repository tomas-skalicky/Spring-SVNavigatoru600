package com.svnavigatoru600.web.records;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.SQLException;

import javax.servlet.http.HttpServletResponse;

import com.svnavigatoru600.domain.records.DocumentRecord;
import com.svnavigatoru600.repository.records.DocumentRecordDao;

/**
 * Provides a set of static functions for retrieving {@link DocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
public final class RetrieveDocumentRecordUtils {
    
    private RetrieveDocumentRecordUtils() {
    }

    public static void retrieve(int recordId, DocumentRecordDao recordDao, HttpServletResponse response)
            throws SQLException, IOException {
        // Gets the record from the DB.
        DocumentRecord record = recordDao.findById(recordId);
        Blob file = record.getFile();
        byte[] bytes = file.getBytes(1L, (int) file.length());

        // Sets the type of the response.
        String fileName = record.getFileName();
        String fileExtension = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length()).trim();
        if (fileExtension.equalsIgnoreCase("txt")) {
            response.setContentType("text/plain");
        } else if (fileExtension.equalsIgnoreCase("doc") || fileExtension.equalsIgnoreCase("docx")) {
            response.setContentType("application/msword");
        } else if (fileExtension.equalsIgnoreCase("pdf")) {
            response.setContentType("application/pdf");
        } else {
            response.setContentType("application/octet-stream");
        }

        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setHeader("cache-control", "no-cache");

        // Outputs the file.
        OutputStream output = response.getOutputStream();
        output.write(bytes);
        output.flush();
        output.close();
    }
}
