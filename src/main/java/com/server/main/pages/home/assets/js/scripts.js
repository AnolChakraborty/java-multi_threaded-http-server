console.log("Js loaded");
function logout() {
    if (document.cookie.length > 0) {
        alert("Logout Successfull");
        document.cookie = "session_expired; Max-Age=0";
    }
    else {
        alert("You are already logged out, please login again");
    }
}