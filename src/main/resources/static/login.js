function login() {

  const phone = document.getElementById("phone").value;
  const password = document.getElementById("password").value;

  fetch("/api/auth/login", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ phone, password })
  })
  .then(res => {
    if (!res.ok) throw new Error("Login failed");
    return res.text();
  })
  .then(() => {
    window.location.href = "/otp.html";
  })
  .catch(err => alert(err.message));
}
