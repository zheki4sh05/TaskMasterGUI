package com.example.taskmaster;

public enum Content {
        MAIN_PAGE("GUI.fxml"),
        LOGIN(""),
        OVERVIEW_PAGE("overview_calendar.fxml"),
        CALENDAR_PAGE("calendar_page.fxml"),
        CALENDAR_PICKER("calendar.fxml"),
        CREATE_PLANE_POPUP("createPlanGuiDialog.fxml"),
        LOADING_PAGE("loading_page.fxml"),
        AUTH_PAGE("auth_page.fxml"),
        KANBAN_PAGE("kanban_page.fxml"),
        PROJECT_EDIT_BAR("edit_project_bar.fxml"),
        TASK_EDIT_BAR("task_edit_bar.fxml"),
        SUBTASK("subtask.fxml"),
        HOME_PAGE("home_page.fxml"),
        SEARCH_RESULT("search_page.fxml"),
        SETTINGS_PAGE("settings_page.fxml");

        public final String location;
        Content(String location) {
            this.location = location;
        }
}
