package com.swharden.imagej;

import java.io.File;
import java.nio.file.Paths;
import java.security.Timestamp;

import ij.IJ;
import ij.ImagePlus;
import ij.measure.Calibration;
import ij.plugin.HyperStackConverter;
import ij.plugin.ImagesToStack;
import ij.plugin.PlugIn;

public class QuickPV implements PlugIn {

    @Override
    public void run(String arg) {

        long timeStart = System.currentTimeMillis();

        String xmlFilePath = IJ.getFilePath("Select XML file");
        if (xmlFilePath == null)
            return;

        String folderPath = new File(xmlFilePath).getParent();

        PVExperiment exp;

        try {
            exp = new PVExperiment(xmlFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            IJ.log("crashed loading PV");
            return;
        }

        ImagePlus[] arrayOfImages = new ImagePlus[exp.ImageCount];

        IJ.showStatus(String.format("Loading %s PrairieView images...", exp.ImageCount));
        for (int i = 0; i < exp.ImageCount; i++) {
            IJ.showProgress(i, exp.ImageCount);
            if (i < exp.FilePaths.length) {
                String filePath = folderPath + "/" + exp.FilePaths[i];
                arrayOfImages[i] = new ImagePlus(filePath);
            } else {
                int width = arrayOfImages[0].getWidth();
                int height = arrayOfImages[0].getHeight();
                int depth = arrayOfImages[0].getBitDepth();
                arrayOfImages[i] = IJ.createImage("empty", "composite", width, height, depth);
            }
        }

        IJ.showStatus("Creating hyperstack...");

        ImagePlus imp = ImagesToStack.run(arrayOfImages);
        imp = HyperStackConverter.toHyperStack(imp, exp.ChannelCount, exp.StackDepth, exp.TimePoints, "composite");
        imp.setTitle(Paths.get(folderPath).getFileName().toString());

        Calibration cal = new Calibration();
        cal.pixelWidth = exp.PixelWidth;
        cal.pixelHeight = exp.PixelHeight;
        cal.pixelDepth = exp.PixelDepth;
        cal.frameInterval = exp.FrameInterval;
        cal.setUnit("micron");
        imp.setCalibration(cal);

        imp.show();

        long timeElapsed = System.currentTimeMillis() - timeStart;
        IJ.showStatus(String.format("QuickPV loaded %d images in %.3f sec", exp.ImageCount, timeElapsed / 1000.0));
    }
}
