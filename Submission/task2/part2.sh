#!/bin/bash
if [ "$1" != '' ]
then

        DATABASE_NAME="assignTwo"
        COLLECTION_NAME="Pedestrian"
        printf "\n\nImporting Data from $1 to MongoDb Collection : Pedestrian"
        time mongoimport --db $DATABASE_NAME --collection $COLLECTION_NAME --bat                                                                             chSize 1 --file ./mongo-data.json --jsonArray
        printf "\n\nImporting Data Successful\n"

        #Open the mongo shell
        mongo
        #Use above mentioned database
        use assignTwo
        #Create index on collection Pedestrian on Hourly_Count
        db.Pedestrian.createIndex({Hourly_Count:1})
        #Get all the indices of Pedestrian Collection
        db.Pedestrian.getIndexes()
        printf "\n\nCreating Index on Hourly_Count"

else
        printf "\n\nError: No .csv file provided as input\n"
fi
