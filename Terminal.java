package com.company;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
public class Terminal {
  String currentWorkingDirectory;
//    This is the constructor of the class. It initializes the currentWorkingDirectory
//    variable with the current working directory obtained from the system.
public Terminal() {
  this.currentWorkingDirectory = System.getProperty("user.dir");
}
//This method clears the terminal screen by printing multiple newline characters.
public void clear(){
  for (int i = 0; i < 30; ++i) System.out.println();
}
//This method changes the current working directory.
// It accepts a directory name as input and handles various cases to determine the new working directory.
public void cd(String directoryName) {
  try {
    directoryName = removeQuotes(directoryName);
    File targetDirectory = new File(directoryName);
    File currentDirectoryFile = new File(currentWorkingDirectory);
    if (directoryName.equals("..")) {
      String[] pathElements = currentWorkingDirectory.split("\\\\");
      int lengthOfPath = pathElements.length;
      if (lengthOfPath > 1) {
        currentWorkingDirectory = String.join("\\", Arrays.copyOf(pathElements, lengthOfPath - 1));
      }
    } else if (targetDirectory.isAbsolute() || (targetDirectory.isDirectory() && directoryName.endsWith(":"))) {
      currentWorkingDirectory = directoryName;
    } else {
      File newDirectory = new File(currentWorkingDirectory, directoryName);
      if (newDirectory.exists() && newDirectory.isDirectory()) {
        currentWorkingDirectory = newDirectory.getPath();
      } else {
        System.out.println("Cannot find path");
      }
    }
  } catch (InvalidPathException | NullPointerException ex) {
    System.out.println("Invalid path");
  }
}
//throws IOException: This method lists the contents of the current working directory.
// It uses the File class to get a list of file names in the directory and returns the list as a string.
public String ls() throws IOException {
  String lsContents = "";
  File file = null;
  String[] filePaths;
  file = new File(currentWorkingDirectory);
  filePaths = file.list();
  for(String path:filePaths) {
    System.out.println(path);
    lsContents += path + "\n";
  }
  return lsContents;
}
//throws IOException: This method copies a file from a source path to a destination path.
// It uses the Files.copy() method to perform the file copy operation.
public void cp(String sourcePath, String destinationPath) throws IOException {
  Path source = Paths.get(sourcePath);
  Path destination = Paths.get(destinationPath);
  if (Files.exists(destination)) {
    Files.delete(destination);
  }
  CopyOption[] options = new CopyOption[] { StandardCopyOption.REPLACE_EXISTING };
  Files.copy(source, destination, options);
}
//throws IOException: This method moves a file from a source path to a destination path.
// It uses the Files.move() method to perform the file move operation.
public void mv(String sourcePath, String destinationPath) throws IOException {
  sourcePath = removeQuotes(sourcePath);
  destinationPath = removeQuotes(destinationPath);
  sourcePath = getPath(sourcePath);
  destinationPath = getPath(destinationPath);
  File sourceFile = new File(sourcePath);
  File destinationFile = new File(destinationPath);
  if (destinationFile.isDirectory()) {
    Path source = sourceFile.toPath();
    Path destination = Paths.get(destinationPath, sourceFile.getName());
    Files.move(source, destination);
  } else {
    boolean success = sourceFile.renameTo(destinationFile);
    if (!success) {
      System.out.println("Move operation failed.");
    }
  }
}
//This method deletes a file. It uses the File.delete() method to delete the file.
public void rm(String sourcePath){
    sourcePath = removeQuotes(sourcePath);
    sourcePath = getPath(sourcePath);
    File file = new File(sourcePath);
    if (file.isDirectory()){
        System.out.println("rm: cannot remove '" + sourcePath + "': Is a directory");
    }
    else {
        file.delete();
    }
}
//This method creates a new directory. It uses the Files.createDirectories() method to create the directory.
public void mkdir(String file){
  file = removeQuotes(file);
  file = getPath(file);
  try {
    Path path = Paths.get(file);
    Files.createDirectories(path);
  } catch(Exception e) {
    e.printStackTrace();
  }
}
//This method recursively deletes a directory and its contents. It uses recursion to delete all files
// and subdirectories within the given directory before deleting the directory itself.
public void rmdir(File file){
  for (File subFile : file.listFiles()) {
    if(subFile.isDirectory()) {
      rmdir(subFile);
    } else {
      subFile.delete();
    }
  }
  file.delete();
}
//throws IOException: This method reads the contents of a file and prints them to the console.
// It supports different options like appending (>>) or overwriting (>) the file with new content.
// It uses Scanner and FileWriter to read from and write to the file.
public File cat(ArrayList<String> input) throws IOException {
  File file = null;
  String option = input.get(1);
  if (option.equals(">>") || option.equals(">")) {
    String path = removeQuotes(input.get(2));
    String fullPath = getPath(path);
    file = new File(fullPath);
    try (FileWriter myWriter = new FileWriter(file, option.equals(">>"))) {
      Scanner scan = new Scanner(System.in);
      String inputs;
      myWriter.write("\n");
      while (scan.hasNext()) {
        inputs = scan.nextLine();
        myWriter.write(inputs + "\n");
      }
    }
  } else {
    try {
      String path = input.get(1);
      if (path.startsWith("\"") && path.endsWith("\"")) {
        path = path.substring(1, path.length() - 1);
      }
      path = getPath(path);
      file = new File(path);
      try (Scanner read = new Scanner(file)) {
        while (read.hasNextLine()) {
          String data = read.nextLine();
          System.out.println(data);
        }
      }
    } catch (FileNotFoundException e) {
      System.out.println("File not found.");
      e.printStackTrace();
    }
  }
  return file;
}
//This method prints the current working directory to the console and returns it as a string.
public String pwd() {
    System.out.println(currentWorkingDirectory);
    return currentWorkingDirectory;
}
//throws IOException: This method displays the contents of a file one page at a time and waits for user input to scroll
// through the file.It uses BufferedReader and Scanner to read from the file and obtain user input.
public void more(String path) throws IOException {
    path = removeQuotes(path);
    path = getPath(path);
    BufferedReader reader;
    reader = new BufferedReader(new FileReader(path));
    String line = reader.readLine();
    Scanner scan = new Scanner(System.in);
    for (int i = 0; i < 50; i++) {
        System.out.println(line);
        line = reader.readLine();
    }
    String input = scan.nextLine();
    while (line != null && input.isEmpty()) {
        System.out.println(line);
        line = reader.readLine();
        input = scan.nextLine();
    }
}
//This method gets the current date and time and prints it to the console.
// It uses the java.util.Calendar class to obtain the current date.
public String date() {
    Date date=java.util.Calendar.getInstance().getTime();
    System.out.println(date);
    return date.toString();
}
public File help() {
    System.out.println("cd: Changes the current directory");
    System.out.println("cp: Copies one or more files to a directory,By default, it does not copy directories.");
    System.out.println("clear: Clears the current terminal screen.");
    System.out.println("date: Displays or sets the date and time of the system.");
    System.out.println("pwd: Prints the working directory.");
    System.out.println("mkdir: Creates a directory with each given name.");
    System.out.println("rmdir: Removes each given directory only if it is empty. ");
    System.out.println("mv: Moves one or more files/directories to a directory. ");
    System.out.println("rm: Removes each given file. ");
    System.out.println("cat: Concatenates the content of the files and prints it.");
    System.out.println("more: Allows us to display and scroll direction only. ");
    System.out.println("ls: Lists the contents (files & directory sorted alphabetically. ");
    return null;
}
//This method takes a filename as input and returns the absolute path by appending it to the current working directory.
public String getPath(String filename){
    File file = new File(filename);
    if(file.isAbsolute() || (file.isDirectory() && filename.endsWith(":"))){
        return filename;
    }
    else {
        return currentWorkingDirectory + "\\" + filename;
    }
}
//This method removes the leading and trailing quotes from a string and also
// removes any trailing backslashes or forward slashes.
public String removeQuotes(String path){
    if(path.startsWith("\"") && path.endsWith("\"")){
        path = path.substring(1,path.length()-1);
    }
    while(path.endsWith("\\") || path.endsWith("/")){
        path = path.substring(0, path.length() - 1);
    }
    return path;
}
//throws IOException: This method handles output redirection. It determines
// the appropriate command to execute based on the given command string and writes the output to a text file.
public void outputOperator(boolean isSingle,String command, String args, String textFile) throws IOException {
    textFile = removeQuotes(textFile);
    textFile = getPath(textFile);
    File file = null;
    String output = null;
    ArrayList<String> catCommand = new ArrayList<>();
    if(command.equals("help")){
        file = help();
    }
    else if(command.equals("ls")){
        output = ls();
    }
    else if(command.equals("date")){
        output = date();
    }
    else if(command.equals("pwd")){
        output = pwd();
    }
    else if(command.equals("cat")){
        args = removeQuotes(args);
        args = getPath(args);
        catCommand.add(command);
        catCommand.add(args);
        file = cat(catCommand);
    }
    if(file!=null){
        readAndWriteFile(file, textFile, isSingle);
    }
    else if(output!=null){
        writeStringToFile(output, textFile, isSingle);
    }
}
//throws IOException: This method reads the contents of a file and writes them to another file.
// It uses Scanner and FileWriter to perform the reading and writing operations.
public void readAndWriteFile(File readFile, String writeToPath, boolean isSingle) throws IOException {
  Scanner read = new Scanner(readFile);
  FileWriter myWriter = null;
  if(isSingle){
    myWriter = new FileWriter(writeToPath);
  }else{
    myWriter = new FileWriter(writeToPath, true);
  }
  while (read.hasNextLine()) {
    String data = read.nextLine();
    myWriter.write(data + "\n");
  }
  read.close();
  myWriter.close();
}
//throws IOException: This method writes a string to a file. It uses FileWriter to perform the writing operation.
public void writeStringToFile(String writeText, String writeToPath, boolean isSingle) throws IOException {
  FileWriter myWriter;
  if(isSingle){
    myWriter = new FileWriter(writeToPath);
  }else{
    myWriter = new FileWriter(writeToPath, true);
  }
  myWriter.write(writeText);
  myWriter.close();
}
//This method returns the current working directory as a string.
public String getCurrentWorkingDirectory() {
  return currentWorkingDirectory;
}
}