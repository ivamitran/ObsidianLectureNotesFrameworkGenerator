import java.io.File;
import java.util.*;

public class ObsidianLectureNotesFrameworkGenerator {

    // this is the name of the directory where the generated markdown files will be outputted
    static final String nameOutputDirectory = "markdownOutput";
    // the separator separates individual parts of a path(e.g., directory/file, in this case '/' is the separator)
    static final String directoryNamePlusSeparator = "markdownOutput" + File.separator;

    // this hash map is used for returning the proper MainMenuOption enumeration based on user input
    static HashMap<String, MainMenuOption> mainMenuOptionsHashMap = new HashMap<>();

    LectureNotesFramework lectureNotesFramework;

    // enumerations for the different main menu options
    private enum MainMenuOption {
        GENERATE_FILES, CLEAR_OUTPUT_DIRECTORY, EXIT
    }

    static String[] mainMenuOptions = {"1. Generate Framework", "2. Clear Output Directory", "3. Exit"};

    // these values are for comparing with the user's input
    // if user input == 1 -> generate files option
    // if user input == 2 -> clear output directory
    // if user input == 3 -> exit program
    static String[] validMainMenuInputs = {"1", "2", "3"};

    public ObsidianLectureNotesFrameworkGenerator()
    {
        // add key:value pairs to main menu options hash map
        mainMenuOptionsHashMap.put("1", MainMenuOption.GENERATE_FILES);
        mainMenuOptionsHashMap.put("2", MainMenuOption.CLEAR_OUTPUT_DIRECTORY);
        mainMenuOptionsHashMap.put("3", MainMenuOption.EXIT);
    }

    private static MainMenuOption promptAndReturnMainMenuOption()
    {
        Scanner keyboard = new Scanner(System.in);
        String keyboardInput = null;
        MainMenuOption mainMenuOption = null;
        Set<String> validUserInputsSet = mainMenuOptionsHashMap.keySet();

        boolean validMainMenuOptionInputted = false; // assume false initially since no choice given yet
        while(!validMainMenuOptionInputted)
        {
            System.out.println("***Obsidian Lecture Notes Framework Generator***");
            System.out.println("Options: ");
            for(String mainMenuOptionStr : mainMenuOptions)
            {
                System.out.println(mainMenuOptionStr);
            }
            System.out.println();
            System.out.print("Please enter choice: ");
            keyboardInput = keyboard.nextLine();
            Iterator<String> iterator = validUserInputsSet.iterator();
            while(iterator.hasNext())
            {
                String currentValidUserInput = iterator.next();
                if(keyboardInput.compareTo(currentValidUserInput) == 0)
                {
                    validMainMenuOptionInputted = true;
                    break;
                }
            }

            if(!validMainMenuOptionInputted)
            {
                System.out.println("Invalid choice, please try again");
            }
        }

        // determining which valid main menu option was chosen and returning corresponding MainMenuOption enum
        Iterator<String> iterator = validUserInputsSet.iterator();
        while(iterator.hasNext())
        {
            String currentMainMenuOption = iterator.next();
            if(keyboardInput.compareTo(currentMainMenuOption) == 0)
            {
                mainMenuOption = mainMenuOptionsHashMap.get(currentMainMenuOption);
                break;
            }
        }

        return mainMenuOption;
    }

    private static String promptAndReturnCourseName()
    {
        Scanner keyboard = new Scanner(System.in);
        String courseName = null;
        boolean validCourseNameInputted = false; // set false initially, once

        while(!validCourseNameInputted)
        {
            System.out.println("Enter course name(e.g., eecs40): ");
            String courseNameInput = keyboard.nextLine();
            if(courseNameInput.contains(" ")) // course name shouldn't have space characters for proper file name
            {
                System.out.println("***Please try again without any space characters***");
            }
            else
            {
                courseName = courseNameInput;
                validCourseNameInputted = true;
            }
        }

        return courseName;
    }

    private static int promptAndReturnLectureNumber()
    {
        int lectureNumber = promptAndReturnIntValue("Enter lecture number(e.g., 4)");
        return lectureNumber;
    }

    private static int promptAndReturnNumSlides()
    {
        return promptAndReturnIntValue("Enter total number of slides(e.g., 11)");
    }

    private static int promptAndReturnIntValue(String promptMessage)
    {
        Scanner keyboard = new Scanner(System.in);
        int intVal = 0;
        boolean validIntValInputted = false;

        while(!validIntValInputted)
        {
            System.out.println(promptMessage + ": ");
            try
            {
                intVal = keyboard.nextInt();
                validIntValInputted = true;
            }
            catch(InputMismatchException e)
            {
                System.out.println("***Please try again with a valid integer value***");
            }
        }

        return intVal;
    }

    void mainLoop()
    {
        boolean exitSignalGiven = false; // the exit signal is initially false
        while(!exitSignalGiven)
        {
            MainMenuOption mainMenuOption = promptAndReturnMainMenuOption();
            if(mainMenuOption == MainMenuOption.GENERATE_FILES)
            {
                instantiateLectureNotesFrameworkAndGenerateFiles();
                System.out.println("Files successfully outputted in " + nameOutputDirectory);
            }
            else if(mainMenuOption == MainMenuOption.CLEAR_OUTPUT_DIRECTORY)
            {
                deleteOutputDirectoryContents();
                System.out.println(nameOutputDirectory + " directory cleared");
            }
            else if(mainMenuOption == MainMenuOption.EXIT)
            {
                exitSignalGiven = true;
            }
        }

        System.out.println("Program shutting down");
    }

    void instantiateLectureNotesFrameworkAndGenerateFiles()
    {
        // will ask the user for course name, lecture number, and number of slides when this task is initiated
        String courseName = promptAndReturnCourseName();
        int lectureNumber = promptAndReturnLectureNumber();
        int numSlides = promptAndReturnNumSlides();

        // instantiate the lectureNotesFramework object
        this.lectureNotesFramework = new LectureNotesFramework(courseName, lectureNumber, numSlides);

        // generate the markdown files using the note objects, will be outputted to the file output directory
        lectureNotesFramework.generateLectureSlideNoteMarkdownFiles();
        lectureNotesFramework.generateAdditionalNotesNoteMarkdownFile();
        lectureNotesFramework.generateRootNoteMarkdownFile();
    }

    private static void deleteDirectoryContents(File directory) {
        File[] allContents = directory.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteFileRecursively(file);
            }
        }
    }

    private static void deleteFileRecursively(File file) {
        // algorithm to delete a file recursively

        // first checks to see
        if (file.isDirectory()) {
            deleteDirectoryContents(file); // Recursively delete contents
        }
        if (file.delete()) {
            System.out.println(file.getName() + " was deleted successfully.");
        } else {
            System.out.println("Failed to delete " + file.getName());
        }
    }

    private void deleteOutputDirectoryContents()
    {
        deleteDirectoryContents(new File(nameOutputDirectory));
    }

    public static void main(String[] args)
    {
        ObsidianLectureNotesFrameworkGenerator obsidianLectureNotesFrameworkGenerator = new ObsidianLectureNotesFrameworkGenerator();
        obsidianLectureNotesFrameworkGenerator.mainLoop();
    }
}