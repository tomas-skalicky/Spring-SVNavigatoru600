package com.svnavigatoru600.web.records.session.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.AbstractListRecordsController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class ListAllSessionRecordsController extends AbstractListRecordsController {

    private static final String BASE_URL = "/zapisy-z-jednani/";

    /**
     * Constructor.
     */
    @Inject
    public ListAllSessionRecordsController(SessionRecordDao recordDao, MessageSource messageSource) {
        super(ListAllSessionRecordsController.BASE_URL, new PageViews(), recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = ListAllSessionRecordsController.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @RequestMapping(value = ListAllSessionRecordsController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @RequestMapping(value = ListAllSessionRecordsController.BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
