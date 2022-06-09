package com.swharden.imagej;

import org.junit.Assert;
import org.junit.Test;

public class PVExperimentTest {
    @Test
    public void single_plane_tseries() throws Exception {
        String xmlFilePath = "C:/Users/scott/Documents/TSeries-05112022-1607-1954 tseries big/TSeries-05112022-1607-1954.xml";
        PVExperiment exp = new PVExperiment(xmlFilePath);
        Assert.assertEquals(1, exp.StackDepth);
        Assert.assertEquals(2, exp.ChannelCount);
        Assert.assertEquals(929, exp.TimePoints);

        Assert.assertNotNull(exp.FilePaths);
        Assert.assertEquals(exp.ImageCount, exp.FilePaths.length);
    }

    @Test
    public void zstack_tseries() throws Exception {
        String xmlFilePath = "C:/Users/scott/Documents/TSeries-05242022-1034-1967 zstacks/TSeries-05242022-1034-1967.xml";
        PVExperiment exp = new PVExperiment(xmlFilePath);
        Assert.assertEquals(31, exp.StackDepth);
        Assert.assertEquals(1, exp.ChannelCount);
        Assert.assertEquals(7, exp.TimePoints);

        Assert.assertNotNull(exp.FilePaths);
        Assert.assertEquals(186, exp.FilePaths.length); // partial recording
    }
}