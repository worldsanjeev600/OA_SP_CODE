package com.oneassist.serviceplatform.commons.enums;

public enum FileType {

    ZIPFILE("ZipFile"), 
    IMAGEFILE("ImageFile"), 
    BYTEARRAYFILE("ByteArrayFile"),
    THUMBNAILIMAGEFILE("ThumbnailImageFile");
	
	private final String fileTypeName;

	FileType(String fileTypeName) {

		this.fileTypeName = fileTypeName;
	}

    public String getFileTypeName() {
        return fileTypeName;
    }

    public static FileType getFileType(String fileTypeName) {
		for (FileType fileType : FileType.values()) {
			if (fileType.getFileTypeName().equals(fileTypeName)) {
				return fileType;
			}
		}
		return null;
	}
}