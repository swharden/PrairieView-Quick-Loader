package com.swharden.imagej;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.swharden.imagej.PrairieMetadata.PFile;

public class PVExperiment {

    public int ChannelCount;
    public int StackDepth;
    public int TimePoints;
    public int ImageCount;
    public String[] FilePaths;

    public PVExperiment(String xmlFilePath) throws Exception {
        File xmlFile = new File(xmlFilePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = dbFactory.newDocumentBuilder();
        Document xml = builder.parse(xmlFile);

        PrairieMetadata meta = new PrairieMetadata(xml, null, null);

        if (!meta.getFirstSequence().isTimeSeries()) {
            throw new Exception("only time series are supported");
        }

        if (meta.getSequences().size() == 1) {
            // TSeries of a single plane
            StackDepth = 1;
            ChannelCount = meta.getActiveChannels().length;
            TimePoints = meta.getFirstSequence().getIndexCount();
            ImageCount = TimePoints * ChannelCount;
            FilePaths = GetFilenamesTSeries(meta);
        } else {
            // multiple sequences
            throw new Exception("only single sequence TSeries are supported");
        }
    }

    private static String[] GetFilenamesTSeries(PrairieMetadata meta) {

        int cycle = meta.getFirstSequence().getCycle();
        int iStart = meta.getFirstSequence().getIndexMin();
        int iEnd = meta.getFirstSequence().getIndexMax();
        int[] channels = meta.getActiveChannels();

        List<String> filenames = new ArrayList<String>();

        for (int i = iStart; i <= iEnd; i++) {
            for (int channel : channels) {
                PFile file = meta.getFile(cycle, i, channel);
                filenames.add(file.getFilename());
            }
        }

        return filenames.toArray(new String[0]);
    }
}
