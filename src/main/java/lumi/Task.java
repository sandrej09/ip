package lumi;

public abstract class Task {
    protected final String description;
    protected boolean done;

    public Task(String description) {
        this.description = description;
        this.done = false;
    }

    public void mark() { done = true; }

    public void unmark() { done = false; }

    protected String status() { return done ? "X" : " "; }

    public boolean isDone() { return done; }

    public String getDescription() { return description; }

    protected abstract String type();

    @Override
    public String toString() {
        return "[" + type() + "][" + status() + "] " + description;
    }
}
