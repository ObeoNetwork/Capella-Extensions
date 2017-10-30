#! /usr/bin/env sh
UPDATE_SITE_DIR=releng/org.obeonetwork.capella.update/target/repository
UPDATE_SITE_DIR_FULL=releng/org.obeonetwork.capella.update.full/target/repository
DEPLOY_LOCAL_DIR=$1
echo "Prepare deploy local dir = ${DEPLOY_LOCAL_DIR}"
# Create nightly folder
mkdir -p $DEPLOY_LOCAL_DIR 
#the full subdirectory contains the "full" update-site
mkdir -p $DEPLOY_LOCAL_DIR/full 
# Copy update-site and target platform to deploy local dir
cp -r $UPDATE_SITE_DIR/* $DEPLOY_LOCAL_DIR
cp -r $UPDATE_SITE_DIR_FULL/* $DEPLOY_LOCAL_DIR/full
echo "find ${DEPLOY_LOCAL_DIR}"
find $DEPLOY_LOCAL_DIR
