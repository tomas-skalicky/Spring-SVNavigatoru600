package com.svnavigatoru600.web.records.session.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.AbstractRetrieveRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class RetrieveAllSessionRecordController extends AbstractRetrieveRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/";

    /**
     * Constructor.
     */
    @Inject
    public RetrieveAllSessionRecordController(SessionRecordDao recordDao, MessageSource messageSource) {
        super(RetrieveAllSessionRecordController.BASE_URL, new PageViews(), recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = RetrieveAllSessionRecordController.BASE_URL + "existujici/{recordId}/stahnout/", method = RequestMethod.GET)
    public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
