package ca.bcit.moffat.comp2601.assignment2;

/**
 * this class represents an audio file
 * Note. initializer used here and this() used in music media because
 * music media has data to populate default artist (and it's private)
 * @author james
 * @version 1
 */
public class AudioFile extends MusicMedia
    implements FileManager
{
    public static final String[] AUDIOFILE_FIELDS = {"SKU", "Title", "Artist", "Year", "Filename", "Resolution"};
    public static final String   AUDIOFILE_PREFIX;
    public static final String   AUDIOFILE_STRING;

    public static final int      MINIMUM_BYTES;
    public static final int      MAXIMUM_BYTES;

    private String fileName;
    private int    fileResolution;

    static
    {
        MINIMUM_BYTES = 0;
        MAXIMUM_BYTES = 1440;
        AUDIOFILE_STRING = "AudioFile";
        AUDIOFILE_PREFIX   = "af";
    }

    {
        fileName       = "untitled";
        fileResolution = 0;
    }

    /**
     * create audio file object with default values
     * given by super and initializer values
     */
    public AudioFile()
    {
        super();
    }

    /**
     * create audio file object
     * @param sku the sku
     * @param title the title
     * @param artist the artist
     * @param year the year
     * @param fileName the name of the file
     */
    public AudioFile(final String sku, final String title,
                     final String artist, final int year,
                     final String fileName)
    {
        super(sku, title, artist, year);

        validateFilename(fileName);
        this.fileName = fileName;
    }

    /**
     * create audio file object
     * @param sku the sku
     * @param title the title
     * @param artist the artist
     * @param year the year
     * @param fileName the name of the file
     * @param fileResolution the file resolution ie bytes per second
     */
    public AudioFile(final String sku, final String title,
                     final String artist, final int year,
                     final String fileName, final int fileResolution)
    {
        super(sku, title, artist, year);

        validateBytesPerSecond(fileResolution);
        this.fileResolution = fileResolution;

        validateFilename(fileName);
        this.fileName = fileName;
    }

    /**
     * @return the number of bytes per second
     */
    public int getFileResolution()
    {
        return fileResolution;
    }

    /**
     * @return the name of the file
     */
    public String getFileName()
    {
        return fileName;
    }

    /**
     * displays a simple message
     */
    @Override
    public void play()
    {
        System.out.println("the file is being played");
    }

    /**
     * displays a message claiming to delete a file
     * @param fileName the file to delete
     * @param path the file path
     */
    @Override
    public void delete(String path, String fileName)
    {
        System.out.println("delete file: " + fileName + "path: " + path);
    }

    /**
     * displays a message claiming to save a file
     * @param fileName the file to save
     * @param path the file path
     */
    @Override
    public void save(String path, String fileName)
    {
        System.out.println("save file: " + fileName + "path: " + path);
    }

    /**
     * set the number of bytes per second
     * @param fileResolution the number of bytes per second
     */
    public final void setFileResolution(int fileResolution)
    {
        validateBytesPerSecond(fileResolution);
        this.fileResolution = fileResolution;
    }

    /**
     * set the name of the file
     * @param fileName the name of the file
     */
    public final void setFileName(final String fileName)
    {
        validateFilename(fileName);
        this.fileName = fileName;
    }

    @Override
    public String getType() {
        return AUDIOFILE_STRING;
    }

    /**
     * check that the bytes per second to set is valid
     * @param bytesPerSecond the number of bytes per second
     * @throws IllegalArgumentException the number of bytes must be between
     * MINIMUM_BYTES and MAXIMUM_BYTES
     */
    public static void validateBytesPerSecond(int bytesPerSecond)
    {
        if(bytesPerSecond < MINIMUM_BYTES ||
                bytesPerSecond > MAXIMUM_BYTES)
        {
            throw new IllegalArgumentException("invalid file resolution");
        }
    }

    /**
     * check that the file name is valid
     * @param fileName the name of the file
     * @throws IllegalArgumentException file name can't be null or empty
     */
    public static void validateFilename(final String fileName)
    {
        if(fileName == null || fileName.isEmpty())
        {
            throw new IllegalArgumentException("file name can't be null or empty");
        }
    }

    /**
     * @return display the fields of this object
     */
    @Override
    public String toString()
    {
        return super.getSku() +
                "|" + super.getTitle() +
                "|" + super.getArtist() +
                "|" + super.getYear() +
                "|" + fileName +
                "|" + fileResolution;
    }
}
