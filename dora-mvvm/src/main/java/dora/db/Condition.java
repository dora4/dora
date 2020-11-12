package dora.db;

public class Condition {

    private String selection;
    private String[] selectionArgs;

    public Condition(String selection, String[] selectionArgs) {
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    public String getSelection() {
        return selection;
    }

    public String[] getSelectionArgs() {
        return selectionArgs;
    }
}
