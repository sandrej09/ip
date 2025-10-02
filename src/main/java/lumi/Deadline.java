package lumi;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that has a deadline date
 */
public class Deadline extends Task {
    private final String by;
    private final LocalDate byDate;

    /**
     * Creates a deadline task
     * @param description task description
     * @param by due date in the format:  yyyy-MM-dd
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
        LocalDate d = null;
        try { d = LocalDate.parse(by); } catch (Exception ignored) { }
        this.byDate = d;
    }

    /**
     * Method to return raw date string
     * @return date string yyyy-MM-dd
     */
    public String getBy() {
        return by;
    }

    @Override
    protected String type() {
        return "D";
    }

    @Override
    public String toString() {
        String nice = (byDate != null)
                ? byDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"))
                : by;
        return super.toString() + " (by: " + nice + ")";
    }
}
