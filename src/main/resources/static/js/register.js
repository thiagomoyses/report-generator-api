const registerButton =
    document.getElementById("registerButton");

const message =
    document.getElementById("message");

registerButton.addEventListener("click", async () => {

    const name =
        document.getElementById("name").value.trim();

    const username =
        document.getElementById("username").value.trim();

    const password =
        document.getElementById("password").value;


    if (!name || !username || !password) {
        showMessage(
            "All fields are required.",
            "danger"
        );
        return;
    }

    try {
        const response = await fetch(
            "/auth/register",
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name,
                    username,
                    password
                })
            }
        );

        if (!response.ok) {
            showMessage(
                "Unable to create account.",
                "danger"
            );
            return;
        }

        const data = await response.json();
        showMessage(
            data.message,
            "success"
        );

        setTimeout(() => {

            window.location.href =
                "/login.html";

        }, 1000);


    } catch (error) {
        console.error(error);
        showMessage(
            "Unable to connect to the server.",
            "danger"
        );
    }
});

function showMessage(text, type) {
    message.textContent = text;
    message.className =
        `alert alert-${type} mt-3`;
}