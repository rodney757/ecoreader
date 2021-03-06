package run;

import imageMediation.image;
import modsDigester.Mods;
import modsDigester.mvzTaccPage;
import modsDigester.mvzSection;
import modsDigester.modsFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Build images at various resolutions for use in the
 * ecoReader library.  We don't want to run this so files get put into the repo as there will
 * be many images.
 */
public class processImages {

    //static String xmlFile = "file:////Users/jdeck/IdeaProjects/ecoreader/ecoreader/docs/mvz/mods/httpweb.corral.tacc.utexas.eduMVZfieldnotesFitchHSv674-mods.xml";


    /**
     * Main method for command-line testing
     *
     * @param args
     */
    public static void main(String[] args) {

        String directoryPath = args[0];
        File dir = new File(directoryPath);
        ArrayList<String> filesAsStrings = new ArrayList<String>();
        if (dir.isDirectory()) {
            for (File child : dir.listFiles()) {
                // Create mods object to hold MODS data
                Mods mods = new modsFactory("file:///" + child.getAbsolutePath()).getMods(false);

                writeImagesForAllSections(mods);
            }
        } else {
            // Create mods object to hold MODS data
            Mods mods = new modsFactory("file:///" + directoryPath).getMods(false);

            writeImagesForAllSections(mods);
        }


    }

    public static void writeImagesForAllSections(Mods mods) {

        // Loop through each section for this XML file
        Iterator sectionsIt = mods.getSections().iterator();
        System.out.println("processing images for " + mods.getFilename() + "\n");

        while (sectionsIt.hasNext()) {

            mvzSection section = (mvzSection) sectionsIt.next();
            System.out.println("\t" + section.getTitle());
            Iterator pagesIt = section.getPages().iterator();
            // Loop through pages
            while (pagesIt.hasNext()) {
                mvzTaccPage page = (mvzTaccPage) pagesIt.next();
                System.out.println("\t\t" + page.getFullPath() + ";" + page.getImageFileInputName());
                try {
                    image image = new image(page);
                    if (!image.getExists()) {
                        image.writeAllScales();
                    } else {
                        System.out.println("\t\tEXISTS, skipping");
                    }
                    image.close();
                } catch (Exception e) {
                    System.out.println("ERROR processing " + page.getFullPath());
                }
            }
        }

    }

}
