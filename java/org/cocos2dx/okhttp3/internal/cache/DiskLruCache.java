package org.cocos2dx.okhttp3.internal.cache;

import java.io.Closeable;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.NoSuchElementException;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import org.cocos2dx.okhttp3.internal.Util;
import org.cocos2dx.okhttp3.internal.io.FileSystem;
import org.cocos2dx.okhttp3.internal.platform.Platform;
import org.cocos2dx.okio.BufferedSink;
import org.cocos2dx.okio.BufferedSource;
import org.cocos2dx.okio.Okio;
import org.cocos2dx.okio.Sink;
import org.cocos2dx.okio.Source;

public final class DiskLruCache implements Closeable, Flushable {
    static final boolean $assertionsDisabled = false;
    static final long ANY_SEQUENCE_NUMBER = -1;
    private static final String CLEAN = "CLEAN";
    private static final String DIRTY = "DIRTY";
    static final String JOURNAL_FILE = "journal";
    static final String JOURNAL_FILE_BACKUP = "journal.bkp";
    static final String JOURNAL_FILE_TEMP = "journal.tmp";
    static final Pattern LEGAL_KEY_PATTERN = Pattern.compile("[a-z0-9_-]{1,120}");
    static final String MAGIC = "libcore.io.DiskLruCache";
    private static final String READ = "READ";
    private static final String REMOVE = "REMOVE";
    static final String VERSION_1 = "1";
    private final int appVersion;
    boolean closed;
    final File directory;
    private final Executor executor;
    final FileSystem fileSystem;
    boolean hasJournalErrors;
    boolean initialized;
    private final File journalFile;
    private final File journalFileBackup;
    private final File journalFileTmp;
    BufferedSink journalWriter;
    private long maxSize;
    boolean mostRecentRebuildFailed;
    boolean mostRecentTrimFailed;
    int redundantOpCount;
    final int valueCount;
    private long size = 0;
    final LinkedHashMap lruEntries = new LinkedHashMap(0, 0.75f, true);
    private long nextSequenceNumber = 0;
    private final Runnable cleanupRunnable = new a();

    public final class Editor {
        private boolean done;
        final d entry;
        final boolean[] written;

        class a extends org.cocos2dx.okhttp3.internal.cache.a {
            a(Sink sink) {
                super(sink);
            }

            @Override
            protected void a(IOException iOException) {
                synchronized (DiskLruCache.this) {
                    Editor.this.detach();
                }
            }
        }

        Editor(d dVar) {
            this.entry = dVar;
            this.written = dVar.e ? null : new boolean[DiskLruCache.this.valueCount];
        }

