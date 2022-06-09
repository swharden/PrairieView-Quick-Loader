package com.swharden.imagej;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

import com.swharden.imagej.PrairieMetadata.PFile;
import com.swharden.imagej.PrairieMetadata.Sequence;

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

        if (meta.getCycleCount() == 1) {
            // TSeries of a single plane
            StackDepth = 1;
            ChannelCount = meta.getActiveChannels().length;
            TimePoints = meta.getFirstSequence().getIndexCount();
            ImageCount = StackDepth * TimePoints * ChannelCount;
            FilePaths = GetFilenamesTSeries(meta);
        } else {
            // Repeated ZSeries
            StackDepth = meta.getFirstSequence().getIndexCount();
            ChannelCount = meta.getActiveChannels().length;
            TimePoints = meta.getCycleCount();
            ImageCount = StackDepth * TimePoints * ChannelCount;
            FilePaths = GetFilenamesTZSeries(meta);
        }
    }

    private static String[] GetFilenamesTSeries(PrairieMetadata meta) {

        List<String> filenames = new ArrayList<String>();

        int[] channels = meta.getActiveChannels();

        int cycle = meta.getFirstSequence().getCycle();
        int iStart = meta.getFirstSequence().getIndexMin();
        int iEnd = meta.getFirstSequence().getIndexMax();

        for (int i = iStart; i <= iEnd; i++) {
            for (int channel : channels) {
                PFile file = meta.getFile(cycle, i, channel);
                filenames.add(file.getFilename());
            }
        }

        return filenames.toArray(new String[0]);
    }

    private static String[] GetFilenamesTZSeries(PrairieMetadata meta) {

        List<String> filenames = new ArrayList<String>();

        int[] channels = meta.getActiveChannels();

        for (Sequence sequence : meta.getSequences()) {
            int iStart = sequence.getIndexMin();
            int iEnd = sequence.getIndexMax();
            int cycle = sequence.getCycle();
            for (int i = iStart; i <= iEnd; i++) {
                for (int channel : channels) {
                    PFile file = meta.getFile(cycle, i, channel);
                    filenames.add(file.getFilename());
                }
            }
        }

        return filenames.toArray(new String[0]);
    }
}
