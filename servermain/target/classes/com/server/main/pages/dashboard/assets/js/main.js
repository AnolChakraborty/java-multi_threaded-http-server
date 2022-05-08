console.log("js loaded");

var request = new XMLHttpRequest();
request.onreadystatechange = function () {
    if (this.readyState == 4) {
        if (request.status == 200) {
            var response = JSON.parse(this.responseText);
            if (response.status == "success") {
                document.getElementById("name").innerHTML = "Name : " + response.name;
                document.getElementById("roll").innerHTML = "Roll : " + response.roll;
                document.getElementById("branch").innerHTML = "Branch : " + response.branch;
                document.getElementById("semester").innerHTML = "Semester : " + response.semester;
                document.getElementById("email").innerHTML = "Email : " + response.email;
                document.getElementById("phone").innerHTML = "Phone : " + response.phone;
                document.getElementById("address").innerHTML = "Addressworkbe: " + response.address;
                document.getElementById("profilePic").src = response.profilePic;
                console.log(response.profilePic);
            } else if (response.status == "failure") {
                window.location.href = "/dashboard/internal_server_error";

            }
        }
    }
};
request.open("GET", "dashboard/getdata", true);
request.send();

function logout() {
    alert("Logout Successfull");
    document.cookie = "session_expired; Max-Age=0";
    window.location.href = "/login";
}