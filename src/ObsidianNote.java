import java.io.File;
import java.util.ArrayList;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class ObsidianNote {
    // this is the name of the directory where the generated markdown files will be outputted
    static final String nameOutputDirectory = "markdownOutput";
    // the separator separates individual parts of a path(e.g., directory/file, in this case '/' is the separator)
    static final String directoryNamePlusSeparator = "markdownOutput" + File.separator;
    String fileName;
    String fileNameWithExtension;
    int fileLength; // number of lines in the markdown file

    // ArrayList so that we can modify the noteContent
    // each element represents a line in the file
    ArrayList<String> noteContent;

    ObsidianNote(String fileName, String[] noteContent)
    {
        this.fileName = fileName;
        this.fileNameWithExtension = fileName + ".md";
        this.fileLength = noteContent.length;
        this.noteContent = new ArrayList<String>(0);
        for(String string : noteContent) // string will be copies of strings in noteContent, not the same reference
        {
            this.noteContent.add(string);
        }
    }

    void addLineToNote(String line)
    {
        this.noteContent.add(line);
        this.fileLength++;
    }

    void bulkAddLinesToNote(String[] stringArray)
    {
        for(String string : stringArray)
        {
            addLineToNote(string);
        }
    }

    void generateNoteFile()
    {
        try (FileWriter writer = new FileWriter(directoryNamePlusSeparator + fileNameWithExtension); BufferedWriter bufferedWriter = new BufferedWriter(writer))
        {
            for(String string : noteContent)
            {
                bufferedWriter.write(string);
                bufferedWriter.newLine();
            }
        }
        catch (IOException e) {
            System.out.println("Could not open/create file");
        }
    }
}
