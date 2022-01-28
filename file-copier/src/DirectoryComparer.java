/*
 * A class for comparing directories and high level file information on files in a directory.
 * */

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DirectoryComparer {

    /**
     * Instantiating a new object of this class using provided directory path to
     * reference a source directory for later comparison.
     *
     * The String pattern containing a regular expression is used to create a
     * global Pattern object, which is used to match / filter files of interest.
     *
     * Only files, which match a provided file extension like ".txt" are
     * regarded in later comparisons.
     *
     * @param dirPath
     *            a path for a directory, which should be used as the source for
     *            comparisons.
     * @param pattern
     *            a Pattern object for identifying files of interest. A files
     *            name has to match the pattern.
     * @param fileExtension
     *            a file extension, which should be used as filter for file
     *            types of interest.
     */
    public DirectoryComparer(String dirPath, String pattern, final String fileExtension) {
        this.srcDirPath = dirPath;
        this.fileExtension = fileExtension;

        /*
         * Compile a Pattern for searching matches in a way that is optimized
         * for performance.
         */
        this.keyPatternStr = pattern;
        try {
            this.keyPattern = Pattern.compile(this.keyPatternStr);
        } catch (PatternSyntaxException ex) {
            this.keyPattern = null;
        }
        /* Reference the source directory as base line for later compare. */
        this.srcDir = new File(this.srcDirPath);

        /*
         * A FilenamFilter object implementing an accept method for provided
         * file type extension.
         */
        if (srcDir.exists()) {
            this.srcFiles = readFileNames(srcDir, new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.endsWith(fileExtension);
                }
            });
        }
    }

    /**
     * Validating, if the source directory is existing and if the global key
     * Pattern for file names has been compiled properly.
     *
     * @return true, if an object of this class is ready for use, else false.
     */
    public boolean isReady() {
        boolean ret;

        /* Source directory has to exist. */
        ret = srcDir.exists();
        /*
         * Provided regular expression has to be successfully compiled by
         * Pattern class.
         */
        ret = ret && (keyPattern != null);

        return ret;
    }

    /**
     * Returning a collection holding the file names matching the global Pattern
     * object and the provided FilenameFilter.
     *
     * @param srcDir
     *            a path to a source directory you want to inspect for file
     *            names matching the global Pattern.
     * @param fileFilter
     *            a FilenameFilter determining the file type of interest.
     * @return a HashMap<String, String> holding Strings from file names matched
     *         by the global Pattern object as keys and the whole file name per
     *         file in source directory as value.
     */
    private HashMap<String, String> readFileNames(File srcDir,
                                                  FilenameFilter fileFilter) {
        HashMap<String, String> ret = new HashMap<String, String>();
        String[] fileNames;
        Matcher keyMatcher;

        /*
         * Get a String array of all files in provided directory. The array only
         * holds a files name, not its full path.
         */
        fileNames = srcDir.list(fileFilter);

        /*
         * Walk through the array of available file names and add corresponding
         * filePath to a returned HashMap in case the file names match the
         * current global Pattern.
         */
        for (int i = 0; i < fileNames.length; i++) {
            keyMatcher = keyPattern.matcher(fileNames[i]);
            if (keyMatcher.find()) {
                ret.put(keyMatcher.group(), srcDir.getPath() + File.separator
                        + fileNames[i]);
            }
        }
        fileNames = null;

        return ret;
    }

    /**
     * Getting a collection containing all file names, which are in synch
     * between two directories compared before using this class.
     *
     * @return - a collection of type HashSet<String> containing file names. The
     *         file names in the returned Set can be used as keys for the
     *         HasMaps returned by the methods getSrcFileNames as well as
     *         getTrgFileNames.
     */
    public HashSet<String> getCommmonFileNames() {
        return this.commonFiles;
    }

    /**
     * Getting a collection of file names matching provided regular expression
     * used in directory comparison from the source directory.
     *
     * The file names matching a regular expression are used as key Strings. The
     * corresponding file path is stored as value in a pair.
     *
     * @return - a collection of type HasMap<String, String> providing a
     *         matching between matched file names and file paths in the source
     *         directory.
     */
    public HashMap<String, String> getSrcFileNames() {
        return this.srcFiles;
    }

    /**
     * Getting a collection of file names matching provided regular expression
     * used in directory comparison from the target directory.
     *
     * The file names matching a regular expression are used as key Strings. The
     * corresponding file path is stored as value in a pair.
     *
     * @return - a collection of type HasMap<String, String> providing a
     *         matching between matched file names and file paths in the target
     *         directory.
     */
    public HashMap<String, String> getTrgFileNames() {
        return this.compFiles;
    }

    /**
     * Comparing all files, which are matching global FileFilter object as well
     * as global Pattern object with files in directory referenced by global
     * object 'srcDir'.
     *
     * Both directories have to exist. Else no comparison is applied.
     *
     * @param trgDir
     *            a path to a directory, which files should be compared to files
     *            in directory referenced by global object 'srcDir'.
     * @return A Set containing all keys of the global HashMap object
     *         'srcFiles', which could not be found in a directory referenced
     *         under provided path 'dirPath'.
     */
    public Set<String> compareDir(String trgDir) {
        Set<String> ret = null;
        /**
         * Free global resources and assure that they are reset before starting
         * processing new directories.
         */
        commonFiles = null;
        compDir = null;
        compFiles = null;

        compDirPath = trgDir;
        compDir = new File(compDirPath);
        if (compDir.exists()) {
            compFiles = readFileNames(compDir, new FilenameFilter() {
                public boolean accept(File directory, String fileName) {
                    return fileName.endsWith(fileExtension);
                }
            });
            if (srcDir.exists()) {
                ret = new HashSet<String>(srcFiles.keySet());
                ret.removeAll(compFiles.keySet());

                commonFiles = new HashSet<String>(srcFiles.keySet());
                commonFiles.removeAll(ret);
            }
        }

        return ret;
    }

    private final String fileExtension;
    private String keyPatternStr;
    private Pattern keyPattern;
    private HashMap<String, String> srcFiles;
    private HashMap<String, String> compFiles;
    private HashSet<String> commonFiles;
    private File srcDir;
    private File compDir;
    private String srcDirPath;
    private String compDirPath;
}