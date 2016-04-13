package com.wildsmith.jarble.provider.jar;

public class JarTableStructure {

    public static final String TABLE_NAME = "jars_table";

    static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ("
        + Column.YEAR + " " + Column.YEAR.format() + ", "
        + Column.MONTH + " " + Column.MONTH.format() + ", "
        + Column.WEEK_OF_MONTH + " " + Column.WEEK_OF_MONTH.format() + ", "
        + Column.DAY_OF_MONTH + " " + Column.DAY_OF_MONTH.format() + ", "
        + Column.DAY_OF_WEEK + " " + Column.DAY_OF_WEEK.format() + ", "
        + Column.TIME_STAMP + " " + Column.TIME_STAMP.format() + ", "
        + Column.IN_PROGRESS + " " + Column.IN_PROGRESS.format() + ", "
        + Column.IMAGE + " " + Column.IMAGE.format() + ", "
        + Column.MARBLE_ZERO_ID + " " + Column.MARBLE_ZERO_ID.format() + ", "
        + Column.MARBLE_ZERO_STATE + " " + Column.MARBLE_ZERO_STATE.format() + ", "
        + Column.MARBLE_ZERO_COLOR + " " + Column.MARBLE_ZERO_COLOR.format() + ", "
        + Column.MARBLE_ZERO_PURPOSE_NOTES + " " + Column.MARBLE_ZERO_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_ZERO_PERFORMANCE_NOTES + " " + Column.MARBLE_ZERO_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_ONE_ID + " " + Column.MARBLE_ONE_ID.format() + ", "
        + Column.MARBLE_ONE_STATE + " " + Column.MARBLE_ONE_STATE.format() + ", "
        + Column.MARBLE_ONE_COLOR + " " + Column.MARBLE_ONE_COLOR.format() + ", "
        + Column.MARBLE_ONE_PURPOSE_NOTES + " " + Column.MARBLE_ONE_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_ONE_PERFORMANCE_NOTES + " " + Column.MARBLE_ONE_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_TWO_ID + " " + Column.MARBLE_TWO_ID.format() + ", "
        + Column.MARBLE_TWO_STATE + " " + Column.MARBLE_TWO_STATE.format() + ", "
        + Column.MARBLE_TWO_COLOR + " " + Column.MARBLE_TWO_COLOR.format() + ", "
        + Column.MARBLE_TWO_PURPOSE_NOTES + " " + Column.MARBLE_TWO_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_TWO_PERFORMANCE_NOTES + " " + Column.MARBLE_TWO_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_THREE_ID + " " + Column.MARBLE_THREE_ID.format() + ", "
        + Column.MARBLE_THREE_STATE + " " + Column.MARBLE_THREE_STATE.format() + ", "
        + Column.MARBLE_THREE_COLOR + " " + Column.MARBLE_THREE_COLOR.format() + ", "
        + Column.MARBLE_THREE_PURPOSE_NOTES + " " + Column.MARBLE_THREE_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_THREE_PERFORMANCE_NOTES + " " + Column.MARBLE_THREE_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_FOUR_ID + " " + Column.MARBLE_FOUR_ID.format() + ", "
        + Column.MARBLE_FOUR_STATE + " " + Column.MARBLE_FOUR_STATE.format() + ", "
        + Column.MARBLE_FOUR_COLOR + " " + Column.MARBLE_FOUR_COLOR.format() + ", "
        + Column.MARBLE_FOUR_PURPOSE_NOTES + " " + Column.MARBLE_FOUR_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_FOUR_PERFORMANCE_NOTES + " " + Column.MARBLE_FOUR_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_FIVE_ID + " " + Column.MARBLE_FIVE_ID.format() + ", "
        + Column.MARBLE_FIVE_STATE + " " + Column.MARBLE_FIVE_STATE.format() + ", "
        + Column.MARBLE_FIVE_COLOR + " " + Column.MARBLE_FIVE_COLOR.format() + ", "
        + Column.MARBLE_FIVE_PURPOSE_NOTES + " " + Column.MARBLE_FIVE_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_FIVE_PERFORMANCE_NOTES + " " + Column.MARBLE_FIVE_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_SIX_ID + " " + Column.MARBLE_SIX_ID.format() + ", "
        + Column.MARBLE_SIX_STATE + " " + Column.MARBLE_SIX_STATE.format() + ", "
        + Column.MARBLE_SIX_COLOR + " " + Column.MARBLE_SIX_COLOR.format() + ", "
        + Column.MARBLE_SIX_PURPOSE_NOTES + " " + Column.MARBLE_SIX_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_SIX_PERFORMANCE_NOTES + " " + Column.MARBLE_SIX_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_SEVEN_ID + " " + Column.MARBLE_SEVEN_ID.format() + ", "
        + Column.MARBLE_SEVEN_STATE + " " + Column.MARBLE_SEVEN_STATE.format() + ", "
        + Column.MARBLE_SEVEN_COLOR + " " + Column.MARBLE_SEVEN_COLOR.format() + ", "
        + Column.MARBLE_SEVEN_PURPOSE_NOTES + " " + Column.MARBLE_SEVEN_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_SEVEN_PERFORMANCE_NOTES + " " + Column.MARBLE_SEVEN_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_EIGHT_ID + " " + Column.MARBLE_EIGHT_ID.format() + ", "
        + Column.MARBLE_EIGHT_STATE + " " + Column.MARBLE_EIGHT_STATE.format() + ", "
        + Column.MARBLE_EIGHT_COLOR + " " + Column.MARBLE_EIGHT_COLOR.format() + ", "
        + Column.MARBLE_EIGHT_PURPOSE_NOTES + " " + Column.MARBLE_EIGHT_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_EIGHT_PERFORMANCE_NOTES + " " + Column.MARBLE_EIGHT_PERFORMANCE_NOTES.format() + ", "
        + Column.MARBLE_NINE_ID + " " + Column.MARBLE_NINE_ID.format() + ", "
        + Column.MARBLE_NINE_STATE + " " + Column.MARBLE_NINE_STATE.format() + ", "
        + Column.MARBLE_NINE_COLOR + " " + Column.MARBLE_NINE_COLOR.format() + ", "
        + Column.MARBLE_NINE_PURPOSE_NOTES + " " + Column.MARBLE_NINE_PURPOSE_NOTES.format() + ", "
        + Column.MARBLE_NINE_PERFORMANCE_NOTES + " " + Column.MARBLE_NINE_PERFORMANCE_NOTES.format() + ");";

    static final String DROP_TABLE = " DROP TABLE IF EXISTS " + TABLE_NAME;

    private static final String INDEX_NAME = "jars_table_index";

    static final String CREATE_INDEX = "CREATE INDEX IF NOT EXISTS " + INDEX_NAME + " ON " + TABLE_NAME + " ("
        + Column.YEAR + " " + "ASC);";

    public enum Column {
        YEAR("year", "INTEGER NOT NULL"),
        MONTH("month", "INTEGER NOT NULL"),
        WEEK_OF_MONTH("week_of_month", "INTEGER NOT NULL"),
        DAY_OF_MONTH("day_of_month", "INTEGER NOT NULL"),
        DAY_OF_WEEK("day_of_week", "INTEGER NOT NULL"),
        TIME_STAMP("time_stamp", "INTEGER NOT NULL"),
        IN_PROGRESS("in_progress", "INTEGER NOT NULL"),
        IMAGE("image", "BLOB"),
        MARBLE_ZERO_ID("marble_zero_id", "INTEGER DEFAULT 0"),
        MARBLE_ZERO_STATE("marble_zero_state", "INTEGER DEFAULT 0"),
        MARBLE_ZERO_COLOR("marble_zero_color", "TEXT"),
        MARBLE_ZERO_PURPOSE_NOTES("marble_zero_purpose_notes", "TEXT"),
        MARBLE_ZERO_PERFORMANCE_NOTES("marble_zero_performance_notes", "TEXT"),
        MARBLE_ONE_ID("marble_one_id", "INTEGER DEFAULT 0"),
        MARBLE_ONE_STATE("marble_one_state", "INTEGER DEFAULT 0"),
        MARBLE_ONE_COLOR("marble_one_color", "TEXT"),
        MARBLE_ONE_PURPOSE_NOTES("marble_one_purpose_notes", "TEXT"),
        MARBLE_ONE_PERFORMANCE_NOTES("marble_one_performance_notes", "TEXT"),
        MARBLE_TWO_ID("marble_two_id", "INTEGER DEFAULT 0"),
        MARBLE_TWO_STATE("marble_two_state", "INTEGER DEFAULT 0"),
        MARBLE_TWO_COLOR("marble_two_color", "TEXT"),
        MARBLE_TWO_PURPOSE_NOTES("marble_two_purpose_notes", "TEXT"),
        MARBLE_TWO_PERFORMANCE_NOTES("marble_two_performance_notes", "TEXT"),
        MARBLE_THREE_ID("marble_three_id", "INTEGER DEFAULT 0"),
        MARBLE_THREE_STATE("marble_three_state", "INTEGER DEFAULT 0"),
        MARBLE_THREE_COLOR("marble_three_color", "TEXT"),
        MARBLE_THREE_PURPOSE_NOTES("marble_three_purpose_notes", "TEXT"),
        MARBLE_THREE_PERFORMANCE_NOTES("marble_three_performance_notes", "TEXT"),
        MARBLE_FOUR_ID("marble_four_id", "INTEGER DEFAULT 0"),
        MARBLE_FOUR_STATE("marble_four_state", "INTEGER DEFAULT 0"),
        MARBLE_FOUR_COLOR("marble_four_color", "TEXT"),
        MARBLE_FOUR_PURPOSE_NOTES("marble_four_purpose_notes", "TEXT"),
        MARBLE_FOUR_PERFORMANCE_NOTES("marble_four_performance_notes", "TEXT"),
        MARBLE_FIVE_ID("marble_five_id", "INTEGER DEFAULT 0"),
        MARBLE_FIVE_STATE("marble_five_state", "INTEGER DEFAULT 0"),
        MARBLE_FIVE_COLOR("marble_five_color", "TEXT"),
        MARBLE_FIVE_PURPOSE_NOTES("marble_five_purpose_notes", "TEXT"),
        MARBLE_FIVE_PERFORMANCE_NOTES("marble_five_performance_notes", "TEXT"),
        MARBLE_SIX_ID("marble_six_id", "INTEGER DEFAULT 0"),
        MARBLE_SIX_STATE("marble_six_state", "INTEGER DEFAULT 0"),
        MARBLE_SIX_COLOR("marble_six_color", "TEXT"),
        MARBLE_SIX_PURPOSE_NOTES("marble_six_purpose_notes", "TEXT"),
        MARBLE_SIX_PERFORMANCE_NOTES("marble_six_performance_notes", "TEXT"),
        MARBLE_SEVEN_ID("marble_seven_id", "INTEGER DEFAULT 0"),
        MARBLE_SEVEN_STATE("marble_seven_state", "INTEGER DEFAULT 0"),
        MARBLE_SEVEN_COLOR("marble_seven_color", "TEXT"),
        MARBLE_SEVEN_PURPOSE_NOTES("marble_seven_purpose_notes", "TEXT"),
        MARBLE_SEVEN_PERFORMANCE_NOTES("marble_seven_performance_notes", "TEXT"),
        MARBLE_EIGHT_ID("marble_eight_id", "INTEGER DEFAULT 0"),
        MARBLE_EIGHT_STATE("marble_eight_state", "INTEGER DEFAULT 0"),
        MARBLE_EIGHT_COLOR("marble_eight_color", "TEXT"),
        MARBLE_EIGHT_PURPOSE_NOTES("marble_eight_purpose_notes", "TEXT"),
        MARBLE_EIGHT_PERFORMANCE_NOTES("marble_eight_performance_notes", "TEXT"),
        MARBLE_NINE_ID("marble_nine_id", "INTEGER DEFAULT 0"),
        MARBLE_NINE_STATE("marble_nine_state", "INTEGER DEFAULT 0"),
        MARBLE_NINE_COLOR("marble_nine_color", "TEXT"),
        MARBLE_NINE_PURPOSE_NOTES("marble_nine_purpose_notes", "TEXT"),
        MARBLE_NINE_PERFORMANCE_NOTES("marble_nine_performance_notes", "TEXT");

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
