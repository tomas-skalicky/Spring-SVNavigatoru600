package com.svnavigatoru600.domain.records;

/**
 * All types of {@link OtherDocumentRecord OtherDocumentRecords} in the application.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OtherDocumentRecordType {

    ACCOUNTING("other-documents.accounting.title"), CONTRACT("other-documents.contract"), REGULAR_REVISION(
            "other-documents.regular-revision"), REMOSTAV("remostav.title"), OTHER("other-documents.other");

    private final String titleLocalizationCode;

    private OtherDocumentRecordType(String titleLocalizationCode) {
        this.titleLocalizationCode = titleLocalizationCode;
    }

    /**
     * Gets the localization code of the title of this {@link OtherDocumentRecordType}. Values which correspond to this
     * code are stored in <code>messages*.properties</code> files.
     */
    public String getTitleLocalizationCode() {
        return this.titleLocalizationCode;
    }

    /**
     * This getter is necessary for Spring Expression Language (SpEL).
     * 
     * @return The same value as {@link #ordinal()}.
     */
    public long getOrdinal() {
        return ordinal();
    }
}
