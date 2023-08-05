package ca.bcit.moffat.comp2601.assignment2;

/**
 * an interface for files that can be deleted and saved
 * @author james
 * @version 1
 */
public interface FileManager
{

    /**
     * save something
     * @param fileName name of file
     * @param path the path of the file
     */
    void save(final String path, final String fileName);

    /**
     * delete something
     * @param fileName name of file
     */
    void delete(final String path, final String fileName);
}
