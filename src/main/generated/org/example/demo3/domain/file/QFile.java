package org.example.demo3.domain.file;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFile is a Querydsl query type for File
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFile extends EntityPathBase<File> {

    private static final long serialVersionUID = 43649776L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFile file = new QFile("file");

    public final StringPath contentType = createString("contentType");

    public final StringPath filePath = createString("filePath");

    public final EnumPath<FileType> fileType = createEnum("fileType", FileType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath originalFilename = createString("originalFilename");

    public final org.example.demo3.domain.post.QPost post;

    public final NumberPath<Long> size = createNumber("size", Long.class);

    public final StringPath storedFilename = createString("storedFilename");

    public final DateTimePath<java.time.LocalDateTime> uploadedAt = createDateTime("uploadedAt", java.time.LocalDateTime.class);

    public final StringPath url = createString("url");

    public QFile(String variable) {
        this(File.class, forVariable(variable), INITS);
    }

    public QFile(Path<? extends File> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFile(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFile(PathMetadata metadata, PathInits inits) {
        this(File.class, metadata, inits);
    }

    public QFile(Class<? extends File> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.post = inits.isInitialized("post") ? new org.example.demo3.domain.post.QPost(forProperty("post"), inits.get("post")) : null;
    }

}

