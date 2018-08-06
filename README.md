# SpringBoot project template

This project provides a template to quickly start a new SpringBoot project that comes pre-configured
with a number of useful features. It is intended to be customizable, extensible, and to be updated as
the underlying template is updated.

## Getting started

### Create the project

#### Clone the project

To get started clone this repo into a local folder, preferably one with a different name:

```bash
git clone --single-branch {url} {new_name}
```

**OR**

#### Create the toolchain

Create the toolchain and clone the project into a new repository. There are three different destinations for 
repositories in the IBM toolchain so there are three different branches/buttons. Click the appropriate link below
then click the subsequent 'Create toolchain'.

* [GitHub](tree/toolchain_github)
* [IBM Hosted GitLab](tree/toolchain_gitlab)
* [GitHub Enterprise Whitewater](tree/toolchain_ibmghe)


### Set up the project from the template

Next, run the `setup-template.sh` script. It will ask for you project name and the url to your new 
repo. If no project name is provided the script will default to using the folder into which the repo 
was checked out.

```bash
./setup-template.sh
```

### (Optionally) Setup the IBM Cloud Continuous Delivery pipeline

Customize the toolchain config by running the `setup-bluemix-toolchain.sh` script. It will ask several
questions related to the project repository and name. When finished, a new version of `.bluemix/toochain.yml`
will have been generated and should be pushed to the repo.

Next, navigate to the `PIPELINE.md` file from the repo url and click
the `Create toolchain` button.


### Periodically update from the template

Finally, the template components can be periodically updated by running the following:

```bash
./update-template.sh
```

## More information

Once the `setup-template.sh` script has been run, this README.md file will be replaced with a new 
file for the project. More information about the template features and how they are used can be 
found in [TEMPLATE.md](TEMPLATE.md)