package com.example.familymapclient;

import java.util.Comparator;

import Model.Event;

public class SortEvents implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        return o1.getYear() - o2.getYear();
    }
}
