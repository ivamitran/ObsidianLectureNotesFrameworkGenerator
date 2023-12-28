import java.util.Arrays;

public class LectureSlideNote extends ObsidianNote {

    // first X: slide number
    // second X: course name
    // third X: lecture number
    static String generalLectureSlidePDFName = "X_XLectureX";
    // first X: lecture number
    // second X: slide number
    static String generalLectureSlideNoteFileName = "lectureXSlideX";

    static final String[] generalNoteContent = {
            "---", // beginning of YAML front matter section
            "slideNumber: X", // X (placeholder for slide number)
            "lectureNumber: X", // X (placeholder for lecture number)
            "---", // end of YAML front matter section
            "", // empty line
            "<font color=\"orange\">Lecture</font>: `=this.lectureNumber`", // Lecture Number
            "<font color=\"orange\">Slide</font>: `=this.slideNumber`", // Slide Number
            "<font color=\"orange\">Lecture Slide PDF Path</font>: `=this.slideNumber`\\_XLecture`=this.lectureNumber`.pdf", // Lecture Slide PDF Path Label, X (placeholder for course name)
            "# Slide", // heading 1
            "---", // horizontal line
            "![lecture_slide](X)", // X (placeholder for lecture slide PDF path, from current location)
            "# Additional Notes", // heading 1
            "---", // horizontal line
            "", // empty line
            "# Links", // heading 1
            "---", // horizontal line
            "Link to previous slide: [[X]]", // X (placeholder for the previous slide, if no previous slide, set as "none"
            "Link to next slide: [[X]]" // X (placeholder for the next slide, if no next slide, set as "none"
    };

    String courseName;
    int lectureNumber;
    int slideNumber;
    String lectureSlidePDFName;
    String lectureSlidePDFNameWithExtension;

    LectureSlideNote(String fileName, String courseName, int lectureNumber, int slideNumber, String lectureSlidePDFName, String[] noteContent)
    {
        super(fileName, noteContent);
        this.courseName = courseName;
        this.lectureNumber = lectureNumber;
        this.slideNumber = slideNumber;
        this.lectureSlidePDFName = lectureSlidePDFName;
        this.lectureSlidePDFNameWithExtension = lectureSlidePDFName + ".pdf";
    }

    static String[] returnSpecificNoteContent(int slideNumber, int lectureNumber, String courseName, String lectureSlidePDFName) {
        String lectureSlidePDFNameWithExtension = lectureSlidePDFName + ".pdf";

        // will replace the first 4 placeholders, the last two placeholders will be replaced by the set class
        String[] specificNoteContent = Arrays.copyOf(generalNoteContent, generalNoteContent.length);

        int count = 0; // used to determine which X currently replacing to replace with the correct value
        for (int i = 0; i < specificNoteContent.length; i++) {
            if (specificNoteContent[i].contains("X")) {
                count++;
                if (count == 1) // this X is for the slide number
                {
                    specificNoteContent[i] = specificNoteContent[i].replaceFirst("X", "\"" + slideNumber + "\"");
                }
                else if (count == 2) // this X is for lecture number
                {
                    specificNoteContent[i] = specificNoteContent[i].replaceFirst("X", "\"" + lectureNumber + "\"");
                }
                else if (count == 3) // this X is for course name
                {
                    specificNoteContent[i] = specificNoteContent[i].replaceFirst("X", courseName);
                }
                else if (count == 4) // this X is for the slide PDF file path
                {
                    specificNoteContent[i] = specificNoteContent[i].replaceFirst("X", lectureSlidePDFNameWithExtension);
                    break;
                }
                // the last two X's will not be replaced here, will be replaced when the set of lecture slide template notes are generated

            }
        }
        return specificNoteContent;
    }
}
