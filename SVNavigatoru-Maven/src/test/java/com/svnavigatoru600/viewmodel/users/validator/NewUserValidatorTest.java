package com.svnavigatoru600.viewmodel.users.validator;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import com.svnavigatoru600.test.category.FastTests;
import com.svnavigatoru600.viewmodel.users.AdministrateUserData;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Category(FastTests.class)
public class NewUserValidatorTest {

    /**
     * Object of a class which is tested.
     */
    private NewUserValidator validator;
    /**
     * Auxiliary object which holds results of the {@link #validator}.
     */
    private Errors errors;

    @Before
    public void initializeValidatorObject() {
        this.validator = new NewUserValidator();
        this.errors = new BeanPropertyBindingResult(new AdministrateUserData(), "userData");
    }

    @Test
    public void testCheckNewFirstNameWithNull() throws Exception {
        this.validator.checkNewFirstName(null, this.errors);
        Assert.assertEquals(1, this.errors.getErrorCount());
    }

    @Test
    public void testCheckNewFirstNameWithLengthZero() throws Exception {
        this.validator.checkNewFirstName("", this.errors);
        Assert.assertEquals(1, this.errors.getErrorCount());
    }

    @Test
    public void testCheckNewFirstNameWithLengthOne() throws Exception {
        this.validator.checkNewFirstName("A", this.errors);
        Assert.assertEquals(1, this.errors.getErrorCount());
    }

    @Test
    public void testCheckNewFirstNameWithSpecialChar() throws Exception {
        this.validator.checkNewFirstName("A!", this.errors);
        Assert.assertEquals(1, this.errors.getErrorCount());
    }

    @Test
    public void testCheckNewFirstNameWithCorrectValue() throws Exception {
        this.validator.checkNewFirstName("AB", this.errors);
        Assert.assertEquals(0, this.errors.getErrorCount());
    }
}
