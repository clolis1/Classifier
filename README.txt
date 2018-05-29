CS436 Intro to Machine Learning
Spring 2016
HOMEWORK 2 README FILE

Due Date: Wednesday, February 22, 2017
Submission Date: Wednesday, February 23, 2017
Author(s): Christian Lolis
e-mail(s): clolis1@binghamton.edu


PURPOSE:

  Create a means of testing an information gain heuristic and a variance impurity
  heuristic. These algorithms should be used to sort through data sets to find
  trends. They will then be pruned to see a change in accuracy (if any).
  
  This project also includes a text classification Naive bayes algorithm. It will
  execute regardless of changes in arguments listed. (However, 6 arguments must
  still be entered).

PERCENT COMPLETE:

  I was responsible for the whole project. To my knowledge, it is 100% complete.

PARTS THAT ARE NOT COMPLETE:

  There are no parts that are incomplete.

BUGS:

  I believe none.

FILES:

  Included with this project are 17 files:
  
  build.xml, the file that helps ant compile and run the program
  Driver.java, the main file associated with the program and also contains main
  DataProcessor.java, the file responsible for parsing the input files
  ScreenPrinter.java, the file responsible for printing to the screen
  test_set_1.txt, test_set_2.txt, the two test set files from the website
  training_set_1.txt, training_set_2.txt, the two training set files from the website
  Accuracy.java, the file responsible for measuring the algorithms' effectiveness
  Attribute.java, a helper file to help manage the attributes in the files
  Example.java, a helper file to help manage the example sets in the files
  ID3.java, the file containing the algorithm for the information gain heuristic
  NB.java, the file containing the Naive Bayes algorithm
  Node.java, a helper file helping to create the Decision Tree
  Pruner.java, the file containing the pruning mechanism for finished trees
  VIH.java, the file containing the algorithm for the variance impurity heuristic
  Word.java, The file that helps manage the contents of the documents
  [Folders] test and train, which contain around ~1300 documents to be parsed
  README.txt, the file that you are currently reading.

SAMPLE OUTPUT:

    $ ant -buildfile src/build.xml run -Darg0=10 -Darg1=10 -Darg2=training_set_2.txt -Darg3=bleh -Darg4=test_set_2.txt -Darg5=no
    Buildfile: /home/ubuntu/workspace/lolis_christian_homework_2/classifier/src/build.xml
    
    jar:
          [jar] Building jar: /home/ubuntu/workspace/lolis_christian_homework_2/classifier/BUILD/jar/classifier.jar
    
    run:
         [java] The accuracy of the Information Gain heuristic with pruning based on the test set is 71.83333333333334%.
         [java] The accuracy of the Variance Impurity heuristic with pruning based on the test set is 74.33333333333333%.
    
    BUILD SUCCESSFUL
    Total time: 1 second
  
====================================================================================================================================
    *NOTE*
    
    The SAMPLE OUTPUT does not include the results of the Naive Bayes algorithm because it takes a very long time to finish! Sorry!
    
====================================================================================================================================

TO COMPILE:
  
  gunzip lolis_christian_homework_2.tar.gz
  tar -zxvf lolis_christian_homework_2.tar
  cd lolis_christian_homework_2/classifier
  ant -buildfile src/build.xml all

TO RUN:

  ant -buildfile src/build.xml run -Darg0=[L] -Darg1=[K] -Darg2=[training_set] -Darg3=[validation_set] -Darg4=[test_set] -Darg5=[yes/no]
  
EXTRA CREDIT:

  N/A

BIBLIOGRAPHY:

  I have done this assignment completely on my own. I have not copied it, nor
  have I given my solution to anyone else. I understand that if I am involved
  in plagiarism or cheating I will have to sign an official form that I have
  cheated and that this form will be stored in my official university record.
  I also understand that I will receive a grade of 0 for the involved assignment
  for my first offense and that I will receive a grade of “F” for the course for
  any additional offense.

ACKNOWLEDGEMENT:

  None.
  
NOTE:

  To see the results of the pruning algorithm only, comment out the section in
  the code which creates the NB object and calls its subsequent method.