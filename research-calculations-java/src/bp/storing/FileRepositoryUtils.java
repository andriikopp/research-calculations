package bp.storing;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileOwnerAttributeView;
import java.text.SimpleDateFormat;

/**
 * File repository utilities.
 *
 * @author Andrii Kopp
 */
public class FileRepositoryUtils {
    private static String gitRepositoryPath = "src/main/resources/repository";

    /**
     * Returns the creation time of a file.
     *
     * @param fileName - the file from repository.
     * @return the creation time of a file.
     */
    public static String getFileCreatedTime(String fileName) {
        String[] classPath = fileName.split("\\.");
        String className = gitRepositoryPath + "/files/" + classPath[classPath.length - 1] + ".java";

        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(
                    Files.readAttributes(Paths.get(className), BasicFileAttributes.class).creationTime().toMillis());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the last modified time of a file.
     *
     * @param fileName - the file from repository.
     * @return the last modified time of a file.
     */
    public static String getFileLastModifiedTime(String fileName) {
        String[] classPath = fileName.split("\\.");
        String className = gitRepositoryPath + "/files/" + classPath[classPath.length - 1] + ".java";

        try {
            return new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(
                    Files.readAttributes(Paths.get(className), BasicFileAttributes.class).lastModifiedTime().toMillis());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the name of a file's owner.
     *
     * @param fileName - the file from repository.
     * @return the name of a file's owner.
     */
    public static String getFileOwnerName(String fileName) {
        String[] classPath = fileName.split("\\.");
        String className = gitRepositoryPath + "/files/" + classPath[classPath.length - 1] + ".java";

        try {
            return Files.getFileAttributeView(Paths.get(className), FileOwnerAttributeView.class).getOwner().getName();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getGitRepositoryPath() {
        return gitRepositoryPath;
    }

    public static void setGitRepositoryPath(String gitRepositoryPath) {
        FileRepositoryUtils.gitRepositoryPath = gitRepositoryPath;
    }
}
