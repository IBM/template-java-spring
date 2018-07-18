#!/bin/bash

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
manifestFile=./devops/cf.manifest.${envName}.yml
if [[ -f ${manifestFile} ]]; then
  echo -e "Using cf manifest file ${manifestFile}"
else
  echo -e "\033[1;31mError: cf manifest file ${manifestFile} not found.\033[0m"
  exit 1
fi

# Check if `bx` cli tool is installed
command -v bx >/dev/null 2>&1 || {
  echo >&2 -e "\033[1;31mbx cli not installed.\033[0m"
  echo -e "Please install bx cli to use deploy script: https://console.bluemix.net/docs/cli/reference/bluemix_cli/get_started.html#getting-started"
  exit 1;
}

# Check `bx target` account|org|space information
echo -e "\nTargeted Bluemix environment:"
targetInfo=$(bx target | awk '/Account:|Org:|Space:/')
echo -e ${targetInfo}
if [[ -z ${targetInfo} ]]; then
  echo -e "\033[1;31mMake sure you're logged in to the bx cli using bx login, or bx login --help for more info.\033[0m"
  echo -e 'E.g 1: bx login'
  echo -e 'E.g 2: bx login -a "https://api.ng.bluemix.net"'
#  echo -e 'E.g 2: bx login -a "https://api.ng.bluemix.net" -c "d7f9e00f-4cc9-4766-aa64-efb5dfb736e5" -o "<org>" -s "<space>" -u "<your email>'
  echo -e "E.g 3: bx login --help"
  exit 1;
fi

# Check for the deploy environment to match the targeted Bluemix space
# i.e. to deploy as `deploy.sh {env}` the `bx cf target` should point to `*-{env}` space.
#
targetSpaceInfo=$(bx target | awk '/Space:/ {print $2}')
targetSpaceInfo=${targetSpaceInfo:-"N/A"}
echo -e "Deploy environment: ${envName} <::> Targeted space: ${targetSpaceInfo}"
if [[ ${targetSpaceInfo} =~ ^.*${envName}$ ]]; then
    echo "All good" >> /dev/null
else
  echo -e "\033[1;33mWarning: Environment target didn't match.\033[0m";
  echo -e "In order to deploy as \033[1;33mdeploy.sh ${envName}\033[0m the targeted Bluemix space should be \033[1;33m*-${envName}\033[0m instead \033[1;33m${targetSpaceInfo}\033[0m";
  echo -e "\033[1;31mPlease target Bluemix space using bx target -s <space>, or run bx target --help for more info.\033[0m"
  echo -e 'E.g 1: bx target -o "<org>" -s "<space>"'
  echo -e 'E.g 2: bx target --cf-api https://api.ng.bluemix.net'
#  echo -e 'E.g 2: bx target --cf-api https://api.ng.bluemix.net -c "ef5ca44a071c8d1c95af0a8b33ed4f52" -o "<org>" -s "<space>"'
  echo -e "E.g 3: bx target --help"
  exit 1;
fi

echo -e "\nChecking CLI config (cf plugins)"
communityPluginRepoName=$(bx cf list-plugin-repos | grep -o -e "^CF-Community")
if [[ "${communityPluginRepoName}" != "CF-Community" ]]; then
  echo -e "\033[1;32mAdding CF-Community plugin-repo\033[0m"
  bx cf add-plugin-repo CF-Community https://plugins.cloudfoundry.org
else
  echo -e "- ${communityPluginRepoName} plugin-repo already added"
fi
bgdPluginName=$(bx cf plugins | grep -o -e "^blue-green-deploy")
if [[ "${bgdPluginName}" != "blue-green-deploy" ]]; then
  echo -e "\033[1;32mInstalling blue-green-deploy plugin\033[0m"
  bx cf install-plugin blue-green-deploy -f -r CF-Community
else
  echo -e "- ${bgdPluginName} plugin already installed"
fi

appName=${baseAppName}-${envName}
echo -e "\nDeploying \033[1;34m${appName}\033[0m to Bluemix  (`date`)"

bx cf bgd ${appName} -f ${manifestFile}

echo -e "\nStopping \033[1;34m${appName}-old\033[0m on Bluemix"
bx cf stop ${appName}-old >> /dev/null

echo -e "\n\033[1;36mDeploy Done!\033[0m\n"
