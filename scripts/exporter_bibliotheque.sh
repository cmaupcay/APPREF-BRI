#!/bin/sh

INITIAL=$PWD
cd $(dirname $0)
SOURCE=$(realpath "../infrastructure/bin")
DESTINATION=$(realpath "../implementation/lib")
JAR="bri.jar"

if [ -f $DESTINATION/$JAR ]
then
    rm $DESTINATION/$JAR
fi

echo "Création de la bibliothèque BRI..."
cd $SOURCE
jar --create --file $JAR bri/
if [ ! $? -eq 0 ]
then
    echo "La création du fichier JAR a échoué."
    exit
fi
mv $JAR $DESTINATION/$JAR
if [ ! $? -eq 0 ]
then
    echo "Le déplacement du fichier JAR."
    exit
fi

echo "Fini."
cd $INITIAL