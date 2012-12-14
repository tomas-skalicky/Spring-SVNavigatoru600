package com.svnavigatoru600.service.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.svnavigatoru600.domain.records.OtherDocumentRecord;
import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.domain.records.OtherDocumentRecordTypeRelation;

/**
 * This utility class is based on the {@link AuthorityUtils} one.
 * 
 * @author Tomas Skalicky
 */
public class OtherDocumentRecordUtils {

    /**
     * Indicates whether the given <code>recordName</code> is valid.
     */
    public static boolean isRecordNameValid(String recordName) {
        return (recordName != null) && (recordName.length() >= 3);
    }

    /**
     * Gets an array of indicators which say which {@link OtherDocumentRecordType}s are checked, and which
     * types are not. The array is filled in according to the given array of checked
     * {@link OtherDocumentRecordTypeRelation} called <code>types</code>.
     */
    public static boolean[] getArrayOfCheckIndicators(Collection<OtherDocumentRecordTypeRelation> types) {
        boolean[] indicators = OtherDocumentRecordUtils.createArrayOfCheckIndicators();
        for (OtherDocumentRecordTypeRelation type : types) {
            int typeOrdinal = type.getId().getTypedType().ordinal();
            indicators[typeOrdinal] = true;
        }
        return indicators;
    }

    /**
     * <p>
     * Gets the default array of indicators which say which {@link OtherDocumentRecordType}s are checked
     * (selected), and which types are not.
     * </p>
     * <b>Precondition:</b> Ordinal values of all {@link OtherDocumentRecordType}s are exactly in the range of
     * <code>[0, 1, ..., OtherDocumentRecordType.values().length-1]</code>.
     */
    public static boolean[] getDefaultArrayOfCheckIndicators() {
        return OtherDocumentRecordUtils.createArrayOfCheckIndicators();
    }

    private static boolean[] createArrayOfCheckIndicators() {
        return CheckboxUtils.createArrayOfCheckIndicators(OtherDocumentRecordType.values().length);
    }

    /**
     * Converts the given array of check <code>indicators</code> to the {@link Set} of
     * {@link OtherDocumentRecordTypeRelation}s associated with an {@link OtherDocumentRecord} with the given
     * <code>recordId</code>.
     * 
     * @return Set of those record types which have been checked, i.e. their indicators equal
     *         <code>true</code>.
     */
    public static Set<OtherDocumentRecordTypeRelation> convertIndicatorsToRelations(boolean[] indicators,
            int recordId) {
        Set<OtherDocumentRecordTypeRelation> checkedTypes = new HashSet<OtherDocumentRecordTypeRelation>();

        for (int i = 0; i < indicators.length; ++i) {
            if (indicators[i] == true) {
                OtherDocumentRecordType recordType = OtherDocumentRecordType.values()[i];
                checkedTypes.add(new OtherDocumentRecordTypeRelation(recordId, recordType));
            }
        }
        return checkedTypes;
    }
}
