package com.kiricle.parser.services;

import com.kiricle.parser.models.DOUExperience;
import com.kiricle.parser.models.Vacancy;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DouParser {
    private static final String DOU_URL = "https://jobs.dou.ua/vacancies/?search=";

    public List<Vacancy> parseBySearch(String search, String experience, String city) {
        try {
            if (!isExperienceValid(experience)) throw new Exception("The wrong experience is given");
            String preparedSearch = prepareSearch(search);

            Document doc = Jsoup.connect(concatURL(preparedSearch, experience, city)).get();

            Elements vacancies = doc.select(".l-vacancy");

            List<Vacancy> items = new ArrayList();
            for (Element vacancy : vacancies) {
                items.add(getVacancyFromHTML(vacancy));
            }

            return items;
        } catch (Exception e) {
            System.out.println(e);
        }

        return null;
    }

    private static Vacancy getVacancyFromHTML(Element vacancy) {
        String date = vacancy.select(".date").text();
        Element linkTag = vacancy.select(".vt").first();
        String reference = linkTag.attr("href");
        String name = linkTag.text();
        String location = vacancy.select(".cities").text();
        String company = vacancy.select(".company").text();

        return new Vacancy(name, date, location, company, reference);
    }

    private String prepareSearch(String search) {
        return search.trim().replace(" ", "+");
    }

    private boolean isExperienceValid(String userExperience) {
        for (DOUExperience experience : DOUExperience.values()) {
            if (experience.getExperience().equals(userExperience)) {
                return true;
            }
        }

        return false;
    }

    private String concatURL(String position, String experience, String city) {
        return DOU_URL + position + "&exp=" + experience + "&city=" + city;
    }
}
