/*
    Singleton pattern, it is the most abused pattern to the point that people call it anti-pattern. 
    But it is absolutely necessary pattern to use when you dont want to create new object every time you call it. 
    In nutshell, this is basically glorified global variable.
 */

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Singleton {
    public static void main(String[] args) {
        FileReader reader1 = FileReader.getFileReader();
        System.out.println(reader1.readFile("README.md"));
        FileReader reader2 = FileReader.getFileReader();
        System.out.println(reader2.readFile("README.md"));
    }    
}

class FileReader {
    private static FileReader instance;
    private final Map<Path, List<String>> fileMap = new HashMap<>();

    public static synchronized FileReader getFileReader() {
        if (instance == null) {
            instance = new FileReader();
        }
        return instance;
    }

    public void loadFile(String filePath) {
        try {
            List<String> fileData = Files.readAllLines(Paths.get(filePath));
            fileMap.put(Paths.get(filePath), fileData);
            System.out.println("\nLoaded file " + filePath + "\n\n");
        } catch (IOException e) {
            System.err.println("Its not really my fault if path was wrong");
        }
    }

    public String readFile(String filePath) {
        if (!fileMap.containsKey(Paths.get(filePath))) {
            loadFile(filePath);
        } else {
            System.out.println("\n\nfile Already present in reader\n\n");
        }
        String fileContent = fileMap.get(Paths.get(filePath)).stream().collect(Collectors.joining("\n"));
        return fileContent;
    }

    public boolean closeFile(String filePath) {
        if (fileMap.containsKey(Paths.get(filePath))) {
            fileMap.remove(Paths.get(filePath));
            return true;
        }
        return false;
    }
}
