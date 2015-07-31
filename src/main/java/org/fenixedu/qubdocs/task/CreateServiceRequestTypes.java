package org.fenixedu.qubdocs.task;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.CurricularYear;
import org.fenixedu.academic.domain.Degree;
import org.fenixedu.academic.domain.DegreeCurricularPlan;
import org.fenixedu.academic.domain.EvaluationConfiguration;
import org.fenixedu.academic.domain.EvaluationSeason;
import org.fenixedu.academic.domain.ExecutionDegree;
import org.fenixedu.academic.domain.ExecutionYear;
import org.fenixedu.academic.domain.administrativeOffice.AdministrativeOffice;
import org.fenixedu.academic.domain.candidacy.IngressionType;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degree.degreeCurricularPlan.DegreeCurricularPlanState;
import org.fenixedu.academic.domain.serviceRequests.AcademicServiceRequestSituationType;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestCategory;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.ServiceRequestTypeOption;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.AcademicServiceRequestType;
import org.fenixedu.academic.domain.serviceRequests.documentRequests.DocumentRequestType;
import org.fenixedu.academic.domain.student.RegistrationProtocol;
import org.fenixedu.academic.domain.student.RegistrationRegimeType;
import org.fenixedu.academic.domain.student.StatuteType;
import org.fenixedu.academic.util.PeriodState;
import org.fenixedu.academictreasury.domain.emoluments.AcademicTax;
import org.fenixedu.academictreasury.domain.emoluments.ServiceRequestMapEntry;
import org.fenixedu.academictreasury.domain.settings.AcademicTreasurySettings;
import org.fenixedu.academictreasury.domain.tariff.AcademicTariff;
import org.fenixedu.academictreasury.domain.tuition.EctsCalculationType;
import org.fenixedu.academictreasury.domain.tuition.TuitionCalculationType;
import org.fenixedu.academictreasury.domain.tuition.TuitionPaymentPlan;
import org.fenixedu.academictreasury.domain.tuition.TuitionPaymentPlanGroup;
import org.fenixedu.academictreasury.dto.tariff.AcademicTariffBean;
import org.fenixedu.academictreasury.dto.tariff.TuitionPaymentPlanBean;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.bennu.scheduler.custom.CustomTask;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.treasury.domain.Currency;
import org.fenixedu.treasury.domain.FinantialEntity;
import org.fenixedu.treasury.domain.FinantialInstitution;
import org.fenixedu.treasury.domain.Product;
import org.fenixedu.treasury.domain.ProductGroup;
import org.fenixedu.treasury.domain.VatType;
import org.fenixedu.treasury.domain.exemption.TreasuryExemptionType;
import org.fenixedu.treasury.domain.settings.TreasurySettings;
import org.fenixedu.treasury.domain.tariff.DueDateCalculationType;
import org.fenixedu.treasury.domain.tariff.InterestType;
import org.joda.time.format.DateTimeFormat;

