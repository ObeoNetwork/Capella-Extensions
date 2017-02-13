#! /usr/bin/env sh
UPDATE_SITE_DIR=org.obeonetwork.capella.update/target/repository
DEPLOY_LOCAL_DIR=$1
echo "Prepare deploy local dir = ${DEPLOY_LOCAL_DIR}"
# Create nightly folder
mkdir $DEPLOY_LOCAL_DIR 
# Copy update-site and target platform to deploy local dir
cp -r $UPDATE_SITE_DIR/* $DEPLOY_LOCAL_DIR
echo "ls ${DEPLOY_LOCAL_DIR}"
ls $DEPLOY_LOCAL_DIR
