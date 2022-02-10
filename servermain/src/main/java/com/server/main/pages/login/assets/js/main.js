console.log("login.js loaded");

function usernameClicked() {
    if (document.getElementById("username").value == "") {
        document.getElementById("username").value = "AEI/";
    }
}

function passwordClicked() {
    document.getElementById("eye").style.visibility = "visible";
}

function eyeClicked() {
    if (document.getElementById("password").type == "password") {
        document.getElementById("password").type = "text";
        document.getElementById("eye").src = "login/assets/img/eye-open.png";
    } else {
        document.getElementById("password").type = "password";
        document.getElementById("eye").src = "login/assets/img/eye-closed.png";
    }
}

// username regex /^AEI\/[A-Za-z0-9\/]+$|^aei\/[A-Za-z0-9\/]+$/gm
function login() {
    const regex = /^AEI\/[A-Za-z0-9\/\(\)]+$|^aei\/[A-Za-z0-9\/\(\)]+$/gm;
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    var isValid = false;

    if (username != "") {
        if (username.match(regex)) {

            if (password != "" && password.length >= 5 && password != username) {
                document.getElementById("password").style.borderColor = "";
                document.getElementById("username").style.borderColor = "";
                isValid = true;
            }
            else if (password.length == 0) {
                passwordMismatch("Please enter a password");
            }
            else if (password.length < 5) {
                passwordMismatch("Password must be at least 5 characters long.");
                document.getElementById("password").focus();
            }
            else if (password == username) {
                passwordMismatch("Password cannot be same as the username.");
            }
            else {
                passwordMismatch("Please enter your password.");
                document.getElementById("password").focus();
            }
        }
        else {
            usernameMismatch("Please enter a valid username.");
            document.getElementById("username").focus();
            document.getElementById("password").style.borderColor = "";
        }
    }
    else if (password == "") {
        allMismatch("Please enter your username & password.");
        document.getElementById("username").focus();
    } else {
        usernameMismatch("Please enter your username.");
        document.getElementById("username").focus();
        document.getElementById("password").style.borderColor = "";
    }
    var data = {
        username: username,
        password: password
    };
    if (isValid) {
        var payload = JSON.stringify(data);
        var request = new XMLHttpRequest();

        request.onreadystatechange = function () {
            if (this.readyState == 4) {
                var response = JSON.parse(this.responseText);
                if (request.status == 200) {
                    if (response.status == "success") {
                        alert(response.message + "\nNow will be redirected to your dashboard");
                        document.cookie = response.token;
                        window.location.href = "/dashboard";
                    }
                }
                else {
                    alert(response.message);
                }
            }
        };
        request.open("POST", "login/verify", true);
        request.setRequestHeader("Content-Type", "application/json");
        request.send(payload);
    }
}

function signUp() {
    window.location.href = "/signup";
}

function passwordMismatch(param) {
    alert(param);
    document.getElementById("password").focus();
    document.getElementById("password").style.borderColor = "red";
    document.getElementById("username").style.borderColor = "";

}

function usernameMismatch(param) {
    alert(param);
    document.getElementById("username").focus();
    document.getElementById("username").style.borderColor = "red";
}

function allMismatch(param) {
    alert(param)
    document.getElementById("username").focus();
    document.getElementById("username").style.borderColor = "red";
    document.getElementById("password").style.borderColor = "red";
}

function resetPassword() {
    alert("This feature is not implemented yet.")
}