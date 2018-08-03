#!/usr/bin/env bash

# console echo colors
#  \033[1;31m red
#  \033[1;32m green
#  \033[1;33m amber
#  \033[1;34m blue
#  \033[1;35m purple
#  \033[1;36m light blue
#  \033[1;37m grey
#  \033[1;38m black

echo -e "\033[1;34mSet up application from template:\033[0m\n"

#########################################
#            User input                 #
#########################################

### project name
project_name_default=${PWD##*/}
echo -e -n "  project name (\033[1;38m${project_name_default}\033[0m): "

read project_name_input

if [[ -n "$project_name_input" ]]; then
    project_name=${project_name_input}
else
    project_name=${project_name_default}
fi

### project repo
echo -e -n "  project repo url: "

read project_repo_url

echo ""

#########################################
#           Process input               #
#########################################

### project name
echo -e "    \033[1;37mSetting project name:\033[0m ${project_name}"

# update rootProject.name value in settings.gradle
sed -i -e "s/rootProject.name.*/rootProject.name = '${project_name}'/g" ./settings.gradle
rm ./settings.gradle-*

sed -i -r "s/  \"name\":.*/  \"name\": \"${project_name}\",/g" ./package.json
rm ./package.json-*

# generate new README.md
echo "# ${project_name}" > README.md


### project repo

template_origin=$(git remote get-url template_origin 2> /dev/null)
current_origin=$(git remote get-url origin 2> /dev/null)

if [[ -z "$template_origin" ]]; then
    echo -e "    \033[1;37mSetting template_origin url:\033[0m ${current_origin}"
    git remote add template_origin "${current_origin}"
fi

if [[ -z "$project_repo_url" && -n "$current_origin" ]]; then
    echo -e "    \033[1;37mRemoving origin url\033[0m"
    git remote remove origin

    sed -i -r "s/    \"url\":.*/    \"url\": \"\",/g" ./package.json
    rm ./package.json-*

elif [[ -n "$project_repo_url" ]]; then
    echo -e "    \033[1;37mSetting origin url:\033[0m ${project_repo_url}"
    if [[ -z "$current_origin" ]]; then
        git remote add origin "${project_repo_url}"
    else
        git remote set-url origin "${project_repo_url}"
    fi

    sed -i -r "s/    \"url\":.*/    \"url\": \"${project_repo_url}\",/g" ./package.json
    rm ./package.json-*
fi

echo ""

#########################################
#        Graft template history         #
#########################################

templateHead=$(git log --name-status HEAD^..HEAD | grep -e "^commit" | sed "s/commit \(.*\)/\1/")

echo "${templateHead}" > .git/info/grafts
