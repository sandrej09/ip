package lumi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;

/**
 * Handles saving and loading tasks to a file.
 */
public class Storage {
    private final Path dir;
    private final Path file;

    /**
     * Creates a Storage for a folder and file
     * @param dirName folder name
     * @param fileName file name
     */
    public Storage(String dirName, String fileName) {
        this.dir = Paths.get(dirName);
        this.file = this.dir.resolve(fileName);
    }

    /**
     * Saves tasks to the file
     * @param tasks tasks to save
     * @param count number of tasks
     * @throws LumiException if saving fails
     */
    public void save(Task[] tasks, int count) throws LumiException {
        try {
            Files.createDirectories(dir);
            try (BufferedWriter w = Files.newBufferedWriter(file)) {
                for (int i = 0; i < count; i++) {
                    Task t = tasks[i];
                    if (t == null) continue;
                    if (t instanceof Todo) {
                        w.write("T|" + (t.isDone() ? "1" : "0") + "|" + t.getDescription());
                    } else if (t instanceof Deadline) {
                        Deadline d = (Deadline) t;
                        w.write("D|" + (t.isDone() ? "1" : "0") + "|" + d.getDescription() + "|" + d.getBy());
                    } else if (t instanceof Event) {
                        Event ev = (Event) t;
                        w.write("E|" + (t.isDone() ? "1" : "0") + "|" + ev.getDescription()
                                + "|" + ev.getFrom() + "|" + ev.getTo());
                    }
                    w.newLine();
                }
            }
        } catch (IOException e) {
            throw new LumiException("Failed to save tasks: " + e.getMessage());
        }
    }

    /**
     * Loads tasks from the file
     * @param dest array to put tasks into
     * @return number of tasks loaded
     * @throws LumiException if loading fails
     */
    public int load(Task[] dest) throws LumiException {
        if (!Files.exists(file)) return 0;
        int n = 0;
        try (BufferedReader r = Files.newBufferedReader(file)) {
            String line;
            while ((line = r.readLine()) != null && n < dest.length) {
                String[] p = line.split("\\|", -1);
                if (p.length < 3) continue;

                String type = p[0].trim();
                boolean done = "1".equals(p[1].trim());
                String desc = p[2].trim();

                Task t = null;
                if ("T".equals(type)) {
                    t = new Todo(desc);
                } else if ("D".equals(type) && p.length >= 4) {
                    t = new Deadline(desc, p[3].trim());
                } else if ("E".equals(type) && p.length >= 5) {
                    t = new Event(desc, p[3].trim(), p[4].trim());
                }

                if (t != null) {
                    if (done) t.mark();
                    dest[n++] = t;
                }
            }
        } catch (IOException e) {
            throw new LumiException("Failed to load tasks: " + e.getMessage());
        }
        return n;
    }
}
