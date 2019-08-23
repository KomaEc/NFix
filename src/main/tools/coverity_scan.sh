# to make a local build
cov-build --dir cov-int compile

# compress the build
tar czvf <project_name>.tgz cov-int