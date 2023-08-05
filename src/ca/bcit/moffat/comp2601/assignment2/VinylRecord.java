package ca.bcit.moffat.comp2601.assignment2;

import java.util.ArrayList;
import java.util.List;

/**
 * a model of a vinyl record
 * @author james moffat
 * @version 1
 */
public class VinylRecord extends MusicMedia
{

    public static final String[]      VINYLRECORD_FIELDS = {"SKU", "Title", "Artist", "Year", "Number of tracks", "Size (Inches)", "Weight (Grams)"};
    public final static String        VINYLRECORD_PREFIX;
    public final static String        VINYLRECORD_STRING;

    public static final List<Integer> LARGE_RECORD_WEIGHTS;
    public static final int           UPPER_CAPACITY;
    public static final int           LOWER_CAPACITY;
    public static final int           SMALL_RECORD_SIZE  = 7;
    public static final int           MEDIUM_RECORD_SIZE = 10;
    public static final int           LARGE_RECORD_SIZE  = 12;
    public static final int           MEDIUM_RECORD_WEIGHT;
    public static final int           SMALL_RECORD_WEIGHT;;
    public static final int           STANDARD_WEIGHT;
    public static final int           DEFAULT_SIZE;

    private int numberOfTracks;
    private int sizeInInches;
    private int weight;

    static
    {
        LARGE_RECORD_WEIGHTS = new ArrayList<>() {{
            add(140);
            add(180);
            add(200);
        }};
        MEDIUM_RECORD_WEIGHT = 100;
        SMALL_RECORD_WEIGHT  = 40;
        DEFAULT_SIZE         = 12;
        STANDARD_WEIGHT      = 140;
        UPPER_CAPACITY       = 20;
        LOWER_CAPACITY       = 0;
        VINYLRECORD_PREFIX = "vr";
        VINYLRECORD_STRING = "VinylRecord";
    }

    {
        numberOfTracks = 0;
    }

    /**
     * create a new vinyl record object using default values given
     * by instance initializer
     */
    public VinylRecord()
    {
        super();
    }

    /**
     * create a new vinyl record
     * @param sku the sku
     * @param artist the artist
     * @param numberOfTracks the total tracks
     * @param title the title
     * @param year the year
     */
    public VinylRecord(final String sku, final String title,
                       final String artist, final int year,
                       final int numberOfTracks)
    {
        this(sku, title, artist, year, numberOfTracks, STANDARD_WEIGHT, DEFAULT_SIZE);
    }

    /**
     * create a new vinyl record
     * @param title the title of the song
     * @param artist the artist
     * @param numberOfTracks the total tracks
     * @param sizeInInches the size of the record in inches
     * @param weight the weight in grams
     */
    public VinylRecord(final String sku, final String title, final String artist,
                       final int year, final int numberOfTracks, final int weight,
                       final int sizeInInches)
    {
        super(sku, title, artist, year);

        validateTotalTracks(numberOfTracks);
        this.numberOfTracks = numberOfTracks;

        validateSizeInInches(sizeInInches);
        this.sizeInInches = sizeInInches;

        validateWeight(weight);
        this.weight = weight;
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
                "|" + numberOfTracks +
                "|" + sizeInInches +
                "|" + weight;
    }

    @Override
    public String getType() {
        return VINYLRECORD_STRING;
    }

    /**
     * @return the total songs on the vinyl record
     */
    public int getNumberOfTracks()
    {
        return numberOfTracks;
    }

    /**
     * @return the size of the record in inches
     */
    public int getSizeInInches()
    {
        return sizeInInches;
    }

    /**
     * @return the weight of the record in grams
     */
    public int getWeight()
    {
        return weight;
    }

    /**
     * set the size of the vinyl record
     * @param sizeInInches the size of the record in inches
     */
    public final void setSizeInInches(final int sizeInInches)
    {
        validateSizeInInches(sizeInInches);
        this.sizeInInches = sizeInInches;
    }

    /**
     * set the number of songs on the compact disk
     * @param totalSongs the total number of songs
     */
    public final void setNumberOfTracks(final int totalSongs)
    {
        validateTotalTracks(totalSongs);
        this.numberOfTracks = totalSongs;
    }

    /**
     * set the weight of the record
     * @param weight the weight in grams
     */
    public final void setWeight(final int weight)
    {
        validateWeight(weight);
        this.weight = weight;
    }

    /**
     * display a simple message
     */
    @Override
    public void play()
    {
        System.out.println("the vinyl is being played");
    }

    /**
     * set the number of songs on the compact disk
     * @param totalTracks the total number of tracks
     * @throws IllegalArgumentException the number of songs must be less than
     * MAXIMUM_CAPACITY and greater than MINIMUM_CAPACITY
     */
    public static void validateTotalTracks(final int totalTracks)
    {
        if(totalTracks < LOWER_CAPACITY
                || totalTracks > UPPER_CAPACITY) {
            throw new IllegalArgumentException("invalid number of tracks");
        }
    }

    /**
     * check that the size being set is valid
     * @param sizeInInches the size of the record in inches
     * @throws IllegalArgumentException the size must be SMALL_RECORD or
     * MEDIUM_RECORD or LARGE_RECORD
     */
    public static void validateSizeInInches(final int sizeInInches)
    {
        if(sizeInInches != SMALL_RECORD_SIZE
                && sizeInInches != MEDIUM_RECORD_SIZE
                && sizeInInches != LARGE_RECORD_SIZE)
        {
            throw new IllegalArgumentException("invalid size");
        }
    }

    /**
     * check that the weight of the record is valid
     * @param weightInGrams the weight in grams
     * @throws IllegalArgumentException the weight must be appropriate to
     * the size of the record
     */
    public final void validateWeight(final int weightInGrams)
    {
        switch(sizeInInches) {
            case LARGE_RECORD_SIZE:
                if(!(LARGE_RECORD_WEIGHTS.contains(weightInGrams)))
                {
                    throw new IllegalArgumentException("invalid weight");
                }
            break;
            case MEDIUM_RECORD_SIZE:
                if(weightInGrams != MEDIUM_RECORD_WEIGHT)
                {
                    throw new IllegalArgumentException("invalid weight");
                }
                break;
            case SMALL_RECORD_SIZE:
                if(weightInGrams != SMALL_RECORD_WEIGHT)
                {
                    throw new IllegalArgumentException("invalid weight");
                }
                break;
            default:
                throw new IllegalArgumentException("can't set weight because incorrect size");
        }
    }
}
