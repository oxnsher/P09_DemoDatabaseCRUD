package sg.edu.rp.c346.id22017424.p09_demo_databasecrud;

import java.io.Serializable;

// We need to pass the class object to another Activity and implementing the Serializable interface
// lets us pass the class object as a Serializable object to another Activity
public class Note implements Serializable {

    private int id;
    private String noteContent;

    public Note( int id, String noteContent  ) {
        this.id = id;
        this.noteContent = noteContent;
    }

    public int getId() {  return id;  }

    public String getNoteContent() { return noteContent; }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }

    @Override
    public String toString() { return "ID:" + id + ", " + noteContent;  }

    // Because of the toString() method, even with a complex data, a simple ListView can be used
    // with a simple ArrayAdapter. The ArrayAdapter will display only 1 line of string, which is
    // defined above.

}
