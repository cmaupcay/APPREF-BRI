#!/bin/sh

INITIAL=$PWD
cd $(dirname $0)
SOURCE="../bin/admin"
DESTINATION="../apache-ftpserver/res/home/admin"

if [ -d $DESTINATION ]
then
    rm -rf $DESTINATION
fi
mkdir $DESTINATION

echo "Copie du service Inversion..."
cp $SOURCE/Inversion.class $DESTINATION

echo "Copie du service AnalyseXML..."
cp $SOURCE/AnalyseXML.class $DESTINATION

echo "Création du fichier JAR pour la Messagerie..."
jar cf $DESTINATION/Messagerie.jar $SOURCE/Messagerie.class $SOURCE/Message.class
if [ ! $? -eq 0 ]
then
    echo "La création du fichier JAR a échoué."
    exit
fi

echo "Fini."
cd $INITIAL