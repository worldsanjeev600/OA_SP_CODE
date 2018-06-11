package com.oneassist.serviceplatform.commons.filegenerator;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportRequestDto;
import com.oneassist.serviceplatform.contracts.dtos.servicerequest.ServiceRequestReportResponseDto;

/**
 * @author Alok Singh
 */
public abstract class BaseFileGenerator implements IFileGenerator {

    private final Logger logger = Logger.getLogger(BaseFileGenerator.class);

    protected Document doFileGeneration(ServiceRequestReportRequestDto serviceRequestReportDto) throws Exception {
        InputStream inputStream = null;
        
        try {
            POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(serviceRequestReportDto.getTemplateFilePath()));
            HWPFDocument doc = new HWPFDocument(fs);
            doc = attchDynamicValueTodoc(serviceRequestReportDto.getData(), doc);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            doc.write(baos);

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            License license = new License();
            logger.info("BaseFileGenerator : doFileGeneration() Adding license to Aspose");
            inputStream = classLoader.getResourceAsStream("Aspose.Words.lic");
            if (inputStream != null) {
                license.setLicense(inputStream);
                logger.info("Aspose.Cells license set? " + (license.getIsLicensed()));
            }
            Document pdfdoc = new Document(new ByteArrayInputStream(baos.toByteArray()));
            
            return pdfdoc;
        } finally {
            if (inputStream != null)
                inputStream.close();
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private HWPFDocument attchDynamicValueTodoc(Map model, HWPFDocument doc) {
        doc = replaceText(doc, model);
        return doc;
    }

    private static HWPFDocument replaceText(HWPFDocument doc, Map<String, String> map) {
        Range r1 = doc.getRange();
        for (int i = 0; i < r1.numSections(); ++i) {
            Section s = r1.getSection(i);
            for (int x = 0; x < s.numParagraphs(); x++) {
                Paragraph p = s.getParagraph(x);
                for (int z = 0; z < p.numCharacterRuns(); z++) {
                    CharacterRun run = p.getCharacterRun(z);
                    String text = run.text();
                    String splittedText[] = text.split("\\$");
                    for (String textSplit : splittedText)
                        if (map.containsKey("$" + textSplit.trim())) {
                            run.replaceText("$" + textSplit.trim(), map.get("$" + textSplit.trim()));
                        }
                }
            }
        }
        return doc;
    }
    
    protected ServiceRequestReportResponseDto generatePdf(Document pdfdoc) throws Exception{
    	ServiceRequestReportResponseDto serviceRequestReportResponseDto = new ServiceRequestReportResponseDto();
    	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    	pdfdoc.save(outputStream, SaveFormat.PDF);
		ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
		serviceRequestReportResponseDto.setInputStream(inputStream);
		/*
		File outputFile = new File("D:\\abc.pdf");
		FileOutputStream ff = new FileOutputStream(outputFile);
		outputStream.writeTo(ff);*/
		return serviceRequestReportResponseDto;
    }
}
