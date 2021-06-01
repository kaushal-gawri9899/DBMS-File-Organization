## TASK 3 : HEAP FILE

#### Make sure you have added the .csv file in task3 folder

#### Run the shell script using :

bash ./part3.sh "Filename.csv"

##### The script will take care of compiling the code required to load data in heap file, read data from heap file, load data in B+ Tree and query over the B+ Tree


#### This command will load the data both in heapfile and B+ Tree
##### Command to run dbload : 	java dbload -p "pageSize" "Filename.csv"

#### This command will query over the nodes in B+ Tree where "text" is the STD_NAME or substring of STD_NAME. For range queries use the other command
##### Command to run dbquery : 	java dbquery "text" "pageSize" -b
###### For example run : java dbquery 3701/02/2021 4096 -b
###### For example run : java dbquery 2018 4096 -b

#### This command will range query over the nodes in B+ Tree where "text1" and "text2" is the STD_NAME or substring of STD_NAME. 
##### Command to run dbquery : java dbquery "text1"--"text2" 4096 -b
###### For example run : java dbquery 1007/27/2021--1010/27/2021 4096 -b
###### For example run : java dbquery 2008--2018 4096 -b