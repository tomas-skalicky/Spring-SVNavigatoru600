package com.svnavigatoru600.web.records.session.sv;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.RetrieveRecordController;

@Controller
public class RetrieveSvSessionRecordController extends RetrieveRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/sv/";

    /**
     * Constructor.
     */
    @Autowired
    public RetrieveSvSessionRecordController(SessionRecordDao recordDao, MessageSource messageSource) {
        super(RetrieveSvSessionRecordController.BASE_URL, new PageViews(),
                SessionRecordType.SESSION_RECORD_OF_SV, recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = RetrieveSvSessionRecordController.BASE_URL + "existujici/{recordId}/stahnout/", method = RequestMethod.GET)
    public void retrieve(@PathVariable int recordId, HttpServletResponse response, ModelMap model) {
        super.retrieve(recordId, response, model);
    }
}
