# Complete The Sentence Program in Java
  Completes The Entered Sentence With Using Trie Data Structure in Java

## Running The Program
  Compile and run "CompleteTheSentence.java"

## How To Use The Program?
  Program completes the entered sentence by using the data given. This source file (data) must be in TXT(.txt) format. Each line in the text file could contain only one sentence. There is an example "data.txt" in the repository. 

### How To Load Data?
  While in the program, go to "File" then "Load" and select the desired text file. If "Overwrite" option is selected, previous data will be deleted
Note: Loaded data will be saved in the same path as "CompleteTheSentence.java" with the name of "data.txt". Therefore, it could be changed directly from there.

## How Is It Working? 
  Calculates all data's distances to the centers. Then cluster them to corresponding center (nearest center). After one step of the clustering, It estimates new clusters' centroids. Then moves the current centers to their new centroids (For each different centroid). After the movement, calculates all data's distances to centers again and clusters them. Repeats this procces until clustering is done.
##
Ali Berk Karaarslan

2023

