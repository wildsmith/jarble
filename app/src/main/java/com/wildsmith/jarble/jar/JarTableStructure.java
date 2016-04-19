package com.wildsmith.jarble.jar;

public class JarTableStructure {

    public static final String TABLE_NAME = "jars_table";

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        + Column.TIME_STAMP + " " + Column.TIME_STAMP.format() + ", "
        + Column.IN_PROGRESS + " " + Column.IN_PROGRESS.format() + ", "
        + Column.IMAGE + " " + Column.IMAGE.format() + ");";

    static final String DROP_TABLE = " DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String INDEX_NAME = "jars_table_index";

    static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " ("
        + Column.TIME_STAMP + " " + "ASC);";

    public enum Column {
        TIME_STAMP("time_stamp", "TEXT NOT NULL"),
        IN_PROGRESS("in_progress", "INTEGER NOT NULL"),
        IMAGE("image", "BLOB");

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
