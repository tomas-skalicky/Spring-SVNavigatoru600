package com.svnavigatoru600.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import com.svnavigatoru600.domain.WysiwygSection;
import com.svnavigatoru600.domain.WysiwygSectionName;
import com.svnavigatoru600.test.category.PersistenceTests;

/**
 * Tests methods of the {@link WysiwygSectionDao} interface.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(PersistenceTests.class)
public class WysiwygSectionDaoTest extends AbstractRepositoryTest {

    /**
     * Name of a test section.
     */
    private static final WysiwygSectionName TEST_SECTION_NAME = WysiwygSectionName.USEFUL_LINKS;
    /**
     * Extension of the existing source code of a test section.
     */
    private static final String TEST_SECTION_SOURCE_CODE_EXTENSION = "section text 2";

    @Test
    public void testUpdateFindByName() throws Exception {
        WysiwygSectionDao sectionDao = TEST_UTILS.getSectionDao();

        // SELECT ONE
        WysiwygSection section = sectionDao.findByName(TEST_SECTION_NAME);
        Assert.assertEquals(TEST_SECTION_NAME.name(), section.getName());

        // UPDATE
        Date sectionLastSaveTime = section.getLastSaveTime();
        String sectionSourceCode = section.getSourceCode();
        String newSourceCode = sectionSourceCode + TEST_SECTION_SOURCE_CODE_EXTENSION;
        section.setSourceCode(newSourceCode);
        sectionDao.update(section);

        // SELECT ONE
        section = sectionDao.findByName(TEST_SECTION_NAME);
        Assert.assertEquals(newSourceCode, section.getSourceCode());
        Assert.assertTrue(section.getLastSaveTime().after(sectionLastSaveTime));
    }
}
