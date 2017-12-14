### RoboLogin Manager

The landing page for all the robocode instances. 

The general idea is that this would be the only page which is exposed to the user.
Rest all of the robocode app would be accessible through this after login. 


https://groups.google.com/a/cloudfoundry.org/forum/#!topic/vcap-dev/J0NDzsAW-z8

https://docs.cloudfoundry.org/api/uaa/version/4.7.0/index.html#token

https://docs.cloudfoundry.org/concepts/architecture/uaa.html#uaa-scopes

https://github.com/cloudfoundry/uaa/blob/master/docs/UAA-APIs.rst#client-registration-administration-apis

Command for registering client in UAAC:
#uaac client add --name triallogin --scope openid --authorized_grant_types 'authorization_code,client_credentials' --access_token_validity 1209600 --authorities oauth.login --redirect_uri 'http://localhost:5000/profile'