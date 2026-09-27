const loginButton =
    document.getElementById("loginButton");

const message =
    document.getElementById("message");


loginButton.addEventListener("click", async () => {

    const username =
        document.getElementById("username").value.trim();

    const password =
        document.getElementById("password").value;


    if (!username || !password) {

        showMessage(
            "Username and password are required.",
            "danger"
        );

        return;
    }


    try {

        const response = await fetch(
            "/auth/login",
            {
                method: "POST",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    username,
                    password
                })
            }
        );


        if (!response.ok) {

            showMessage(
                "Invalid username or password.",
                "danger"
            );

            return;
        }


        const data =
            await response.json();


        // Store JWT
        localStorage.setItem(
            "token",
            data.token
        );


        localStorage.setItem(
            "username",
            data.username
        );


        // Go to report generator
        window.location.href = "/";


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