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

echo -e "\033[1;34mSet up bluemix toolchain:\033[0m\n"

#########################################
#            User input                 #
#########################################

origin_url=$(git remote get-url origin | sed "s~git@\(.*\):\(.*\)~https://\1/\2~g" | sed "s/\.git$//g")

PS3='  git service type (enter the #): '
options=("GitHub" "IBM Hosted GitLab" "GitHub Enterprise Whitewater")
select opt in "${options[@]}"
do
    case $opt in
        "GitHub")
            service_id="githubconsolidated"
            break
            ;;
        "IBM Hosted GitLab")
            service_id="hostedgit"
            break
            ;;
        "GitHub Enterprise Whitewater")
            service_id="github_integrated"
            break
            ;;
        *) echo "invalid option $REPLY";;
    esac
done

echo ""

PS3='  git repo type (enter the #): '
options=("link or existing - start this toolchain by linking to an existing project" "fork - start this toolchain by forking another project" "clone - start this toolchain by cloning another project")
select opt in "${options[@]}"
do
    case $REPLY in
        "1")
            repo_type="link"
            break
            ;;
        "2")
            repo_type="fork"
            break
            ;;
        "3")
            repo_type="clone"
            break
            ;;
        *) echo "invalid option $REPLY";;
    esac
done

echo ""

### Repo url
default_repo_url=${origin_url}

echo -e -n "  repo url (\033[1;38m${default_repo_url}\033[0m): "

read repo_url

if [[ -z "${repo_url}" ]]; then
  repo_url=${default_repo_url}
fi

echo ""

### project name
default_project_name=$(cat settings.gradle | grep "rootProject.name" | sed "s/rootProject.name \{0,1\}= \{0,1\}'\(.*\)'/\1/g")
echo -e -n "  repo name (\033[1;38m${default_project_name}\033[0m): "

read project_name

if [[ -z "${project_name}" ]]; then
  project_name=${default_project_name}
fi

echo ""

cat ./.bluemix/toolchain-template.yml | sed "s/PROJECT_NAME/${project_name}/g" | sed "s/SERVICE_ID/${service_id}/g" | sed "s~REPO_URL~${repo_url}~g" | sed "s/REPO_TYPE/${repo_type}/g" > ./.bluemix/toolchain.yml
