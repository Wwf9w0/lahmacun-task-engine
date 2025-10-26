package org.engine.virtual.model;

import java.util.ArrayList;
import java.util.List;

public class EventService {

    public List<Event> eventList() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("event1", 1));
        events.add(new Event("event2", 2));
        return events;
    }
}
