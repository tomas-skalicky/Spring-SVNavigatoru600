package com.svnavigatoru600.domain.records;

/**
 * All types of {@link OtherDocumentRecord OtherDocumentRecords} in the application.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public enum OtherDocumentRecordTypeEnum {

    // @formatter:off
    //                   titleLocalizationCode
    ACCOUNTING          ("other-documents.accounting.title"),
    CONTRACT            ("other-documents.contract"),
    REGULAR_REVISION    ("other-documents.regular-revision"),
    REMOSTAV            ("remostav.title"),
    OTHER               ("other-documents.other"),
    ;
    // @formatter:on

    private final String titleLocalizationCode;

    private OtherDocumentRecordTypeEnum(final String titleLocalizationCode) {
        this.titleLocalizationCode = titleLocalizationCode;
    }

    /**
     * Gets the localization code of the title of this {@link OtherDocumentRecordTypeEnum}. Values which correspond to
     * this code are stored in <code>messages*.properties</code> files.
     */
    public String getTitleLocalizationCode() {
        return titleLocalizationCode;
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
