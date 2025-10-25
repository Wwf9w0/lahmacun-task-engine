package org.engine.virtual;

import java.util.ArrayList;
import java.util.List;

public class VirtualMain {

    public static void main(String[] args) {
        VirtualThreadOven oven = new VirtualThreadOven();
        VirtualChef chef = new VirtualChef(oven);
        VirtualFlow virtualFlow = new VirtualFlow(chef);
        UserService userService = new UserService();
        EventService eventService = new EventService();

        VirtualTask<UserProfile> profileTask = virtualFlow.addTask(userService::getProfile);
        VirtualTask<List<Event>> eventTask = virtualFlow.addTask(eventService::eventList);
        VirtualTask<String> stringTask = virtualFlow.addTask(() -> "Hello");

        virtualFlow.waitAll();
        UserProfile profile = profileTask.join();
        List<Event> events = eventTask.join();
        String string = stringTask.join();



        System.out.println("Profile: " + profile);
        System.out.println("Events: " + events);
        System.out.println("String: " + string);

    }


}

class EventService {
    public List<Event> eventList() {
        List<Event> events = new ArrayList<>();
        events.add(new Event("event1", 1));
        events.add(new Event("event2", 2));
        return events;
    }
}


class UserService {
    public UserProfile getProfile() {
        return new UserProfile("emre", "emre@gmail.com");
    }
}


record UserProfile(String userName, String email) {
}

record Event(String name, int time) {
}