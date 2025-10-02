package lumi;

/**
 * A task that spans a start and end time window
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates an event task
     * @param description description of the event
     * @param from start time as text
     * @param to end time as text
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    protected String type() {
        return "E";
    }

    @Override
    public String toString() {
        return super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
