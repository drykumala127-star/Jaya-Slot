package org.cocos2dx.okhttp3.internal.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;

public interface FileSystem {
    public static final FileSystem SYSTEM = new a();

    class a implements FileSystem {
        a() {
        }

        @Override
        public Sink appendingSink(File file) {
            try {
                return Okio.appendingSink(file);
            } catch (FileNotFoundException unused) {
                file.getParentFile().mkdirs();
                return Okio.appendingSink(file);
            }
        }

        @Override
        public void delete(File file) throws IOException {
            if (file.delete() || !file.exists()) {
                return;
            }
            throw new IOException("failed to delete " + file);
        }

        @Override
        public void deleteContents(File file) throws IOException {
            File[] fileArrListFiles = file.listFiles();
            if (fileArrListFiles == null) {
                throw new IOException("not a readable directory: " + file);
            }
            for (File file2 : fileArrListFiles) {
                if (file2.isDirectory()) {
                    deleteContents(file2);
                }
                if (!file2.delete()) {
                    throw new IOException("failed to delete " + file2);
                }
            }
        }

        @Override
        public boolean exists(File file) {
            return file.exists();
        }

        @Override
        public void rename(File file, File file2) throws IOException {
            delete(file2);
            if (file.renameTo(file2)) {
                return;
            }
            throw new IOException("failed to rename " + file + " to " + file2);
        }

        @Override
        public Sink sink(File file) {
            try {
                return Okio.sink(file);
            } catch (FileNotFoundException unused) {
                file.getParentFile().mkdirs();
                return Okio.sink(file);
            }
        }

        @Override
        public long size(File file) {
            return file.length();
        }

        @Override
        public Source source(File file) {
            return Okio.source(file);
        }
    }

    Sink appendingSink(File file);

    void delete(File file);

    void deleteContents(File file);

    boolean exists(File file);

    void rename(File file, File file2);

    Sink sink(File file);

    long size(File file);

    Source source(File file);
}
