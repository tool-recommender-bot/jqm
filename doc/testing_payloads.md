# Testing payloads

A this step the following artifacts exist:

* a JAR file containing the payload
* a descriptor XML file containing all the metadata

Moreover, this section makes the assumption that you have a working JQM engine at your disposal.
If this is not the case, please read [how to install JQM](install.md).

## Copy files

Place the two files inside JQM_INSTALL_DIR/jobs/xxxxx where xxxxx is a directory of your choice.
Please note that the name of this directory must be the same as the one inside the "filePath" tag from the XML.

If there have libraries to copy (pom.xml is not used), they must be placed inside a directory named "lib".

Example (with explicit libraries):
```bash
$JQM_DIR\
$JQM_DIR\jobs\
$JQM_DIR\jobs\myjob\myjob.xml
$JQM_DIR\jobs\myjob\myjob.jar
$JQM_DIR\jobs\myjob\lib\
$JQM_DIR\jobs\myjob\lib\mylib1.jar
$JQM_DIR\jobs\myjob\lib\mylib2.jar
```

> :grey_exclamation: the jar files can be replaced at will, without restarting anything as long as the class path does not change.
The class path will change, for example, when a library is added, or when a library is upgraded.

## Import the metadata

> :exclamation: this only has to be done the first time. Later, this is only ncessaeray if the XML changes.
Each time the XML is imported, it overwrites the previous values so it can also be done at will.

Open a command line (bash, powershell, ksh...) and run the following commands (adapt JQM_DIR and xxxx)

```PowerShell
cd $JQM_DIR
java -jar jqm.jar -importjobdef ./jobs/xxxxx/xxxx.xml
```

## Run the payload

This part can be run as many times as needed. (adapt the job name, it is the "name" attribute from the XML)

```PowerShell
java -jar jqm.jar -enqueue JOBNAME
```

The logs are inside JQM_ROOT/logs. The user may want to do "tail -f" (or "cat -Wait" in PowerShell) on these files
during tests.
