package lumi;

/**
 * A simple task without date/time
 */
public class Todo extends Task {
    /**
     * Creates a todo task.
     * @param description description of the task
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    protected String type() {
        return "T";
    }
}
