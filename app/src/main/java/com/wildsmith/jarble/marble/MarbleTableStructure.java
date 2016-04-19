package com.wildsmith.jarble.marble;

public class MarbleTableStructure {

    public static final String TABLE_NAME = "marble_table";

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        + Column.TIME_STAMP + " " + Column.TIME_STAMP.format() + ", "
        + Column.NUMBER + " " + Column.NUMBER.format() + ", "
        + Column.STATE + " " + Column.STATE.format() + ", "
        + Column.COLOR + " " + Column.COLOR.format() + ", "
        + Column.PURPOSE_NOTES + " " + Column.PURPOSE_NOTES.format() + ", "
        + Column.PERFORMANCE_NOTES + " " + Column.PERFORMANCE_NOTES.format() + ");";

    static final String DROP_TABLE = " DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String INDEX_NAME = "marbles_table_index";

    static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " ("
        + Column.TIME_STAMP + " " + "ASC);";

    public enum Column {
        TIME_STAMP("time_stamp", "TEXT NOT NULL"),
        NUMBER("number", "INTEGER NOT NULL"),
        STATE("state", "INTEGER DEFAULT 0"),
        COLOR("color", "TEXT"),
        PURPOSE_NOTES("purpose_notes", "TEXT"),
        PERFORMANCE_NOTES("performance_notes", "TEXT");

        private String title;

        private String format;

        Column(String title, String format) {
            this.title = title;
            this.format = format;
        }

        public String title() {
            return title;
        }

        private String format() {
            return format;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
