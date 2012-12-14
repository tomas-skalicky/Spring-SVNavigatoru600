package com.svnavigatoru600.web.records.otherdocuments.contracts;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.records.otherdocuments.ListDocumentsController;

@Controller
public class ListContractDocumentsController extends ListDocumentsController {

    private static final String BASE_URL = "/dalsi-dokumenty/smlouvy/";

    /**
     * Constructor.
     */
    @Autowired
    public ListContractDocumentsController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(ListContractDocumentsController.BASE_URL, new PageViews(), OtherDocumentRecordType.CONTRACT,
                recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = ListContractDocumentsController.BASE_URL, method = RequestMethod.GET)
    public String initPage(HttpServletRequest request, ModelMap model) {
        return super.initPage(request, model);
    }

    @Override
    @RequestMapping(value = ListContractDocumentsController.BASE_URL + "vytvoreno/", method = RequestMethod.GET)
    public String initPageAfterCreate(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterCreate(request, model);
    }

    @Override
    @RequestMapping(value = ListContractDocumentsController.BASE_URL + "smazano/", method = RequestMethod.GET)
    public String initPageAfterDelete(HttpServletRequest request, ModelMap model) {
        return super.initPageAfterDelete(request, model);
    }
}
