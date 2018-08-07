#!/usr/bin/env bash

current_template_origin=$(git remote get-url template_origin 2> /dev/null)
template_origin="$(cat ./.template_origin 2>/dev/null)"

if [[ -z "$current_template_origin" && -n "$template_origin" ]]; then
    echo -e "\033[1;37mSetting template_origin url:\033[0m ${template_origin}"
    git remote add template_origin "${template_origin}"
elif [[ -n "$template_origin" ]]; then
    echo -e "\033[1;37mSetting template_origin url:\033[0m ${template_origin}"
    git remote set-url template_origin "${template_origin}"
else
    echo -e "\033[1;31mNo template_origin url set!! Add the git url to the template repo in .template_origin\033[0m"
    exit 1
fi

echo -e "\033[1;34mRefreshing project from template\033[0m\n"

git pull template_origin master
