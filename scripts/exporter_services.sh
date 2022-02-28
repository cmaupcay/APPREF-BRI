#!/bin/sh

INITIAL=$PWD
cd $(dirname $0)
SOURCE=$(realpath "../implementation/bin")
DESTINATION=$(realpath "../apache-ftpserver/res/home")

if [ -d $DESTINATION ]
then
    rm -rf $DESTINATION
fi
mkdir $DESTINATION
mkdir $DESTINATION/admin

echo "Copie du service admin.Inversion..."
cp $SOURCE/admin/Inversion.class $DESTINATION/admin
if [ ! $? -eq 0 ]
then
    echo "La copie du fichier a échoué."
    exit
fi

echo "Copie du service admin.AnalyseXML..."
cp $SOURCE/admin/AnalyseXML.class $DESTINATION/admin
if [ ! $? -eq 0 ]
then
    echo "La copie du fichier a échoué."
    exit
fi

echo "Création du fichier JAR pour le service admin/Messagerie..."
cd $SOURCE
jar --create --file Messagerie.jar admin/Messagerie.class admin/Message.class
if [ ! $? -eq 0 ]
then
    echo "La création du fichier JAR a échoué."
    exit
fi
mv Messagerie.jar $DESTINATION/admin/Messagerie.jar
if [ ! $? -eq 0 ]
then
    echo "Le déplacement du fichier JAR sur le serveur a échoué."
    exit
fi

echo "Fini."
cd $INITIAL