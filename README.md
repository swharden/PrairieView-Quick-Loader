# QuickPV

**QuickPV is an ImageJ plugin for quickly loading large PrairieView folders as 5D hyperstacks.** Performance is enhanced by limiting the number of times the filesystem is scanned and XML documents are parsed and read.

<div align='center'>
<img src='dev/screenshot.png'>
</div>

### Installation

* Get the latest `.jar` from the [Releases page](https://github.com/swharden/ImageJ-QuickPV/releases/)
* Move it into ImageJ's `plugins` folder
* In ImageJ click `Plugins` and select `Quick PrairieView Loader`

### What About the Bio-Formats Importer?

The [Bio-Formats ImageJ plugin](https://docs.openmicroscopy.org/bio-formats/5.8.2/users/imagej/load-images.html) does a good job at loading PrairieView folders, but because of how the importer works it searches for and re-parses XML and ENV files repeatedly for every TIF file loaded. For recordings with tens of thousands of TIF files, this is extremely slow. 

Can the Bio-Formats importer be improved? I'm not sure. I looked into it, but I think this is an architectural issue related to how the abstract importers operate. I think they're locked into re-loading everything when each TIF is loaded. See [`PrairieReader.java`](https://github.com/ome/bioformats/blob/master/components/formats-gpl/src/loci/formats/in/PrairieReader.java) and [`PrairieMetadata.java`](https://github.com/ome/bioformats/blob/master/components/formats-gpl/src/loci/formats/in/PrairieMetadata.java) for details.