package com.android.gallery3d.exif;

public class ExifInvalidFormatException extends Exception
{
    private static final long serialVersionUID = 1L;
    
    public ExifInvalidFormatException(String meg)
    {
        super(meg);
    }
}