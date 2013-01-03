package com.svnavigatoru600.repository.users.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.transaction.annotation.Transactional;

import com.svnavigatoru600.repository.users.UserDao;

/**
 * Provides an authentication via {@link com.svnavigatoru600.domain.users.User User's} username, or email.
 * From http://blog.solidcraft.eu/2011/03/spring-security-by-example-set-up-and.html.
 * 
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
public class AuthenticationUserDetailsGetter extends JdbcDaoImpl {

    private final Log logger = LogFactory.getLog(this.getClass());

    private UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) {
        try {
            return this.userDao.findByUsername(username);
        } catch (DataAccessException e) {
            // User with the given username not found.
            this.logger.info(String.format("User with the given username '%s' not found", username));
        }

        try {
            // Since the user has not been found till now and the username
            // variable can also contain an email, it is now time to check
            // stored emails.
            return this.userDao.findByEmail(username);
        } catch (NonTransientDataAccessException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
