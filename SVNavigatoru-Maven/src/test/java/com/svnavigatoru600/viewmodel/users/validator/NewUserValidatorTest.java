package com.svnavigatoru600.viewmodel.users.validator;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.svnavigatoru600.test.category.UnitTests;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(UnitTests.class)
@RunWith(Parameterized.class)
public final class NewUserValidatorTest {

    @Parameters(name = "Run #{index}: firstName={0} => errorCount={1}")
    public static Iterable<Object[]> data() {
        return Arrays
                .asList(new Object[][] { { null, 1 }, { StringUtils.EMPTY, 1 }, { "A", 1 }, { "A!", 1 }, { "AB", 0 } });
    }

    /**
     * Object of a class which is tested.
     */
    private NewUserValidator validator;
    /**
     * Auxiliary object which holds results of the {@link #validator}.
     */
    private Errors errors;

    private final String firstName;
    private final int errorCount;

    public NewUserValidatorTest(final String firstName, final int errorCount) {
        this.firstName = firstName;
        this.errorCount = errorCount;
    }

    @Before
    public void initializeValidatorObject() {
        this.validator = new NewUserValidator();
        this.errors = new BeanPropertyBindingResult(new AdministrateUserData(), "userData");
    }

    @Test
    public void testCheckNewFirstName() throws Exception {
        this.validator.checkNewFirstName(this.firstName, this.errors);
        Assert.assertEquals(this.errorCount, this.errors.getErrorCount());
    }
}
