package io.agistep.event.repository;

class CreateTodoCommand implements TodoCommand {
    private final String title;

    public CreateTodoCommand(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
