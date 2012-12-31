package com.svnavigatoru600.web.records.otherdocuments.contracts;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.svnavigatoru600.domain.records.OtherDocumentRecordType;
import com.svnavigatoru600.repository.records.OtherDocumentRecordDao;
import com.svnavigatoru600.web.records.otherdocuments.AbstractDeleteDocumentController;

/**
 * @author <a href="mailto:skalicky.tomas@gmail.com">Tomas Skalicky</a>
 */
@Controller
public class DeleteContractDocumentController extends AbstractDeleteDocumentController {

    private static final String BASE_URL = "/dalsi-dokumenty/smlouvy/";

    /**
     * Constructor.
     */
    @Inject
    public DeleteContractDocumentController(OtherDocumentRecordDao recordDao, MessageSource messageSource) {
        super(DeleteContractDocumentController.BASE_URL, new PageViews(), OtherDocumentRecordType.CONTRACT,
                recordDao, messageSource);
    }

    @Override
    @RequestMapping(value = DeleteContractDocumentController.BASE_URL + "existujici/{recordId}/smazat/", method = RequestMethod.GET)
    public String delete(@PathVariable int recordId, HttpServletRequest request, ModelMap model) {
        return super.delete(recordId, request, model);
    }
}
