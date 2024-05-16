var isAuthentication = false; // 로그인 유무
var authentications = [];    // 권한
var authenticationName = "";    // 권한 ID


function accessToken() {
    var accessToken = getCookie("accessToken");
    if (accessToken === null || accessToken === "" || accessToken === " ") {
        return accessRefreshToken();
    } else {
        console.log("accessToken() token " + accessToken);
        return fetchWithToken("/all/api/token/access", accessToken)
            .then(response => {
                if (response.ok) {
                    return response.json();
                }
                return accessRefreshToken();
            }).then(json => {
                if (json != null) {
                    isAuthentication = true;
                    authentications = json.authentications;
                    authenticationName = json.username;
                }
                console.log("accessToken result : " + isAuthentication + "," + authentications + "," + authenticationName);
            });
    }
}

function sout(message) {
    console.log(message);
}

function accessTokenRe() {
    var accessToken = getCookie("accessToken");
    console.log("accessTokenRe() token: " + accessToken);
    if (accessToken === null || accessToken === "" || accessToken === " " || accessToken === undefined) {
        return reject("response");
    } else {
        return fetchWithToken("/all/api/token/access", accessToken)
            .then(response => {
                if (response.ok) {
                    return response.json();
                } else {
                    deleteCookie("accessToken");
                    return reject("response");
                }
            }).then(json => {
                if (json != null) {
                    isAuthentication = true;
                    authentications = json.authentications;
                    authenticationName = json.username;
                }
                console.log("accessToken result : " + isAuthentication + "," + authentications + "," + authenticationName);
        });
    }

}

function accessRefreshToken() {
    var accessToken = getCookie("refreshToken");
    if (accessToken === null || accessToken === "" || accessToken === " ") {
        sout("re out")
        logout();
        return reject("response");
    }
    return fetchWithToken("/all/api/token/refresh", accessToken)
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                logout();
            }
        }).then(json => {
            console.log(json);
            setCookie("accessToken", "Bearer " + json.token, 1);
            sout("Bearer return ")
            return accessTokenRe();
        });

}

function fetchWithToken(url, accessToken) {
    return fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': accessToken
        }
    }).then(response => {
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

function logout() {
    deleteCookie("accessToken");
    deleteCookie("refreshToken");
    location.reload();
}

function deleteCookie(cookieName, path = '/') {
    document.cookie = cookieName + '=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=' + path + ';';
}



