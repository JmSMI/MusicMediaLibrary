package ca.bcit.moffat.comp2601.assignment2;

/**
 * This class represent a disk that stores music
 * @author james
 * @version 1
 */
public class CompactDisc extends MusicMedia
{
    public static final String[] COMPACTDISC_FIELDS = {"SKU", "Title", "Artist", "Year", "Number of tracks"};
    public static final String   COMPACTDISC_PREFIX;
    public static final String   COMPACTDISC_STRING;
    public static final String   READING_METHOD;

    public static final int     MINIMUM_CAPACITY;
    public static final int     MAXIMUM_CAPACITY;

    private int numberOfTracks;

    static
    {
        READING_METHOD     = "laser";
        COMPACTDISC_PREFIX = "cd";
        COMPACTDISC_STRING = "CompactDisc";
        MINIMUM_CAPACITY   = 0;
        MAXIMUM_CAPACITY   = 150;
    }

    {
        numberOfTracks = 0;
    }

    /**
     * create new compact disk using default values given by super
     * and initializer
     */
    public CompactDisc()
    {
        super();
    }

    /**
     * create new compact disk
     * @param sku the sku
     * @param title the title
     * @param artist the artist
     * @param year the year
     * @param numberOfTracks the total tracks
     */
    public CompactDisc(final String sku, final String title,
                       final String artist, final int year,
                       final int numberOfTracks)
    {
        super(sku, title, artist, year);

        validateNumberOfTracks(numberOfTracks);
        this.numberOfTracks = numberOfTracks;
    }

    /**
     * @return the total number of tracks on the compact disk
     */
    public int getNumberOfTracks()
    {
        return numberOfTracks;
    }

    /**
     * set the number of tracks on the disk
     * @param numberOfTracks total number of songs
     */
    public final void setNumberOfTracks(final int numberOfTracks)
    {
        validateNumberOfTracks(numberOfTracks);
        this.numberOfTracks = numberOfTracks;
    }

    @Override
    public String getType() {
        return COMPACTDISC_STRING;
    }

    /**
     * a simple message
     */
    @Override
    public void play()
    {
        System.out.println("the CD is being played");
    }

    /**
     * validate the total number of songs that the disk stores
     * @param totalTracks total number of songs
     * @throws IllegalArgumentException the number of tracks must be less than
     * MAXIMUM_CAPACITY and greater than MINIMUM_CAPACITY
     */
    public static void validateNumberOfTracks(final int totalTracks)
    {
        if(totalTracks < MINIMUM_CAPACITY ||
        totalTracks > MAXIMUM_CAPACITY)
        {
            throw new IllegalArgumentException("invalid number of tracks");
        }
    }

    /**
     * Show all the object fields
     */
    @Override
    public String toString()
    {
        return super.getSku() +
                "|" + super.getTitle() +
                "|" + super.getArtist() +
                "|" + super.getYear() +
                "|" + numberOfTracks;
    }
}
