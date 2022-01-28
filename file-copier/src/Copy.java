import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class Copy {

    public void copyFiles(Map<String, File> files, String srcDir, String checkDir) throws IOException {
        Iterator<String> i = files.keySet().iterator();
        while(i.hasNext()){
            String n = i.next();
            File f = files.get(n);
            System.out.println(f.getAbsolutePath());
            String fullPathAndName = f.getAbsolutePath();
            String fullDestination = fullPathAndName.replace(srcDir, checkDir);
            File d = new File(fullDestination);
            System.out.println("About to copy: " + fullPathAndName  + " to " + fullDestination);
            FileUtils.copyFile(f, d);
        }
    }
    
}
