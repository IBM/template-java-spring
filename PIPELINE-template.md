# Create Continuous Delivery Toolchain

[![Deploy To Bluemix](https://console.ng.bluemix.net/devops/graphics/create_toolchain_button.png)](https://console.ng.bluemix.net/devops/setup/deploy/?repository=REPO_URL)


## Url format

https://console.ng.bluemix.net/devops/setup/deploy/?repository=URL_TO_REPO&repository_token=REPO_PAT&branch=REPO_BRANCH

where:

* `URL_TO_REPO` is the url where the repo containing the .bluemix folder can be accessed
* `REPO_PAT` (optional) the personal access token for the repo, likely needed for GHE
* `REPO_BRANCH` (optional) the name of the branch in the repo that contains the .bluemix folder