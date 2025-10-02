package lumi;

/**
 * Represents a user task with a description and completion status
 */
public abstract class Task {
    protected final String description;
    protected boolean done;

    /**
     * Creates a new task with a given description
     * @param description short description of the task
     */
    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    /**
     * Marks this task as done
     */
    public void mark() {
        done = true;
    }

    /**
     * Marks this task as not done
     */
    public void unmark() {
        done = false;
    }

    /**
     * Returns a single-character status icon
     * @return "X" if done, otherwise a blank space
     */
    protected String status() {
        if (done) {
            return "X";
        } else {
            return " ";
        }
    }

    /**
     * Returns true if this task is done
     * @return completion flag
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Returns the description of the task
     * @return description text
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the type of the task (T/D/E)
     * @return task type code
     */
    protected abstract String type();

    @Override
    public String toString() {
        return "[" + type() + "][" + status() + "] " + description;
    }
}