import com.google.common.base.Strings;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CreateServiceRequestTypes extends CustomTask {

    /******************************************
     * ++++ THINGS TO DO FIRST ++++
     * 
     * 1. Migrate Finantial Institutions
     * 2. Open and make current 2015/2016
     * 3. Create Execution Degrees
     * ****************************************
     */

    private static final String PAGAMENTO_EM_AVANCO = "PAGAMENTO_EM_AVANCO";
    private static final String INTEREST_CODE = "JURO";
    private static final Map<String, String> FI_LOOKUP = Maps.newHashMap();
    private static final int NOT_APPLIED = -1;

    static {
//        FI_LOOKUP.put("FMD", "503013366");
//        FI_LOOKUP.put("FL", "502657456");
//        FI_LOOKUP.put("FF", "502659807");
        FI_LOOKUP.put("RUL", "510739024");
//        FI_LOOKUP.put("FMV", "502286326");
    }

    @Override
    public void runTask() throws Exception {
        getLogger().info("createDefaultServiceRequestTypes()");
        createDefaultServiceRequestTypes();

        getLogger().info("createServiceRequestTypesToProducts_FROM_SPREADSHEET()");
        createServiceRequestTypes_FROM_SPREADSHEET();
    }

    private void createServiceRequestTypes_FROM_SPREADSHEET() {
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("DIPLOMA_REQUEST", academicServiceRequestType("DIPLOMA_REQUEST"),
                            documentRequestType("DIPLOMA_REQUEST"), "Carta de Curso", "", ServiceRequestCategory.CERTIFICATES,
                            true, true, false, false, "", true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CARTA_CURSO_2_VIA", academicServiceRequestType(""), documentRequestType(""),
                            "Carta de Curso - 2º Via", "", ServiceRequestCategory.CERTIFICATES, true, true, false, false, "",
                            true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CARTA_TITULO_AGREGACAO", academicServiceRequestType(""), documentRequestType(""),
                            "Carta de Título - Agregação", "", ServiceRequestCategory.CERTIFICATES, true, true, false, false, "",
                            true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CARTA_TITULO_HABILITACAO_COORDENACAO_CIENTIFICA", academicServiceRequestType(""),
                            documentRequestType(""), "Carta de Título - Habilitação para Coordenação Científica", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CARTA_TITULO_2_VIA", academicServiceRequestType(""), documentRequestType(""),
                            "Carta de Título - 2º Via", "", ServiceRequestCategory.CERTIFICATES, true, true, false, false, "",
                            true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PROCESSO_RECONHECIMENTO_GRAU", academicServiceRequestType(""),
                            documentRequestType(""), "Processos de Reconhecimento de Grau", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PROCESSO_EQUIVALENCIA_GRAU", academicServiceRequestType(""),
                            documentRequestType(""), "Processos de Equivalência de Grau", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_RECONHECIMENTO_GRAU", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de Reconhecimento", "", ServiceRequestCategory.CERTIFICATES, true,
                            true, false, false, "", true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_EQUIVALENCIA_GRAU", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de Equivalência", "", ServiceRequestCategory.CERTIFICATES, true,
                            true, false, false, "", true);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PEDIDO_REGISTO_GRAUS_DL_341_2007", academicServiceRequestType(""),
                            documentRequestType(""), "Pedido de Registo de Graus - DL n.º 341/2007, de 12 outubro", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PROVAS_AVALIACAO_CAPACIDADE_M23_ADMISSAO", academicServiceRequestType(""),
                            documentRequestType(""),
                            "Provas de capacidade para frequência em Maiores de 23 anos — Admissão a provas", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType(
                            "PROVAS_AVALIACAO_CAPACIDADE_M23_RECLAMACAO",
                            academicServiceRequestType(""),
                            documentRequestType(""),
                            "Provas de capacidade para frequência em Maiores de 23 anos — Reclamação da classificação das provas",
                            "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("REGISTRY_DIPLOMA_REQUEST", academicServiceRequestType("REGISTRY_DIPLOMA_REQUEST"),
                            documentRequestType("REGISTRY_DIPLOMA_REQUEST"), "Certidão de Registo", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_REGISTO_2_VIA", academicServiceRequestType(""), documentRequestType(""),
                            "Certidão de Registo - 2º Via", "", ServiceRequestCategory.CERTIFICATES, true, true, false, false,
                            "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("SUPLEMENTO_DIPLOMA_2_VIA", academicServiceRequestType(""), documentRequestType(""),
                            "Suplemento ao Diploma - 2º Via", "", ServiceRequestCategory.CERTIFICATES, true, true, false, false,
                            "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_REGISTO_CURSO_POS_GRADUADO_ESPECIALIZACAO",
                            academicServiceRequestType(""), documentRequestType(""),
                            "Certidão de Registo de Curso pós-graduado de especialização", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("DEGREE_FINALIZATION_CERTIFICATE", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("DEGREE_FINALIZATION_CERTIFICATE"), "Certidão de Conclusão", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_OBTENCAO_TITULO_AGREGADO", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de título de agregado e das capacidades ciêntificas", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_CONCLUSAO_CURSO_DOUTORAMENTO", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de conclusão do curso de Doutoramento", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_CONCLUSAO_CURSO_MESTRADO", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de conclusão do curso de Mestrado", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_PROVAS_APTIDAO_PEDAGOGICA", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de Provas de Aptidão Pedagógica e Capacidade Ciêntifica", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_CONCLUSAO_CURSO_ESPECIALIZACAO", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de conclusão do curso de Especialização", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("SCHOOL_REGISTRATION_CERTIFICATE", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("SCHOOL_REGISTRATION_CERTIFICATE"), "Certidão de Matrícula", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("ENROLMENT_CERTIFICATE", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("ENROLMENT_CERTIFICATE"), "Certidão de Inscrição", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_FREQUENCIA_EXAME", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de Frequência", "", ServiceRequestCategory.CERTIFICATES, true,
                            true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_CONDUTA_ACADEMICA", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de Conduta Académica", "", ServiceRequestCategory.CERTIFICATES,
                            true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIFICADO_NARRATIVA_TEOR", academicServiceRequestType(""),
                            documentRequestType(""), "Certificado de narrativa ou de teor", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType(
                            "CERTIFICADO_AVALIACAO_CAPACIDADE_M23",
                            academicServiceRequestType(""),
                            documentRequestType(""),
                            "Certificado de aprovação no processo de avaliação da capacidade para a frequência do Ensino Superior de Maiores de 23 anos",
                            "", ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PROGRAM_CERTIFICATE", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("PROGRAM_CERTIFICATE"),
                            "Certificado de cargas horárias e conteúdos programáticos", "", ServiceRequestCategory.CERTIFICATES,
                            true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PHOTOCOPY", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("PHOTOCOPY"), "Certidão por Fotocópia", "", ServiceRequestCategory.CERTIFICATES,
                            true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PEDIDO_CREDITACAO_CONHECIMENTOS_COMPETENCIAS", academicServiceRequestType(""),
                            documentRequestType(""), "Pedido de Creditação de Conhecimentos e Competências", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("DIPLOMA_CURSO_DOUTORAMENTO", academicServiceRequestType(""),
                            documentRequestType(""), "Diploma — Curso de doutoramento (componente curricular) ", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("DIPLOMA_CURSO_MESTRADO", academicServiceRequestType(""), documentRequestType(""),
                            "Diploma — Curso de mestrado (componente curricular)", "", ServiceRequestCategory.CERTIFICATES, true,
                            true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("DIPLOMA_CURSO_ESPECIALIZACAO", academicServiceRequestType(""),
                            documentRequestType(""), "Diploma — Curso de especialização", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("ADMISSAO_PROVAS_ACADEMICAS_AGREGACAO", academicServiceRequestType(""),
                            documentRequestType(""), "Admissão a Provas Académicas — Agregação", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType(
                            "ADMISSAO_PROVAS_ACADEMICAS_HABILITACAO_COORDENACAO_CIENTIFICA",
                            academicServiceRequestType(""),
                            documentRequestType(""),
                            "Admissão a Provas Académicas — Habilitação para o Exercício de Atividades de Coordenação Científica",
                            "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("ADMISSAO_PROVAS_ACADEMICAS_DOUTORAMENTO_ART_33_DL_74_2006",
                            academicServiceRequestType(""), documentRequestType(""),
                            "Admissão a Provas Académicas — Doutoramento art. 33.º do DL n.º 74/2006", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("ADMISSAO_PROVAS_ACADEMICAS_DOUTORAMENTO", academicServiceRequestType(""),
                            documentRequestType(""), "Admissão a Provas Académicas — Doutoramento", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("ADMISSAO_PROVAS_ACADEMICAS_MESTRADO", academicServiceRequestType(""),
                            documentRequestType(""), "Admissão a Provas Académicas — Mestrado ", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PEDIDO_CREDITACAO_COMPETENCIAS_ACADEMICAS", academicServiceRequestType(""),
                            documentRequestType(""), "Pedido de creditação de Competências Académicas", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PEDIDO_CREDITACAO_COMPETENCIAS_PROFISSIONAIS", academicServiceRequestType(""),
                            documentRequestType(""), "Pedido de creditação de Competências Profissionais", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_DOUTORAMENTO", academicServiceRequestType(""), documentRequestType(""),
                            "Candidatura: Doutoramento", "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_MESTRADO", academicServiceRequestType(""), documentRequestType(""),
                            "Candidatura: Mestrado", "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_REINGRESSO", academicServiceRequestType(""), documentRequestType(""),
                            "Candidatura: Reingresso", "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_REINGRESSO_ALUNOS_ULISBOA", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Reingresso (Alunos da ULisboa)", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_TRANSFERENCIA", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Transferência", "", ServiceRequestCategory.SERVICES, true,
                            true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_TRANSFERENCIA_ALUNOS_ULISBOA", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Transferência (Alunos ULisboa)", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_MUDANCA_CURSO", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Mudança de Curso", "", ServiceRequestCategory.SERVICES, true,
                            true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_MUDANCA_CURSO_ALUNOS_ULISBOA", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Mudança de Curso (Alunos ULisboa)", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_OUTRO", academicServiceRequestType(""), documentRequestType(""),
                            "Candidatura: Outro concurso especial de acesso", "", ServiceRequestCategory.SERVICES, true, true,
                            false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_OUTRO_ALUNOS_ULISBOA", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Outro concurso especial de acesso (Alunos ULisboa)", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CANDIDATURA_UNIDADES_CURRICULARES_ISOLADAS", academicServiceRequestType(""),
                            documentRequestType(""), "Candidatura: Unidades Curriculares Isoladas", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PRATICA_ATOS_FORA_PRAZO", academicServiceRequestType(""), documentRequestType(""),
                            "Prática de Atos Fora do Prazo", "", ServiceRequestCategory.SERVICES, true, true, false, true,
                            "Nº de dias", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("AVERBAMENTO", academicServiceRequestType(""), documentRequestType(""),
                            "Averbamento", "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("REVISAO_PROVAS_CAUCAO", academicServiceRequestType(""), documentRequestType(""),
                            "Revisão de provas — caução", "", ServiceRequestCategory.SERVICES, true, true, false, false, "",
                            false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PEDIDO_PERMUTA", academicServiceRequestType(""), documentRequestType(""),
                            "Pedido de permuta", "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("VALIDACAO_PROCESSOS_ACESSO_M23_OUTRAS_INSTITUICOES",
                            academicServiceRequestType(""), documentRequestType(""),
                            "Validação de processos de acesso de Maiores de 23 anos (outras Instituições)", "",
                            ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMV").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("_2_VIA_LOGBOOK", academicServiceRequestType(""), documentRequestType(""),
                            "2º Via do logbook", "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("FOTOCOPIA", academicServiceRequestType(""), documentRequestType(""), "Fotocópia",
                            "", ServiceRequestCategory.SERVICES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("TAXA_ENVIO_CORREIO", academicServiceRequestType(""), documentRequestType(""),
                            "Taxa de envio por correio  ", "", ServiceRequestCategory.SERVICES, true, true, false, false, "",
                            false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("TAXA_DEVOLUCAO_CHEQUE", academicServiceRequestType(""), documentRequestType(""),
                            "Taxa por devolução de cheque  ", "", ServiceRequestCategory.SERVICES, true, true, false, false, "",
                            false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PORTES_CORREIO_NACIONAL", academicServiceRequestType(""), documentRequestType(""),
                            "Portes de Correio (Nacional)", "", ServiceRequestCategory.SERVICES, true, true, false, false, "",
                            false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("PORTES_CORREIO_INTERNACIONAL", academicServiceRequestType(""),
                            documentRequestType(""), "Portes de Correio (Internacional)", "", ServiceRequestCategory.SERVICES,
                            true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("ENROLMENT_DECLARATION", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("ENROLMENT_DECLARATION"), "Declaração de Inscrição", "",
                            ServiceRequestCategory.DECLARATIONS, true, false, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FMD, FF, FMV, FL, RUL").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("SCHOOL_REGISTRATION_DECLARATION", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("SCHOOL_REGISTRATION_DECLARATION"), "Declaração de Matrícula", "",
                            ServiceRequestCategory.DECLARATIONS, true, false, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FF").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("APPROVEMENT_MOBILITY_CERTIFICATE", academicServiceRequestType("DOCUMENT"),
                            documentRequestType("APPROVEMENT_MOBILITY_CERTIFICATE"), "Certidão de Aproveitamento", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
        if (!fromAcronymsToFinantialInstitutionList("FF").isEmpty()) {
            final ServiceRequestType serviceRequestType =
                    createServiceRequestType("CERTIDAO_APROVEITAMENTO_ESCOLAR_TRANSICAO_ANO", academicServiceRequestType(""),
                            documentRequestType(""), "Certidão de Aproveitamento Escolar / Transição de Ano", "",
                            ServiceRequestCategory.CERTIFICATES, true, true, false, false, "", false);
        }
    }

    private ServiceRequestType serviceRequestType(final String code, final AcademicServiceRequestType academicServiceRequestType,
            final DocumentRequestType documentRequestType) {
        if (!Strings.isNullOrEmpty(code)) {
            if (ServiceRequestType.findUniqueByCode(code).isPresent()) {
                return ServiceRequestType.findUniqueByCode(code).get();
            }
        }

        if (academicServiceRequestType != null) {
            if (ServiceRequestType.findUnique(academicServiceRequestType, documentRequestType) != null) {
                return ServiceRequestType.findUnique(academicServiceRequestType, documentRequestType);
            }
        }

        return null;
    }

    private DocumentRequestType documentRequestType(final String value) {
        if (Strings.isNullOrEmpty(value.trim())) {
            return null;
        }

        return DocumentRequestType.valueOf(value);
    }

    private AcademicServiceRequestType academicServiceRequestType(final String value) {
        if (Strings.isNullOrEmpty(value.trim())) {
            return null;
        }

        return AcademicServiceRequestType.valueOf(value);
    }

    private ServiceRequestType createServiceRequestType(final String code,
            final AcademicServiceRequestType academicServiceRequestType, final DocumentRequestType documentRequestType,
            final String namePT, final String nameEN, final ServiceRequestCategory category, final boolean active,
            final boolean payable, final boolean detailed, final boolean numberOfUnits, final String numberOfUnitsDesignationPT,
            final boolean urgent) {

        if (!Strings.isNullOrEmpty(code)) {
            if (ServiceRequestType.findUniqueByCode(code).isPresent()) {
                return ServiceRequestType.findUniqueByCode(code).get();
            }
        }

        if (academicServiceRequestType != null) {
            if (ServiceRequestType.findUnique(academicServiceRequestType, documentRequestType) != null) {
                return ServiceRequestType.findUnique(academicServiceRequestType, documentRequestType);
            }
        }

        final ServiceRequestType createdServiceRequestType;
        final LocalizedString name = new LocalizedString(pt(), namePT).with(en(), nameEN);
        if (academicServiceRequestType != null) {
            createdServiceRequestType =
                    ServiceRequestType.createLegacy(code, name, true, academicServiceRequestType, documentRequestType, payable,
                            category);
        } else {
            createdServiceRequestType = ServiceRequestType.create(code, name, true, payable, category);
        }

        if (detailed) {
            createdServiceRequestType.associateOption(ServiceRequestTypeOption.findDetailedOption().get());
        }

        if (numberOfUnits) {
            createdServiceRequestType.associateOption(ServiceRequestTypeOption.findNumberOfUnitsOption().get());
            createdServiceRequestType.edit(createdServiceRequestType.getCode(), createdServiceRequestType.getName(),
                    createdServiceRequestType.getActive(), createdServiceRequestType.getPayable(),
                    createdServiceRequestType.getServiceRequestCategory(), new LocalizedString(pt(), numberOfUnitsDesignationPT));
        }

        return createdServiceRequestType;
    }

    private Set<ServiceRequestTypeOption> requestTypeDetailed(final boolean isDetailed) {

        if (isDetailed) {
            return Sets.newHashSet(ServiceRequestTypeOption.findDetailedOption().get());
        }

        return Sets.newHashSet();
    }

    private List<FinantialInstitution> fromAcronymsToFinantialInstitutionList(final String acronyms) {
        String[] split = acronyms.split(",");

        final List<FinantialInstitution> result = new ArrayList<FinantialInstitution>();
        for (String acronym : split) {
            if (!FI_LOOKUP.containsKey(acronym.trim())) {
                continue;
            }

            result.add(FinantialInstitution.findUniqueByFiscalCode(FI_LOOKUP.get(acronym.trim())).get());
        }

        return result;
    }

    private FinantialEntity oneOfFinantialEntity(final String acronyms) {
        return FinantialEntity.findAll()
                .filter(l -> l.getAdministrativeOffice() == AdministrativeOffice.readDegreeAdministrativeOffice()).findFirst()
                .get();
    }

    private DegreeType findDegreeTypeByCode(final String code) {
        if (code.isEmpty()) {
            return null;
        }

        for (DegreeType degreeType : Bennu.getInstance().getDegreeTypeSet()) {
            if (code.equals(degreeType.getCode())) {
                return degreeType;
            }
        }

        return null;
    }

    private Degree findDegree(final String code) {
        if (code.isEmpty()) {
            return null;
        }

        return Degree.find(code);
    }

    private BigDecimal maximumAmount(int v) {
        if (v == NOT_APPLIED) {
            return BigDecimal.ZERO;
        }

        return new BigDecimal(v);
    }

    private org.joda.time.LocalDate fixedDueDate(final String v) {
        if (v.isEmpty()) {
            return null;
        }

        return DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(v);
    }

    private org.joda.time.LocalDate parseLocalDate(final String v) {
        if (v.isEmpty()) {
            return null;
        }

        return DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(v);
    }

    private BigDecimal amount(final String v) {
        if (v.isEmpty()) {
            return null;
        }

        return new BigDecimal(v);
    }

    private Locale pt() {
        return new Locale("PT", "pt");
    }

    private Locale en() {
        return new Locale("EN", "en");
    }

    private LocalizedString defaultUnitOfMeasure() {
        return new LocalizedString(pt(), "Unidade").with(en(), "Unit");
    }

    private static void createDefaultServiceRequestTypes() {
        ServiceRequestTypeOption.create(
                "DETAILED",
                BundleUtil.getLocalizedString("resources.AcademicAdminOffice", ServiceRequestTypeOption.class.getSimpleName()
                        + ".detailed"), true, false);

        ServiceRequestTypeOption.create(
                "NUMBER_OF_UNITS",
                BundleUtil.getLocalizedString("resources.AcademicAdminOffice", ServiceRequestTypeOption.class.getSimpleName()
                        + ".numberOfUnitsOption"), false, true);
    }
}