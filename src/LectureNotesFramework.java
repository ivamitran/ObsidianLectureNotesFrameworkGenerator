public class LectureNotesFramework {

    // variables of which values will be supplied by the user
    String courseName;
    int lectureNumber;
    int totalNumSlides;

    // variables of which values will be synthesized from what the user supplies
    LectureSlideTemplateNote[] arrayOfLectureSlideTemplateNotes;
    RootNote rootNote;
    ObsidianNote additionalNotesNote; // even though this note will be em

    LectureNotesFramework(String courseName, int lectureNumber, int totalNumSlides)
    {
        this.courseName = courseName;
        this.lectureNumber = lectureNumber;
        this.totalNumSlides = totalNumSlides;
        arrayOfLectureSlideTemplateNotes = new LectureSlideTemplateNote[totalNumSlides];

        // at this point we are able to instantiate all the note objects since we have all the information we need

        // instantiate the lecture slide note objects
        this.instantiateLectureSlideObjects();

        // instantiate the root note object
        this.instantiateRootNote();

        // instantiate the additional notes note object(link to this can be found in the root note object)
        this.instantiateAdditionalNotesNote();
    }

    /**
     * Instantiates lecture slide objects in memory (contains all the information necessary to generate markdown files)
     * Does not generate the files though, generating the files is done using another method
     */
    void instantiateLectureSlideObjects()
    {
        // What this method does is create specific iterations of general note content (since all the lecture note
        // Files follow the same format
        // Example: General Note Content: Lecture X, Slide X -> Specific Note Content: Lecture 1, Slide 10

        // Each iteration will instantiate a unique note file
        for(int i = 0; i < totalNumSlides; i++)
        {
            // Constructing the file name of the current note
            String currentLectureSlideNoteFileName = LectureSlideTemplateNote.generalLectureSlideNoteFileName.replaceFirst("X", Integer.toString(lectureNumber)); // replace the first X with the lecture number
            currentLectureSlideNoteFileName = currentLectureSlideNoteFileName.replaceFirst("X", Integer.toString(i + 1)); // replace the second X with the slide number

            // Constructing the file name of the current note's corresponding slide PDF
            // This will be used in order to properly generate the specific content below
            String currentLectureSlidePDFName = getCurrentLectureSlidePDFName(i);

            // Constructing specific content of the current note
            String[] specificNoteContent = LectureSlideTemplateNote.returnSpecificNoteContent(i + 1, lectureNumber, courseName, currentLectureSlidePDFName);

            // Finally instantiating the note itself
            arrayOfLectureSlideTemplateNotes[i] = new LectureSlideTemplateNote(currentLectureSlideNoteFileName, courseName, lectureNumber, i + 1, currentLectureSlidePDFName, specificNoteContent);
        }

        this.addLinksToSpecificFileContentAfterInitialized();
    }

    private String getCurrentLectureSlidePDFName(int i) {
        String currentLectureSlidePDFName = LectureSlideTemplateNote.generalLectureSlidePDFName.replaceFirst("X", Integer.toString(i + 1)); // replace the first X with slide number
        currentLectureSlidePDFName = currentLectureSlidePDFName.replaceFirst("X", courseName); // replaceFirst the second X with the course name
        currentLectureSlidePDFName = currentLectureSlidePDFName.replaceFirst("X", Integer.toString(lectureNumber)); // replaceFirst the third X with lecture number
        return currentLectureSlidePDFName;
    }

    void instantiateRootNote()
    {
        String rootNoteName = courseName + "Lecture" + lectureNumber + "RootNote";
        this.rootNote = new RootNote(rootNoteName, RootNote.generalPortionOfNoteContent, courseName, lectureNumber, totalNumSlides, this);
    }

    void instantiateAdditionalNotesNote()
    {
        String additionalNotesNoteName = courseName + "Lecture" + lectureNumber + "AdditionalNotes";
        this.additionalNotesNote = new ObsidianNote(additionalNotesNoteName, new String[0]); // empty string array since the additional notes note won't have any content upon creation
    }

    /**
     *
     */
    void generateLectureSlideNoteMarkdownFiles()
    {
        for(LectureSlideTemplateNote lectureSlideTemplateNoteInst : arrayOfLectureSlideTemplateNotes)
        {
            lectureSlideTemplateNoteInst.generateNoteFile();
        }
    }

    /**
     * Simply calls the generateNoteFile() method from the additionalNotes note object.
     * A file for the additionalNotes note object is generated in the markdownOutput directory
     */
    void generateAdditionalNotesNoteMarkdownFile()
    {
        additionalNotesNote.generateNoteFile();
    }

    /**
     * Simply calls the generateNoteFile() method from the rootNote object
     */
    void generateRootNoteMarkdownFile()
    {
        rootNote.generateNoteFile();
    }

    /**
     * This specifically adds the links that direct between contiguous note files (which are located at the very
     * end of each note file)
     * Example: At the bottom of lecture slide 2 note: links to lecture slide 1 note and lecture slide 3 note
     */
    void addLinksToSpecificFileContentAfterInitialized()
    {
        for(int i = 0; i < arrayOfLectureSlideTemplateNotes.length; i++)
        {
            LectureSlideTemplateNote currentLectureSlideTemplateNoteInst = arrayOfLectureSlideTemplateNotes[i];
            int count = 0;
            for(int j = 0; j < currentLectureSlideTemplateNoteInst.noteContent.size(); j++) {
                if (currentLectureSlideTemplateNoteInst.noteContent.get(j).contains("X")) {

                    String currentXLine = currentLectureSlideTemplateNoteInst.noteContent.get(j);
                    String currentXLineUpdated;

                    count++;
                    if (count == 1)
                    {
                        if(i == 0) // if on the lecture slide 1
                        {
                            currentXLineUpdated = currentXLine.replaceFirst("X", "null"); //
                            currentLectureSlideTemplateNoteInst.noteContent.set(j, currentXLineUpdated);
                        }
                        else
                        {
                            currentXLineUpdated = currentXLine.replaceFirst("X", arrayOfLectureSlideTemplateNotes[i - 1].fileName);
                            currentLectureSlideTemplateNoteInst.noteContent.set(j, currentXLineUpdated);
                        }
                    }
                    else if (count == 2)
                    {
                        if(i == arrayOfLectureSlideTemplateNotes.length - 1) // if on the last lecture slide
                        {
                            currentXLineUpdated = currentXLine.replaceFirst("X", "null"); //
                            currentLectureSlideTemplateNoteInst.noteContent.set(j, currentXLineUpdated);
                        }
                        else
                        {
                            currentXLineUpdated = currentXLine.replaceFirst("X", arrayOfLectureSlideTemplateNotes[i + 1].fileName);
                            currentLectureSlideTemplateNoteInst.noteContent.set(j, currentXLineUpdated);
                        }
                    }
                }
            }
        }
    }
}
