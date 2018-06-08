package de.slg.datadome;

import java.util.List;
import java.util.Date;

public abstract class Filter {

    public static List<MapLocation> filterCategory(List<Article> articleList, List<Short> category) {
        List<MapLocation> filtertList = null;
        for (Article current : articleList) {
            List<Short> currentCategories = current.getCategoryIds();

            for (short s : category) {
                for (short c : currentCategories) {
                    if (s == changeCategories(c)) {
                        if (!current.getAbstractText().equals(""))
                            filtertList.add(changeID(current));
                    }
                }
            }
        }

        return filtertList;
    }


    public static List<Article> filterTime(List<Article> articleList, Date from, Date to) {
        List<Article> filtertList = null;
        for (Article current : articleList) {
            if (current.getDates().isEmpty())
                filtertList.add(current);
            else {
                for (DateRange d : current.getDates()) {
                    if (d.getFrom().before(from) && d.getTo().after(to))
                        filtertList.add(changeID(current));
                    else if (d.getFrom().before(from) && d.getTo() == null)
                        filtertList.add(changeID(current));

                }
            }

        }

        return filtertList;
    }

    private static MapLocation changeID(Article current) {
        return new MapLocation(compareCategories(current), current.getId(), current.getCategoryIds(), current.getGeo(), current.getTitle(), current.getAbstractText(), current.getArticle(), current.getDates(), current.getAdress(), current.getPostalCode());
    }

    private static int compareCategories(Article current) {
        int category = current.getCategoryIds().get(0);
        int newCategory = 0;
        switch (category) {
            case 41:
                newCategory = 1;
                break;
            case 25:
                newCategory = 1;
                break;
            case 61:
                newCategory = 2;
                break;
            case 63:
                newCategory = 3;
                break;
            case 60:
                newCategory = 3;
                break;
            case 71:
                newCategory = 4;
                break;
            case 38:
                newCategory = 5;
                break;
            case 26:
                newCategory = 5;
                break;
        }

        return newCategory;
    }


    private static int changeCategories(int oldId) {

        int newCategory = 0;
        switch (oldId) {
            case 41:
                newCategory = 1;
                break;
            case 25:
                newCategory = 1;
                break;
            case 61:
                newCategory = 2;
                break;
            case 63:
                newCategory = 3;
                break;
            case 60:
                newCategory = 3;
                break;
            case 71:
                newCategory = 4;
                break;
            case 38:
                newCategory = 5;
                break;
            case 26:
                newCategory = 5;
                break;
        }

        return newCategory;

    }
}


