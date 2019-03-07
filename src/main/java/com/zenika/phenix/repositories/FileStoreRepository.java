package com.zenika.phenix.repositories;

import java.io.*;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileStoreRepository implements StoreRepository {
    private final File db;

    private List<String> ids;

    public FileStoreRepository(final String dbfile) {
        Objects.requireNonNull(dbfile, "dbfile can't be null");
        this.db = new File(dbfile);
        if (!this.db.exists()) {
            throw new IllegalArgumentException("no file exist for path " + dbfile);
        }
    }

    @Override
    public Collection<String> getAllStoreIds() {
        if (ids == null) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(db));
                ids = br.lines().collect(Collectors.toList());
                br.close();
            } catch (IOException e) {
                throw new RuntimeException("Unexpected error while reading file " + db.getAbsolutePath());
            }
        }
        return Collections.unmodifiableCollection(ids);
    }
}
