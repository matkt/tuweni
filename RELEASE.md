# Releasing Tuweni

This guide comprises of all the steps to prepare, run a release and follow up on the next steps.

## One-time setup

Open the file `~/.gradle/gradle.properties` and add the following information:

```
asfNexusUsername=${asf LDAP id}
asfNexusPassword=${asf LDAP password}
signing.keyId=${GPG key ID}
signing.gnupg.keyName=${GPG key ID}
```

Make sure your computer has gpg utilities installed. Follow the [Gradle signing plugin's GPG agent](https://docs.gradle.org/current/userguide/signing_plugin.html#sec:using_gpg_agent) instructions to set up.

## Branch into a release branch

Create a new release branch formed with the `major.minor` version numbers (see [semver](https://semver.org/) for glossary). For `1.0.0-SNAPSHOT` version, the release branch will therefore be `1.0`.

```
git checkout -b 1.0
git push origin 1.0
```

For patch releases, use the existing release branch.

## Prepare the release

Change the README file to update version numbers to the new release. Make a commit ($commitId) to the release branch and push.
TODO : point to commit example

Change the version number in the master branch in a commit and push.
TODO : point to commit example

## Running the release

### Build the artifacts

```
export ENABLE_SIGNING=true
export BUILD_RELEASE=true
./gradlew clean publish
```

This checks the code, the licenses, runs all the tests and creates all the artifacts (binaries, sources, javadoc).

This also publishes the artifacts to repository.apache.org in a staging repository.

### Push the distribution to dist.apache.org staging area

If necessary create the folder:
```
svn mkdir -m "Add new Tuweni folder for release ${RELEASE VERSION}" https://dist.apache.org/repos/dist/dev/tuweni/${RELEASE VERSION}
```

Check out the folder locally:
```
svn checkout https://dist.apache.org/repos/dist/dev/tuweni/${RELEASE VERSION} _staged
```

Delete contents of the folder if necessary.
```
TODO
```

Copy the distribution artifacts to it - make sure to change the name to ${RELEASE VERSION}-incubating:

```
cp build/distributions/tuweni-${RELEASE VERSION}.zip _staged/tuweni-${RELEASE VERSION}-incubating.zip
```

Copy the site to it:
```
TODO
```

Commit the changes

### Tag the git repository

```
git tag -m "Release ${RELEASE VERSION}" v${RELEASE VERSION}
git push origin 1.0 --tags
```

### Open a thread for a vote

Send an email to dev@tuweni.apache.org with the following:

Subject:[VOTE] Tuweni ${RELEASE VERSION} release
```
We're voting on the source distributions available here:
https://dist.apache.org/repos/dist/dev/tuweni/${RELEASE VERSION}/dist/
Specifically:
https://dist.apache.org/repos/dist/dev/tuweni/${RELEASE VERSION}/dist/tuweni-${RELEASE VERSION}.tgz
https://dist.apache.org/repos/dist/dev/tuweni/${RELEASE VERSION}/dist/tuweni-${RELEASE VERSION}.zip
The documentation generated for this release is available here:
https://dist.apache.org/repos/dist/dev/tuweni/${RELEASE VERSION}/site/
The following changes were made since ${PREVIOUS VERSION}:

${fill in changes}

```

The vote should be opened for at least 72 hours, longer if there is a week-end.

## Close the release

After the time of the vote has elapsed, close the vote thread with a recap showing the votes.

### Push the release to the dist final location

TODO

### Move the artifacts to maven central

TODO

### Publish the site

TODO

### Send an [ANNOUNCE] email

```
Subject: [ANNOUNCE] Apache Tuweni ${RELEASE VERSION} released

${TUWENI DESCRIPTION}

New in this release:
${list of changes}
To learn more about Tuweni and get started:
http://tuweni.apache.org/
Thanks!
The Apache Tuweni Team
```

### Cherry-pick the commit changing versions back to master

See $commitId above.

```
git cherry-pick ${commitId}
```

### Clean up jira

If applicable mark the version as released.