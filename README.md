# PrairieView Quick Loader

**PrairieView Quick Loader is an ImageJ plugin for quickly loading large PrairieView TSeries folders as 5D hyperstacks.** Image metadata is read from XML using code from the [bioformats](https://github.com/ome/bioformats/blob/master/components/formats-gpl/src/loci/formats/in/PrairieMetadata.java) library, but performance is enhanced by limiting the number of times the filesystem is scanned and XML documents are parsed and interpreted.

<div align='center'>
<img src='dev/screenshot.png'>
</div>

## Installation

* [Stock ImageJ](https://imagej.nih.gov/ij/download.html) (not [Fiji](https://fiji.sc/)) is recommended for performance

* Download the plugin `.jar` from the [Releases page](https://github.com/swharden/ImageJ-QuickPV/releases/) 

* Place the `.jar` in ImageJ's `plugins/` folder and restart ImageJ

* Click `Plugins`, `Quick PrairieView Loader`, and select a TSeries XML file

## Benchmark

Time to load a TSeries with 99 sequences of 2-channel 31-slice stacks (6,138 TIFs, 776 MB) from a SSD:

* Bio-Formats Importer: 10 minutes and 17 seconds

* QuickPV: 58 seconds

* Single file TIF: <1 second

## Build from Source

* Get the [JDK](https://www.oracle.com/java/technologies/downloads/)

* Download [Maven](https://maven.apache.org/download.cgi) and extract it somewhere important

* Create `JAVA_HOME` environment variable with value `C:\Program Files\Java\jdk-17.0.2`

* Create `MAVEN_HOME` environment variable with value `C:\path\to\apache-maven-3.8.4`

* Add `C:\path\to\apache-maven-3.8.4\bin` to your list of system path variables

* Run `mvn -v` to verify Maven is setup correctly

* Run `mvn` in this folder to compile the plugin and put it in `target/`

## Automate Conversion

The following ImageJ macro can be used to automate loading XML files from a collection of PrairieView folders and save the data as a single file .tif in the References subfolder. This greatly improves loading times over network connections where loading thousands of individual files is extremely slow:

```java
function ProcessFolder(dir, folderName){
	
	// locate the XML file
    xmlFilePath = dir+folderName+"/"+folderName+".xml";
    if (!File.exists(xmlFilePath))
        exit("XML file does not exist:\n" + xmlFilePath);
        
    // determine output filenames
    outputTifPath = dir+folderName+"/References/"+folderName+".tif";
    outputPngPath = dir+folderName+"/References/"+folderName+"_proj.png";
    
    // check if analysis is required
    if (File.exists(outputTifPath)){
    	print("Skipping (output file exists)");
    	return;
    }
    
    // load source data
    print("Loading...");
    run("Quick PrairieView Loader", "select=[" + xmlFilePath + "]");
    
    // make channel 1 magenta instead of red
	Stack.setPosition(1,1,1);
	run("Magenta");
	
	// save the full stack
    print("Saving...");
	saveAs("Tiff", outputTifPath);

	// create and save a projection
	run("Z Project...", "projection=[Average Intensity]");
	run("RGB Color");
	saveAs("PNG", outputPngPath);
}

function ProcessSubFolders(dir){
	print("\\Clear");
	if (!endsWith(dir, "/"))
		dir = dir + "/";
		
	if (!File.isDirectory(dir))
	    exit("Folder not found:\n" + dir);
	        
	list = getFileList(dir);
	for (i=0; i<list.length; i++) {
	    if (!endsWith(list[i], "/"))
	        continue;
	    folderName = list[i].replace("/","");
	    
	    print("");
	    print("Processing " + i + " of " + list.length + ": " + folderName);
	    ProcessFolder(dir, folderName);
		close("*");
	}
}

ProcessSubFolders("C:\\folder\\containing\\tseries\\subfolders");
```
