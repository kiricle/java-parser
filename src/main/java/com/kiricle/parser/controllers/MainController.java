package com.kiricle.parser.controllers;

import com.kiricle.parser.models.Vacancy;
import com.kiricle.parser.services.DouParser;
import com.kiricle.parser.services.ExcelManager;
import com.kiricle.parser.services.WorkUAParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MainController {

    @GetMapping("/vacancy")
    public List<Vacancy> getVacancy(@RequestParam("search") String search, @RequestParam("city") String city, @RequestParam("exp") String experience) {
        DouParser douParser = new DouParser();
        WorkUAParser workUAParser = new WorkUAParser();

        List<Vacancy> douVacancies = douParser.parseBySearch(search, experience, city);
        List<Vacancy> workUAVacancies = workUAParser.parseBySearch(search, city);

        douVacancies.addAll(workUAVacancies);

        return douVacancies;
    }

    @PostMapping("/export")
    public ResponseEntity<byte[]> getExcelFile(@RequestBody List<Vacancy> vacancies) {
        byte[] createdFile = new ExcelManager().createFileFromVacancies(vacancies);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "example.xlsx");
        return  new ResponseEntity<>(createdFile, headers, HttpStatus.OK);
    }
}
