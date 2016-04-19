package com.wildsmith.jarble.jar.provider;

import android.content.UriMatcher;
import android.net.Uri;
import android.os.Build;

public class JarTableProviderUri {

    private static final String PROVIDER_NAME = "com.wildsmith.jarble.jar";

    public static final String URL = "content://" + PROVIDER_NAME;

    protected static final Uri CONTENT_URI = Uri.parse(URL);

    protected static final UriMatcher URI_MATCHER;

    static {
        URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    }

    /**
     * Contains the available {@link Uri} paths for this application. During the creation of each enum value, their respective
     * properties are added to the {@link #URI_MATCHER}. It is strongly recommended that while looking for matching {@link Uri}
     * you leverage the {@link #URI_MATCHER}.
     * <p/>
     * ie:
     * <pre>
     * {@code
     *      switch (UriCode.fromCode(URI_MATCHER.match(uri))) {
     *          case MATCHING_URI:
     *          //things to perform for that uri
     *          break;
     *      }
     * }
     * </pre>
     */
    public enum UriCode {
        INVALID(PROVIDER_NAME, "invalid", 0),
        JAR(PROVIDER_NAME, "/jar/", 1),
        JAR_FOR_TIME_PERIOD(PROVIDER_NAME, "/time/jar/", 2),
        JARS_FOR_TIME_PERIOD(PROVIDER_NAME, "/time/jar/*", 3);

        private String authority;

        private String path;

        private int code;

        UriCode(String authority, String path, int code) {
            this.authority = authority;
            this.path = path;
            this.code = code;

            //Fixes issue with older OS levels not stripping out the prefixed forward slash while adding URIs
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                path = path.replaceFirst("/", "");
            }

            URI_MATCHER.addURI(authority, path, code);
        }

        public static UriCode fromCode(int code) {
            for (UriCode uriCode : UriCode.values()) {
                if (uriCode.getCode() == code) {
                    return uriCode;
                }
            }

            return INVALID;
        }

        public String getAuthority() {
            return authority;
        }

        public String getPath() {
            return path;
        }

        public int getCode() {
            return code;
        }
    }
}
