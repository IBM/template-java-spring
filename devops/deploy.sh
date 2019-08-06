#!/bin/bash

scriptDir=$(dirname $0)

baseAppName="myapp-tsb"

if [[ -z "$1" ]]; then
  echo -e "\033[1;31mError: Please pass the environment name (e.g. dev, tst, prd) as the first arg.\033[0m"
  echo -e "Example: ./deploy.sh dev"
  echo -e "Example: ./deploy.sh tst"
  exit 1
else
  envName="$1"
fi

configFile=deploy.${envName}
if [[ -f ${configFile} ]]; then
  echo -e "Loading configuration values from ${configFile}"
  source ${configFile}
else
  echo -e "Unable to find configuration file ${configFile}. Using default config values."
fi

if [[ -n "${APP_BASENAME}" ]]; then
  echo -e "Setting baseAppName to ${APP_BASENAME}"
  baseAppName="${APP_BASENAME}"
fi


# Select Cloudfoundry deployment manifest file
srcManifestFile=${scriptDir}/cf.manifest.${envName}.yml
if [[ -f ${srcManifestFile} ]]; then
  echo -e "Using cf manifest file ${srcManifestFile}"
else
  echo -e "\033[1;31mError: cf manifest file ${srcManifestFile} not found.\033[0m"
  exit 1
fi

# Check if `cf` cli tool is installed
command -v cf >/dev/null 2>&1 || {
  echo >&2 -e "\033[1;31mcf cli not installed.\033[0m"
  echo -e "Please install cf cli to use deploy script: https://github.com/cloudfoundry/cli#downloads"
  exit 1;
}

# Check `bx target` account|org|space information
echo -e "\nTargeted Bluemix environment:"
targetInfo=$(cf target | awk '/Account:|[Oo]rg:|[Ss]pace:/')
echo -e ${targetInfo}
if [[ -z ${targetInfo} ]]; then
  echo -e "\033[1;31mMake sure you're logged in to the cf cli using cf login, or cf login --help for more info.\033[0m"
  echo -e 'E.g 1: cf login'
  echo -e 'E.g 2: cf login -a "https://api.ng.bluemix.net"'
  echo -e "E.g 3: cf login --help"
  exit 1;
fi

# Check for the deploy environment to match the targeted Bluemix space
# i.e. to deploy as `deploy.sh {env}` the `cf target` should point to `*-{env}` space.
#
targetSpaceInfo=$(cf target | awk '/[Ss]pace:/ {print $2}')
targetSpaceInfo=${targetSpaceInfo:-"N/A"}
echo -e "Deploy environment: ${envName} <::> Targeted space: ${targetSpaceInfo}"
if [[ ${targetSpaceInfo} =~ ^.*${envName}$ ]]; then
    echo "All good" >> /dev/null
else
  echo -e "\033[1;33mWarning: Environment target didn't match.\033[0m";
  echo -e "In order to deploy as \033[1;33mdeploy.sh ${envName}\033[0m the targeted Bluemix space should be \033[1;33m*-${envName}\033[0m instead \033[1;33m${targetSpaceInfo}\033[0m";
  echo -e "\033[1;31mPlease target Bluemix space using cf target -s <space>, or run cf target --help for more info.\033[0m"
  echo -e 'E.g 1: cf target -o "<org>" -s "<space>"'
  echo -e "E.g 3: cf target --help"
  exit 1;
fi

echo -e "\nChecking CLI config (cf plugins)"
communityPluginRepoName=$(cf list-plugin-repos | grep -o -e "^CF-Community")
if [[ "${communityPluginRepoName}" != "CF-Community" ]]; then
  echo -e "\033[1;32mAdding CF-Community plugin-repo\033[0m"
  cf add-plugin-repo CF-Community https://plugins.cloudfoundry.org
else
  echo -e "- ${communityPluginRepoName} plugin-repo already added"
fi
bgdPluginName=$(cf plugins | grep -o -e "^blue-green-deploy")
if [[ "${bgdPluginName}" != "blue-green-deploy" ]]; then
  echo -e "\033[1;32mInstalling blue-green-deploy plugin\033[0m"
  cf install-plugin blue-green-deploy -f -r CF-Community
else
  echo -e "- ${bgdPluginName} plugin already installed"
fi

appFile=$(cd $scriptDir && ls ../build/libs/*)
echo "currentDir=$PWD"
manifestFile=${scriptDir}/tmp.cf.manifest.yml

echo -e "\nGenerating manifest file with artifact path \033[1;34m${appFile}\033[0m"

cp ${srcManifestFile} ${manifestFile}
echo "  path: ${appFile}" >> ${manifestFile}

appName=${baseAppName}-${envName}
echo -e "\nDeploying \033[1;34m${appName}\033[0m to Bluemix  (`date`)"

cf bgd ${appName} -f ${manifestFile}

echo -e "\nStopping \033[1;34m${appName}-old\033[0m on Bluemix"
cf stop ${appName}-old >> /dev/null

echo -e "\n\033[1;36mDeploy Done!\033[0m\n"
