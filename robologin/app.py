import json
import os

import requests
from flask import Flask, render_template, session, request, url_for, \
    redirect, json, jsonify
from requests_oauthlib import OAuth2Session

app = Flask(__name__)
client_id = "triallogin"
client_secret = "trial"
authorization_base_url = 'https://uaa.local.pcfdev.io/oauth/authorize/'
token_url = 'https://uaa.local.pcfdev.io/oauth/token'
baseAPIurl = 'https://api.local.pcfdev.io'
baseUAAurl = 'https://uaa.local.pcfdev.io'


@app.route('/')
def index():
    return render_template('index.html')


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
    session['logged_in'] = True
    session['uaa'] = baseUAAurl
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
    profileInfo = {'access_token': session['oauth_token']['access_token']}

    # get user summary
    userinfourl = '{}/userinfo'.format(baseUAAurl)
    userinfo = json.loads(requests.get(
        userinfourl, headers=headers, verify=False).text)
    session['userinfo'] = userinfo
    profileInfo['userinfo'] = json.dumps(session['userinfo'])

    # Method 1 : get user roles by orgs and space
    usersummaryurl = '{0}/v2/users/{1}/summary'.format(
        baseAPIurl, userinfo['user_id'])
    usersummary = json.loads(requests.get(
        usersummaryurl, headers=headers, verify=False).text)

    if usersummary.get('entity'):
        spaceWiseUserRoles = getSpaceWiseUserRoles(usersummary['entity'])
    else:
        # Method 2 : get user roles by orgs and space
        spaceWiseUserRoles = {}
        spaceurl = baseAPIurl + '/v2/spaces'
        spaceresponse = requests.get(spaceurl, headers=headers, verify=False)
        space_json_data = json.loads(spaceresponse.text)
        for spaceresource in space_json_data['resources']:
            entity = spaceresource['entity']
            spaceGuid = spaceresource['metadata']['guid']

            # get all auditors
            auditorurl = 'http://api.local.pcfdev.io' + entity['auditors_url']
            auditorresponse = json.loads(requests.get(
                auditorurl, headers=headers, verify=False).text)
            if isInThisRole(auditorresponse, userinfo['user_name']):
                spaceWiseUserRoles[spaceGuid] = {
                    'role': 'auditor',
                    'name': spaceresource['entity']['name']
                }

            # get all developers
            devurl = 'http://api.local.pcfdev.io' + entity['developers_url']
            devresponse = json.loads(requests.get(
                devurl, headers=headers, verify=False).text)
            if isInThisRole(devresponse, userinfo['user_name']):
                spaceWiseUserRoles[spaceGuid] = {
                    'role': 'developer',
                    'name': spaceresource['entity']['name']
                }

            # get all managers
            managerurl = 'http://api.local.pcfdev.io' + entity['managers_url']
            managerresponse = json.loads(requests.get(
                managerurl, headers=headers, verify=False).text)
            if isInThisRole(managerresponse, userinfo['user_name']):
                spaceWiseUserRoles[spaceGuid] = {
                    'role': 'manager',
                    'name': spaceresource['entity']['name']
                }

    profileInfo['spaceWiseUserRoles'] = json.dumps(spaceWiseUserRoles)
    session['spaceWiseUserRoles'] = spaceWiseUserRoles

    # get user apps from all spaces
    url = '{}/v2/apps'.format(baseAPIurl)
    response = requests.get(url, headers=headers, verify=False)
    appsData = json.loads(response.text)
    appsUrls = {}

    # user accessible app url
    for resource in appsData['resources']:
        routes_url = baseAPIurl + \
            resource['entity']['routes_url']
        routes_url_response = json.loads(requests.get(
            routes_url, headers=headers, verify=False).text)
        for app in routes_url_response['resources']:
            hostname = app['entity']['host']
            appsUrls[hostname] = {
                'url': 'http://{}.local.pcfdev.io'.format(hostname),
                'space_guid': app['entity']['space_guid'],
                'userRole': getSpaceRole(spaceWiseUserRoles, app['entity'][
                    'space_guid'], userinfo['user_name'])}
    profileInfo['apps'] = appsUrls

    organization_guid = getOrganizationId(space_json_data)
    profileInfo['org_id'] = organization_guid
    profileInfo['org_users'] = json.dumps(getOrganizationUsers(
        session, organization_guid))
    return render_template('profile.html', data=profileInfo)


def getSpaceRole(spaceWiseUserRoles, spaceguid, username):
    for id in spaceWiseUserRoles:
        if id == spaceguid:
            return spaceWiseUserRoles[id]['role']


def isInThisRole(roleresponse, username):
    for resource in roleresponse['resources']:
        if resource['entity']['username'] == username:
            return True

    return False


def getSpaceWiseUserRoles(entity):
    spaceWiseUserRoles = {}

    for subentity in entity['spaces']:
        spaceWiseUserRoles[subentity['metadata']['guid']] = {
            'role': 'developer',
            'name': subentity['entity']['name']
        }
    for subentity in entity['managed_spaces']:
        spaceWiseUserRoles[subentity['metadata']['guid']] = {
            'role': 'manager',
            'name': subentity['entity']['name']
        }
    for subentity in entity['audited_spaces']:
        spaceWiseUserRoles[subentity['metadata']['guid']] = {
            'role': 'auditor',
            'name': subentity['entity']['name']
        }

    return spaceWiseUserRoles


def getOrganizationUsers(session, organization_guid):
    tokenString = "bearer {0}".format(session['oauth_token']['access_token'])
    headers = {"Authorization": tokenString}
    orgusersurl = baseAPIurl + '/v2/organizations/{}/users'.format(
        organization_guid)
    orgusersresponse = json.loads(requests.get(
        orgusersurl, headers=headers, verify=False).text)
    orgusers = {}
    if orgusersresponse['resources']:
        for orguser in orgusersresponse['resources']:
            orgusers[orguser['entity']['username']] = orguser['metadata']['guid']
    return orgusers


def getOrganizationId(space_json_data):
    if space_json_data.get('resources'):
        return space_json_data['resources'][0]['entity']['organization_guid']


@app.route('/manage')
def manage():
    if not session.get('oauth_token'):
        return redirect(url_for('login'))
    spaceWiseUserRoles = session['spaceWiseUserRoles']
    managerSpaces = []
    for space in spaceWiseUserRoles:
        if spaceWiseUserRoles[space]['role'] == 'manager':
            managerSpaces.append((spaceWiseUserRoles[space]['name'], space))
    return render_template('managespace.html', spaces=managerSpaces)


@app.route('/getusers')
def getUsersInSpace():
    guid = request.args.get('guid')
    orgusersurl = baseAPIurl + '/v2/spaces/{0}/user_roles'.format(guid)
    tokenString = "bearer {0}".format(session['oauth_token']['access_token'])
    headers = {"Authorization": tokenString}
    orgusersresponse = json.loads(requests.get(
        orgusersurl, headers=headers, verify=False).text)
    users = []
    for user in orgusersresponse['resources']:
        newuser = {
            'guid': user['metadata']['guid'],
            'name': user['entity']['username'],
            'roles': user['entity']['space_roles'],
        }
        users.append(newuser)
    return jsonify(users)


@app.route('/logout')
def logout():
    session['logged_in'] = False
    return redirect(url_for('index'))


if __name__ == '__main__':
    os.environ['DEBUG'] = "1"
    os.environ['OAUTHLIB_INSECURE_TRANSPORT'] = '1'
    app.secret_key = os.urandom(24)
    app.run(debug=True, use_reloader=True, ssl_context='adhoc')
