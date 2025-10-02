package lumi;

/**
 * The main Lumi chatbot class
 */
public class Lumi {
    private static final int MAX_TASKS = 100;
    private static final String TAG_BY = "/by";
    private static final String TAG_FROM = "/from";
    private static final String TAG_TO = "/to";

    private final Ui ui;
    private final Storage storage;
    private final TaskList tasks;

    /**
     * Creates the Lumi app, preparing UI, storage and task list
     * and load previously saved tasks
     * @param dir  data folder
     * @param file data file name
     */
    public Lumi(String dir, String file) {
        this.ui = new Ui();
        this.storage = new Storage(dir, file);
        this.tasks = new TaskList();
        loadFromDisk();
    }

    public static void main(String[] args) {
        new Lumi("data", "duke.txt").run();
    }

    /**
     * Starts the main command loop of the chatbot
     * Reads user commands and executes until word bye will occur
     */
    public void run() {
        ui.showWelcome();

        while (true) {
            String input = ui.readCommand();
            if (input == null) {
                break;
            }
            input = input.trim();
            if (input.isEmpty()) {
                continue;
            }

            String cmd = Parser.getCommandWord(input);
            String args = Parser.getArguments(input);

            try {
                switch (cmd) {
                    case "bye":
                        ui.showMessage("Bye. Hope to see you again soon!");
                        return;

                    case "list":
                        tasks.listAll();
                        break;

                    case "find":
                        handleFind(args);
                        break;

                    case "mark":
                        handleMark(args);
                        break;

                    case "unmark":
                        handleUnmark(args);
                        break;

                    case "delete":
                        handleDelete(args);
                        break;

                    case "todo":
                        handleTodo(args);
                        break;

                    case "deadline":
                        handleDeadline(args);
                        break;

                    case "event":
                        handleEvent(args);
                        break;

                    default:
                        throw new IllegalArgumentException(
                                "OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (IllegalArgumentException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private void handleFind(String args) {
        String kw = args.toLowerCase();
        if (kw.isEmpty()) {
            throw new IllegalArgumentException("Use: find <keyword>");
        }

        ui.showMessage("Here are the matching tasks in your list:");
        int shown = 0;
        for (int i = 0; i < tasks.size(); i++) {
            if (tasks.get(i).getDescription().toLowerCase().contains(kw)) {
                ui.showMessage((++shown) + "." + tasks.get(i));
            }
        }
        if (shown == 0) {
            ui.showMessage("No matching tasks found.");
        }
    }

    private void handleMark(String args) {
        int idx = parseIndex(args, tasks.size());
        if (idx == -1) {
            throw new IllegalArgumentException("OOPS!!! Invalid task number.");
        }
        tasks.get(idx).mark();
        ui.showMessage("Nice! I've marked this task as done:");
        ui.showMessage("  " + tasks.get(idx));
        saveToDisk();
    }

    private void handleUnmark(String args) {
        int idx = parseIndex(args, tasks.size());
        if (idx == -1) {
            throw new IllegalArgumentException("OOPS!!! Invalid task number.");
        }
        tasks.get(idx).unmark();
        ui.showMessage("OK, I've marked this task as not done yet:");
        ui.showMessage("  " + tasks.get(idx));
        saveToDisk();
    }

    private void handleDelete(String args) {
        int idx = parseIndex(args, tasks.size());
        if (idx == -1) {
            throw new IllegalArgumentException("OOPS!!! Invalid task number.");
        }
        Task removed = tasks.remove(idx);
        ui.showMessage("Noted. I've removed this task:");
        ui.showMessage("  " + removed);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        saveToDisk();
    }

    private void handleTodo(String args) {
        String desc = args.trim();
        if (desc.isEmpty()) {
            throw new IllegalArgumentException("OOPS!!! The description of a todo cannot be empty.");
        }
        Task t = new Todo(desc);
        tasks.add(t);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        saveToDisk();
    }

    private void handleDeadline(String args) {
        String rest = args.trim();
        int p = rest.indexOf(TAG_BY);
        if (p == -1) {
            throw new IllegalArgumentException("Use: deadline <desc> /by <time>");
        }
        String desc = rest.substring(0, p).trim();
        String by = rest.substring(p + TAG_BY.length()).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new IllegalArgumentException("Use: deadline <desc> /by <time>");
        }
        Task t = new Deadline(desc, by);
        tasks.add(t);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        saveToDisk();
    }

    private void handleEvent(String args) {
        String rest = args.trim();
        int pf = rest.indexOf(TAG_FROM);
        int pt = rest.indexOf(TAG_TO);
        if (pf == -1 || pt == -1 || pt < pf) {
            throw new IllegalArgumentException("Use: event <desc> /from <start> /to <end>");
        }
        String desc = rest.substring(0, pf).trim();
        String from = rest.substring(pf + TAG_FROM.length(), pt).trim();
        String to = rest.substring(pt + TAG_TO.length()).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new IllegalArgumentException("Use: event <desc> /from <start> /to <end>");
        }
        Task t = new Event(desc, from, to);
        tasks.add(t);
        ui.showMessage("Got it. I've added this task:");
        ui.showMessage("  " + t);
        ui.showMessage("Now you have " + tasks.size() + " tasks in the list.");
        saveToDisk();
    }

    private static int parseIndex(String text, int max) {
        try {
            int idx = Integer.parseInt(text.trim()) - 1;
            if (idx >= 0 && idx < max) {
                return idx;
            }
        } catch (NumberFormatException e) {
            // Invalid number; fall through to -1.
        }
        return -1;
    }

    private void saveToDisk() {
        try {
            Task[] arr = tasks.getAll().toArray(new Task[0]);
            storage.save(arr, arr.length);
        } catch (LumiException e) {
            ui.showError(e.getMessage());
        }
    }

    private void loadFromDisk() {
        try {
            Task[] buf = new Task[MAX_TASKS * 2];
            int n = storage.load(buf);
            for (int i = 0; i < n; i++) {
                tasks.add(buf[i]);
            }
        } catch (LumiException e) {
            ui.showError(e.getMessage());
        }
    }
}
