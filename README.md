# QuickPV

**QuickPV is an ImageJ plugin for quickly loading large PrairieView TSeries folders as 5D hyperstacks.** Image metadata is read from XML using code from the [bioformats](https://github.com/ome/bioformats/blob/master/components/formats-gpl/src/loci/formats/in/PrairieMetadata.java) library, but performance is enhanced by limiting the number of times the filesystem is scanned and XML documents are parsed and interpreted.

<div align='center'>
<img src='dev/screenshot.png'>
</div>

## Installation

* Get the latest `.jar` from the [Releases page](https://github.com/swharden/ImageJ-QuickPV/releases/) 

* Place the `.jar` in ImageJ's `plugins/` folder and restart ImageJ

* Click `Plugins`, `Quick PrairieView Loader`, and select a TSeries XML file