package lumi;

import java.util.Scanner;

public class Lumi {
    private static final String TAG_BY = "/by";
    private static final String TAG_FROM = "/from";
    private static final String TAG_TO = "/to";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int count = 0;

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

                } else if (input.startsWith("unmark ")) {
                    int idx = parseIndex(input.substring(7), count);
                    if (idx == -1) throw new IllegalArgumentException("OOPS!!! Invalid task number.");
                    tasks[idx].unmark();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[idx]);

                } else if (input.startsWith("todo")) {
                    String desc = input.length() > 5 ? input.substring(5).trim() : "";
                    if (desc.isEmpty()) throw new IllegalArgumentException("OOPS!!! The description of a todo cannot be empty.");
                    if (count >= tasks.length) throw new IllegalArgumentException("OOPS!!! Task list is full.");
                    Task t = new Todo(desc);
                    tasks[count++] = t;
                    System.out.println("Got it. I've added this task:");
                    System.out.println("  " + t);
                    System.out.println("Now you have " + count + " tasks in the list.");

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
}
