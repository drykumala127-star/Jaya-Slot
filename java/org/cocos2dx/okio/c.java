package org.cocos2dx.okio;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;

public abstract class c {
    public static InputStream a(Path path, OpenOption[] openOptionArr) {
        return Files.newInputStream(path, openOptionArr);
    }
}
