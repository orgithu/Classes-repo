 <?php
$myfile = fopen("password", "r") or die("Error: Unable to open file!");
echo fread($myfile, filesize("webdictionary.txt"));
fclose($myfile);
?> 
