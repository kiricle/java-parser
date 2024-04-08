package com.kiricle.parser.services;

import com.kiricle.parser.models.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WorkUAParser {
    private final String WORK_UA_URL = "https://www.work.ua/jobs";
    private final String WORK_UA_BASE_URL = "https://www.work.ua";

    public List<Vacancy> parseBySearch(String search, String city) {
        try {
            String formattedURL = formatURL(search, city);
            Document doc = Jsoup.connect(formattedURL).get();

            Elements vacanciesElements = doc.select(".job-link");

            List<Vacancy> vacancies = new ArrayList();
            for (Element vacancyElement : vacanciesElements) {
                vacancies.add(getVacancyFromHTML(vacancyElement));
            }

            return vacancies;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Vacancy getVacancyFromHTML(Element vacancyElement) {
        String title = vacancyElement.select(".cut-top").text();

        Elements infoDiv = vacancyElement.select("div.add-top-xs");
        String location = infoDiv.select("span[class=\"\"]").text();
        String company = infoDiv.select("span:nth-of-type(1)").text();

        String reference = WORK_UA_BASE_URL + vacancyElement.select("a[title]").attr("href");
        String date = vacancyElement.select(".text-default-7.add-top").text();

        return new Vacancy(title, date, location,company,reference);
    }

    private String formatURL(String search, String city) {
        String result = WORK_UA_URL;

        if (city != "") {
            result += "-" + city.toLowerCase();
        }

        result += "-" + String.join("+", search.trim().split(" "));

        return result;
    }
}
