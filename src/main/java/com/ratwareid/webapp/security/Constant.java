package com.ratwareid.webapp.security;

import java.util.ArrayList;
import java.util.List;

/***********************************************************************
 * Module:  com.ratwareid.webapp.security.Constant
 * Author:  Ratwareid
 * Created: 26/11/2022
 * Info:  If You dont know me ? Just type ratwareid in google.
 ***********************************************************************/
public class Constant {

    public static List<String> getListLevels() {
        ArrayList<String> listLevels = new ArrayList<>();
        listLevels.add("ADMIN");
        listLevels.add("CREATOR");
        listLevels.add("EDITOR");
        listLevels.add("USER");

        return listLevels;
    }

    public static List<Long> getListDaya() {
        ArrayList<Long> daya = new ArrayList<>();
        daya.add(450L);
        daya.add(900L);
        daya.add(1300L);
        daya.add(2200L);
        daya.add(3500L);
        daya.add(4400L);
        daya.add(5500L);
        daya.add(7700L);
        daya.add(11000L);

        return daya;
    }

    public static class SORTMETHOD{
        public static final String ASCENDING = "ASC";
        public static final String DESCENDING = "DESC";
    }

}
