package com.svnavigatoru600.web.records.session.all;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.AbstractDeleteRecordController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteAllSessionRecordController extends AbstractDeleteRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/";

    /**
     * Constructor.
     */
    @Inject
    public DeleteAllSessionRecordController(SessionRecordDao recordDao, MessageSource messageSource) {
        super(DeleteAllSessionRecordController.BASE_URL, new PageViews(), recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = DeleteAllSessionRecordController.BASE_URL + "existujici/{recordId}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
