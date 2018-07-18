# SpringBoot project template

This project provides a template to quickly start a new SpringBoot project that comes pre-configured
with a number of useful features. It is intended to be customizable, extensible, and to be updated as
the underlying template is updated.

## Getting started

To get started clone this repo into a local folder, preferably one with a different name:

```bash
git clone {url} {new_name}
```

Next, run the `setup-template.sh` script. It will ask for you project name and the url to your new 
repo. If no project name is provided the script will default to using the folder into which the repo 
was checked out.

```bash
./setup-template.sh
```

Finally, the template components can be periodically updated by running the following:

```bash
./update-template.sh
```

## More information

Once the `setup-template.sh` script has been run, this README.md file will be replaced with a new 
file for the project. More information about the template features and how they are used can be 
found in [TEMPLATE.md](TEMPLATE.md)