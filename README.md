<h2>SeAMKTimeTableReader</h2>
Simple script to filter SeAMK time tables with only courses you have


This script allows you to easily download mangas from mangafox.me. You can also download a specific volume or a specific chapter.

This script allows you to filter SeAMK time tables and create a simple pdf file with your courses ordered by day.

In order to use this script, you must have at least <b>Java Runtime Environment 1.5</b> installed on your computer.<br/>
The script can be direclty downloaded from this repository's target directory on GitHub.


<h3>Syntax and Usage</h3>

In order to this script to work,make sure there is a plain text file called <b>not_my_courses.txt.</b> containing <b>ALL THE COURSES YOU DON'T HAVE</b> (one course per line) in the same directory as the jar file.
To run it, just double click on it. One pdf file per time table is created in the same directory.

You can also run form the terminal with 
<code>java -jar SeAMK Timetable Reader.jar</code>


<h3>Installation</h3>
If you want to generate the jar file yourself, download this project and open it as a Maven project in Eclipse then use the Maven goal <code>package</code> to generate the file.<br/>
In Eclipse, right click on the project -> Run as -> Maven build ... and type <code>package</code> in the goal field.

You can also execute <code>mvn package</code> at the project's root.

The generated jar file will be generated in the target directory, with the name <code>SeAMK Timetable Reader.jar</code>. Ignore the file <code>SeAMKTimeTableReader-X.X.X.jar</code>.

<h3>TODO</h3>
<ul>
</ul>
