package com.kiricle.parser.services;

import com.kiricle.parser.models.Vacancy;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelManager {
    public byte[] createFileFromVacancies(List<Vacancy> vacancies) {
        var workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("List of vacancies");

        createHeader(sheet);

        for (int i = 1; i < vacancies.size() + 1; i++) {
            Vacancy currentVacancy = vacancies.get(i - 1);
            createVacancyRow(sheet, i, currentVacancy, workbook);
        }

        for (int i = 0; i < 6; i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createVacancyRow(Sheet sheet, int i, Vacancy currentVacancy, XSSFWorkbook workbook) {
        Row row = sheet.createRow(i);

        Cell nameCell = row.createCell(0);
        nameCell.setCellValue(currentVacancy.getName());

        Cell dateCell = row.createCell(1);
        dateCell.setCellValue(currentVacancy.getDate());

        Cell companyCell = row.createCell(2);
        companyCell.setCellValue(currentVacancy.getCompany());

        Cell locationCell = row.createCell(3);
        locationCell.setCellValue(currentVacancy.getLocation());

        CreationHelper creationHelper = workbook.getCreationHelper();
        Hyperlink hyperlink = creationHelper.createHyperlink(HyperlinkType.URL);
        hyperlink.setAddress(currentVacancy.getReference());

        Cell referenceCell = row.createCell(4);
        referenceCell.setCellValue(currentVacancy.getReference());
        referenceCell.setHyperlink(hyperlink);
    }

    private void createHeader(Sheet sheet) {
        Row headRow = sheet.createRow(0);

        headRow.createCell(0).setCellValue("Vacancy");
        headRow.createCell(1).setCellValue("Date");
        headRow.createCell(2).setCellValue("Company");
        headRow.createCell(3).setCellValue("Location");
        headRow.createCell(4).setCellValue("Reference");
    }
}
