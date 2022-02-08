console.log("login.js loaded");

function login() {
    var username = document.getElementById("username").value;
    var password = document.getElementById("password").value;

    var xhttp = new XMLHttpRequest();
    
    xhttp.open("POST", "http://localhost:8000/login");
    xhttp.setRequestHeader("Content-type", "application/json");
    
    var data = JSON.stringify({ "username": username, "password": password });
    
    console.log(data);
    xhttp.send(data);
}

function resetPassword() {
    alert("This feature is not implemented yet.")
}