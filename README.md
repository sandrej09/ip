# Lumi — User Guide

Lumi is the simple task manager chatbot.  
You can add todos, deadlines, and events, mark or unmark them as done, delete, list, and search tasks and more.  
All tasks are saved automatically to your computer and loaded the next time you run Lumi.

## Quick Start
1. You have to have Java17.
2. Download the `Lumi.jar` file from the Releases page of this repo.
3. Place the JAR in an empty folder.
4. Open a terminal in that folder and run:


## Features

### Add Todo

Command: todo <description>
Example: 
todo read book
Output:
[T][ ] read book

### Add Deadline

Command: deadline <description> /by yyyy-MM-dd
Example:
deadline return book /by 2025-10-03
Output:
[D][ ] return book (by: Oct 3 2025)

### Add Event

Command: event <description> /from <start> /to <end>
Example:
event project meeting /from Mon 2pm /to 4pm
Output:
[E][ ] project meeting (from: Mon 2pm to: 4pm)

### List

Command: list
Output: shows all tasks with numbers.

### Find

Command: find <keyword>
Example:
find book
Output: shows matching tasks.

### Mark / Unmark

Commands:
mark <index>
unmark <index>
Example:
mark 1
Output:
[T][X] read book

### Delete

Command: delete <index>
Example:
delete 2
Output: task removed.

### Exit

Command: bye
Output: program ends.

## Data File
Tasks are saved automatically in file: ./data/lumi.txt

Example file content:
T | 1 | read book
D | 0 | return book | 2025-10-03
E | 0 | project meeting | Mon 2pm | 4pm
---
---

- `T` = Todo task
- `D` = Deadline task
- `E` = Event task
- `1` = done
- `0` = not done

