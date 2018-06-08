package de.slg.datadome;

import java.util.List;
import java.util.ListIterator;

public class Filter {

    public List<Article> filtern (List<Article> listedatenbank, List<Short> kategorie)
    {
        List<Article> gefilterteListe = null;
        int a = kategorie.size();
        int i = listedatenbank.size();
        while (i>0)
        {
            Article ding1 = listedatenbank.get(i);
            List<Short> kategorienDinge = ding1.getCategoryIds();
            int b = kategorienDinge.size();

            while (a>0)
            {
                while(b>0)
                {
                    if (kategorie.get(a).equals(kategorienDinge.get(b)))
                    {
                      if (!ding1.getAbstractText().equals(""))
                      {gefilterteListe.add(ding1);}
                    }
                }
            }

            i--;
        }

        return gefilterteListe;
    }
}
