package com.company;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
public class Main {
//    This function takes a command string and a split character as input and splits the command into individual parts
//    based on the split character. It returns an ArrayList of the split command parts.
public static ArrayList<String> splitCommands(String command, String splitByChar){
  ArrayList<String> splittedCommand = new ArrayList<>();
  for (int i = 0; i < command.split(splitByChar).length; i++) {
    splittedCommand.add( command.split(splitByChar)[i]);
  }
  return splittedCommand;
}
//    This is the main method of the program. It creates an instance of the Parser class and enters a loop
//    that prompts the user for input and processes the entered commands.
//    It splits the input command by the pipe character (|) and then further splits each command by spaces.
//    It calls the parse() method of the Parser class to process each command.
public static void main(String[] args) throws IOException {
  Parser parser = new Parser();
  while(true) {
    System.out.print("PS " + parser.currentWorkingDirectory() + "> ");
    Scanner input = new Scanner(System.in);
    String cmd = input.nextLine();
    ArrayList<String> pipeCommand = new ArrayList<>();
    ArrayList<String> command = new ArrayList<>();
    pipeCommand = splitCommands(cmd, " \\| ");
    for (int i = 0; i < pipeCommand.size(); i++) {
      command = splitCommands(pipeCommand.get(i), " ");
      parser.parse(command);
    }
  }
}
}
