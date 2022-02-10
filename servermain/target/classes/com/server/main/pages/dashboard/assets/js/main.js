console.log("js loaded");
function logout() {
    alert("Logout Successfull");
    document.cookie = "session_expired; Max-Age=0";
    window.location.href = "/login";
}