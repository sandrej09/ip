package lumi;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;

public class Lumi {
    private static final String TAG_BY = "/by";
    private static final String TAG_FROM = "/from";
    private static final String TAG_TO = "/to";

    private static final Path DATA_DIR = Paths.get("data");
    private static final Path DATA_FILE = DATA_DIR.resolve("duke.txt");

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int count = load(tasks);

        System.out.println("Hello! I'm Lumi");
        System.out.println("What can I do for you?");

        while (true) {
            if (!sc.hasNextLine()) break;
            String input = sc.nextLine().trim();
            if (input.isEmpty()) continue;

            try {
                if (input.equals("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;

                } else if (input.equals("list")) {
                    System.out.println("Here are the tasks in your list:");
                    for (int i = 0; i < count; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }

                } else if (input.startsWith("mark ")) {
                    int idx = parseIndex(input.substring(5), count);
                    if (idx == -1) throw new IllegalArgumentException("OOPS!!! Invalid task number.");
                    tasks[idx].mark();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[idx]);
                    save(tasks, count);

                } else if (input.startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7), count);
                    if (idx == -1) throw new IllegalArgumentException("OOPS!!! Invalid task number.");
                    tasks[idx].unmark();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[idx]);
                    save(tasks, count);

                } else if (input.startsWith("delete ")) {
                    int idx = parseIndex(input.substring(7), count);
                    if (idx == -1) throw new IllegalArgumentException("OOPS!!! Invalid task number.");
                    Task removed = tasks[idx];
                    for (int i = idx; i < count - 1; i++) tasks[i] = tasks[i + 1];
                    tasks[--count] = null;
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removed);
                    System.out.println("Now you have " + count + " tasks in the list.");
                    save(tasks, count);

                } else if (input.startsWith("todo")) {
                    String desc = input.length() > 5 ? input.substring(5).trim() : "";
                    if (desc.isEmpty()) throw new IllegalArgumentException("OOPS!!! The description of a todo cannot be empty.");
                    if (count >= tasks.length) throw new IllegalArgumentException("OOPS!!! Task list is full.");
                    Task t = new Todo(desc);
                    tasks[count++] = t;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t);
                    System.out.println("Now you have " + count + " tasks in the list.");
                    save(tasks, count);

                } else if (input.startsWith("deadline ")) {
                    String rest = input.substring(9).trim();
                    int p = rest.indexOf(TAG_BY);
                    if (p == -1) throw new IllegalArgumentException("Use: deadline <desc> /by <time>");
                    String desc = rest.substring(0, p).trim();
                    String by = rest.substring(p + TAG_BY.length()).trim();
                    if (desc.isEmpty() || by.isEmpty()) throw new IllegalArgumentException("Use: deadline <desc> /by <time>");
                    if (count >= tasks.length) throw new IllegalArgumentException("OOPS!!! Task list is full.");
                    Task t = new Deadline(desc, by);
                    tasks[count++] = t;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t);
                    System.out.println("Now you have " + count + " tasks in the list.");
                    save(tasks, count);

                } else if (input.startsWith("event ")) {
                    String rest = input.substring(6).trim();
                    int pf = rest.indexOf(TAG_FROM);
                    int pt = rest.indexOf(TAG_TO);
                    if (pf == -1 || pt == -1 || pt < pf) throw new IllegalArgumentException("Use: event <desc> /from <start> /to <end>");
                    String desc = rest.substring(0, pf).trim();
                    String from = rest.substring(pf + TAG_FROM.length(), pt).trim();
                    String to = rest.substring(pt + TAG_TO.length()).trim();
                    if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) throw new IllegalArgumentException("Use: event <desc> /from <start> /to <end>");
                    if (count >= tasks.length) throw new IllegalArgumentException("OOPS!!! Task list is full.");
                    Task t = new Event(desc, from, to);
                    tasks[count++] = t;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t);
                    System.out.println("Now you have " + count + " tasks in the list.");
                    save(tasks, count);

                } else {
                    throw new IllegalArgumentException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static int parseIndex(String text, int max) {
        try {
            int idx = Integer.parseInt(text.trim()) - 1;
            if (idx >= 0 && idx < max) return idx;
        } catch (NumberFormatException ignored) { }
        return -1;
    }

    private static void save(Task[] tasks, int count) {
        try {
            Files.createDirectories(DATA_DIR);
            try (BufferedWriter w = Files.newBufferedWriter(DATA_FILE)) {
                for (int i = 0; i < count; i++) {
                    Task t = tasks[i];
                    if (t instanceof Todo) {
                        w.write("T|" + (t.isDone() ? "1" : "0") + "|" + t.getDescription());
                    } else if (t instanceof Deadline) {
                        Deadline d = (Deadline) t;
                        w.write("D|" + (t.isDone() ? "1" : "0") + "|" + t.getDescription() + "|" + d.getBy());
                    } else if (t instanceof Event) {
                        Event ev = (Event) t;
                        w.write("E|" + (t.isDone() ? "1" : "0") + "|" + t.getDescription() + "|" + ev.getFrom() + "|" + ev.getTo());
                    }
                    w.newLine();
                }
            }
        } catch (IOException ignored) { }
    }

    private static int load(Task[] dest) {
        if (!Files.exists(DATA_FILE)) return 0;
        int n = 0;
        try (BufferedReader r = Files.newBufferedReader(DATA_FILE)) {
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
        } catch (IOException ignored) { }
        return n;
    }
}
