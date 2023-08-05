package ca.bcit.moffat.comp2601.assignment2;

/**
 * This is an abstract model of music media
 * @author james moffat
 * @version 1
 */
public abstract class MusicMedia
{
    public static final String DEFAULT_SONG_TITLE;
    public static final String DEFAULT_ARTIST;
    public static final String DEFAULT_SKU;
    public static final int    DEFAULT_YEAR;
    public static final int    CURRENT_YEAR;
    public static final int    FIRST_YEAR;

    private String title;
    private String artist;
    private String sku;
    private int    year;

    static
    {
        DEFAULT_SONG_TITLE = "untitled";
        DEFAULT_ARTIST     = "unknown";
        CURRENT_YEAR       = 2022;
        FIRST_YEAR         = 1860;
        DEFAULT_YEAR       = 2022;
        DEFAULT_SKU        = "none";
    }

    /**
     * initialize object with default values
     */
    public MusicMedia()
    {
        this(DEFAULT_SKU, DEFAULT_SONG_TITLE, DEFAULT_ARTIST, DEFAULT_YEAR);
    }

    /**
     * initializes object
     * @param title title of media
     * @param artist artist name
     */
    public MusicMedia(final String sku, final String title, final String artist)
    {
        this(sku, title, artist, DEFAULT_YEAR);
    }

    /**
     * initializes object
     * @param sku the unique sku identifier
     * @param title the title of media
     * @param artist artist name
     */
    public MusicMedia(final String sku, final String title, final String artist, int year)
    {
        validateTitle(title);
        this.title = title;

        validateArtist(artist);
        this.artist = artist;

        validateSku(sku);
        this.sku = sku;

        validateYear(year);
        this.year = year;
    }

    /**
     * @return display the fields of this object
     */
    @Override
    public String toString()
    {
        return "{title: " + title
                + ", artist: " + artist
                + "}";
    }

    public abstract String getType();

    /**
     * @return the artist
     */
    public String getArtist()
    {
        return artist;
    }

    /**
     * @return sku
     */
    public String getSku()
    {
        return sku;
    }

    /**
     * @return the title
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return the year
     */
    public int getYear()
    {
        return year;
    }

    /**
     * play media
     */
    public abstract void play();

    /**
     * set the artist name
     * @param artist the artist name
     */
    public final void setArtist(String artist)
    {
        validateArtist(artist);
        this.artist = artist;
    }

    /**
     * set sku
     * @param sku the sku
     */
    public final void setSku(final String sku)
    {
        validateSku(sku);
        this.sku = sku;
    }

    /**
     * set the media title
     * @param title the title
     */
    public final void setTitle(String title)
    {
        validateTitle(title);
        this.title = title;
    }


    /**
     * set the year
     * @param year the year
     */
    public final void setYear(int year)
    {
        validateYear(year);
        this.year = year;
    }

    /**
     * validate the artist
     * @param artist artist name
     * @throws IllegalArgumentException the artist can't be null or empty
     */
    public static void validateArtist(final String artist)
    {
        if(artist == null || artist.isEmpty()) {
            throw new IllegalArgumentException("invalid artist");
        }
    }

    /**
     * validate sku
     * @param sku the sku
     * @throws IllegalArgumentException sku can't be null or empty
     */
    public static void validateSku(final String sku)
    {
        if(sku == null || sku.isEmpty())
        {
            throw new IllegalArgumentException("invalid sku");
        }
    }

    /**
     * check that the title of the song is valid
     * @param title the title of the song
     * @throws IllegalArgumentException the title can't be null or empty
     */
    public static void validateTitle(String title)
    {
        if(title == null || title.isEmpty()) {
            throw new IllegalArgumentException("invalid title");
        }
    }

    /**
     * validate the year
     * @param year the year
     * @throws IllegalArgumentException year must be between FIRST_YEAR and
     * CURRENT_YEAR
     */
    public static void validateYear(final int year)
    {
        if(year < FIRST_YEAR || year > CURRENT_YEAR)
        {
            throw new IllegalArgumentException("invalid year");
        }
    }

}
