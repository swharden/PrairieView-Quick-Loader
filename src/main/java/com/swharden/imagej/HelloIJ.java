package com.swharden.imagej;

import java.io.Console;

import ij.ImagePlus;
import ij.gui.GenericDialog;
import ij.plugin.PlugIn;
import ij.text.TextWindow;
import ij.text.TextPanel;

public class HelloIJ implements PlugIn {
    protected ImagePlus image;

    private void Alert(String title, String message) {
        GenericDialog gd = new GenericDialog(title);
        gd.addMessage(message);
        gd.showDialog();
    }

    public void log(String s) {
        TextWindow logWindow = new TextWindow("Hello Log", "", 400, 250);
        TextPanel logPanel = logWindow.getTextPanel();
        logPanel.append(s);
    }

    @Override
    public void run(String arg) {
        Alert("Hello", "Hello, ImageJ!");
        log("logging this");
    }
}