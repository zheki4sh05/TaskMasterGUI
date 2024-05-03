package com.example.taskmaster.Controllers.Interfaces;
import javafx.event.Event;
import javafx.event.EventHandler;

public interface EventClickCreator<T extends Event> {
    void addButtonClickListener(EventHandler<T> listener);
}
