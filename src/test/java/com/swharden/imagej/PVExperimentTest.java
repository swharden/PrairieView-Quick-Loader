package com.swharden.imagej;

import org.junit.Assert;
import org.junit.Test;

public class PVExperimentTest {
    @Test
    public void xml_image_dimensions_match_expectation() throws Exception {
        String xmlFilePath = "C:/Users/scott/Documents/TSeries-05112022-1607-1954/TSeries-05112022-1607-1954/TSeries-05112022-1607-1954.xml";
        PVExperiment exp = new PVExperiment(xmlFilePath);
        Assert.assertEquals(1, exp.StackDepth);
        Assert.assertEquals(2, exp.ChannelCount);
        Assert.assertEquals(929, exp.TimePoints);

        Assert.assertNotNull(exp.FilePaths);
        Assert.assertEquals(exp.TimePoints * exp.ChannelCount, exp.FilePaths.length);
    }
}