        public void abort() {
            synchronized (DiskLruCache.this) {
                try {
                    if (this.done) {
                        throw new IllegalStateException();
                    }
                    if (this.entry.f == this) {
                        DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
                    }
                    this.done = true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public void abortUnlessCommitted() {
            synchronized (DiskLruCache.this) {
                if (!this.done && this.entry.f == this) {
                    try {
                        DiskLruCache.this.completeEdit(this, DiskLruCache.$assertionsDisabled);
                    } catch (IOException unused) {
                    }
                }
            }
        }

        public void commit() {
            synchronized (DiskLruCache.this) {
                try {
                    if (this.done) {
                        throw new IllegalStateException();
                    }
                    if (this.entry.f == this) {
                        DiskLruCache.this.completeEdit(this, true);
                    }
                    this.done = true;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        void detach() {
            if (this.entry.f != this) {
                return;
            }
            int i = 0;
            while (true) {
                DiskLruCache diskLruCache = DiskLruCache.this;
                if (i >= diskLruCache.valueCount) {
                    this.entry.f = null;
                    return;
                } else {
                    try {
                        diskLruCache.fileSystem.delete(this.entry.d[i]);
                    } catch (IOException unused) {
                    }
                    i++;
                }
            }
        }

        public Sink newSink(int i) {
            synchronized (DiskLruCache.this) {
                try {
                    if (this.done) {
                        throw new IllegalStateException();
                    }
                    d dVar = this.entry;
                    if (dVar.f != this) {
                        return Okio.blackhole();
                    }
                    if (!dVar.e) {
                        this.written[i] = true;
                    }
                    try {
                        return new a(DiskLruCache.this.fileSystem.sink(dVar.d[i]));
                    } catch (FileNotFoundException unused) {
                        return Okio.blackhole();
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        public Source newSource(int i) {
            synchronized (DiskLruCache.this) {
                try {
                    if (this.done) {
                        throw new IllegalStateException();
                    }
                    d dVar = this.entry;
                    if (!dVar.e || dVar.f != this) {
                        return null;
                    }
                    try {
                        return DiskLruCache.this.fileSystem.source(dVar.c[i]);
                    } catch (FileNotFoundException unused) {
                        return null;
                    }
                } catch (Throwable th) {
                    throw th;
                }
            }
        }
    }

    public final class Snapshot implements Closeable {
        private final String key;
        private final long[] lengths;
        private final long sequenceNumber;
        private final Source[] sources;

        Snapshot(String str, long j, Source[] sourceArr, long[] jArr) {
            this.key = str;
            this.sequenceNumber = j;
            this.sources = sourceArr;
            this.lengths = jArr;
        }

        static String access$000(Snapshot snapshot) {
            return snapshot.key;
        }

        @Override
        public void close() {
            for (Source source : this.sources) {
                Util.closeQuietly(source);
            }
        }

        @Nullable
        public Editor edit() {
            return DiskLruCache.this.edit(this.key, this.sequenceNumber);
        }

        public long getLength(int i) {
            return this.lengths[i];
        }

        public Source getSource(int i) {
            return this.sources[i];
        }

        public String key() {
            return this.key;
        }
    }

    class a implements Runnable {
        a() {
        }

        @Override
        public void run() {
            synchronized (DiskLruCache.this) {
                DiskLruCache diskLruCache = DiskLruCache.this;
                if ((!diskLruCache.initialized) || diskLruCache.closed) {
                    return;
                }
                try {
                    diskLruCache.trimToSize();
                } catch (IOException unused) {
                    DiskLruCache.this.mostRecentTrimFailed = true;
                }
                try {
                    if (DiskLruCache.this.journalRebuildRequired()) {
                        DiskLruCache.this.rebuildJournal();
                        DiskLruCache.this.redundantOpCount = 0;
                    }
                } catch (IOException unused2) {
                    DiskLruCache diskLruCache2 = DiskLruCache.this;
                    diskLruCache2.mostRecentRebuildFailed = true;
                    diskLruCache2.journalWriter = Okio.buffer(Okio.blackhole());
                }
            }
        }
    }

    class b extends org.cocos2dx.okhttp3.internal.cache.a {
        b(Sink sink) {
            super(sink);
        }

        @Override
        protected void a(IOException iOException) {
            DiskLruCache.this.hasJournalErrors = true;
        }
    }

    class c implements Iterator {
        final Iterator a;
        Snapshot b;
        Snapshot c;

        c() {
            this.a = new ArrayList(DiskLruCache.this.lruEntries.values()).iterator();
        }

        @Override
        public Snapshot next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            Snapshot snapshot = this.b;
            this.c = snapshot;
            this.b = null;
            return snapshot;
        }

        @Override
        public boolean hasNext() {
            Snapshot snapshotC;
            if (this.b != null) {
                return true;
            }
            synchronized (DiskLruCache.this) {
                try {
                    if (DiskLruCache.this.closed) {
                        return DiskLruCache.$assertionsDisabled;
                    }
                    while (this.a.hasNext()) {
                        d dVar = (d) this.a.next();
                        if (dVar.e && (snapshotC = dVar.c()) != null) {
                            this.b = snapshotC;
                            return true;
                        }
                    }
                    return DiskLruCache.$assertionsDisabled;
                } catch (Throwable th) {
                    throw th;
                }
            }
        }

        @Override
        public void remove() {
            Snapshot snapshot = this.c;
            if (snapshot == null) {
                throw new IllegalStateException("remove() before next()");
            }
            try {
                DiskLruCache.this.remove(Snapshot.access$000(snapshot));
            } catch (IOException unused) {
            } finally {
                this.c = null;
            }
        }
    }

    private final class d {
        final String a;
        final long[] b;
        final File[] c;
        final File[] d;
        boolean e;
        Editor f;
        long g;

        d(String str) {
            this.a = str;
            int i = DiskLruCache.this.valueCount;
            this.b = new long[i];
            this.c = new File[i];
            this.d = new File[i];
            StringBuilder sb = new StringBuilder(str);
            sb.append('.');
            int length = sb.length();
            for (int i2 = 0; i2 < DiskLruCache.this.valueCount; i2++) {
                sb.append(i2);
                this.c[i2] = new File(DiskLruCache.this.directory, sb.toString());
                sb.append(".tmp");
                this.d[i2] = new File(DiskLruCache.this.directory, sb.toString());
                sb.setLength(length);
            }
        }

        private IOException a(String[] strArr) throws IOException {
            throw new IOException("unexpected journal line: " + Arrays.toString(strArr));
        }

        void b(String[] strArr) throws IOException {
            if (strArr.length != DiskLruCache.this.valueCount) {
                throw a(strArr);
            }
            for (int i = 0; i < strArr.length; i++) {
                try {
                    this.b[i] = Long.parseLong(strArr[i]);
                } catch (NumberFormatException unused) {
                    throw a(strArr);
                }
            }
        }

        Snapshot c() {
            DiskLruCache diskLruCache;
            Source source;
            if (!Thread.holdsLock(DiskLruCache.this)) {
                throw new AssertionError();
            }
            Source[] sourceArr = new Source[DiskLruCache.this.valueCount];
            long[] jArr = (long[]) this.b.clone();
            int i = 0;
            int i2 = 0;
            while (true) {
                try {
                    DiskLruCache diskLruCache2 = DiskLruCache.this;
                    if (i2 >= diskLruCache2.valueCount) {
                        return diskLruCache2.new Snapshot(this.a, this.g, sourceArr, jArr);
                    }
                    sourceArr[i2] = diskLruCache2.fileSystem.source(this.c[i2]);
                    i2++;
                } catch (FileNotFoundException unused) {
                    while (true) {
                        diskLruCache = DiskLruCache.this;
                        if (i >= diskLruCache.valueCount || (source = sourceArr[i]) == null) {
                            break;
                        }
                        Util.closeQuietly(source);
                        i++;
                    }
                    try {
                        diskLruCache.removeEntry(this);
                        return null;
                    } catch (IOException unused2) {
                        return null;
                    }
                }
            }
        }

        void d(BufferedSink bufferedSink) {
            for (long j : this.b) {
                bufferedSink.writeByte(32).writeDecimalLong(j);
            }
        }
    }

    DiskLruCache(FileSystem fileSystem, File file, int i, int i2, long j, Executor executor) {
        this.fileSystem = fileSystem;
        this.directory = file;
        this.appVersion = i;
        this.journalFile = new File(file, JOURNAL_FILE);
        this.journalFileTmp = new File(file, JOURNAL_FILE_TEMP);
        this.journalFileBackup = new File(file, JOURNAL_FILE_BACKUP);
        this.valueCount = i2;
        this.maxSize = j;
        this.executor = executor;
    }

    private synchronized void checkNotClosed() {
        if (isClosed()) {
            throw new IllegalStateException("cache is closed");
        }
    }

    public static DiskLruCache create(FileSystem fileSystem, File file, int i, int i2, long j) {
        if (j <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (i2 > 0) {
            return new DiskLruCache(fileSystem, file, i, i2, j, new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory("OkHttp DiskLruCache", true)));
        }
        throw new IllegalArgumentException("valueCount <= 0");
    }

    private BufferedSink newJournalWriter() {
        return Okio.buffer(new b(this.fileSystem.appendingSink(this.journalFile)));
    }

    private void processJournal() {
        this.fileSystem.delete(this.journalFileTmp);
        Iterator it = this.lruEntries.values().iterator();
        while (it.hasNext()) {
            d dVar = (d) it.next();
            int i = 0;
            if (dVar.f == null) {
                while (i < this.valueCount) {
                    this.size += dVar.b[i];
                    i++;
                }
            } else {
                dVar.f = null;
                while (i < this.valueCount) {
                    this.fileSystem.delete(dVar.c[i]);
                    this.fileSystem.delete(dVar.d[i]);
                    i++;
                }
                it.remove();
            }
        }
    }

    private void readJournal() {
        BufferedSource bufferedSourceBuffer = Okio.buffer(this.fileSystem.source(this.journalFile));
        try {
            String utf8LineStrict = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict2 = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict3 = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict4 = bufferedSourceBuffer.readUtf8LineStrict();
            String utf8LineStrict5 = bufferedSourceBuffer.readUtf8LineStrict();
            if (!MAGIC.equals(utf8LineStrict) || !VERSION_1.equals(utf8LineStrict2) || !Integer.toString(this.appVersion).equals(utf8LineStrict3) || !Integer.toString(this.valueCount).equals(utf8LineStrict4) || !"".equals(utf8LineStrict5)) {
                throw new IOException("unexpected journal header: [" + utf8LineStrict + ", " + utf8LineStrict2 + ", " + utf8LineStrict4 + ", " + utf8LineStrict5 + "]");
            }
            int i = 0;
            while (true) {
                try {
                    readJournalLine(bufferedSourceBuffer.readUtf8LineStrict());
                    i++;
                } catch (EOFException unused) {
                    this.redundantOpCount = i - this.lruEntries.size();
                    if (bufferedSourceBuffer.exhausted()) {
                        this.journalWriter = newJournalWriter();
                    } else {
                        rebuildJournal();
                    }
                    Util.closeQuietly(bufferedSourceBuffer);
                    return;
                }
            }
        } catch (Throwable th) {
            Util.closeQuietly(bufferedSourceBuffer);
            throw th;
        }
    }

    private void readJournalLine(String str) throws IOException {
        String strSubstring;
        int iIndexOf = str.indexOf(32);
        if (iIndexOf == -1) {
            throw new IOException("unexpected journal line: " + str);
        }
        int i = iIndexOf + 1;
        int iIndexOf2 = str.indexOf(32, i);
        if (iIndexOf2 == -1) {
            strSubstring = str.substring(i);
            if (iIndexOf == 6 && str.startsWith(REMOVE)) {
                this.lruEntries.remove(strSubstring);
                return;
            }
        } else {
            strSubstring = str.substring(i, iIndexOf2);
        }
        d dVar = (d) this.lruEntries.get(strSubstring);
        if (dVar == null) {
            dVar = new d(strSubstring);
            this.lruEntries.put(strSubstring, dVar);
        }
        if (iIndexOf2 != -1 && iIndexOf == 5 && str.startsWith(CLEAN)) {
            String[] strArrSplit = str.substring(iIndexOf2 + 1).split(" ");
            dVar.e = true;
            dVar.f = null;
            dVar.b(strArrSplit);
            return;
        }
        if (iIndexOf2 == -1 && iIndexOf == 5 && str.startsWith(DIRTY)) {
            dVar.f = new Editor(dVar);
            return;
        }
        if (iIndexOf2 == -1 && iIndexOf == 4 && str.startsWith(READ)) {
            return;
        }
        throw new IOException("unexpected journal line: " + str);
    }

    private void validateKey(String str) {
        if (LEGAL_KEY_PATTERN.matcher(str).matches()) {
            return;
        }
        throw new IllegalArgumentException("keys must match regex [a-z0-9_-]{1,120}: \"" + str + "\"");
    }

    @Override
    public synchronized void close() {
        try {
            if (this.initialized && !this.closed) {
                for (d dVar : (d[]) this.lruEntries.values().toArray(new d[this.lruEntries.size()])) {
                    Editor editor = dVar.f;
                    if (editor != null) {
                        editor.abort();
                    }
                }
                trimToSize();
                this.journalWriter.close();
                this.journalWriter = null;
                this.closed = true;
                return;
            }
            this.closed = true;
        } catch (Throwable th) {
            throw th;
        }
    }

    synchronized void completeEdit(Editor editor, boolean z) {
        d dVar = editor.entry;
        if (dVar.f != editor) {
            throw new IllegalStateException();
        }
        if (z && !dVar.e) {
            for (int i = 0; i < this.valueCount; i++) {
                if (!editor.written[i]) {
                    editor.abort();
                    throw new IllegalStateException("Newly created entry didn't create value for index " + i);
                }
                if (!this.fileSystem.exists(dVar.d[i])) {
                    editor.abort();
                    return;
                }
            }
        }
        for (int i2 = 0; i2 < this.valueCount; i2++) {
            File file = dVar.d[i2];
            if (!z) {
                this.fileSystem.delete(file);
            } else if (this.fileSystem.exists(file)) {
                File file2 = dVar.c[i2];
                this.fileSystem.rename(file, file2);
                long j = dVar.b[i2];
                long size = this.fileSystem.size(file2);
                dVar.b[i2] = size;
                this.size = (this.size - j) + size;
            }
        }
        this.redundantOpCount++;
        dVar.f = null;
        if (dVar.e || z) {
            dVar.e = true;
            this.journalWriter.writeUtf8(CLEAN).writeByte(32);
            this.journalWriter.writeUtf8(dVar.a);
            dVar.d(this.journalWriter);
            this.journalWriter.writeByte(10);
            if (z) {
                long j2 = this.nextSequenceNumber;
                this.nextSequenceNumber = 1 + j2;
                dVar.g = j2;
            }
        } else {
            this.lruEntries.remove(dVar.a);
            this.journalWriter.writeUtf8(REMOVE).writeByte(32);
            this.journalWriter.writeUtf8(dVar.a);
            this.journalWriter.writeByte(10);
        }
        this.journalWriter.flush();
        if (this.size > this.maxSize || journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public void delete() {
        close();
        this.fileSystem.deleteContents(this.directory);
    }

    @Nullable
    public Editor edit(String str) {
        return edit(str, ANY_SEQUENCE_NUMBER);
    }

    public synchronized void evictAll() {
        try {
            initialize();
            for (d dVar : (d[]) this.lruEntries.values().toArray(new d[this.lruEntries.size()])) {
                removeEntry(dVar);
            }
            this.mostRecentTrimFailed = $assertionsDisabled;
        } catch (Throwable th) {
            throw th;
        }
    }

    @Override
    public synchronized void flush() {
        if (this.initialized) {
            checkNotClosed();
            trimToSize();
            this.journalWriter.flush();
        }
    }

    public synchronized Snapshot get(String str) {
        initialize();
        checkNotClosed();
        validateKey(str);
        d dVar = (d) this.lruEntries.get(str);
        if (dVar != null && dVar.e) {
            Snapshot snapshotC = dVar.c();
            if (snapshotC == null) {
                return null;
            }
            this.redundantOpCount++;
            this.journalWriter.writeUtf8(READ).writeByte(32).writeUtf8(str).writeByte(10);
            if (journalRebuildRequired()) {
                this.executor.execute(this.cleanupRunnable);
            }
            return snapshotC;
        }
        return null;
    }

    public File getDirectory() {
        return this.directory;
    }

    public synchronized long getMaxSize() {
        return this.maxSize;
    }

    public synchronized void initialize() {
        try {
            if (this.initialized) {
                return;
            }
            if (this.fileSystem.exists(this.journalFileBackup)) {
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.delete(this.journalFileBackup);
                } else {
                    this.fileSystem.rename(this.journalFileBackup, this.journalFile);
                }
            }
            if (this.fileSystem.exists(this.journalFile)) {
                try {
                    readJournal();
                    processJournal();
                    this.initialized = true;
                    return;
                } catch (IOException e) {
                    Platform.get().log(5, "DiskLruCache " + this.directory + " is corrupt: " + e.getMessage() + ", removing", e);
                    try {
                        delete();
                        this.closed = $assertionsDisabled;
                        rebuildJournal();
                        this.initialized = true;
                    } catch (Throwable th) {
                        this.closed = $assertionsDisabled;
                        throw th;
                    }
                }
            }
            rebuildJournal();
            this.initialized = true;
        } catch (Throwable th2) {
            throw th2;
        }
    }

    public synchronized boolean isClosed() {
        return this.closed;
    }

    boolean journalRebuildRequired() {
        int i = this.redundantOpCount;
        if (i < 2000 || i < this.lruEntries.size()) {
            return $assertionsDisabled;
        }
        return true;
    }

    synchronized void rebuildJournal() {
        try {
            BufferedSink bufferedSink = this.journalWriter;
            if (bufferedSink != null) {
                bufferedSink.close();
            }
            BufferedSink bufferedSinkBuffer = Okio.buffer(this.fileSystem.sink(this.journalFileTmp));
            try {
                bufferedSinkBuffer.writeUtf8(MAGIC).writeByte(10);
                bufferedSinkBuffer.writeUtf8(VERSION_1).writeByte(10);
                bufferedSinkBuffer.writeDecimalLong(this.appVersion).writeByte(10);
                bufferedSinkBuffer.writeDecimalLong(this.valueCount).writeByte(10);
                bufferedSinkBuffer.writeByte(10);
                for (d dVar : this.lruEntries.values()) {
                    if (dVar.f != null) {
                        bufferedSinkBuffer.writeUtf8(DIRTY).writeByte(32);
                        bufferedSinkBuffer.writeUtf8(dVar.a);
                        bufferedSinkBuffer.writeByte(10);
                    } else {
                        bufferedSinkBuffer.writeUtf8(CLEAN).writeByte(32);
                        bufferedSinkBuffer.writeUtf8(dVar.a);
                        dVar.d(bufferedSinkBuffer);
                        bufferedSinkBuffer.writeByte(10);
                    }
                }
                bufferedSinkBuffer.close();
                if (this.fileSystem.exists(this.journalFile)) {
                    this.fileSystem.rename(this.journalFile, this.journalFileBackup);
                }
                this.fileSystem.rename(this.journalFileTmp, this.journalFile);
                this.fileSystem.delete(this.journalFileBackup);
                this.journalWriter = newJournalWriter();
                this.hasJournalErrors = $assertionsDisabled;
                this.mostRecentRebuildFailed = $assertionsDisabled;
            } catch (Throwable th) {
                bufferedSinkBuffer.close();
                throw th;
            }
        } catch (Throwable th2) {
            throw th2;
        }
    }

    public synchronized boolean remove(String str) {
        initialize();
        checkNotClosed();
        validateKey(str);
        d dVar = (d) this.lruEntries.get(str);
        if (dVar == null) {
            return $assertionsDisabled;
        }
        boolean zRemoveEntry = removeEntry(dVar);
        if (zRemoveEntry && this.size <= this.maxSize) {
            this.mostRecentTrimFailed = $assertionsDisabled;
        }
        return zRemoveEntry;
    }

    boolean removeEntry(d dVar) {
        Editor editor = dVar.f;
        if (editor != null) {
            editor.detach();
        }
        for (int i = 0; i < this.valueCount; i++) {
            this.fileSystem.delete(dVar.c[i]);
            long j = this.size;
            long[] jArr = dVar.b;
            this.size = j - jArr[i];
            jArr[i] = 0;
        }
        this.redundantOpCount++;
        this.journalWriter.writeUtf8(REMOVE).writeByte(32).writeUtf8(dVar.a).writeByte(10);
        this.lruEntries.remove(dVar.a);
        if (journalRebuildRequired()) {
            this.executor.execute(this.cleanupRunnable);
        }
        return true;
    }

    public synchronized void setMaxSize(long j) {
        this.maxSize = j;
        if (this.initialized) {
            this.executor.execute(this.cleanupRunnable);
        }
    }

    public synchronized long size() {
        initialize();
        return this.size;
    }

    public synchronized Iterator snapshots() {
        initialize();
        return new c();
    }

    void trimToSize() {
        while (this.size > this.maxSize) {
            removeEntry((d) this.lruEntries.values().iterator().next());
        }
        this.mostRecentTrimFailed = $assertionsDisabled;
    }

    synchronized Editor edit(String str, long j) {
        initialize();
        checkNotClosed();
        validateKey(str);
        d dVar = (d) this.lruEntries.get(str);
        if (j != ANY_SEQUENCE_NUMBER && (dVar == null || dVar.g != j)) {
            return null;
        }
        if (dVar != null && dVar.f != null) {
            return null;
        }
        if (!this.mostRecentTrimFailed && !this.mostRecentRebuildFailed) {
            this.journalWriter.writeUtf8(DIRTY).writeByte(32).writeUtf8(str).writeByte(10);
            this.journalWriter.flush();
            if (this.hasJournalErrors) {
                return null;
            }
            if (dVar == null) {
                dVar = new d(str);
                this.lruEntries.put(str, dVar);
            }
            Editor editor = new Editor(dVar);
            dVar.f = editor;
            return editor;
        }
        this.executor.execute(this.cleanupRunnable);
        return null;
    }
}
