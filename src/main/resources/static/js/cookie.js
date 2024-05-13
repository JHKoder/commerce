var isAuthentication=false; // 로그인 유무
var authentications;    // 권한
var authenticationName;    // 권한 ID



function accessToken() {
    return fetchWithToken("/api/token/access","accessToken", 'POST')
    .then(response => {
        if(response.ok){
            return response.json();
        }else{
            return null;
        }
    }).then(json => {
        console.log(json);
        if(json != null){
            isAuthentication = true;
            authentications = json.authentications;
            authenticationName = json.username;
        }else{
            accessTokenRe();
            isAuthentication = false;
            authentications = [];
            authenticationName = "";
        }
        console.log("accessToken result : "+isAuthentication +","+authentications +","+authenticationName );
    });
}

function accessTokenRe() {
    return fetchWithToken("/api/token/access","accessToken", 'POST')
    .then(response => {
        if(response.ok){
            return response.json();
        }else{
            return null;
        }
    }).then(json => {
        console.log(json);
        if(json != null){
            isAuthentication = true;
            authentications = json.authentications;
            authenticationName = json.username;
        }else{
            isAuthentication = false;
            authentications = [];
            authenticationName = "";
        }
        console.log("accessToken result : "+isAuthentication +","+authentications +","+authenticationName );
    });
}

function accessRefreshToken() {
    return fetchWithToken("/api/token/refresh", "refreshToken",'POST')
    .then(response => {
        if(response.ok){
            var data = response.json();
            setCookie("accessToken", "Bearer " + data.token, 1);
        }else{
            logout();
            window.location.href = "/login";
        }
    });
}

function fetchWithToken(url, tokenName, method, body) {
    var accessToken = getCookie(tokenName);
    if (!accessToken) {
        return Promise.reject("Access token not found.");
    }

    return fetch(url, {
        method: method,
        headers: {
            'Content-Type': 'application/json',
            'Authorization': accessToken
        },
        body: JSON.stringify(body)
    })
    .then(response => {
        return response;
    });
}

function setCookie(cookieName, cookieValue, expirationDays) {
    var d = new Date();
    d.setTime(d.getTime() + (expirationDays * 24 * 60 * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";path=/";
}

function setCookieMin(cookieName, cookieValue, expirationMin) {
    var d = new Date();
    d.setTime(d.getTime() + (expirationMin * 60 * 1000));
    var expires = "expires=" + d.toUTCString();
    document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";path=/";
}

function getCookie(name) {
    var cookies = document.cookie.split("; ");
    for (var i = 0; i < cookies.length; i++) {
        var cookie = cookies[i].split("=");
        if (cookie[0] === name) {
            return decodeURIComponent(cookie[1]);
        }
    }
    return "";
}

function logout(){
    deleteCookie("accessToken");
    deleteCookie("refreshToken");
    location.reload();
}
function deleteCookie(cookieName, path = '/') {
    document.cookie = cookieName + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=' + path + ';';
}



