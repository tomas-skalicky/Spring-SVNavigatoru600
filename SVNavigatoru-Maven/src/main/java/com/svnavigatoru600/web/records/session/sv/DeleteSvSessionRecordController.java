package com.svnavigatoru600.web.records.session.sv;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.SessionRecordType;
import com.svnavigatoru600.repository.records.SessionRecordDao;
import com.svnavigatoru600.web.records.session.DeleteRecordController;

@Controller
public class DeleteSvSessionRecordController extends DeleteRecordController {

    private static final String BASE_URL = "/zapisy-z-jednani/sv/";

    /**
     * Constructor.
     */
    @Autowired
    public DeleteSvSessionRecordController(SessionRecordDao recordDao, MessageSource messageSource) {
        super(DeleteSvSessionRecordController.BASE_URL, new PageViews(),
                SessionRecordType.SESSION_RECORD_OF_SV, recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = DeleteSvSessionRecordController.BASE_URL + "existujici/{recordId}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
