package com.svnavigatoru600.service.util;

import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeEnum;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;

/**
 * This utility class is based on the {@link AuthorityUtils} one.
 *
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public final class OtherDocumentRecordUtils {

    /**
     * The minimal allowed length of {@link com.svnavigatoru600.domain.records.OtherDocumentRecord
     * OtherDocumentRecord's} name.
     */
    private static final int RECORD_NAME_MINIMAL_LENGTH = 3;

    private OtherDocumentRecordUtils() {
    }

    /**
     * Indicates whether the given <code>recordName</code> is valid.
     */
    public static boolean isRecordNameValid(final String recordName) {
        return (recordName != null) && (recordName.length() >= RECORD_NAME_MINIMAL_LENGTH);
    }

    /**
     * Gets an array of flags which say which {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes} are checked,
     * and which types are not. The array is filled in according to the given array of checked
     * {@link OtherDocumentRecordTypeRelation} called <code>types</code>.
     */
    public static boolean[] getArrayOfCheckIndicators(final Collection<OtherDocumentRecordTypeRelation> types) {
        final boolean[] indicators = OtherDocumentRecordUtils.createArrayOfCheckIndicators();
        for (final OtherDocumentRecordTypeRelation type : types) {
            final int typeOrdinal = type.getId().getTypedType().ordinal();
            indicators[typeOrdinal] = true;
        }
        return indicators;
    }

    /**
     * Gets the default array of flags which say which {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes} are
     * checked (selected), and which types are not.
     * <p>
     * <b>Precondition:</b> Ordinal values of all {@link OtherDocumentRecordTypeEnum OtherDocumentRecordTypes} are
     * exactly in the range of <code>[0, 1, ..., OtherDocumentRecordType.values().length-1]</code>.
     */
    public static boolean[] getDefaultArrayOfCheckIndicators() {
        return OtherDocumentRecordUtils.createArrayOfCheckIndicators();
    }

    private static boolean[] createArrayOfCheckIndicators() {
        return CheckboxUtils.createArrayOfCheckIndicators(OtherDocumentRecordTypeEnum.values().length);
    }

    /**
     * Converts the given array of check <code>indicators</code> to the {@link Set} of
     * {@link OtherDocumentRecordTypeRelation OtherDocumentRecordTypeRelations} associated with an
     * {@link com.svnavigatoru600.domain.records.OtherDocumentRecord OtherDocumentRecord} with the given
     * <code>recordId</code>.
     *
     * @return Set of those record types which have been checked, i.e. their indicators equal <code>true</code>.
     */
    public static Set<OtherDocumentRecordTypeRelation> convertIndicatorsToRelations(final boolean[] indicators,
            final int recordId) {
        final Set<OtherDocumentRecordTypeRelation> checkedTypes = Sets.newHashSet();

        for (int i = 0; i < indicators.length; ++i) {
            final boolean isRelationChecked = indicators[i];
            if (isRelationChecked) {
                final OtherDocumentRecordTypeEnum recordType = OtherDocumentRecordTypeEnum.values()[i];
                checkedTypes.add(OtherDocumentRecordTypeRelation.createFrom(recordId, recordType));
            }
        }
        return checkedTypes;
    }
}
