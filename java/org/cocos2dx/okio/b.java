package org.cocos2dx.okio;

import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public abstract class b {
    public static OutputStream a(Path path, OpenOption[] openOptionArr) {
        return Files.newOutputStream(path, openOptionArr);
    }
}
