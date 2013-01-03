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
import com.svnavigatoru600.web.records.AbstractDocumentRecordController;
import com.svnavigatoru600.web.records.AbstractPageViews;

/**
 * Parent of all controllers which handle all operations upon the
 * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecords}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractOtherDocumentRecordController extends AbstractDocumentRecordController {

    /**
     * If all {@link OtherDocumentRecord OtherDocumentRecords} are considered (i.e. processed) by this
     * controller, <code>allRecordTypes</code> equals <code>true</code>. Otherwise,
     * <code>allRecordTypes</code> equals <code>false</code> and <code>RECORD_TYPE</code> determines the exact
     * type of treated records.
     */
    private final OtherDocumentRecordType recordType;
    private boolean allRecordTypes = false;
    private OtherDocumentRecordDao recordDao = null;

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of all
     * {@link OtherDocumentRecordType OtherDocumentRecordTypes}.
     */
    public AbstractOtherDocumentRecordController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        this(baseUrl, views, null, recordDao, messageSource);
        this.allRecordTypes = true;
    }

    /**
     * Constructs a controller which considers all {@link OtherDocumentRecord OtherDocumentRecords} of the
     * given <code>recordType</code>.
     */
    public AbstractOtherDocumentRecordController(String baseUrl, AbstractPageViews views,
            OtherDocumentRecordType recordType, OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(baseUrl, views, messageSource);
        this.recordType = recordType;
        this.recordDao = recordDao;
    }

    /**
     * Trivial getter
     */
    protected OtherDocumentRecordType getRecordType() {
        return this.recordType;
    }

    /**
     * Trivial getter
     */
    protected boolean isAllRecordTypes() {
        return this.allRecordTypes;
    }

    /**
     * Trivial getter
     */
    protected OtherDocumentRecordDao getRecordDao() {
        return this.recordDao;
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
                    Localization.findLocaleMessage(this.getMessageSource(), request, titleCode));
        }
        return checkboxTitles;
    }
}
