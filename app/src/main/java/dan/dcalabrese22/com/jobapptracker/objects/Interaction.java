package dan.dcalabrese22.com.jobapptracker.objects;

/**
 * Created by dcalabrese on 12/5/2017.
 */

public class Interaction {

    private String note;

    public Interaction(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return note;
    }
}
