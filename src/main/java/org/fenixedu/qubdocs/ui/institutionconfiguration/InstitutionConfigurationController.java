package org.fenixedu.qubdocs.ui.institutionconfiguration;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.fenixedu.bennu.FenixeduQubdocsReportsSpringConfiguration;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.spring.portal.SpringFunctionality;
import org.fenixedu.commons.StringNormalizer;
import org.fenixedu.qubdocs.domain.InstitutionLogo;
import org.fenixedu.qubdocs.domain.InstitutionReportConfiguration;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsBaseController;
import org.fenixedu.qubdocs.ui.FenixeduQubdocsReportsController;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import pt.ist.fenixframework.Atomic;

@SpringFunctionality(app = FenixeduQubdocsReportsController.class, title = "label.title.institutionConfiguration",
        accessGroup = "logged")
@RequestMapping(InstitutionConfigurationController.CONTROLLER_URL)
public class InstitutionConfigurationController extends FenixeduQubdocsReportsBaseController {

    public static final String CONTROLLER_URL = "/qubdocsreports/institutionconfiguration";

    private void setInstitutionConfiguration(InstitutionReportConfiguration institutionReportConfiguration, Model model) {
        model.addAttribute("institutionReportConfiguration", institutionReportConfiguration);
    }

    @RequestMapping
    public String home(Model model) {
        return "forward:" + CONTROLLER_URL + _READ_URI;
    }

    private static final String _READ_URI = "/read";
    public static final String READ_URL = CONTROLLER_URL + _READ_URI;

    @RequestMapping(value = _READ_URI)
    public String read(Model model) {
        setInstitutionConfiguration(InstitutionReportConfiguration.getInstance(), model);
        return "qubdocsreports/institutionReportConfiguration/read";
    }

    private static final String _UPDATE_URI = "/update";
    public static final String UPDATE_URL = CONTROLLER_URL + _UPDATE_URI;

    @RequestMapping(value = _UPDATE_URI, method = RequestMethod.GET)
    public String update(Model model) {
        setInstitutionConfiguration(InstitutionReportConfiguration.getInstance(), model);

        return "qubdocsreports/institutionReportConfiguration/update";
    }

    @RequestMapping(value = _UPDATE_URI, method = RequestMethod.POST)
    public String update(@RequestParam(value = "institution", required = true) String institutionName,
            @RequestParam(value = "logoFile", required = false) MultipartFile logoFile, Model model,
            RedirectAttributes redirectAttributes) {
        try {
            InstitutionReportConfiguration configuration = InstitutionReportConfiguration.getInstance();

            update(institutionName, logoFile);

            setInstitutionConfiguration(configuration, model);
            return redirect(READ_URL, model, redirectAttributes);
        } catch (Exception de) {
            addErrorMessage(BundleUtil.getString(FenixeduQubdocsReportsSpringConfiguration.BUNDLE, "label.error.create")
                    + de.getLocalizedMessage(), model);
        }
        return update(model);
    }

    @Atomic
    private void update(String institutionName, MultipartFile logoFile) throws IOException {
        InstitutionReportConfiguration configuration = InstitutionReportConfiguration.getInstance();

        configuration.setName(institutionName);
        InstitutionLogo institutionLogo = new InstitutionLogo(logoFile.getOriginalFilename(), logoFile.getBytes());
        configuration.setInstitutionLogo(institutionLogo);
    }

    private static final String _DOWNLOAD_URI = "/download";
    public static final String DOWNLOAD_URL = CONTROLLER_URL + _DOWNLOAD_URI;

    @RequestMapping(value = _DOWNLOAD_URI + "/{logoFileId}", method = RequestMethod.GET)
    public void processSearchToDownloadAction(@PathVariable("logoFileId") InstitutionLogo institutionLogo,
            HttpServletResponse response) {
        try {
            response.setContentType(institutionLogo.getContentType());
            String filename = URLEncoder.encode(
                    StringNormalizer.normalizePreservingCapitalizedLetters(institutionLogo.getFilename()).replaceAll("\\s", "_"),
                    "UTF-8");
            response.setHeader("Content-disposition", "attachment; filename=" + filename);
            response.getOutputStream().write(institutionLogo.getContent());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
