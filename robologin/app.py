from flask import Flask, render_template, session, request, url_for, redirect
import requests
import json
from requests_oauthlib import OAuth2Session
from flask.json import jsonify
import os
import requests
import jwt

app = Flask(__name__)
client_id = "triallogin"
client_secret = "trial"
authorization_base_url = 'https://uaa.local.pcfdev.io/oauth/authorize/'
token_url = 'https://uaa.local.pcfdev.io/oauth/token'


@app.route('/')
def index():
    return render_template('index.html', sign='Sign In')


@app.errorhandler(404)
def page_not_found(e):
    return render_template('404.html'), 404


@app.route('/login', methods=['GET'])
def login():
    """Step 1: User Authorization.

    Redirect the user/resource owner to the OAuth provider (i.e. Github)
    using an URL with a few key OAuth parameters.
    """
    cloudfoundry = OAuth2Session(client_id)
    authorization_url, state = cloudfoundry.authorization_url(
        authorization_base_url)

    # State is used to prevent CSRF, keep this for later.
    session['oauth_state'] = state
    return redirect(authorization_url)

@app.route("/callback", methods=["GET"])
def callback():
    """ Step 3: Retrieving an access token.

    The user has been redirected back from the provider to your registered
    callback URL. With this redirection comes an authorization code included
    in the redirect URL. We will use that to obtain an access token.
    """

    cloudfoundry = OAuth2Session(client_id, state=session['oauth_state'])
    token = cloudfoundry.fetch_token(token_url, client_secret=client_secret,
                               authorization_response=request.url, 
                               verify=False)

    # At this point you can fetch protected resources but lets save
    # the token and show how this is done from a persisted token
    # in /profile.
    session['oauth_token'] = token

    return redirect(url_for('.profile'))


@app.route("/profile", methods=["GET"])
def profile():
    """Fetching a protected resource using an OAuth 2 token.
    """

    if not session.get('oauth_token'):
        return redirect(url_for('login'))
    tokenString = "bearer {0}".format(session['oauth_token']['access_token'])
    headers = {"Authorization": tokenString}
    profileInfo = {'access_token':session['oauth_token']['access_token']}

    #get user summary
    userinfourl = 'https://uaa.local.pcfdev.io/userinfo'
    userinfo = json.loads(requests.get(
        userinfourl, headers=headers, verify=False).text)
    session['userinfo'] = userinfo
    profileInfo['userinfo'] = session['userinfo']

    #get user roles by orgs and space
    usersummaryurl = 'https://api.local.pcfdev.io/v2/users/{' \
                     '}/summary'.format(userinfo['user_id'])
    usersummary = json.loads(requests.get(
        usersummaryurl, headers=headers, verify=False).text)
    profileInfo['spaceWiseUserRoles'] = getSpaceWiseUserRoles(usersummary[
                                                               'entity'])

    # get user apps from all spaces
    url = 'http://api.local.pcfdev.io/v2/apps'
    response = requests.get(url, headers=headers, verify=False)
    appsData = json.loads(response.text)
    appsUrls = {}
    for resource in appsData['resources']:
        routes_url = 'http://api.local.pcfdev.io' + \
            resource['entity']['routes_url']
        routes_url_response = json.loads(requests.get(
            routes_url, headers=headers, verify=False).text)
        for app in routes_url_response['resources']:
            hostname = app['entity']['host']
            appsUrls[hostname] = 'http://{}.local.pcfdev.io'.format(hostname)
    profileInfo['apps'] = appsUrls

    return jsonify(profileInfo)


def getSpaceWiseUserRoles(entity):
    spaceWiseUserRoles = {}

    for subentity in entity['spaces']:
        spaceWiseUserRoles[subentity['metadata']['guid']] = {
            'role':'developer',
            'name':subentity['entity']['name']
        }
    for subentity in entity['managed_spaces']:
        spaceWiseUserRoles[subentity['metadata']['guid']] = {
            'role':'manager',
            'name':subentity['entity']['name']
        }
    for subentity in entity['audited_spaces']:
        spaceWiseUserRoles[subentity['metadata']['guid']] = {
            'role':'auditor',
            'name':subentity['entity']['name']
        }

    return spaceWiseUserRoles


@app.route('/logout')
def logout():
    session.pop('signedin')
    return redirect(url_for('index'))


if __name__ == '__main__':
    os.environ['DEBUG'] = "1"
    os.environ['OAUTHLIB_INSECURE_TRANSPORT'] = '1'
    app.secret_key = os.urandom(24)
    app.run(debug=True, use_reloader=True,ssl_context='adhoc')