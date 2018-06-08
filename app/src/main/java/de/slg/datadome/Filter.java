package de.slg.datadome;

import java.util.List;
import java.util.ListIterator;

public class Filter {

    public List<Article> filtern (List<Article> articleList, List<Short> category)
    {
        List<Article> filtertList = null;
        int numbersOfCategories = category.size();
        int i = articleList.size();
        while (i>0)
        {
            Article current = articleList.get(i);
            List<Short> currentCategories = current.getCategoryIds();
            int b = currentCategories.size();

            while (numbersOfCategories>0)
            {
                while(b>0)
                {
                    if (category.get(numbersOfCategories).equals(currentCategories.get(b)))
                    {
                      if (!current.getAbstractText().equals(""))
                      {filtertList.add(current);}
                    }
                }
            }

            i--;
        }

        return filtertList;
    }
}
