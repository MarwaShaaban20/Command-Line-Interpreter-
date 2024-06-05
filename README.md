### Command Line Interpreter (CLI) Simulator

This assignment involves developing a Command Line Interpreter (CLI) simulator for an operating system. The CLI prompts users for input through the keyboard, parses the input string, and executes the indicated commands. After execution, the user is prompted for another command.

#### Key Features

1. **Implementation in Java**:
   - The CLI must be implemented using Java.

2. **Class Structure**:
   - **Parser Class**:
     - Responsible for parsing user input to extract commands and arguments.
     - Reports errors for invalid commands or incorrect arguments.
   - **Terminal Class**:
     - Contains functions for executing commands.
     - Examples include:
       - `public void date();` // Prints the current date and time.
       - `public void pwd();`  // Prints the current working directory.

3. **Command Handling**:
   - Commands and parameters are entered via the keyboard, parsed, verified, and executed by the program.
   - The program prints error messages for incorrect commands or parameters.
   - Handles various parameters for each command.

4. **Supported Commands**:
   - **Essential Commands**: clear, cd, ls, cp, mv, rm, mkdir, rmdir, cat, more, pwd, date.
   - **Additional Commands**:
     - `help` - Lists all commands and their syntax.
     - `exit` - Terminates the program.

5. **Redirection**:
   - Supports redirection (i.e., `>` and `>>`) to output command results to a file.

6. **Pipe Operator**:
   - Supports the "&" pipe operator to combine multiple commands. For example, `cd C:/ & pwd` changes the directory to C:/ and then displays the current directory.


#### Example Usage
- Entering `mkdir` without parameters should produce an error message.
- Entering `cd C:/ & pwd` should change to directory C:/ and display it.
