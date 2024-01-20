package zigy.playeranimatorapi.utils.forge;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.compress.utils.FileNameUtils;
import org.tomlj.Toml;
import org.tomlj.TomlParseResult;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlatformImpl {
    public static boolean isModLoaded(String modID) {
        if (ModList.get() != null) {
            return ModList.get().isLoaded(modID);
        }
        else if (FMLEnvironment.production) {
            return true;
        }
        else {
            for (File file : listFilesForFolder(FMLPaths.MODSDIR.get().toFile())) {
                if (FileNameUtils.getExtension(file.getAbsolutePath()).equals("jar")) {
                    String inputFile = "jar:file:/" + file.getAbsolutePath() + "META-INF/mods.toml";
                    if (testIfModExists(inputFile, modID)) {
                        return true;
                    }
                    for (File file2 : listFilesForFolder(new File(file.getAbsolutePath() + "META-INF/jars"))) {
                        String inputFile2 = "jar:file:/" + file2.getAbsolutePath() + "META-INF/mods.toml";
                        if (testIfModExists(inputFile2, modID)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }

    public static boolean testIfModExists(String inputFile, String modID) {
        if (inputFile.startsWith("jar:")) {
            try {
                URL inputURL = new URL(inputFile);
                JarURLConnection conn = (JarURLConnection) inputURL.openConnection();
                InputStream in = conn.getInputStream();

                TomlParseResult result = Toml.parse(in);
                return Objects.equals(result.get("modId"), modID);
            } catch (IOException ignore) {
            }
        }
        return false;
    }

    public static List<File> listFilesForFolder(final File folder) {
        List<File> list = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                list.add(fileEntry);
            }
        }

        return list;
    }
}
