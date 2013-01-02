package com.svnavigatoru600.web.users.administration;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;

import com.svnavigatoru600.domain.users.AuthorityType;
import com.svnavigatoru600.repository.users.UserDao;
import com.svnavigatoru600.service.util.Localization;
import com.svnavigatoru600.web.AbstractPrivateSectionMetaController;

/**
 * Parent of all controllers which handle all operations concerning administration of
 * {@link com.svnavigatoru600.domain.users.User users}.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public abstract class AbstractUserController extends AbstractPrivateSectionMetaController {

    private UserDao userDao = null;
    private MessageSource messageSource = null;

    /**
     * Constructor.
     */
    @Inject
    public AbstractUserController(UserDao userDao, MessageSource messageSource) {
        LogFactory.getLog(this.getClass()).debug("The UserController object created.");
        this.userDao = userDao;
        this.messageSource = messageSource;
    }

    /**
     * Trivial getter
     */
    protected UserDao getUserDao() {
        return this.userDao;
    }

    /**
     * Trivial getter
     */
    protected MessageSource getMessageSource() {
        return this.messageSource;
    }

    /**
     * Gets a {@link Map} which for each {@link AuthorityType} (more precisely its ordinal) contains an
     * appropriate ID of its checkbox.
     */
    protected Map<Integer, String> getRoleCheckboxId() {
        String commonIdFormat = "newAuthorities[%s]";
        Map<Integer, String> checkboxIds = new HashMap<Integer, String>();

        for (AuthorityType type : AuthorityType.values()) {
            int typeOrdinal = type.ordinal();
            checkboxIds.put(typeOrdinal, String.format(commonIdFormat, typeOrdinal));
        }
        return checkboxIds;
    }

    /**
     * Gets a {@link Map} which for each {@link AuthorityType} (more precisely its ordinal) contains an
     * appropriate localized title of its checkbox.
     */
    protected Map<Integer, String> getLocalizedRoleCheckboxTitles(HttpServletRequest request) {
        Map<Integer, String> checkboxTitles = new HashMap<Integer, String>();

        for (AuthorityType type : AuthorityType.values()) {
            String titleCode = null;
            switch (type) {
            case ROLE_REGISTERED_USER:
                titleCode = "user-roles.registered-user";
                break;
            case ROLE_MEMBER_OF_SV:
                titleCode = "user-roles.member-of-sv";
                break;
            case ROLE_MEMBER_OF_BOARD:
                titleCode = "user-roles.member-of-board";
                break;
            case ROLE_USER_ADMINISTRATOR:
                titleCode = "user-roles.user-administrator";
                break;
            default:
                throw new RuntimeException("Unsupported role.");
            }
            checkboxTitles.put(type.ordinal(),
                    Localization.findLocaleMessage(this.messageSource, request, titleCode));
        }
        return checkboxTitles;
    }
}
