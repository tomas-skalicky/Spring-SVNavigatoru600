package com.svnavigatoru600.web.records.otherdocuments;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.records.DocumentRecordController;
import com.svnavigatoru600.web.records.PageViews;

/**
 * Parent of all controllers which handle all operations upon the {@link OtherDocumentRecord}s.
 * 
 * @author Tomas Skalicky
 */
@Controller
public abstract class OtherDocumentRecordController extends DocumentRecordController {

    /**
     * If all {@link OtherDocumentRecord}s are considered (i.e. processed) by this controller,
     * <code>allRecordTypes</code> equals <code>true</code>. Otherwise, <code>allRecordTypes</code> equals
     * <code>false</code> and <code>RECORD_TYPE</code> determines the exact type of treated records.
     */
    protected final OtherDocumentRecordType RECORD_TYPE;
    protected boolean allRecordTypes = false;
    protected OtherDocumentRecordDao recordDao = null;

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of all
     * {@link OtherDocumentRecordType}s.
     */
    public OtherDocumentRecordController(String baseUrl, PageViews views, OtherDocumentRecordDao recordDao,
            MessageSource messageSource) {
        this(baseUrl, views, null, recordDao, messageSource);
        this.allRecordTypes = true;
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord}s of the given
     * <code>recordType</code>.
     */
    public OtherDocumentRecordController(String baseUrl, PageViews views, OtherDocumentRecordType recordType,
            OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, messageSource);
        this.RECORD_TYPE = recordType;
        this.recordDao = recordDao;
    }

    /**
     * Gets a {@link Map} which for each {@link OtherDocumentRecordType} (more precisely its ordinal) contains
     * an appropriate ID of its checkbox.
     */
    protected Map<Integer, String> getTypeCheckboxId() {
        String commonIdFormat = "newTypes[%s]";
        Map<Integer, String> checkboxIds = new HashMap<Integer, String>();

        for (OtherDocumentRecordType type : OtherDocumentRecordType.values()) {
            int typeOrdinal = type.ordinal();
            checkboxIds.put(typeOrdinal, String.format(commonIdFormat, typeOrdinal));
        }
        return checkboxIds;
    }

    /**
     * Gets a {@link Map} which for each {@link OtherDocumentRecordType} (more precisely its ordinal) contains
     * an appropriate localized title of its checkbox.
     */
    protected Map<Integer, String> getLocalizedTypeCheckboxTitles(HttpServletRequest request) {
        Map<Integer, String> checkboxTitles = new HashMap<Integer, String>();

        for (OtherDocumentRecordType type : OtherDocumentRecordType.values()) {
            String titleCode = null;
            switch (type) {
            case ACCOUNTING:
                titleCode = "other-documents.accounting.title";
                break;
            case CONTRACT:
                titleCode = "other-documents.contract";
                break;
            case REGULAR_REVISION:
                titleCode = "other-documents.regular-revision";
                break;
            case REMOSTAV:
                titleCode = "remostav.title";
                break;
            case OTHER:
                titleCode = "other-documents.other";
                break;
            default:
                throw new RuntimeException("Unsupported type of documents.");
            }
            checkboxTitles.put(type.ordinal(),
                    Localization.findLocaleMessage(this.messageSource, request, titleCode));
        }
        return checkboxTitles;
    }
}
