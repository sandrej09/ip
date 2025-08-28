import java.util.Scanner;

public class Lumi {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Task[] tasks = new Task[100];
        int count = 0;

        System.out.println("Hello! I'm Lumi");
        System.out.println("What can I do for you?");

        while (true) {
            String input = sc.nextLine().trim();

            if (input.equals("bye")) {
                System.out.println("Bye. Hope to see you again soon!");
                break;

            } else if (input.equals("list")) {
                System.out.println("Here are the tasks in your list:");
                for (int i = 0; i < count; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }

            } else if (input.startsWith("mark ")) {
                int idx = Integer.parseInt(input.substring(5)) - 1;
                tasks[idx].mark();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[idx]);

            } else if (input.startsWith("unmark ")) {
                int idx = Integer.parseInt(input.substring(7)) - 1;
                tasks[idx].unmark();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[idx]);

            } else if (input.startsWith("todo ")) {
                Task t = new Todo(input.substring(5));
                tasks[count++] = t;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else if (input.startsWith("deadline ")) {
                String[] parts = input.substring(9).split("/by", 2);
                Task t = new Deadline(parts[0].trim(), parts[1].trim());
                tasks[count++] = t;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else if (input.startsWith("event ")) {
                String[] parts1 = input.substring(6).split("/from", 2);
                String desc = parts1[0].trim();
                String[] parts2 = parts1[1].split("/to", 2);
                String from = parts2[0].trim();
                String to = parts2[1].trim();
                Task t = new Event(desc, from, to);
                tasks[count++] = t;
                System.out.println("Got it. I've added this task:");
                System.out.println("  " + t);
                System.out.println("Now you have " + count + " tasks in the list.");

            } else {
                Task t = new Todo(input);
                tasks[count++] = t;
                System.out.println("added: " + input);
            }
        }
    }
}
