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
                if (idx == -1) {
                    System.out.println("OOPS!!! Invalid task number.");
                    continue;
                }
                tasks[idx].mark();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[idx]);

            } else if (input.startsWith("unmark ")) {
                int idx = parseIndex(input.substring(7), count);
                if (idx == -1) {
                    System.out.println("OOPS!!! Invalid task number.");
                    continue;
                }
                tasks[idx].unmark();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[idx]);

            } else if (input.startsWith("todo")) {
                String desc = input.length() > 5 ? input.substring(5).trim() : "";
                if (desc.isEmpty()) {
                    System.out.println("OOPS!!! The description of a todo cannot be empty.");
                    continue;
                }
                if (count >= tasks.length) {
                    System.out.println("OOPS!!! Task list is full.");
                    continue;
                }
                Task t = new Todo(desc);
                tasks[count++] = t;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else if (input.startsWith("deadline ")) {
                String rest = input.substring(9).trim();
                int p = rest.indexOf("/by");
                if (p == -1) {
                    System.out.println("Use: deadline <desc> /by <time>");
                    continue;
                }
                String desc = rest.substring(0, p).trim();
                String by = rest.substring(p + 3).trim();
                if (desc.isEmpty() || by.isEmpty()) {
                    System.out.println("Use: deadline <desc> /by <time>");
                    continue;
                }
                if (count >= tasks.length) {
                    System.out.println("OOPS!!! Task list is full.");
                    continue;
                }
                Task t = new Deadline(desc, by);
                tasks[count++] = t;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else if (input.startsWith("event ")) {
                String rest = input.substring(6).trim();
                int pf = rest.indexOf("/from");
                int pt = rest.indexOf("/to");
                if (pf == -1 || pt == -1 || pt < pf) {
                    System.out.println("Use: event <desc> /from <start> /to <end>");
                    continue;
                }
                String desc = rest.substring(0, pf).trim();
                String from = rest.substring(pf + 5, pt).trim();
                String to = rest.substring(pt + 3).trim();
                if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                    System.out.println("Use: event <desc> /from <start> /to <end>");
                    continue;
                }
                if (count >= tasks.length) {
                    System.out.println("OOPS!!! Task list is full.");
                    continue;
                }
                Task t = new Event(desc, from, to);
                tasks[count++] = t;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else {
                System.out.println("OOPS!!! I'm sorry, but I don't know what that means :-(");
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
