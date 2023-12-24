public class RootNote extends ObsidianNote {

    static final String[] generalPortionOfNoteContent = {
            "Lecture X", // X here is the lecture number
            "---",
            "Additional Notes: [[XLectureXAdditionalNotes]]"
    };

    static final String generalKey = "Slide X"; // X is the slide number
    static final String generalValue = "[[X]]"; // X is the lecture slide note name, should match the name of the lecture slide note so proper linkage can occur

    String courseName;
    int lectureNumber;

    RootNote(String fileName, String[] noteContent, String courseName, int lectureNumber, int totalNumSlides, LectureNotesFramework lectureNotesFrameworkInst) {
        super(fileName, noteContent);
        this.courseName = courseName;
        this.lectureNumber = lectureNumber;

        this.noteContent.set(0, super.noteContent.get(0).replaceFirst("X", Integer.toString(lectureNumber)));

        String additionalNotesLine = super.noteContent.get(2);
        additionalNotesLine = additionalNotesLine.replaceFirst("X", courseName);
        additionalNotesLine = additionalNotesLine.replaceFirst("X", Integer.toString(lectureNumber));
        this.noteContent.set(2, additionalNotesLine);


        // extract the names of the lecture slide note files directly, therefore the root note should be created after the lecture slide notes are created
        for(LectureSlideTemplateNote lectureSlideTemplateNoteInst : lectureNotesFrameworkInst.arrayOfLectureSlideTemplateNotes)
        {
            String generalKeyValuePairEntry = generalKey + ": " + generalValue;
            String specificKeyValuePairEntry = generalKeyValuePairEntry.replaceFirst("X", Integer.toString(lectureSlideTemplateNoteInst.slideNumber));
            specificKeyValuePairEntry = specificKeyValuePairEntry.replaceFirst("X", lectureSlideTemplateNoteInst.fileName);
            this.noteContent.add(specificKeyValuePairEntry);
        }
    }
}
