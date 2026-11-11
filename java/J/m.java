package J;

import java.util.regex.Pattern;

public abstract class m {
    private static final Pattern a = Pattern.compile("\\$\\{(.*?)\\}");

    public static boolean a(String str) {
        return str == null || str.trim().isEmpty();
    }
}
