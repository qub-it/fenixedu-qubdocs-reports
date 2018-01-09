/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: anil.mamede@qub-it.com
 *
 *
 * This file is part of FenixEdu QubDocs.
 *
 * FenixEdu QubDocs is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu QubDocs is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu QubDocs.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.fenixedu.qubdocs.domain;

import java.io.File;
import java.util.Set;

import org.fenixedu.bennu.core.domain.Bennu;

import com.google.common.collect.Sets;
import com.qubit.terra.docs.core.IDocumentTemplateService;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.Atomic.TxMode;

public class DocumentPrinterConfiguration extends DocumentPrinterConfiguration_Base implements IDocumentTemplateService {

    private static String DEFAULT_FONTS_PATH = "/usr/share/fonts";

    private DocumentPrinterConfiguration() {
        super();
        setBennu(Bennu.getInstance());
        initFontsPath();
        setOpenOfficeConverting(true);
    }

    private void initFontsPath() {
        if (System.getProperty("os.name").startsWith("Windows")) {
            String path = System.getenv("WINDIR");
            File fontDirectory = new File(path, "Fonts");
            setFontsPath(fontDirectory.getAbsolutePath());
        } else {
            setFontsPath(DEFAULT_FONTS_PATH);
        }
    }

    @Override
    public boolean isOpenOfficeConverting() {
        return getOpenOfficeConverting() != null && getOpenOfficeConverting();
    }

    @Override
    public Set<? extends DocumentTemplate> readAllDocuments() {
        return Bennu.getInstance().getDocumentTemplatesSet();
    }

    @Override
    public Set<? extends DocumentTemplate> readActiveDocuments() {
        return Sets.filter(readAllDocuments(), DocumentTemplate.filters.active(true));
    }

    @Atomic(mode = TxMode.SPECULATIVE_READ)
    public static DocumentPrinterConfiguration getInstance() {
        if (Bennu.getInstance().getDocumentPrinterConfiguration() == null) {
            return initialize();
        }
        return Bennu.getInstance().getDocumentPrinterConfiguration();
    }

    private static DocumentPrinterConfiguration initialize() {
        if (Bennu.getInstance().getDocumentPrinterConfiguration() == null) {
            return new DocumentPrinterConfiguration();
        }
        return Bennu.getInstance().getDocumentPrinterConfiguration();
    }

}
